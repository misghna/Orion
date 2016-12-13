package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Item;



public interface ItemDAO {
	
	public List<Item> list(String rev);
	
	public List<Item> getItemByName(String productName);
		
	public Item getItemByHsCode(long hsCode);
	
	public Item get(long id);

	public void saveOrUpdate(Item item);
	
	public void delete(long id);
	
	public boolean isExists(String productName,String brand);
	
	public List<String> getRevisions();
	
	public List<String> getNameList();
	
	public List<String> getBrandList();
	
	public List<String> getNameBrandList();

	public void deleteByRev(String rev);
	
}