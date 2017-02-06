package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.sesnu.orion.web.utility.ConfigFile;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class UserController {

	
	@Autowired UserDAO userDao;
	@Autowired Util util;
	@Autowired private ConfigFile conf;

	@RequestMapping(value = "/api/check", method = RequestMethod.GET)
	public String ckeck(ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		return "index";
	}
	
	@RequestMapping(value = "/api/user/approvers/{type}", method = RequestMethod.GET)
	public @ResponseBody  List<String> getApprovers(@PathVariable("type") String type,
												HttpServletResponse response) throws IOException {
		
		List<String> approvers = userDao.getApprovers(type.trim());
		if(approvers!=null){
			return userDao.getApprovers(type.trim());
		}else{
			response.sendError(404, Util.parseError("Not found"));
			return null;
		}
	}
	
	@RequestMapping(value = "/api/user/user", method = RequestMethod.GET)
	public @ResponseBody JSONObject getUser(ModelMap model,HttpServletRequest request,
			HttpServletResponse response) {	
		JSONObject jo = new JSONObject();	
		try {
			
			HttpSession session = request.getSession();			
		if(session.getAttribute("user")!=null){			
			User user = (User) session.getAttribute("user");
			if(!user.getStatus().equals("Active")){
			    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			    new SecurityContextLogoutHandler().logout(request, response, auth);
			    response.sendError(403,"inactive");
				jo.put("access", "inactive");
			}else{
				return filterUser(user,session.getId(),request.getServletContext());
			}
			return jo;
		
		
		}else if(conf.getProp().get("devMode").equals("true"))	{
			User user = util.getDevUser(userDao);
			return filterUser(user,session.getId(),request.getServletContext());
		}
			response.sendError(401);
			
		} catch (IOException e) {}
		
		jo.put("access", "denied");
		return jo;
	}
	
	
	@SuppressWarnings("unchecked")
	private JSONObject filterUser(User user,String sId, ServletContext context) 
			throws IOException{
		JSONObject jo = new JSONObject();
		jo.put("access", "granted");
		jo.put("role", user.getRole());
		jo.put("fname", user.getFullname());
		jo.put("status", user.getStatus());
		jo.put("sId", sId);
		jo.put("id", user.getId());
		jo.put("email", user.getEmail());
		jo.put("buildTime", Util.getBuildTime(context));
		jo.put("homeHeaders", user.getHomeHeaders());
		jo.put("homeColor", user.getHomeColor());
		jo.put("notifications", user.getNotification());
		return jo;
	}
	
	
	@RequestMapping(value = "/api/admin/users", method = RequestMethod.GET)
	public @ResponseBody JSONArray users(ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		return filterUsers(userDao.list());
	}
	
	@RequestMapping(value = "/api/open/user", method = RequestMethod.POST)
	public @ResponseBody Boolean addUser(HttpServletResponse response,@RequestBody User user)
			throws Exception {
		
		User user1 = userDao.getUserByEmail(user.getEmail());
		if(user1!=null){
			response.sendError(404, "user already exists");
			return null;
		}
		
		if(user.getPassphrase() ==null){
			response.sendError(404, "password is empty");
			return null;
		}
		
		user.setPassphrase(Util.encodePassword(user.getPassphrase()));
		user.setStatus("Inactive");
		user.setApprover(null);
		user.setRole("User");
		userDao.saveOrUpdate(user);
		StringBuilder msg = new StringBuilder();
		msg.append("Hello  AdminNameHere, \n\n ");
		msg.append(user.getFullname() + " is requesting access to Eriango App, if you decide to grant access to ");
		msg.append(user.getFullname() +" please login and activate the account");
		
		String emailPostFix = "\n\n Do not replay to this email, this is an automated message. \n\n Thank you!!";
		List<User> users = userDao.list();
		for (User user2 : users) {
			if(user2.getRole().equals("Admin")){
				util.sendText(msg.toString().replace("AdminNameHere", user2.getFullname()),user2.getPhone());
				util.sendMail("Account Activation Request", user2.getEmail() ,
						msg.toString().replace("AdminNameHere", user2.getFullname()) + emailPostFix);
			}
		}
			
		return userDao.getUserByEmail(user.getEmail()).getId()>0l;
	}
	
	@RequestMapping(value = "/api/user/user/{id}", method = RequestMethod.GET)
	public @ResponseBody User getUserById(@PathVariable("id") long id) {				
		return userDao.get(id);
	}
	
	
	@RequestMapping(value = "/api/admin/user", method = RequestMethod.PUT)
	public @ResponseBody List<User> updateUser(HttpServletRequest request,@RequestBody User user)
			throws Exception {
		User existingUser = userDao.getUserByEmail(user.getEmail());
		user.setPassphrase(existingUser.getPassphrase());
		userDao.saveOrUpdate(user);
		
		if(!existingUser.getStatus().equals("Active") && user.getStatus().equals("Active")){
			StringBuilder msg = new StringBuilder();
			msg.append("Hello  "+user.getFullname()+",\n\n ");
			msg.append("Your access request to eriango webapp is approved!");
			msg.append("\n\n To access your account please go to the webapp and login");
			String emailPostFix = ("\n\n Do not replay to this email, this is an automated message.\n\n Thank you!!");
			util.sendText(msg.toString(), user.getPhone());
			util.sendMail("Account Activated", user.getEmail() ,msg.toString() + emailPostFix);	
		}
		
		return filterUsers(userDao.list());
	}	

	
	@RequestMapping(value = "/api/user/notification", method = RequestMethod.PUT)
	public @ResponseBody JSONArray notification(HttpServletRequest request,@RequestBody String notification)
			throws Exception {
		
		User activeUser = util.getActiveUser(request, userDao);
		activeUser.setNotification(notification);
		userDao.saveOrUpdate(activeUser);		
		return new JSONArray();
	}	
	
	
	
	@RequestMapping(value = "/api/admin/deleteUser/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JSONArray deleteUser(@PathVariable("id") long id)
			throws Exception {
		userDao.delete(id);
		return filterUsers(userDao.list());
	}

	
	@RequestMapping(value = "/api/open/reqCode", method = RequestMethod.POST)
	public @ResponseBody String reqCode(@RequestBody JSONObject body,
			HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		String email = body.get("email").toString();
		User user = userDao.getUserByEmail(email);
		if(user==null){
			response.sendError(404,"user with the given Email-Id doesnt exist");
			return null;
		}
		
		String generatedCode = util.generateString(7);
		request.getServletContext().setAttribute(email, util.getTime().toString().concat("-").concat(generatedCode));
		StringBuilder msg = new StringBuilder();

		msg.append("Hello  "+user.getFullname()+",\n\n ");
		msg.append("please use the following code to recover your password");
		msg.append("\n\n" + generatedCode);

		util.sendText(msg.toString(), user.getPhone());
	
		msg.append("\n\n if you didnt initiate this request plz inform admin immidately");
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
	
	@RequestMapping(value = "/api/user/changePass", method = RequestMethod.POST)
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
	
	@SuppressWarnings("unchecked")
	private JSONArray filterUsers(List<User> userList){
		JSONArray users = new JSONArray();
		for (User user : userList) {
			JSONObject jo = new JSONObject();
			jo.put("fullname", user.getFullname());
			jo.put("id", user.getId());
			jo.put("role",user.getRole() );
			jo.put("status", user.getStatus());
			jo.put("email", user.getEmail());
			jo.put("phone", user.getPhone());
			jo.put("department", user.getDepartment());
			jo.put("approver", user.getApprover());
			jo.put("header", user.getFullname() + "-" + user.getId());			
			users.add(jo);
		}
		
		return users;
	}
	
	
}
