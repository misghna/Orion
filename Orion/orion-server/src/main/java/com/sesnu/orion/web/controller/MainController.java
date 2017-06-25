package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.Util;


@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class MainController {

	
	@Autowired
	UserDAO userDao;
	
	
	@RequestMapping(value = {"/open/health"},
			method = RequestMethod.GET)
	public  @ResponseBody String health (HttpServletRequest request,ModelMap model) {
		
		return "healthy";
	}
	
	@RequestMapping(value = {"/","/status/**","/exporter/**","/finance/**","/setting/**","/import/**","/other/**","/document/**","/open/**"},
			method = RequestMethod.GET)
	public String allPageRequests(HttpServletRequest request,ModelMap model) {
		
		return "index";
	}

	@RequestMapping(value = {"/login"},method = RequestMethod.GET)
	public String login(HttpServletRequest request,ModelMap model) {
		return "login";
	}

	@RequestMapping(value = "/access_denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model,HttpServletResponse response) 
			throws IOException {
		model.addAttribute("user", getPrincipal());
		return "accessDenied";
	}
	
	@RequestMapping(value = "/api_access_denied", method = RequestMethod.GET)
	public @ResponseBody JSONObject apiAccessDenied(HttpServletResponse response) 
			throws IOException {
		response.sendError(403);
		return null;
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	

	@RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
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
				jo.put("buildTime", Util.getBuildTime(request.getServletContext()));
			}
			return jo;
		}		
			response.sendError(401);
		} catch (IOException e) {}
		jo.put("access", "denied");
		return jo;
	}
	
    @RequestMapping(value = {"/404"}, method = RequestMethod.GET)
    public String NotFoudPage() {
    	System.out.println("404");
        return "index";

    } 
    
    
	private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}
