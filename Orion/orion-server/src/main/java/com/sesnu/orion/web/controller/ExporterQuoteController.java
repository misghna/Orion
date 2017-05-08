package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ExporterQuoteController {

	
	@Autowired
	OrderDAO orderDao;
	
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/exporterQuote", method = RequestMethod.GET)
	public @ResponseBody List<Order> exporters(HttpServletResponse response) throws IOException {

		List<Order> orderList = orderDao.listAllOrders();

		if(orderList.size()>0){
			return orderList;
		}
		response.sendError(404);
		return null;
	}
	
		
	
	@RequestMapping(value = "/api/user/exporterQuote", method = RequestMethod.PUT)
	public @ResponseBody List<Order> updateItem(HttpServletResponse response,
			@RequestBody Order order)
			throws Exception {
		
		
		if(orderDao.get(order.getId())==null){
			response.sendError(400);
			return null;
		}
		
		if(order.getStatus().equals("Approved")){
			response.sendError(400,Util.parseError("This margin is already approved, to change please void it from approval page first"));
			return null;
		}
				
		order.setUpdatedOn(Util.parseDate(new Date(),"/"));
		orderDao.saveOrUpdate(order);
		
		List<Order> orderList = orderDao.listAllOrders();

		if(orderList.size()>0){
			return orderList;
		}
		response.sendError(404);
		return null;
	}
	


	
			

}
