package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;



public interface OrderDAO {
	
	public Order list(long id);
	
	public List<Order> list(int year,int month);
	
	public List<Order> getOrderByTime(int year,String month);
	
//	public List<SalesPlan> getSalesPlanBy(String productName);
		
//	public SalesPlan getItemByHsCode(long hsCode);
	
	public Order get(long id);

	public void saveOrUpdate(Order order);
	
	public void delete(long id);
	
	public void deleteByTime(int year, String month);
	
	public boolean isExists(Order order);
	
	public String getLatest();
	


	
}