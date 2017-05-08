package com.sesnu.orion.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.ExchangeDAO;
import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.dao.TerminalDAO;
import com.sesnu.orion.dao.impl.SalesPlanDAOImpl;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.model.Exchange;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.model.PortFee;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.ShippingView;
import com.sesnu.orion.web.model.Terminal;
import com.sesnu.orion.web.utility.Util;

@SuppressWarnings("unchecked")
@Component
public class EstimatorService {

	
	@Autowired private ShippingDAO shipDao;
	@Autowired private PortFeeDAO portFeeDAO;
	@Autowired private MiscSettingDAO miscDao;
	@Autowired private ExchangeDAO exchangeDao;
	@Autowired private TerminalDAO terminalDao;
	
	
	private Map<String,Double> rateCache;
	
	public EstimatorService(){
		rateCache = new HashMap<String,Double>();
	}
	
	private JSONObject addTotal(Estimate est){
		JSONObject j =est.getDetails();
		j.put("Total", est.getValue());
		return j;
	}
	
	public Estimate totalEstimate(Order order, Payment pay,Item item) {

		Estimate est =null;
		JSONObject pd = new JSONObject();
		double total =0;
		
		// legalization
		est = legalization(order,pay);
		total += est.getValue();
		pd.put("Shipping Agency", addTotal(est));
		
		// license
		est = license(order);
		total += est.getValue();
		pd.put("License", addTotal(est));

		// customs
		est = customs(order.getTotalPrice(),order.getCurrency(), item);
		total += est.getValue();
		pd.put("Customs", addTotal(est));
		
		// bromangol
		est = bromangol(order);
		total += est.getValue();
		pd.put("Bromangol", addTotal(est));
		
		// transport
		est = transport(order);
		total += est.getValue();
		pd.put("Transport", addTotal(est));
		
		// terminal

		est = terminal(order);
		pd.put("Terminal",addTotal(est));
		total += est.getValue();

		
		// port
		est = port(order);
		total += est.getValue();
		pd.put("Port", addTotal(est));
		
		// certificateOfQuality
		est = certificateOfQuality();
		total += est.getValue();
		pd.put("Certificate Of Quality", addTotal(est));
		
		// certificateOfHealth
		est = certificateOfHealth();
		total += est.getValue();
		pd.put("Agriculture Phyto.", addTotal(est));
				
		// phytosanitary
		est = phytosanitary();
		total += est.getValue();
		pd.put("Phytosanitary", addTotal(est));

		// forwardingAgent
		est = forwardingAgent(order);
		total += est.getValue();
		pd.put("Forwarding Agent", addTotal(est));
		
		double usdRate = getExchange("Other", "Other",order.getCurrency(), "USD");
		double aoaRate = getExchange("Other", "Other",order.getCurrency(), "AOA");
		
		JSONObject summary = new JSONObject();
		double totalInvAmount = order.getTotalPrice() * aoaRate;
		summary.put("TotalCIFUSD", order.getTotalPrice() * usdRate);
		summary.put("TotalInvoiceAmount", totalInvAmount);
		summary.put("TotalFees", total);
		summary.put("TotalCost", total + totalInvAmount);
		summary.put("CostPerContainer", (total + totalInvAmount)/order.getContQnt());
		double costPerPack = (total + totalInvAmount)/order.getContQnt()/order.getPckPerCont();
		summary.put("CostPerPack", costPerPack);
		summary.put("pricePerPack", costPerPack*1.12);
		summary.put("costCifRatio", (total + totalInvAmount)/totalInvAmount *100);
		summary.put("pricePerPackUsd", costPerPack*1.2/usdRate);
				
		return  new Estimate(total,pd,summary);
	}
	
	
	
