package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class OrderController {

	
	@Autowired
	OrderDAO orderDao;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/order/{id}", method = RequestMethod.GET)
	public @ResponseBody Order items(@PathVariable("id") long id,
				HttpServletResponse response) throws IOException {

		Order order = orderDao.list(id);
		if(order!=null){
			return order;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/order/{year}/{month}", method = RequestMethod.GET)
	public @ResponseBody List<Order> items(@PathVariable("year") int year,
			@PathVariable("month") String monthStr,HttpServletResponse response)
						throws IOException {
		int month =0;
		if(year==0 || monthStr.equals("latest")){
			String latestTime = orderDao.getLatest();
			year = Integer.parseInt(latestTime.split("-")[0]);
			month = Integer.parseInt(latestTime.split("-")[1].toString());
		}else{
			month =Util.getMonth(monthStr.trim());
		}
		
		List<Order> Orders = orderDao.list(year,month);
		if(Orders.size()>0){
			return Orders;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/admin/order/{year}/{month}", method = RequestMethod.POST)
	public @ResponseBody List<Order> addItem(HttpServletResponse response,@RequestBody Order order,
			@PathVariable("year") int year,@PathVariable("month") String month)
			throws Exception {
		
		order.setUpdatedOn(Util.parseDate(new Date(),"/"));
		order.setId(null);
		order.setCreatedOn(new Date());
		orderDao.saveOrUpdate(order);
		
		List<Order> Orders = orderDao.list(year,Util.getMonth(month));
		if(Orders.size()>0){
			return Orders;
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/admin/order/{year}/{month}", method = RequestMethod.PUT)
	public @ResponseBody List<Order> updateItem(HttpServletResponse response,
			@RequestBody Order order,@PathVariable("year") int year,
			@PathVariable("month") String month)
			throws Exception {
		
		if(orderDao.get(order.getId())==null){
			response.sendError(400);
			return null;
		}
		order.setUpdatedOn(Util.parseDate(new Date(),"/"));
		orderDao.saveOrUpdate(order);
		
		List<Order> orders = orderDao.list(year,Util.getMonth(month.trim()));
		if(orders.size()>0){
			return orders;
		}
		response.sendError(404);
		return null;

	}
	
		
//	@RequestMapping(value = "api/admin/order/duplicatePlan", method = RequestMethod.POST)
//	public @ResponseBody List<Order> duplicatePlan(@RequestBody JSONObject prop,HttpServletResponse response)
//			throws IOException {
//		List<Order> Orders = orderDao.getOrderByTime(Integer.parseInt(prop.get("sourceYear").toString()),
//														prop.get("sourceMonth").toString());
//	
//		int year = Integer.parseInt(prop.get("newYear").toString());
//		String month = prop.get("newMonth").toString();
//		
//		if(Orders.size()>0){
//			for (Order sp : Orders) {
//				 sp.setYear(year);
//			//	 sp.setMonth(month);
//			//	 sp.setMon(Util.getMonth(month));
//				 sp.setId(null);
//				 orderDao.saveOrUpdate(sp);
//			}
//		}
//		
//		List<Order> Orders2 = orderDao.list(year,Util.getMonth(month.trim()));
//		if(Orders2.size()>0){
//			return Orders2;
//		}
//		response.sendError(404);
//		return null;
//		
//	}
	

	@RequestMapping(value = "/api/admin/order/{id}/{year}/{month}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<Order> deleteItem(@PathVariable("id") long id,@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
		System.out.println("caling **********************");
		Order sp = orderDao.get(id);
		if(sp != null){
			orderDao.delete(id);
			List<Order> Orders = orderDao.list(year,Util.getMonth(month.trim()));
			if(Orders.size()>0){
				return Orders;
			}else{
				String latestTime = orderDao.getLatest();
				year = Integer.parseInt(latestTime.split("-")[0]);
				month = latestTime.split("-")[1].toString();
				Orders = orderDao.list(year,Util.getMonth(month.trim()));
				if(Orders.size()>0){
					return Orders;
				}else{
					response.sendError(404);
					return null;
				}
			}
		}
		response.sendError(400);
		return null;
	}
	
	@RequestMapping(value = "/api/admin/order/{year}/{month}", method = RequestMethod.DELETE)
	public @ResponseBody List<Order> deleteOrder(@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
		
		List<Order> sp =  orderDao.list(year,Util.getMonth(month.trim()));
		if(sp != null){
			orderDao.deleteByTime(year, month);
			String latestTime = orderDao.getLatest();
			year = Integer.parseInt(latestTime.split("-")[0]);
			month = latestTime.split("-")[1].toString();
			List<Order> Orders = orderDao.list(year,Util.getMonth(month.trim()));
			if(Orders.size()>0){
				return Orders;
			}else{
				response.sendError(404);
				return null;
			}
		}
		response.sendError(400);
		return null;
	}
	
}
