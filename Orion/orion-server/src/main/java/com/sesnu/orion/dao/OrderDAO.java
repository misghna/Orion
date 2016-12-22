package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;



public interface OrderDAO {
	
	public OrderView list(long id);
	
	public List<OrderView> list(int year,int month);
	
	public List<OrderView> getOrderByTime(int year,String month);
	
	public List<OrderView> getOrdersByInvNo(String invNo);
	
	public List<OrderView> listAll() ;
		
	
	public OrderView get(long id);

	public void saveOrUpdate(Order order);
	
	public void delete(long id);
	
	public void deleteByTime(int year, String month);
	
	public boolean isExists(Order order);
	
	public String getLatest();
	


	
}