package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.PayView;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.model.PortFee;
import com.sesnu.orion.web.model.Shipping;
import com.sesnu.orion.web.model.ShippingView;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class PaymentController {

	
	@Autowired
	PaymentDAO payDao;
	@Autowired ShippingDAO shipDao;
	@Autowired Util util;
	@Autowired PortFeeDAO portFeeDAO;
	@Autowired OrderDAO orderDAO;
	@Autowired ItemDAO itemDAO;
	@Autowired BidDAO bidDao;
	

	@RequestMapping(value = "/api/user/pay/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<PayView> items(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<PayView> pays =null;
		if(orderRef.equals("all")){
			 pays = payDao.listAll();
		}else{
		 pays = payDao.listByOrderRef(Long.parseLong(orderRef));
		}
		if(pays.size()>0){
			return pays;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/pay", method = RequestMethod.POST)
	public @ResponseBody List<PayView> addItem(HttpServletResponse response,@RequestBody Payment pay)
			throws Exception {
		List<ShippingView> ships = shipDao.listByOrderId(pay.getOrderRef());
		if(ships.size()==0 && pay.getName().equals("Legalization")){
			response.sendError(400, "Item not yet shipped");
			return null;
		}
		
		pay.setEstimate(calcEstimate(pay,ships.get(0)).getValue());
		pay.setUpdatedOn(Util.parseDate(new Date(),"/"));
		pay.setId(null);
		payDao.saveOrUpdate(pay);
		
		List<PayView> pays = payDao.listByOrderRef(pay.getOrderRef());
		if(pays.size()>0){
			return pays;
		}
		response.sendError(404);
		return null;

	}
	
	@RequestMapping(value = "/api/user/pay/estimate", method = RequestMethod.POST)
	public @ResponseBody JSONObject getestimate(HttpServletResponse response,@RequestBody Payment pay)
			throws Exception {
		List<ShippingView> ships = shipDao.listByOrderId(pay.getOrderRef());
		if(ships.size()==0 && pay.getName().equals("Legalization")){
			response.sendError(400, "Item not yet shipped");
			return null;
		}else if(pay.getName().equals("Customs")){
			List<Bid> bid = bidDao.getBidWinner(pay.getOrderRef());
			if(bid.size()==0){
				response.sendError(400, "Supplier not yet selected, total Amount unknow");
				return null;
				}
			}
		
		return calcEstimate(pay,ships.get(0)).getDetails();

	}
	
	
	@RequestMapping(value = "/api/user/pay", method = RequestMethod.PUT)
	public @ResponseBody List<PayView> updateItem(HttpServletResponse response,
			@RequestBody Payment pay)
			throws Exception {
		
		if(payDao.get(pay.getId())==null){
			response.sendError(400);
			return null;
		}
		pay.setUpdatedOn(Util.parseDate(new Date(),"/"));
		payDao.saveOrUpdate(pay);
		
		List<PayView> Payments = payDao.listByOrderRef(pay.getOrderRef());
		if(Payments.size()>0){
			return Payments;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/pay/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<PayView> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		Payment pay = payDao.get(id);
		long orderRef= pay.getOrderRef();
		if(pay != null){
			payDao.delete(pay);
			List<PayView> pays = payDao.listByOrderRef(orderRef);
			if(pays.size()>0){
				return pays;
			}
		}
		response.sendError(400);
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	private Estimate calcEstimate(Payment pay, ShippingView ship){
		Date d = new Date();
		Date arrival = ship.getEta()!=null? ship.getEta():ship.getEta();
		long diff = d.getTime() - arrival.getTime();
		long daysInPort = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		OrderView order = orderDAO.get(pay.getOrderRef());
		double total = 0.0;
		JSONObject pd = new JSONObject();
		if(pay.getName().equals("Legalization")){
			
			PortFee portFees = portFeeDAO.getByName(ship.getShipAgency());
			total += portFees.getLegalizationFee();		// add legalization
			pd.put("Legalization", portFees.getLegalizationFee());
			
			total += portFees.getContLiftFee() * order.getContQnt(); // add container lift fee
			pd.put("Container Lift", portFees.getContLiftFee() * order.getContQnt());
			
			if (pay.getDeposit()==0 &&  order.getContSize()==20){
				total += portFees.getDepositCont20()* order.getContQnt(); // container charge for 20
				pd.put("Container service Charge", portFees.getDepositCont20()* order.getContQnt());
			}else if (pay.getDeposit()==0 &&  order.getContSize()==40){
				total += portFees.getDepositCont40()* order.getContQnt(); // container charge for 40
				pd.put("Container service Charge", portFees.getDepositCont40()* order.getContQnt());
			}
			
			pd.put("Consumer Tax",  portFees.getConsumerTax()/100 * total);
			total += portFees.getConsumerTax()/100 * total;  // add consumer tax

			if (daysInPort >0 && portFees.getContFreeDays() - daysInPort > 0){ // add penality if exists
				total += portFees.getDailyPenality() * (portFees.getContFreeDays() - daysInPort);
				pd.put("Penality", portFees.getDailyPenality() * (portFees.getContFreeDays() - daysInPort));
			}
			
		}else if(pay.getName().equals("Customs")){
			Item item = itemDAO.get(order.getItemId());
			List<Bid> bid = bidDao.getBidWinner(order.getId());
			double totalTaxPercent = item.getFinancialServices() + item.getStampTax() + item.getConsumerTax() + item.getFees() + item.getOthers();
			total = bid.get(0).getTotalBid() * totalTaxPercent;
			pd.put("Customs", total);
		}
		
		Estimate paymentEstimate = new Estimate(total,pd);
		
		return paymentEstimate;
	}
	
		

}
