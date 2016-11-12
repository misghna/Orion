package com.sesnu.orion.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class UserController {

	
	@Autowired
	UserDAO userDao;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	public @ResponseBody JSONArray users(ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		return filterUsers(userDao.list());
	}
	
	@RequestMapping(value = "/api/user", method = RequestMethod.POST)
	public @ResponseBody Boolean addUser(HttpServletResponse response,@RequestBody User user)
			throws Exception {
		
		User user1 = userDao.getUserByEmail(user.getEmail());
		if(user1!=null){
			response.sendError(404, "user already exists");
			return null;
		}
		
		user.setPassphrase(Util.encodePassword(user.getPassphrase()));
		user.setStatus("Not Active");
		user.setRole("User");
		userDao.saveOrUpdate(user);
		StringBuilder msg = new StringBuilder();
		msg.append("Hello  AdminNameHere,\n\n ");
		msg.append(user.getFullname() + " is requesting access to Eriango App, if you decide to grant access to ");
		msg.append(user.getFullname() +" please login and activate the account in ...admin/users page ");
		msg.append("\n\n Do not replay to this email, this is an automated message.");
		msg.append("\n\n Thank you!!");
		List<User> users = userDao.list();
		for (User user2 : users) {
			if(user2.getRole().equals("Admin")){
				util.sendMail("Account Activation Request", user2.getEmail() ,msg.toString().replace("AdminNameHere", user2.getFullname()));
			}
		}
			
		return userDao.getUserByEmail(user.getEmail()).getId()>0l;
	}
	
	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public @ResponseBody User getUserById(@PathVariable("id") long id) {				
		return userDao.get(id);
	}
	
	@RequestMapping(value = "/api/admin/changeStatus", method = RequestMethod.PUT)
	public @ResponseBody JSONArray changeStatus(HttpServletRequest request,@RequestBody JSONObject userDetail)
			throws Exception {
		User user = userDao.get(Long.parseLong(userDetail.get("userId").toString()));
		user.setStatus(userDetail.get("status").toString());
		userDao.saveOrUpdate(user);
		if(user.getStatus().equals("Active")){
			StringBuilder msg = new StringBuilder();
			msg.append("Hello  "+user.getFullname()+",\n\n ");
			msg.append("Your access request to eriango webapp is approved!");
			msg.append("\n\n To access your account please go to the webapp and login");
			msg.append("\n\n Do not replay to this email, this is an automated message.");
			msg.append("\n\n Thank you!!");
			util.sendMail("Account Activated", user.getEmail() ,msg.toString());	
		}
		return filterUsers(userDao.list());
	}
	
	@RequestMapping(value = "/api/admin/changeRole", method = RequestMethod.PUT)
	public @ResponseBody List<User> changeRole(HttpServletRequest request,@RequestBody JSONObject userDetail)
			throws Exception {
		User user = userDao.get(Long.parseLong(userDetail.get("userId").toString()));
		user.setRole(userDetail.get("role").toString());
		userDao.saveOrUpdate(user);
		System.out.println(userDetail.toJSONString());
		return filterUsers(userDao.list());
	}
	
	@RequestMapping(value = "/api/admin/deleteUser/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JSONArray deleteUser(@PathVariable("id") long id)
			throws Exception {
		userDao.delete(id);
		System.out.println(id);
		return filterUsers(userDao.list());
	}

	
	@RequestMapping(value = "/api/open/reqCode", method = RequestMethod.POST)
	public @ResponseBody String reqCode(@RequestBody JSONObject body,
			HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		String email = body.get("email").toString();
		System.out.println("email " + email);
		User user = userDao.getUserByEmail(email);
		if(user==null){
			response.sendError(404,"user with the given Email-Id doesnt exist");
			return null;
		}
		
		String generatedCode = util.generateString();
		request.getServletContext().setAttribute(email, util.getTime().toString().concat("-").concat(generatedCode));
		StringBuilder msg = new StringBuilder();

		msg.append("Hello  "+user.getFullname()+",\n\n ");
		msg.append("please use the following code to recover your password");
		msg.append("\n\n verification code = " + generatedCode);
		msg.append("\n\n if you didnt initiate this request plz inform admin");
		msg.append("\n\n Do not replay to this email, this is an automated message.");
		msg.append("\n\n Thank you!!");
		util.sendMail("Password recovery code", email ,msg.toString());
		response.setStatus(200);
		return "{\"response\" : \"please check your email for verification code!\"}";
	}
	
	@RequestMapping(value = "/api/open/changeForgotenPass", method = RequestMethod.POST)
	public @ResponseBody String changeForgotenPass(@RequestBody JSONObject body,
			HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		
		String email = body.get("email").toString();
		
		User user = userDao.getUserByEmail(email);
		if(user==null){
			response.sendError(404,"user with the given Email-Id doesnt exist");
			return null;
		}
		
		if(request.getServletContext().getAttribute(email)==null){
			System.out.println("email not found in context");
			response.sendError(404, "start the recovery again");
			return null;
		}
		
		String realVCodeWithTStamp = request.getServletContext().getAttribute(email).toString();
		if(realVCodeWithTStamp==null){
			System.out.println("recovery code not found");
			response.sendError(404, "start the recovery again");
			return null;
		}else{
			long tStamp = Long.parseLong(realVCodeWithTStamp.split("-")[0].toString());
			if (Util.getTime()-tStamp>3600000){
				System.out.println("recovery code expired");
				response.sendError(404, "start the recovery again");
				return null;
			}
		}
		String vcode = body.get("vCode").toString().trim();
		String realVCode = realVCodeWithTStamp.split("-")[1].toString().trim();
		
		if(!realVCode.equals(vcode)){
			System.out.println("invalid verification code");
			response.sendError(404, "invalid verification code");
			return null;
		}
		
		String password = body.get("password").toString();
		
		user.setPassphrase(Util.encodePassword(password));
		userDao.saveOrUpdate(user);

		return "{\"result\": \"Password successfully changed!\"}";
		
	}
	
	@RequestMapping(value = "/api/changePass", method = RequestMethod.POST)
	public @ResponseBody String changePass(@RequestBody JSONObject body,
			HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		
		long id = Long.parseLong(body.get("id").toString());
		
		User user = userDao.get(id);
		if(user==null){
			response.sendError(404,"user with the given Email-Id doesnt exist");
			return null;
		}
		
		String cPass = body.get("cPass").toString();
		
		if(!util.encodePassword(cPass).equals(user.getPassphrase())){
			System.out.println("incorrect password");
			response.sendError(404, "incorrect password");
			return null;
		}
			
		String password = body.get("nPass").toString();
		
		user.setPassphrase(Util.encodePassword(password));
		userDao.saveOrUpdate(user);

		return "{\"result\": \"Password successfully changed!\"}";
		
	}
	
	private JSONArray filterUsers(List<User> userList){
		JSONArray users = new JSONArray();
		for (User user : userList) {
			JSONObject jo = new JSONObject();
			jo.put("fullname", user.getFullname());
			jo.put("id", user.getId());
			jo.put("role",user.getRole() );
			jo.put("status", user.getStatus());
			jo.put("email", user.getEmail());
			jo.put("header", user.getFullname() + "-" + user.getId());
			users.add(jo);
		}
		
		return users;
	}
	
	
}
