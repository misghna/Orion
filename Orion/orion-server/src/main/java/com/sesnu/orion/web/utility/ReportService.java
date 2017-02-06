package com.sesnu.orion.web.utility;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.DocumentException;
import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.dao.DuLicenseDAO;
import com.sesnu.orion.dao.ExchangeDAO;
import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.Approval;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.Document;
import com.sesnu.orion.web.model.DuLicenseView;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.model.Exchange;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Payment;

@Component
public class ReportService {

	
	@Autowired private ConfigFile conf;
	@Autowired private Util util;
	@Autowired private DocumentDAO docDao;
	@Autowired private OrderDAO orderDao;
	@Autowired private ShippingDAO shipDao;
	@Autowired private ItemDAO itemDao;
	@Autowired private BidDAO bidDao;
	@Autowired private EstimatorService estService;
	@Autowired private ExchangeDAO exchangeDao;
	@Autowired private DuLicenseDAO licenseDao;
	@Autowired private PaymentDAO payDao;
	
	public String generateOrderAuthReport(Approval appr,String state) 
			throws DocumentException, IOException{
		Bid bid = bidDao.get(appr.getForId());
		OrderView order = orderDao.get(bid.getOrderRef());
		Item item = itemDao.get(order.getItemId());
		List<BidView> bids = bidDao.list(order.getId());
		
		String orginalHtml = conf.getFile("orderAuth.html");
		Estimate est = estService.totalEstimate(order, null, bid, item);

		String editedHtml = orginalHtml.replace("ORDER_DATE", Util.parseDate(order.getCreatedOn()));
		editedHtml = setPaths(editedHtml,state);
		editedHtml = editedHtml.replace("PRODUCT_NAME",item.getName());
		editedHtml = editedHtml.replace("ORDERED_BY",order.getOrderedBy());
		editedHtml = editedHtml.replace("BRAND_NAME",item.getBrand());
		editedHtml = editedHtml.replace("DEPARTMENT","Import");  // whose department ?
		editedHtml = editedHtml.replace("PACKAGING",order.getBaseSize().toString() + order.getBaseUnit() + "X" + order.getQtyPerPack() + "pcs");
		editedHtml = editedHtml.replace("BUDGET_REF",order.getBudgetRef());
		editedHtml = editedHtml.replace("QTY_PER_CONT",order.getPckPerCont().toString());
		editedHtml = editedHtml.replace("DESTINATION",order.getDestinationPort());
		editedHtml = editedHtml.replace("QUANTITY",order.getContQnt() + "X" + order.getContSize() + "'");
		editedHtml = editedHtml.replace("LATEST_ETA",Util.parseDate(order.getLatestETA()));
	
		editedHtml = editedHtml.replace("IN_TRANSIT",shipDao.InTransitCount(item.getId()).toString());
		editedHtml = editedHtml.replace("IN_PORT",shipDao.InPortCount(item.getId()).toString());
		editedHtml = editedHtml.replace("IN_TERMINAL",shipDao.InTerminalCount(item.getId()).toString());
		BigInteger newItemOrders = orderDao.newOrdersCount(item.getId()).subtract(new BigInteger("1"));
		editedHtml = editedHtml.replace("NEW_ORDERS",newItemOrders.toString());
		
		StringBuilder sb = new StringBuilder();
		for (int i =0; i< bids.size(); i++) {
			BidView abid = bids.get(i);
			sb.append("<tr>");
			sb.append("<td>");sb.append(i+1);sb.append("</td>");
			sb.append("<td>");sb.append(abid.getSupplier());sb.append("</td>");
			sb.append("<td>");sb.append(abid.getCifCnf());sb.append("</td>");
			sb.append("<td>");sb.append(abid.getFob());sb.append("</td>");
			sb.append("<td>");sb.append(abid.getCurrency());sb.append("</td>");
			sb.append("<td>");sb.append(abid.getPaymentMethod()==null? "" : abid.getPaymentMethod());sb.append("</td>");
			sb.append("<td>");sb.append(abid.isSelected()?"Yes":"No");sb.append("</td>");
			sb.append("<td>");sb.append(abid.getRemark()==null?"":abid.getRemark());sb.append("</td>");
			sb.append("</tr>");
		}

		editedHtml = editedHtml.replace("BID_DATA_TABLE",sb.toString());
		editedHtml = editedHtml.replace("EST_TRANSIT_DAYS",bid.getEstTransitDays().toString());
		Calendar c = Calendar.getInstance();
		c.setTime(order.getLatestETA()); 
		c.add(Calendar.DATE, bid.getEstTransitDays()); 
		editedHtml = editedHtml.replace("LATEST_DATE_OF_SHIP",Util.parseDate(c.getTime()));
		editedHtml = editedHtml.replace("IMPORTER",order.getImporter());
		
		Exchange cur = exchangeDao.get("Other", "Other","USD", "AOA");
		if(cur==null){
			return null;
		}
		
		editedHtml = editedHtml.replace("TOTAL_CNF_USD",Util.parseCurrency(bid.getTotalBid()));
		Double pricePerPack = (bid.getTotalBid() * cur.getRate() + est.getValue())/order.getContQnt()/order.getPckPerCont();
		pricePerPack = pricePerPack/cur.getRate();
		pricePerPack = (double) (Math.round (pricePerPack * 100.0)/100);
		editedHtml = editedHtml.replace("LANDED_COST_TO_WH",pricePerPack.toString());
		Double totalEstPrice = pricePerPack * 1.12;
		editedHtml = editedHtml.replace("COST_PLUS_MRG",totalEstPrice.toString());


				
		if(!state.equals("preview")){
			editedHtml = editedHtml.replace("SIGNATURE", appr.getApprover());
			String pdfFilePath = util.convertToPdf(editedHtml); // convert to pdf
			Path path = Paths.get(pdfFilePath);
			byte[] data = Files.readAllBytes(path);	// convert to byte array
			String[] frag = pdfFilePath.split("/");
			String fileName = frag[frag.length-1]; // get file name
			util.writeToS3(data, fileName);		// write to s3
			Files.deleteIfExists(path);
			
			Document doc = new Document(order.getId(), fileName, "Order Authorization", Util.parseDate(new Date()), "");
			docDao.saveOrUpdate(doc);
		}
		return editedHtml;
	}
	
	
	
