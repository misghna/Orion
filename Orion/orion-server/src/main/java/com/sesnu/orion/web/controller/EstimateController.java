package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
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
import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.dao.StatusDAO;
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Estimate;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderStat;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Required;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.Status;
import com.sesnu.orion.web.model.Summary;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.service.EstimatorService;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class EstimateController {

	static List<String> MUST_DOCS = Arrays.asList(new String []{"Order Initiated","Proforma Invoice","Supplier Selected",
																"Order Authorized","Commercial Invoice","Item Shipped"});

	@Autowired private EstimatorService estService;
	@Autowired private OrderDAO orderDao;
	@Autowired private ItemDAO itemDao;
	@Autowired private SalesPlanDAO salesPlan;
	
	@RequestMapping(value = "/api/user/estimate/total/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate orderEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);		
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		Item item = itemDao.get(order.getItemId());
		return estService.totalEstimate(order, null,item);				
	}

	@RequestMapping(value = "/api/user/estimate/license/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate licenseEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.license(order);				
	}
	
	@RequestMapping(value = "/api/user/estimate/bromangol/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate bromangolEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.bromangol(order);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/certificateOfHealth/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate certificateOfHealthEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
//		OrderView order = orderDao.get(orderId);
		// i think its better if we relate this to no of containers
		return estService.certificateOfHealth();	 
	}
	
	@RequestMapping(value = "/api/user/estimate/certificateOfQuality/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate certificateOfQualityEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
//		OrderView order = orderDao.get(orderId);
		// i think its better if we relate this to no of containers
		return estService.certificateOfQuality();	 
	}
	
	@RequestMapping(value = "/api/user/estimate/customs/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate customsEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);		
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		Item item = itemDao.get(order.getItemId());	
		return estService.customs(order.getTotalPrice(), order.getCurrency(), item);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/port/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate portEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.port(order);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/terminal/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate terminalEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.terminal(order);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/transport/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate transportEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.transport(order);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/forwardingAgent/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate forwardingAgentEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.forwardingAgent(order);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/legalization/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate legalizationEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.legalization(order, null); // include pay to include deposite in the estimate
	}
	
	@RequestMapping(value = "/api/user/estimate/containerPenality/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate penalityEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		Order order = orderDao.getOrder(orderId);
		order = updateOrder(order);
		if(order==null){
			response.sendError(400,Util.parseError("cif value couldnt not be referenced"));
			return null;}
		return estService.penality(order);	 
	}
	
	@RequestMapping(value = "/api/user/estimate/phytosanitary/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Estimate phytosanitaryEstimate(HttpServletResponse response,
			@PathVariable("orderId") long orderId) throws IOException {
		// TBD add container dependency
		return estService.phytosanitary();	 
	}
	
   private Order updateOrder(Order order){
		if(order.getTotalPrice()==null || order.getCurrency()==null){
			SalesPlan sp = salesPlan.get(Long.parseLong(order.getBudgetRef()));
			if(sp==null)return null;
			order.setCurrency(sp.getCurrency());
			order.setTotalPrice(sp.getPckPerCont()*sp.getContQnt()*sp.getCif());
		}
		return order;
   } 
	
}
