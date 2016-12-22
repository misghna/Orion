package com.sesnu.orion.web.controller;

import java.io.IOException;
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
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Summary;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class SummaryController {

	
	@Autowired
	SummaryDAO summaryDao;
	@Autowired Util util;
	@Autowired
	UserDAO userDao;


	
	@RequestMapping(value = "/api/user/summary", method = RequestMethod.GET)
	public @ResponseBody List<Summary> getAll(
				HttpServletResponse response) throws IOException {

		List<Summary> summary = summaryDao.listAll();
		if(summary!=null){
			return summary;
		}
		response.sendError(404);
		return null;
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
	
	
	
}
