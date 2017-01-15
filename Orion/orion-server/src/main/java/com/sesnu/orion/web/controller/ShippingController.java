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

import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.dao.impl.OrderDAOImpl;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Shipping;
import com.sesnu.orion.web.model.ShippingView;
import com.sesnu.orion.web.utility.TrackingService;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ShippingController {

	
	@Autowired
	ShippingDAO shipDao;
	@Autowired OrderDAO orderDao;
	@Autowired TrackingService trackService;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/ship/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<ShippingView> items(@PathVariable("orderRef") String orderRef,
			HttpServletResponse response) throws IOException {

		List<ShippingView> shippings = null;
		if(orderRef.equals("all")){
			shippings= shipDao.listAll();
		}else{
			shippings= shipDao.listByOrderId(Long.parseLong(orderRef));
		}
		if(shippings.size()>0){
			return shippings;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/ship/track/{bl}", method = RequestMethod.GET)
	public @ResponseBody String conrTrack(@PathVariable("bl") String bl,
			HttpServletResponse response) throws IOException {
		List<ShippingView> ship = shipDao.listByBL(bl);
		if(ship.size()==0){
			response.sendError(400,Util.parseError("Shipment info not found"));
		}
		return trackService.getShipmentTracking(ship.get(0).getShipAgency(), bl);
	}
	
	
	@RequestMapping(value = "/api/user/ship/{state}", method = RequestMethod.POST)
	public @ResponseBody List<ShippingView> addItem(HttpServletResponse response,
			@RequestBody Shipping shipping,@PathVariable("state") String state)
			throws Exception {
		
		OrderView order = orderDao.get(shipping.getOrderRef());
		if(order==null){
			response.sendError(400,Util.parseError("Invalid Inv No."));
			return null;
		}
				
		List<ShippingView> shipings = shipDao.listByOrderId(shipping.getOrderRef());
		if(shipings.size()>0){
			response.sendError(400,Util.parseError("Shipping detail for this order(Inv No) already exists"));
			return null;
		}
		
		shipings = shipDao.listByBL(shipping.getBl());
		if(shipings.size()>0){
			response.sendError(400,Util.parseError("Specified BL already exists"));
			return null;
		}
				
		shipping.setUpdatedOn(Util.parseDate(new Date(),"/"));
		shipping.setId(null);
		shipDao.saveOrUpdate(shipping);

		List<ShippingView> shippings=null;
		if(state.equals("all")){
			shippings = shipDao.listAll();
		}else{
			shippings = shipDao.listByOrderId(shipping.getOrderRef());
		}
		if(shippings.size()>0){
			return shippings;
		}
		response.sendError(404);
		return null;
		

	}
	
	
	@RequestMapping(value = "/api/user/ship/{state}", method = RequestMethod.PUT)
	public @ResponseBody List<ShippingView> updateItem(HttpServletResponse response,
			@PathVariable("state") String state,
			@RequestBody Shipping shipping)
			throws Exception {
		
		
		if(shipDao.get(shipping.getId())==null){
			response.sendError(400);
			return null;
		}
		
		List<ShippingView> shipings = shipDao.listByOrderId(shipping.getOrderRef());
		System.out.println("existing " + shipings.get(0).getId() + " new " +  shipping.getId());
		if(shipings.size()>0 && !shipings.get(0).getId().equals(shipping.getId())){
			response.sendError(400,Util.parseError("Shipping detail for this order already exists!"));
			return null;
		}
		
		shipings = shipDao.listByBL(shipping.getBl());
		if(shipings.size()>0 && !shipings.get(0).getId().equals(shipping.getId())){
			response.sendError(400,Util.parseError("Specified BL already exists"));
			return null;
		}
		
		shipping.setUpdatedOn(Util.parseDate(new Date(),"/"));
		shipDao.saveOrUpdate(shipping);
		List<ShippingView> shippings=null;
	
		if(state.equals("all")){
			shippings = shipDao.listAll();
		}else{
			shippings = shipDao.listByOrderId(shipping.getOrderRef());
		}
		if(shippings.size()>0){
			return shippings;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/ship/{id}/{state}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<ShippingView> deleteItem(@PathVariable("id") long id,@PathVariable("state") String state,
			HttpServletResponse response) throws Exception {
		Shipping shipping = shipDao.get(id);
	
		if(shipping != null){
			shipDao.delete(shipping);
			List<ShippingView> shippings=null;			
		
			if(state.equals("all")){
				shippings = shipDao.listAll();
			}else{
				shippings = shipDao.listByOrderId(shipping.getOrderRef());
			}
			if(shippings.size()>0){
				return shippings;
			}else{
				response.sendError(404,Util.parseError("not found"));
			}
		}
		response.sendError(400,Util.parseError("Shipment not found"));
		return null;
	}
	
		

}
