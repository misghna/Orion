package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Item;



public interface ItemDAO {
	
	public List<Item> list();
	
	public List<Item> getItemByProductName(String productName);
	
	public Item getItemByProductHsCode(long hsCode);
	
	public Item get(long id);

	public void saveOrUpdate(Item item);
	
	public void delete(long id);
	
}