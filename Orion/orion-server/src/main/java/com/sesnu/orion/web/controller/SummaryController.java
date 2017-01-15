package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Summary;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.TrackingService;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class SummaryController {

	
	@Autowired
	SummaryDAO summaryDao;
	@Autowired Util util;
	@Autowired UserDAO userDao;
	@Autowired OrderDAO orderDao;
	@Autowired ShippingDAO shipDao;
	


	
	@RequestMapping(value = "/api/user/summary/{state}", method = RequestMethod.GET)
	public @ResponseBody List<Summary> getAll(@PathVariable("state") String state,
				HttpServletResponse response) throws IOException {

		List<Summary> summary = summaryDao.listAll(state);
		if(summary!=null){
			return summary;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/newOrder", method = RequestMethod.GET)
	public @ResponseBody List<BigInteger> newOrdersList(
				HttpServletResponse response) throws IOException {
		List<BigInteger> newOrderList = orderDao.newOrdersList();
		if(newOrderList!=null){
			return newOrderList;
		}
		return new ArrayList<BigInteger>();
	}

	
	@RequestMapping(value = "/api/user/inTransit", method = RequestMethod.GET)
	public @ResponseBody List<BigInteger> inTransitList(
				HttpServletResponse response) throws IOException {
		List<BigInteger> newOrderList = shipDao.inTransitList();
		if(newOrderList!=null){
			return newOrderList;
		}
		return new ArrayList<BigInteger>();
	}
	
	@RequestMapping(value = "/api/user/inPort", method = RequestMethod.GET)
	public @ResponseBody List<BigInteger> inPortList(
				HttpServletResponse response) throws IOException {
		List<BigInteger> newOrderList = shipDao.inPortList();
		if(newOrderList!=null){
			return newOrderList;
		}
		return new ArrayList<BigInteger>();
	}
	
	@RequestMapping(value = "/api/user/inTerminal", method = RequestMethod.GET)
	public @ResponseBody List<BigInteger> inTerminal(
				HttpServletResponse response) throws IOException {
		List<BigInteger> newOrderList = shipDao.inTerminalList();
		if(newOrderList!=null){
			return newOrderList;
		}
		return new ArrayList<BigInteger>();
	}
	
	@RequestMapping(value = "/api/user/homeHeaders", method = RequestMethod.PUT)
	public @ResponseBody String updateHomeHeaders(HttpServletRequest request,@RequestBody String heders)
			throws Exception {
		User user=null;
		if(request.getSession().getAttribute("user")!=null){	
			user = (User) request.getSession().getAttribute("user");
		}else{
			user = util.getDevUser(userDao);
		}
		System.out.println("got this ******* " + heders);
		user.setHomeHeaders(heders);
		userDao.saveOrUpdate(user);
		return user.getHomeHeaders();
	}
	
	@RequestMapping(value = "/api/user/homeColor", method = RequestMethod.PUT)
	public @ResponseBody String updateHomeColorSet(HttpServletRequest request,@RequestBody String homeColor)
			throws Exception {
		User user=null;
		if(request.getSession().getAttribute("user")!=null){	
			user = (User) request.getSession().getAttribute("user");
		}else{
			user = util.getDevUser(userDao);
		}
		user.setHomeColor(homeColor);
		userDao.saveOrUpdate(user);
		return user.getHomeColor();
	}
	
	
}
