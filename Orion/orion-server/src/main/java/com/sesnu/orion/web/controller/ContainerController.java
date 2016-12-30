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

import com.sesnu.orion.dao.ContainerDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Container;
import com.sesnu.orion.web.model.ContainerView;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ContainerController {

	
	@Autowired
	ContainerDAO contDao;
	@Autowired OrderDAO orderDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/cont/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<ContainerView> items(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<ContainerView> containers = null;
		if(orderRef.equals("all")){
			containers= contDao.listAll();
		}else{
			containers= contDao.listByOrderId(Long.parseLong(orderRef));
		}
		if(containers.size()>0){
			return containers;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/cont/{state}", method = RequestMethod.POST)
	public @ResponseBody List<ContainerView> addItem(HttpServletResponse response,@RequestBody Container container,
			@PathVariable("state") String state)
			throws Exception {
		
		OrderView order = orderDao.get(container.getOrderRef());
		if(order==null){
			response.sendError(400,Util.parseError("Invalid Bill of Loading, please select from the provided list"));
			return null;
		}
		
		container.setUpdatedOn(Util.parseDate(new Date(),"/"));
		container.setId(null);
		contDao.saveOrUpdate(container);
		
		List<ContainerView> containers2 = null;
		if(state.equals("all")){
			containers2= contDao.listAll();
		}else{
			containers2= contDao.listByOrderId(container.getOrderRef());
		}
		if(containers2.size()>0){
			return containers2;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/cont/{state}", method = RequestMethod.PUT)
	public @ResponseBody List<ContainerView> updateItem(HttpServletResponse response,
			@PathVariable("state") String state,@RequestBody Container container)
			throws Exception {
		
		
		if(contDao.get(container.getId())==null){
			response.sendError(400,Util.parseError("Bad data, try again"));
			return null;
		}
				
		container.setUpdatedOn(Util.parseDate(new Date(),"/"));
		contDao.saveOrUpdate(container);
		
		List<ContainerView> containers2 = null;
		if(state.equals("all")){
			containers2= contDao.listAll();
		}else{
			containers2= contDao.listByOrderId(container.getOrderRef());
		}
		if(containers2.size()>0){
			return containers2;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/cont/{id}/{state}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<ContainerView> deleteItem(@PathVariable("id") long id,
			@PathVariable("state") String state, HttpServletResponse response) throws Exception {
		
		Container container = contDao.get(id);
		if(container != null){
			contDao.delete(container);
			List<ContainerView> containers2 = null;
			if(state.equals("all")){
				containers2= contDao.listAll();
			}else{
				containers2= contDao.listByOrderId(container.getOrderRef());
			}
			if(containers2.size()>0){
				return containers2;
			}
			response.sendError(404);
		}
		
		return null;
	}
	
		

}
