package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.Interval;
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
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.Container;
import com.sesnu.orion.web.model.ContainerView;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.ShippingView;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ContainerController {

	
	@Autowired
	ContainerDAO contDao;
	@Autowired OrderDAO orderDao;
	@Autowired ShippingDAO shipDao;
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
		
		List<ContainerView> conts = contDao.listContIdByOrderId(container.getContNo(), container.getOrderRef());
		
		if(conts.size()>0){
			response.sendError(400,Util.parseError("Container number already exists"));
			return null;
		}
		
		container = setTotalDays(container);
		if(container==null){
			response.sendError(400,Util.parseError("Invalid date range"));
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
	
	private Container setTotalDays(Container container){
		ShippingView ship= shipDao.getByOrderId(container.getOrderRef());
		Long interval =null;
		if(container.getContReturnDate()!=null && ship!=null){
			long retDate = container.getContReturnDate().getTime();
			if(ship.getAta()!=null){
				 if(ship.getAta().getTime() >= retDate){
					 return null;
				 }
				 interval = (new Interval(ship.getAta().getTime(), retDate)).toDuration().getStandardDays();
			}else if(ship.getEta()!=null){
				 if(ship.getEta().getTime() >= retDate){
					 return null;
				 }
				 interval = (new Interval(ship.getEta().getTime(), retDate)).toDuration().getStandardDays();
			}
		}		
		if(interval!=null)container.setTotalDays(Integer.parseInt(interval.toString()));
		return container;
	}
	
	
	@RequestMapping(value = "/api/user/cont/{state}", method = RequestMethod.PUT)
	public @ResponseBody List<ContainerView> updateItem(HttpServletResponse response,
			@PathVariable("state") String state,@RequestBody Container container)
			throws Exception {
		
		
		if(contDao.get(container.getId())==null){
			response.sendError(400,Util.parseError("Bad data, try again"));
			return null;
		}
		
		container = setTotalDays(container);	
		if(container==null){
			response.sendError(400,Util.parseError("Invalid eta/return date range"));
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
