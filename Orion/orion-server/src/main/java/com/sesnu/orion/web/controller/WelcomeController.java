package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.User;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class WelcomeController {

	
	@Autowired
	UserDAO userDao;
	
	
	@RequestMapping(value = {"/","/admin/users"}, method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		List<User> listUsers = userDao.list();

		System.out.println("size ....."  + listUsers.size());
		
		return "index";
	}


	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(ModelMap model) {
		
		return "index";
	}

	@RequestMapping(value = "/api/login", method = RequestMethod.POST)
	public @ResponseBody JSONObject printTest(ModelMap model,HttpServletRequest request,HttpServletResponse response) {		
		JSONObject jo = new JSONObject();	
		try {
		if(request.getSession().getAttribute("user")!=null){
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if(!user.getStatus().equals("Active")){
			    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			    new SecurityContextLogoutHandler().logout(request, response, auth);
			    response.sendError(403,"inactive");
				jo.put("access", "inactive");
			}else{
				jo.put("access", "granted");
				jo.put("role", user.getRole());
				jo.put("fname", user.getFullname());
				jo.put("status", user.getStatus());
				jo.put("sId", session.getId());
				jo.put("id", user.getId());
				jo.put("email", user.getEmail());
			}
			System.out.println(jo);
			return jo;
		}		
			response.sendError(401);
		} catch (IOException e) {}
		jo.put("access", "denied");
		return jo;
	}
	
}
