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

import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.dao.StatusDAO;
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderStat;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Required;
import com.sesnu.orion.web.model.Status;
import com.sesnu.orion.web.model.Summary;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class StatusController {

	static List<String> MUST_DOCS = Arrays.asList(new String []{"Order Initiated","Proforma Invoice","Supplier Selected",
																"Order Authorization","Commercial Invoice","Item Shipped"});

	
	@Autowired
	StatusDAO statusDao;
	@Autowired Util util;
	@Autowired OrderDAO orderDao;

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/status/document/{orderRefId}", method = RequestMethod.GET)
	public @ResponseBody JSONArray documentStatus(
				HttpServletResponse response,@PathVariable("orderRefId") long orderRefId) throws IOException {

		OrderView order = orderDao.get(orderRefId);
		if(order==null){
			response.sendError(400, "invalid order id");
			return null;
		}
		
		List<String> status = statusDao.listName(orderRefId,"Document");

		List<Required> required = statusDao.listRequired("Document", order.getItemType());
		JSONArray docStat = new JSONArray();
		for (Required req : required) {
			JSONObject jo = new JSONObject();
			jo.put("name",req.getName());
			if(status.contains(req.getName())){
				jo.put("done",true);
			}else{
				jo.put("done", false);
			}
			docStat.add(jo);
		}
				
		return docStat;
	}
	
	
	@RequestMapping(value = "/api/user/required/{type}", method = RequestMethod.GET)
	public @ResponseBody List<Required> requiredAll(@PathVariable("type") String type,
				HttpServletResponse response) throws IOException {

		List<Required> required = statusDao.listRequiredByType(type);
			return required;

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/status/payment/{orderRefId}", method = RequestMethod.GET)
	public @ResponseBody JSONArray paymentStatus(
				HttpServletResponse response,@PathVariable("orderRefId") long orderRefId) throws IOException {

		OrderView order = orderDao.get(orderRefId);
		if(order==null){
			response.sendError(400, "invalid order id");
			return null;
		}
		
		List<String> status = statusDao.listName(orderRefId,"Payment");

		List<Required> required = statusDao.listRequired("Payment", order.getItemType());
		JSONArray docStat = new JSONArray();
		for (Required req : required) {
			JSONObject jo = new JSONObject();
			jo.put("name",req.getName());
			if(status.contains(req.getName())){
				jo.put("done",true);
			}else{
				jo.put("done", false);
			}
			docStat.add(jo);
		}
				
		return docStat;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/user/status/order/{orderRefId}", method = RequestMethod.GET)
	public @ResponseBody JSONArray orderStatus(
				HttpServletResponse response,@PathVariable("orderRefId") long orderRefId) throws IOException {
		JSONArray orStat = new JSONArray();
		List<OrderStat> orderStat = statusDao.listOrderStat(orderRefId);		
		List<String> orderStatList = new ArrayList<String>();
		orderStatList.add("Order Initiated");
		
		if(orderStat.size()>0){			
			OrderStat os =orderStat.get(0);
			if(os.getBid() != null){
				orderStatList.add("Supplier Selected");
			}

			if(os.getBl() != null){
				orderStatList.add("Item Shipped");
			}	
						
			//*** finished docs
			for (OrderStat ost : orderStat) {
				if(ost.getType()!=null){
					orderStatList.add(ost.getType());
				}
			}
			
			JSONObject jo = null;
			for (String mustDoc : MUST_DOCS) {
				jo = new JSONObject();
				jo.put("name",mustDoc);
				jo.put("done",orderStatList.contains(mustDoc));
				orStat.add(jo);
			}
		}
		 
		return orStat;
	}
	
	
	
}