	public Estimate legalization(Order order, Payment pay){
		double total=0;JSONObject pd = new JSONObject();
		PortFee portFees = null;		
		ShippingView ship = shipDao.getByOrderId(order.getId());		
		if(ship==null){
			portFees = portFeeDAO.listAll().get(0);
		}else{
			portFees = portFeeDAO.getByName(ship.getShipAgency());
		}
		
		total += portFees.getLegalizationFee();		// add legalization
		pd.put("Legalization", portFees.getLegalizationFee());
		Integer contQty = order.getContQnt();
		Integer contSize = order.getContSize();
		total += portFees.getContLiftFee() * contQty; // add container lift fee
		pd.put("Container Lift", portFees.getContLiftFee() * contQty);
		
		if (((pay!=null && pay.getDeposit()==0) || pay==null)  &&  contSize==20){
			total += portFees.getDepositCont20()* contQty; // container charge for 20
			pd.put("Container service Charge", portFees.getDepositCont20()* contQty);
		}else if (((pay!=null && pay.getDeposit()==0) || pay==null)  &&  contSize==40){
			total += portFees.getDepositCont40()* contQty; // container charge for 40
			pd.put("Container service Charge", portFees.getDepositCont40()* contQty);
		}
		
		pd.put("Consumer Tax",  portFees.getConsumerTax()/100 * total);
		total += portFees.getConsumerTax()/100 * total;  // add consumer tax
		
		return new Estimate(total,pd);

	}
	
	
	public Estimate customs(Double totalCnfValue,String currency,Item item){

		double xrate = getExchange("Customs", "Custom",currency, "AOA");

		double total=0;JSONObject pd = new JSONObject();		
		double totalTaxPercent = item.getFinancialServices() + item.getStampTax() + 
								 item.getConsumerTax() + item.getFees() + item.getOthers();
	
		total = totalCnfValue * totalTaxPercent/100 * xrate;
		pd.put("Financial Service", item.getFinancialServices() * totalCnfValue* xrate);
		pd.put("Stamp tax",item.getStampTax()*totalCnfValue* xrate);
		pd.put("Consumer tax",item.getConsumerTax()*totalCnfValue* xrate);
		pd.put("Fees",item.getFees()*totalCnfValue* xrate);
		pd.put("Others",item.getOthers()*totalCnfValue* xrate);

		return new Estimate(total,pd);
	}
	
	public Estimate transport(Order order){
		Integer contQty=order.getContQnt();Integer contSize = order.getContSize();
		double total=0;JSONObject pd = new JSONObject();
		if(contSize==20){
			total = Double.parseDouble(miscDao.getByName("Container Transport Fee 20ft(Est/cont-AOA)").getValue())*contQty;
		}else {
			total = Double.parseDouble(miscDao.getByName("Container Transport Fee 40ft(Est/cont-AOA)").getValue()) *contQty;
		}
		pd.put("Contaner Transport",total);
		return new Estimate(total,pd);
	}
	
	
	public Estimate terminal(Order order){
		ShippingView ship = shipDao.getByOrderId(order.getId());
		Integer contQty=order.getContQnt();Integer contSize = order.getContSize();
		double tmp=0;

		String type = ship==null?"Other":ship.getTerminal();
		double xrate = getExchange("Terminal", type,order.getCurrency(), "AOA");
		
		Terminal terminal =null;
		
		if(ship!=null){
			terminal = terminalDao.getByName(ship.getTerminal());
		}
		
		if(terminal==null){
			List<Terminal> ts = terminalDao.listAll();
			terminal = ts.get(0);
		}
		
		if(terminal==null){
			return null;
		}
		
		double total=0;JSONObject pd = new JSONObject();
		
		pd.put("Terminal Name",terminal.getName());		
		pd.put("Terminal Free Days",terminal.getFreeDays());
		
		long diffDays =0;
		if(ship!=null && ship.getEta()!=null){
			pd.put("ETA",Util.parseDate(ship.getEta()));
			long diff = Math.abs(System.currentTimeMillis() - ship.getEta().getTime());
			diffDays = diff / (24 * 60 * 60 * 1000);
			pd.put("Paid Days",diffDays+terminal.getFreeDays());
		}
		if(diffDays<=0){
			diffDays = 10;
			pd.put("Estimated Paid Days",diffDays);
		}
		
		if(contSize==20){
			tmp = terminal.getOffloadFee20ft()*xrate * contQty;
		}else {
			tmp =  terminal.getOffloadFee40ft()*xrate * contQty;
		}	
		pd.put("Offloading",tmp);
		total +=tmp;
		
		tmp = terminal.getAdminServiceCharge() * xrate;
		pd.put("Admin & Service Charge",tmp);
		total +=tmp;
		
		tmp = calcStorage(terminal,contSize,diffDays,xrate);
		pd.put("Storage",tmp);	
		total +=tmp;
		
		tmp = terminal.getTransport() * contQty * xrate;
		pd.put("Transport ",tmp);
		total +=tmp;
			
		tmp = terminal.getImportTarrif()* contQty * xrate;
		pd.put("Tariff ",tmp);
		total +=tmp;
		
		tmp = terminal.getOtherPercent()* order.getTotalPrice() * xrate/100;
		pd.put("Others(% from total) ",tmp);
		total +=tmp;
		
		return new Estimate(total,pd);
	}
	
