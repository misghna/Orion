package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.AccessHistoryDAO;
import com.sesnu.orion.dao.AddressBookDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.AccessView;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class AccessController {

	
	@Autowired
	AccessHistoryDAO accessDAO;
	@Autowired Util util;

	

	@RequestMapping(value = "/api/user/access/{type}", method = RequestMethod.GET)
	public @ResponseBody List<AccessView> items(HttpServletResponse response
			,@PathVariable("type") String type) throws IOException {

		List<AccessView> access= null;
		if(type.equals("all")){
			access= accessDAO.getAllAccess();
		}else{
			access= accessDAO.getAccessByUser(type);
		}
		if(access.size()>0){
			return access;
		}
		response.sendError(404);
		return null;
	}

		

}
