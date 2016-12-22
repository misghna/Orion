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

import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.Shipping;
import com.sesnu.orion.web.model.ShippingView;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ShippingController {

	
	@Autowired
	ShippingDAO shipDao;

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
	
	
	@RequestMapping(value = "/api/user/ship", method = RequestMethod.POST)
	public @ResponseBody List<ShippingView> addItem(HttpServletResponse response,@RequestBody Shipping shipping)
			throws Exception {
		
		List<ShippingView> shipings = shipDao.listByOrderId(shipping.getOrderRef());
		if(shipings.size()>0){
			response.sendError(404,"Shipping detail for this order already exists, please edit it if you want to add information.");
			return null;
		}
		shipping.setUpdatedOn(Util.parseDate(new Date(),"/"));
		shipping.setId(null);
		shipDao.saveOrUpdate(shipping);
		
		List<ShippingView> shippings = shipDao.listByOrderId(shipping.getOrderRef());
		if(shippings.size()>0){
			return shippings;
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/user/ship", method = RequestMethod.PUT)
	public @ResponseBody List<ShippingView> updateItem(HttpServletResponse response,
			@RequestBody Shipping shipping)
			throws Exception {
		
		
		if(shipDao.get(shipping.getId())==null){
			response.sendError(400);
			return null;
		}
		
		List<ShippingView> shipings = shipDao.listByOrderId(shipping.getOrderRef());
		if(shipings.size()>1){
			response.sendError(404,"Shipping detail for this order already exists, please edit it if you want to add information.");
			return null;
		}
		
		shipping.setUpdatedOn(Util.parseDate(new Date(),"/"));
		shipDao.saveOrUpdate(shipping);
		
		List<ShippingView> shippings = shipDao.listByOrderId(shipping.getOrderRef());
		if(shippings.size()>0){
			return shippings;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/ship/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<ShippingView> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		Shipping shipping = shipDao.get(id);
		long orderRef= shipping.getOrderRef();
		if(shipping != null){
			shipDao.delete(shipping);
			List<ShippingView> shippings = shipDao.listByOrderId(orderRef);
			if(shippings.size()>0){
				return shippings;
			}
		}
		response.sendError(400);
		return null;
	}
	
		

}
