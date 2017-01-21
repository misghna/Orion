package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Budget;



public interface BudgetDAO {
	
	public List<Budget> list(int year,String month);
	
	public List<Budget> getPlanByTime(int year,String month);
	
	public Budget get(long id);

	public void saveOrUpdate(Budget budget);
	
	public void delete(long id);
	
	public void deleteByTime(int year, String month);
	
	public boolean isExists(Budget budget);
	
	public String getLatest();
	


	
}