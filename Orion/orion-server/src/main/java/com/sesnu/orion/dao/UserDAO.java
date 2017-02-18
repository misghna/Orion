package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.User;



public interface UserDAO {
	public List<User> list();
	
	public User getUserByEmail(String email);
	
	public User getUserName(String name);
	
	public User get(long id);

	public void saveOrUpdate(User user);
	
	public void delete(long id);
	
	public List<String> getApprovers(String type);
	
	public List<Long> getAllowedOrderIds(String importers);
	
}