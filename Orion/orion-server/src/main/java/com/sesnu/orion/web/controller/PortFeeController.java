package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
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

import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.web.model.PortFee;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class PortFeeController {

	
	@Autowired
	PortFeeDAO portFeeDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/portFee", method = RequestMethod.GET)
	public @ResponseBody List<PortFee> items(HttpServletResponse response) throws IOException {

		List<PortFee> portFees = portFeeDao.listAll();

		if(portFees.size()>0){
			return portFees;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/agency", method = RequestMethod.GET)
	public @ResponseBody List<String> getAllAgency(HttpServletResponse response) throws IOException {

		List<String> agencies = portFeeDao.listAllAgency();

		if(agencies.size()>0){
			return agencies;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/portFee", method = RequestMethod.POST)
	public @ResponseBody List<PortFee> addItem(HttpServletResponse response,@RequestBody PortFee portFee)
			throws Exception {
		
		PortFee portFee1 = portFeeDao.getByName(portFee.getAgency());
		if(portFee1!=null){
			response.sendError(400,Util.parseError("Agency setting already exists"));
			return null;
		}
		
		portFee.setUpdatedOn(Util.parseDate(new Date(),"/"));
		portFee.setId(null);
		portFeeDao.saveOrUpdate(portFee);
		
		List<PortFee> portFees = portFeeDao.listAll();
		if(portFees.size()>0){
			return portFees;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/portFee", method = RequestMethod.PUT)
	public @ResponseBody List<PortFee> updateItem(HttpServletResponse response,
			@RequestBody PortFee portFeww)
			throws Exception {
		
		
		if(portFeeDao.get(portFeww.getId())==null){
			response.sendError(400);
			return null;
		}
				
		portFeww.setUpdatedOn(Util.parseDate(new Date(),"/"));
		portFeeDao.saveOrUpdate(portFeww);
		
		List<PortFee> portFees = portFeeDao.listAll();
		if(portFees.size()>0){
			return portFees;
		}
		response.sendError(404);
		return null;

	}
	
	@RequestMapping(value = "/api/user/portFee/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List<PortFee> updateItem(HttpServletResponse response,@PathVariable("id") long id)
			throws Exception {
		
		PortFee portFee = portFeeDao.get(id);
		if(portFee==null){
			response.sendError(400);
			return null;
		}
				
		portFeeDao.delete(portFee);
		
		List<PortFee> portFees = portFeeDao.listAll();
		if(portFees.size()>0){
			return portFees;
		}
		response.sendError(404);
		return null;

	}
	
			

}
