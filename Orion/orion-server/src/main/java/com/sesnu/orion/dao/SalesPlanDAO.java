package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;



public interface SalesPlanDAO {
	
	public List<SalesView> list(int year,String month);
	
	public List<SalesPlan> getPlanByTime(int year,String month);
	
	public SalesPlan get(long id);

	public void saveOrUpdate(SalesPlan item);
	
	public void delete(long id);
	
	public void deleteByTime(int year, String month);
	
	public boolean isExists(SalesPlan salesPlan);
	
	public String getLatest();
	


	
}