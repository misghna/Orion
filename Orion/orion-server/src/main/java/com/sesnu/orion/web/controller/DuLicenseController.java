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

import com.sesnu.orion.dao.DuLicenseDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class DuLicenseController {

	
	@Autowired
	DuLicenseDAO licDAO;
	@Autowired Util util;
	@Autowired OrderDAO orderDao;
	

	@RequestMapping(value = "/api/user/lic/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<DuLicenseView> items(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<DuLicenseView> licenses = null;
		if(orderRef.equals("all")){
			licenses= licDAO.listAll();
		}else{
			licenses= licDAO.listByOrderId(Long.parseLong(orderRef));
		}
		if(licenses.size()>0){
			return licenses;
		}
		response.sendError(404);
		return null;
	}
	

	
	@RequestMapping(value = "/api/user/lic/{state}", method = RequestMethod.POST)
	public @ResponseBody List<DuLicenseView> addItem(HttpServletResponse response,
			@RequestBody DuLicense lic,@PathVariable("state") String state)
			throws Exception {
		
		OrderView order = orderDao.get(lic.getOrderRef());
		if(order==null){
			response.sendError(400,Util.parseError("Invalid Inv No, please select from the list"));
			return null;
		}
				
		lic.setId(null);
		licDAO.saveOrUpdate(lic);

		List<DuLicenseView> licenses=null;
		if(state.equals("all")){
			licenses = licDAO.listAll();
		}else{
			licenses = licDAO.listByOrderId(lic.getOrderRef());
		}
		if(licenses.size()>0){
			return licenses;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/lic/{state}", method = RequestMethod.PUT)
	public @ResponseBody List<DuLicenseView> updateItem(HttpServletResponse response,
			@PathVariable("state") String state,
			@RequestBody DuLicense lic2)
			throws Exception {
		
		
		if(licDAO.get(lic2.getId())==null){
			response.sendError(400);
			return null;
		}
		
		
		licDAO.saveOrUpdate(lic2);
		List<DuLicenseView> licenses=null;
	
		if(state.equals("all")){
			licenses = licDAO.listAll();
		}else{
			licenses = licDAO.listByOrderId(lic2.getOrderRef());
		}
		if(licenses.size()>0){
			return licenses;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/lic/{id}/{state}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<DuLicenseView> deleteItem(@PathVariable("id") long id,@PathVariable("state") String state,
			HttpServletResponse response) throws Exception {
		DuLicense lic2 = licDAO.get(id);
	
		if(lic2 != null){
			licDAO.delete(lic2);
			List<DuLicenseView> licenses=null;			
		
			if(state.equals("all")){
				licenses = licDAO.listAll();
			}else{
				licenses = licDAO.listByOrderId(lic2.getOrderRef());
			}
			if(licenses.size()>0){
				return licenses;
			}else{
				response.sendError(404,Util.parseError("not found"));
			}
		}
		response.sendError(400,Util.parseError("Shipment not found"));
		return null;
	}
	
		

}