	private double calcStorage(Terminal terminal, Integer contSize, long daysInTerminal, double rate){
		double total=0;
		if (daysInTerminal <= terminal.getFreeDays()){
			return total;
		}
		
		if(contSize==20){
			if(daysInTerminal-terminal.getFreeDays() > terminal.getStorageFirstRangeDays()){
				total = (terminal.getStorageFirstRangeDays()-terminal.getStorageFirstRangeFee20ft())*rate;
				long daysInSecondStorage = daysInTerminal - terminal.getStorageFirstRangeDays()-terminal.getFreeDays();
					total += daysInSecondStorage*terminal.getStorageSecondRangeFee20ft()*rate;	
			}else{
				total = (daysInTerminal-terminal.getFreeDays())*terminal.getStorageFirstRangeFee20ft()*rate;
			}

		}
		else{
			if(daysInTerminal-terminal.getFreeDays() > terminal.getStorageFirstRangeDays()){
				total = (terminal.getStorageFirstRangeDays()-terminal.getStorageFirstRangeFee20ft())*rate;
				long daysInSecondStorage = daysInTerminal - terminal.getStorageFirstRangeDays()-terminal.getFreeDays();
					total += daysInSecondStorage*terminal.getStorageSecondRangeFee20ft()*rate;	
			}else{
				total = (daysInTerminal-terminal.getFreeDays())*terminal.getStorageFirstRangeFee20ft()*rate;
			}
		}
		return total;
	}
	
	public Estimate port(Order order){
		Integer contQty=order.getContQnt();Integer contSize = order.getContSize();

		double xrate = getExchange("Port", "Port",order.getCurrency(), "AOA");
		
		double total=0;double temp=0;JSONObject pd = new JSONObject();
		temp = Double.parseDouble(miscDao.getByName("Port documentation and Copy fee(Est/bill-USD)").getValue())*xrate;
		pd.put("documentation and Copy",temp);
		total += temp;
		
		temp = Double.parseDouble(miscDao.getByName("Port Other Fee(Est/bill-USD)").getValue())*xrate;
		pd.put("Other",temp);
		total += temp;
			
		if(contSize==20){
			temp = Double.parseDouble(miscDao.getByName("Port expenses Fee 20ft(Est/Cont-USD)").getValue())*contQty*xrate;
		}else {
			temp = Double.parseDouble(miscDao.getByName("Port expenses Fee 40ft(Est/Cont-USD)").getValue())*contQty*xrate;
		}
		
		pd.put("expenses",temp);
		total += temp;
		return new Estimate(total,pd);
	}
	
	
	public Estimate certificateOfQuality(){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate of Quality fee(Est/cont-AOA)").getValue());
		pd.put("Certificate of Quality",total);
		return new Estimate(total,pd);
	}
	
