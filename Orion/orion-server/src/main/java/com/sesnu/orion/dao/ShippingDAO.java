package com.sesnu.orion.dao;

import java.util.Date;
import java.util.List;

import com.sesnu.orion.web.model.Item;



public interface ShippingDAO {
	
	public List<Item> list(String rev);
	
	public List<Item> getItemByName(String productName);
		
	public Item getItemByHsCode(long hsCode);
	
	public Item get(long id);

	public void saveOrUpdate(Item item);
	
	public void delete(long id);
	
	public boolean isExists(String productName, long hsCode);
	
	public List<String> getRevisions();
	
}