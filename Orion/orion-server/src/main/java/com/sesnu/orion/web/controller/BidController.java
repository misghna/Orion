package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class BidController {

	
	@Autowired
	BidDAO bidDao;
	UserDAO userDao;
	OrderDAO orderDao;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/bid/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<Bid> items(@PathVariable("orderRef") long orderRef,
			HttpServletResponse response) throws IOException, InterruptedException {
	//	Thread.sleep(2000);
		List<Bid> bids = bidDao.list(orderRef);
		if(bids.size()>0){
			return bids;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/bid", method = RequestMethod.POST)
	public @ResponseBody List<Bid> addItem(HttpServletResponse response,@RequestBody Bid bid)
			throws Exception {
		
		bid.setUpdatedOn(Util.parseDate(new Date(),"/"));
		bid.setId(null);
		bidDao.saveOrUpdate(bid);
		
		List<Bid> bids = bidDao.list(bid.getOrderRef());
		if(bids.size()>0){
			return bids;
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/user/bid", method = RequestMethod.PUT)
	public @ResponseBody List<Bid> updateItem(HttpServletResponse response,
			@RequestBody Bid bid)
			throws Exception {
		
		if(bidDao.get(bid.getId())==null){
			response.sendError(400);
			return null;
		}
		bid.setUpdatedOn(Util.parseDate(new Date(),"/"));
		bidDao.saveOrUpdate(bid);
		
		List<Bid> bids = bidDao.list(bid.getOrderRef());
		if(bids.size()>0){
			return bids;
		}
		response.sendError(404);
		return null;

	}
	
	@RequestMapping(value = "/api/user/bid/select/{id}", method = RequestMethod.PUT)
	public @ResponseBody List<Bid> selectBidder(HttpServletResponse response,
			@PathVariable("id") long id)
			throws Exception {
		
		Bid bid = bidDao.get(id);
		if(bid == null){
			response.sendError(400, Util.parseError("Bid file not found"));
			return null;
		}
		
		List<Bid> selBid = bidDao.getBidWinner(bid.getOrderRef());
		
		if(selBid.size()>0){
			response.sendError(400, Util.parseError(selBid.get(0).getSupplier() + " is already selected!"));
			return null;
		}
		
		bid.setSelected(true);
		bidDao.saveOrUpdate(bid);
		
		List<Bid> bids = bidDao.list(bid.getOrderRef());

		return bids;
	}
	
	@RequestMapping(value = "/api/user/bid/diselect/{id}", method = RequestMethod.PUT)
	public @ResponseBody List<Bid> diselectBidder(HttpServletRequest request,HttpServletResponse response,
			@PathVariable("id") long id)
			throws Exception {
		
		Bid bid = bidDao.get(id);
		if(bid == null){
			response.sendError(400, Util.parseError("Bid file not found"));
			return null;
		}
		
		User user = (User) request.getSession().getAttribute("user");
		if(user==null || (bid.getApproval()!=null && bid.getApproval().indexOf("Approved")>=0 && !user.getRole().equals("Admin"))){
			response.sendError(400, Util.parseError("Bid is in Approval state, only admin can diselect it."));
			return null;
		}
		
		bid.setSelected(false);
		bidDao.saveOrUpdate(bid);
		
		List<Bid> bids = bidDao.list(bid.getOrderRef());

		return bids;
	}
	
	@RequestMapping(value = "/api/user/bid/reqApproval/{id}", method = RequestMethod.PUT)
	public @ResponseBody List<String> reqApproval(HttpServletRequest request,HttpServletResponse response,
			@PathVariable("id") long id)
			throws Exception {
		
		Bid bid = bidDao.get(id);
		if(bid == null){
			response.sendError(400, Util.parseError("Bid file not found"));
			return null;
		}
		
		if(!bid.isSelected()){
			response.sendError(400, Util.parseError("Selected supplier is not a winner"));
			return null;
		}
		
		if(bid.getApproval()!=null){
			response.sendError(400, Util.parseError("request already sent!"));
			return null;
		}
		
		bid.setApproval("requested");
		// send request to approvers
		List<User> users = userDao.getApprovers();
		StringBuilder msg = new StringBuilder();
		Order order = orderDao.get(bid.getOrderRef());
		List<String> auth = new ArrayList<String>();
		for (User user : users) {
			msg.append("Hello  "+user.getFullname()+",\n\n ");
			msg.append("A new order("+ order.getInvNo() +") is waiting for your approval");				
			util.sendText(msg.toString(), user.getPhone());
			msg.append("\n\n Do not replay to this email, this is an automated message.");
			msg.append("\n\n Thank you!!");
			Util.sendMail("Order Authorization request",user.getEmail(), msg.toString());
			auth.add(user.getFullname());
		}
		
		bidDao.saveOrUpdate(bid);		
		return auth;
	}

	@RequestMapping(value = "/api/user/bid/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<Bid> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		Bid bid = bidDao.get(id);
		long orderRef= bid.getOrderRef();
		if(bid != null){
			bidDao.delete(bid);
			List<Bid> bids = bidDao.list(orderRef);
			if(bids.size()>0){
				return bids;
			}
		}
		response.sendError(400);
		return null;
	}
	
		
////	@RequestMapping(value = "api/admin/order/duplicatePlan", method = RequestMethod.POST)
////	public @ResponseBody List<Order> duplicatePlan(@RequestBody JSONObject prop,HttpServletResponse response)
////			throws IOException {
////		List<Order> Orders = orderDao.getOrderByTime(Integer.parseInt(prop.get("sourceYear").toString()),
////														prop.get("sourceMonth").toString());
////	
////		int year = Integer.parseInt(prop.get("newYear").toString());
////		String month = prop.get("newMonth").toString();
////		
////		if(Orders.size()>0){
////			for (Order sp : Orders) {
////				 sp.setYear(year);
////			//	 sp.setMonth(month);
////			//	 sp.setMon(Util.getMonth(month));
////				 sp.setId(null);
////				 orderDao.saveOrUpdate(sp);
////			}
////		}
////		
////		List<Order> Orders2 = orderDao.list(year,Util.getMonth(month.trim()));
////		if(Orders2.size()>0){
////			return Orders2;
////		}
////		response.sendError(404);
////		return null;
////		
////	}
//	
//

//	@RequestMapping(value = "/api/admin/order/{year}/{month}", method = RequestMethod.DELETE)
//	public @ResponseBody List<Order> deleteOrder(@PathVariable("year") int year,
//			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
//		
//		List<Order> sp =  orderDao.list(year,Util.getMonth(month.trim()));
//		if(sp != null){
//			orderDao.deleteByTime(year, month);
//			String latestTime = orderDao.getLatest();
//			year = Integer.parseInt(latestTime.split("-")[0]);
//			month = latestTime.split("-")[1].toString();
//			List<Order> Orders = orderDao.list(year,Util.getMonth(month.trim()));
//			if(Orders.size()>0){
//				return Orders;
//			}else{
//				response.sendError(404);
//				return null;
//			}
//		}
//		response.sendError(400);
//		return null;
//	}
	
}