	public Estimate certificateOfHealth(){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate Of Health (Est/cont-AOA)").getValue());
		pd.put("Certificate of Health",total);
		return new Estimate(total,pd);
	}
	
	public Estimate bromangol(Order order){
		double total=0;double temp=0;JSONObject pd = new JSONObject();		
		Double sampleRatio = Double.parseDouble(miscDao.getByName("Bromangol Cont to samples(ratio-#)").getValue());
		double sampleQty = Math.ceil(order.getContQnt()/sampleRatio);
		temp = Double.parseDouble(miscDao.getByName("Bromangol Process Fee(Est/sample-AOA)").getValue()) * sampleQty;
		pd.put("Processing",temp);
		total += temp;
		
		temp = Double.parseDouble(miscDao.getByName("Bromangol PIC %(Est/bill-AOA)").getValue())*total/100;
		pd.put("PIC",temp);
		total += temp;
		
		temp = Double.parseDouble(miscDao.getByName("Bromangol Sample collection & prep Fee(Est/bill-AOA)").getValue());
		pd.put("Sample collection & prep",temp);
		total += temp;
		
		return new Estimate(total,pd);
	}
	
	
	public Estimate phytosanitary(){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate of Phytosanitary Fee(Est/bill-AOA)").getValue());
		pd.put("Phytosanitary",total);
		return new Estimate(total,pd);
	}
	
	public Estimate license(Order order){
		double total=0;JSONObject pd = new JSONObject();
		total = Double.parseDouble(miscDao.getByName("Certificate of License fee(Est/cont-AOA)").getValue())*order.getContQnt();
		pd.put("Certificate of License",total);
		return new Estimate(total,pd);
	}
	
	public Estimate forwardingAgent(Order order){
		double total=0;JSONObject pd = new JSONObject();
		double xrate = getExchange("Customs", "Customs",order.getCurrency(), "AOA");
		total = Double.parseDouble(miscDao.getByName("Forwarding agent %(Est/bill-AOA)").getValue()) * order.getTotalPrice() * xrate/100;
		pd.put("Forwarding agent",total);
		return new Estimate(total,pd);
	}
	
	public Estimate penality(Order order){
//		double total=0;JSONObject pd = new JSONObject();
//		if (daysInPort >0 && portFees.getContFreeDays() - daysInPort > 0){ // add penality if exists
//			total += portFees.getDailyPenality() * (portFees.getContFreeDays() - daysInPort);
//			pd.put("Penality", portFees.getDailyPenality() * (portFees.getContFreeDays() - daysInPort));
//		}
//		pd.put("Forwarding agent",total);
//		return new EstimateDetail(total,pd);
		return null;
	}

	public Estimate itemCost(Order order,Payment pay) {
		
		if(pay.getCurr().equals(order.getCurrency())){
			return new Estimate(order.getTotalPrice(),null);
		}

		return new Estimate(0,null);
	}
	
	
	private double getExchange(String name,String type,String from, String to) {		
		Double xChange = 0d;
		String key = from + to;
		try{			
			if(rateCache.containsKey(key)){
				return rateCache.get(key);
			}
			Exchange cur = exchangeDao.get(name, type,from, to);
			if(cur==null){
				cur = exchangeDao.get("Other", "Other",from, to);				
				if(cur==null){
					Set<String> toCur = new HashSet<String>();
					toCur.add(to);
					JSONObject ex= new Util().getExchangeRates(from, toCur);					
					xChange = Double.parseDouble(ex.get(key).toString());					
				}
			}else{
				xChange = cur.getRate();
			}
		}catch(Exception e){
			e.printStackTrace();
			return 0d;
		}
		rateCache.put(key, xChange);
		return xChange;
	}
	
}


