package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;
import com.sesnu.orion.web.model.Summary;



public interface SummaryDAO {
	
	
	public List<Summary> list(int year,int month);
	
	public List<Summary> getOrderByTime(int year,String month);
	
	public List<Summary> listAll() ;
		



	
}