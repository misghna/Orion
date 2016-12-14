package com.sesnu.orion.web.controller;

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
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ItemController {

	
	@Autowired
	ItemDAO itemDao;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/items/{rev}", method = RequestMethod.GET)
	public @ResponseBody List<Item> items(@PathVariable("rev") String rev) throws InterruptedException {
		Thread.sleep(2000);
		return itemDao.list(rev);
	}
	
	
	@RequestMapping(value = "/api/admin/item", method = RequestMethod.POST)
	public @ResponseBody List<Item> addItem(HttpServletResponse response,@RequestBody Item item)
			throws Exception {
		if(itemDao.isExists(item.getName(), item.getBrand())){
			response.sendError(400);
			return null;
		}
		itemDao.saveOrUpdate(item);
		return itemDao.list(item.getRevision().toString());

	}
	
	
	@RequestMapping(value = "/api/admin/item", method = RequestMethod.PUT)
	public @ResponseBody List<Item> updateItem(HttpServletResponse response,@RequestBody Item item)
			throws Exception {
		Item orginal = itemDao.get(item.getId());
		if(orginal==null){
			response.sendError(400);
			return null;
		}
		item.setRevision(orginal.getRevision());
		item.setUpdatedOn(Util.parseDate(new Date(),"/"));
		itemDao.saveOrUpdate(item);
		return itemDao.list(Util.parseDate(orginal.getRevision()));

	}
	
	@RequestMapping(value = "/api/user/item/{id}", method = RequestMethod.GET)
	public @ResponseBody Item getItemById(@PathVariable("id") long id) {				
		return itemDao.get(id);
	}
	
	@RequestMapping(value = "/api/user/item/revisions", method = RequestMethod.GET)
	public @ResponseBody List<String> revisions() {				
		return itemDao.getRevisions();
	}
	
	@RequestMapping(value = "/api/user/item/names", method = RequestMethod.GET)
	public @ResponseBody List<String> names() {				
		return itemDao.getNameList();
	}
	
	@RequestMapping(value = "/api/user/item/brands", method = RequestMethod.GET)
	public @ResponseBody List<String> brands() {				
		return itemDao.getBrandList();
	}
	
	@RequestMapping(value = "/api/user/item/nameBrand", method = RequestMethod.GET)
	public @ResponseBody List<String> nameBrand() {				
		return itemDao.getNameBrandList();
	}
	
	@RequestMapping(value = "/api/user/item/revisions", method = RequestMethod.POST)
	public @ResponseBody List<Item> createNewRevision(@RequestBody JSONObject revDate) {
		Date date = new Date();
		List<Item> existing = itemDao.list(Util.parseDate(date));
		if(existing.size()>0){
			return existing;
		}
		
		List<Item> itemList = itemDao.list(revDate.get("rev").toString());
		String today = Util.parseDate(new Date());
		for (Item item : itemList) {
			Item newItem = copyItem(item);
			newItem.setRevision(date);
			newItem.setUpdatedOn(today);
			itemDao.saveOrUpdate(newItem);
		}
		return itemDao.list(Util.parseDate(date));
	}
	
	private Item copyItem(Item oi){
		Item item = new Item();
		item.setHsCode(oi.getHsCode());
		item.setName(oi.getName());
		item.setFinancialServices(oi.getFinancialServices());
		item.setConsumerTax(oi.getConsumerTax());
		item.setStampTax(oi.getStampTax());
		item.setFees(oi.getFees());
		item.setOthers(oi.getOthers());
		item.setBrand(oi.getBrand());
		return item;
	}
	
	
	@RequestMapping(value = "/api/admin/item/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List<Item> deleteItem(@PathVariable("id") long id,HttpServletResponse response) throws Exception {
		Item item = itemDao.get(id);
		if(item != null){
			itemDao.delete(id);
			return itemDao.list(item.getRevision().toString());
		}
		response.sendError(400);
		return null;
	}
	
	@RequestMapping(value = "/api/admin/item/revision/{rev}", method = RequestMethod.DELETE)
	public @ResponseBody List<Item> deleteItemRev(@PathVariable("rev") String rev,HttpServletResponse response) throws Exception {
		List<Item> item = itemDao.list(rev);
		if(item != null){
			itemDao.deleteByRev(rev);
			return itemDao.list("latest");
		}
		response.sendError(400);
		return null;
	}
	
}