	public String generatePayAuthReport(Approval app,String state) 
			throws DocumentException, IOException{
		Payment pay = payDao.get(app.getForId());
		OrderView order = orderDao.get(pay.getOrderRef());
		Item item = itemDao.get(order.getItemId());
		List<DuLicenseView> licenses = licenseDao.listByOrderId(order.getId());
		String git ="NA";
		if(licenses.size()>0){
			git = licenses.get(0).getGit();
		}
		
		String orginalHtml = conf.getFile("payAuth.html");
		
		String editedHtml = orginalHtml.replace("ORDER_REF", order.getInvNo());
		editedHtml = setPaths(editedHtml,state);
		editedHtml = editedHtml.replace("PRODUCT_NAME",item.getName());
		String bl = order.getBl()==null? "NA": order.getBl();
		editedHtml = editedHtml.replace("BILL_OF_LOADING",bl);
		editedHtml = editedHtml.replace("BRAND_NAME",item.getBrand());
		editedHtml = editedHtml.replace("G_I_T",git);  
		editedHtml = editedHtml.replace("PACKAGING",order.getBaseSize().toString() + order.getBaseUnit() + "X" + order.getQtyPerPack() + "pcs");
		editedHtml = editedHtml.replace("QTY_PER_CONT",order.getPckPerCont().toString());
		editedHtml = editedHtml.replace("DESTINATION",order.getDestinationPort());
		editedHtml = editedHtml.replace("QUANTITY",order.getContQnt() + "X" + order.getContSize() + "'");
		
		// create request body
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append("<td>" + pay.getName() + "</td>");
		sb.append("<td>" + pay.getPaymentMethod() + "</td>");
		sb.append("<td>" + pay.getCurr() + "</td>");
		sb.append("<td>" + pay.getDeposit() + "</td>");
		sb.append("<td>" + pay.getEstimate() + "</td>");
		sb.append("<td>" + pay.getPaymentAmount() + "</td>");
		String rmrk = pay.getRemark()==null?"":pay.getRemark();
		sb.append("<td>" + rmrk + "</td>");
		sb.append("</tr>");
		editedHtml = editedHtml.replace("BID_DATA_TABLE",sb.toString());

			
		if(!state.equals("preview")){
			editedHtml = editedHtml.replace("SIGNATURE", app.getApprover());
			String pdfFilePath = util.convertToPdf(editedHtml); // convert to pdf
			Path path = Paths.get(pdfFilePath);
			byte[] data = Files.readAllBytes(path);	// convert to byte array
			String[] frag = pdfFilePath.split("/");
			String fileName = frag[frag.length-1]; // get file name
			util.writeToS3(data, fileName);		// write to s3
			Files.deleteIfExists(path);
			
			Document doc = new Document(order.getId(), fileName, "Payment[" + pay.getName() + "]", Util.parseDate(new Date()), "Approval");
			docDao.saveOrUpdate(doc);
		}
		
		return editedHtml;
	}
	
	
	private String setPaths(String htmlContent, String state){
		if(conf.getProp().get("devMode").equals("true") && state.equals("preview")){
			htmlContent = htmlContent.replace("SignatureFontPath","assets/fonts/Brotherhood_Script.ttf");
			htmlContent = htmlContent.replace("SIGNATURE", "");
			return htmlContent.replace("LOGO_PATH/", "http://localhost:8080/");
		}else{
			String signaturePath = getClass().getClassLoader().getResource("").getPath() + "Brotherhood_Script.ttf";
			htmlContent = htmlContent.replace("SignatureFontPath",signaturePath);
			return htmlContent.replace("LOGO_PATH/", "");
		}
	}
	
	
}
