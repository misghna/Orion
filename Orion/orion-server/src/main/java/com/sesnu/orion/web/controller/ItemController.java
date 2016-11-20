package com.sesnu.orion.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.dao.ItemDAOImpl;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ItemController {

	
	@Autowired
	ItemDAO itemDao;
	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/items", method = RequestMethod.GET)
	public @ResponseBody List<Item> items(ModelMap model,HttpServletRequest request,HttpServletResponse response) {	
		return itemDao.list();
	}
	
	
	@RequestMapping(value = "/api/user/item", method = RequestMethod.POST)
	public @ResponseBody List<Item> addItem(HttpServletResponse response,@RequestBody Item item)
			throws Exception {
		System.out.println("***************" + item.getProduct() + item.getHsCode());
		if(itemDao.isExists(item.getProduct(), item.getHsCode())){
			response.sendError(400);
			return null;
		}
		itemDao.saveOrUpdate(item);
		return itemDao.list();

	}
	
	@RequestMapping(value = "/api/user/item/search", method = RequestMethod.POST)
	public @ResponseBody List<Item> searchItem(HttpServletResponse response,@RequestBody String searchText)
			throws Exception {
		if(searchText.isEmpty()){
			return itemDao.list();
		}
		return itemDao.searchItem(searchText);

	}
	
	@RequestMapping(value = "/api/user/item", method = RequestMethod.PUT)
	public @ResponseBody List<Item> updateItem(HttpServletResponse response,@RequestBody Item item)
			throws Exception {
		if(itemDao.get(item.getId())==null){
			response.sendError(400);
			return null;
		}
		itemDao.saveOrUpdate(item);
		return itemDao.list();

	}
	
	@RequestMapping(value = "/api/user/item/{id}", method = RequestMethod.GET)
	public @ResponseBody Item getItemById(@PathVariable("id") long id) {				
		return itemDao.get(id);
	}
	
	@RequestMapping(value = "/api/user/item/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List<Item> deleteItem(@PathVariable("id") long id,HttpServletResponse response) throws Exception {
		if(itemDao.get(id)!=null){
			itemDao.delete(id);
			return itemDao.list();
		}
		response.sendError(400);
		return null;
	}
	
}
