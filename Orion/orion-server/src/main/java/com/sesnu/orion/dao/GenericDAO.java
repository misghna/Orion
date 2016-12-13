package com.sesnu.orion.dao;

import java.util.Date;
import java.util.List;

import com.sesnu.orion.web.model.Item;



public interface GenericDAO {
	
	public List<?> list(String tableName,String rev);
	
	public List<?> getItemByName(String tableName, String productName);
		
	public Object getItemByHsCode(String tableName, long hsCode);
	
	public Object get(String tableName, long id);

	public void saveOrUpdate(String tableName, Object item);
	
	public void delete(String tableName, long id);
	
	public boolean isExists(String tableName, String productName, long hsCode);
	
	public List<String> getRevisions(String tableName);
	
}