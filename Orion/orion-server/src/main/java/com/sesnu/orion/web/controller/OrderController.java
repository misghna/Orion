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
import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class OrderController {

	
	@Autowired
	OrderDAO orderDao;
	@Autowired Util util;
	@Autowired ShippingDAO shipDao;
	

	@RequestMapping(value = "/api/user/order/{id}", method = RequestMethod.GET)
	public @ResponseBody OrderView items(@PathVariable("id") long id,
				HttpServletResponse response) throws IOException {

		OrderView order = orderDao.list(id);
		if(order!=null){
			return order;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/orders/{state}", method = RequestMethod.GET)
	public @ResponseBody List<OrderView> getAll(@PathVariable("state") String state,
				HttpServletResponse response) throws IOException {
		List<OrderView> orders =null;
		switch(state){
			case "all" :
				orders = orderDao.listAll();
				break;
			case "new" :
				orders = orderDao.listByIdList(orderDao.newOrdersList());
				break;
			case "inTransit" :
				orders = orderDao.listByIdList(shipDao.inTransitList());
				break;
			case "inPort" :
				orders = orderDao.listByIdList(shipDao.inPortList());
				break;
			case "inTerminal" :
				orders = orderDao.listByIdList(shipDao.inTerminalList());
				break;
			default:
				response.sendError(400,Util.parseError("Invalid Order Param"));
				return null;
		}
		
		if(orders!=null){
			return orders;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/order/{year}/{month}", method = RequestMethod.GET)
	public @ResponseBody List<OrderView> items(@PathVariable("year") int year,
			@PathVariable("month") String monthStr,HttpServletResponse response)
						throws IOException {
		int month =0;
		if(year==0 || monthStr.equals("latest")){
			String latestTime = orderDao.getLatest();
			if(latestTime!=null){
				year = Integer.parseInt(latestTime.split("-")[0]);
				month = Integer.parseInt(latestTime.split("-")[1].toString());
			}else{
				response.sendError(404);
				return null;
			}
		}else{
			month =Util.getMonth(monthStr.trim());
		}
		
		List<OrderView> Orders = orderDao.list(year,month);
		if(Orders.size()>0){
			return Orders;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/admin/order/{year}/{month}", method = RequestMethod.POST)
	public @ResponseBody List<OrderView> addItem(HttpServletResponse response,@RequestBody Order order,
			@PathVariable("year") int year,@PathVariable("month") String month)
			throws Exception {
		
		OrderView ord = orderDao.getOrderByInvNo(order.getInvNo());
		if(ord!=null){
			response.sendError(400,Util.parseError("Invoice number is already assigned"));
			return null;
		}
		order.setUpdatedOn(Util.parseDate(new Date(),"/"));
		order.setId(null);
		order.setCreatedOn(new Date());
		orderDao.saveOrUpdate(order);
		
		List<OrderView> Orders = orderDao.list(year,Util.getMonth(month));
		if(Orders.size()>0){
			return Orders;
		}
		
		Orders = orderDao.listAll();
		if(Orders.size()>0){
			return Orders;
		}
			
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/admin/order/{year}/{month}", method = RequestMethod.PUT)
	public @ResponseBody List<OrderView> updateItem(HttpServletResponse response,
			@RequestBody Order order,@PathVariable("year") int year,
			@PathVariable("month") String month)
			throws Exception {
		
		OrderView ord = orderDao.getOrderByInvNo(order.getInvNo());
		if(ord!=null && ord.getId()!=order.getId()){
			response.sendError(400,Util.parseError("Invoice number is already assigned"));
			return null;
		}
		
		if(orderDao.get(order.getId())==null){
			response.sendError(400);
			return null;
		}
		order.setUpdatedOn(Util.parseDate(new Date(),"/"));
		orderDao.saveOrUpdate(order);
		
		List<OrderView> orders = orderDao.list(year,Util.getMonth(month.trim()));
		if(orders.size()>0){
			return orders;
		}
		response.sendError(404);
		return null;

	}
	

	

	@RequestMapping(value = "/api/admin/order/{id}/{year}/{month}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<OrderView> deleteItem(@PathVariable("id") long id,@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
		System.out.println("caling **********************");
		OrderView sp = orderDao.get(id);
		if(sp != null){
			orderDao.delete(id);
			List<OrderView> Orders = orderDao.list(year,Util.getMonth(month.trim()));
			if(Orders.size()>0){
				return Orders;
			}else{
				String latestTime = orderDao.getLatest();
				if(latestTime!=null){
				year = Integer.parseInt(latestTime.split("-")[0]);
				month = latestTime.split("-")[1].toString();
				Orders = orderDao.list(year,Util.getMonth(month.trim()));
				if(Orders.size()>0){
					return Orders;
				}else{
					response.sendError(404);
					return null;
				}
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
	public @ResponseBody List<OrderView> deleteOrder(@PathVariable("year") int year,
			@PathVariable("month") String month,HttpServletResponse response) throws Exception {
		
		List<OrderView> sp =  orderDao.list(year,Util.getMonth(month.trim()));
		if(sp != null){
			orderDao.deleteByTime(year, month);
			String latestTime = orderDao.getLatest();
			if(latestTime!=null){
			year = Integer.parseInt(latestTime.split("-")[0]);
			month = latestTime.split("-")[1].toString();
			List<OrderView> Orders = orderDao.list(year,Util.getMonth(month.trim()));
			if(Orders.size()>0){
				return Orders;
			}else{
				response.sendError(404);
				return null;
			}
			}else{
				response.sendError(404);
				return null;
			}
		}
		response.sendError(400);
		return null;
	}
	
}
