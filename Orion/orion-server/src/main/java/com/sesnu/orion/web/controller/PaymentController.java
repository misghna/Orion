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
import com.sesnu.orion.dao.StatusDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.PayView;
import com.sesnu.orion.web.model.Payment;
import com.sesnu.orion.web.model.PortFee;
import com.sesnu.orion.web.model.Required;
import com.sesnu.orion.web.model.ShippingView;
import com.sesnu.orion.web.utility.EstimatorService;
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
	@Autowired StatusDAO statDao;
	@Autowired private EstimatorService estService;
	@Autowired private ItemDAO itemDao;
	

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
	
	
	@RequestMapping(value = "/api/user/pay/list", method = RequestMethod.GET)
	public @ResponseBody List<Required> list(
			HttpServletResponse response) throws IOException {
			List<Required> req =statDao.listRequiredByType("Payment");

		return req;
	}
	
	
	@RequestMapping(value = "/api/user/pay", method = RequestMethod.POST)
	public @ResponseBody List<PayView> addItem(HttpServletResponse response,@RequestBody Payment pay)
			throws Exception {
		
		List<ShippingView> ships = shipDao.listByOrderId(pay.getOrderRef());
		if(ships.size()==0 && !pay.getName().equals("DU License")){
			response.sendError(400, Util.parseError("Item not yet marked as shipped"));
			return null;
		}
		OrderView order = orderDAO.get(pay.getOrderRef());
		
		pay.setEstimate(calcEstimate(order,pay).getValue());
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
	private Estimate calcEstimate(OrderView order, Payment pay){
		Estimate est=null;
		Bid bid = null;
		switch(pay.getName()){
			case "DU Licence" :
				return estService.license(order);
			case "Phytosanitary" :
				return estService.phytosanitary();
			case "Agriculture Phyto." :
				return estService.certificateOfHealth();
			case "Min of Industry cert." :
				return null;
			case "Item Cost" :
				return estService.itemCost(order,pay);
			case "Bromangol" :
				return estService.bromangol(order);
			case "Shipping Agency Fee" :
				return estService.legalization(order,pay);
			case "Customs Fee" :
				 bid = getBidWinner(order.getId());
				if(bid !=null){
					Item item = itemDao.get(order.getItemId());
					return estService.customs(bid.getTotalBid(),bid.getCurrency(), item);
				}
				return null;
			case "Port Fee" :
				return estService.port(order);
			case "Terminal Fee" :
				 bid = getBidWinner(order.getId());
				if(bid !=null){
					return estService.terminal(order, bid.getTotalBid());
				}
				return null;
			case "Transpiration Fee" :
				return estService.transport(order);
			case "Detention Fee" :
				return estService.penality(order);
			case "Closing FA Fee" :
				 bid = getBidWinner(order.getId());
				if(bid !=null){
					return estService.forwardingAgent(bid);
				}
				return null;
			default:
					break;			
		}
		
		return est;
	}
	
	private Bid getBidWinner(long orderId){
		List<Bid> bids = bidDao.getBidWinner(orderId);
		if(bids.size()>0){
			return bids.get(0);
		}
		return null;
	}
		

}
