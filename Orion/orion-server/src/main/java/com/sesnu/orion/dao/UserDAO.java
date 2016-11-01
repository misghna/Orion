package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.User;



public interface UserDAO {
	public List<User> list();
	
	public User getUserByName(String username);
	
//	public User get(int id);
//	
//	public void saveOrUpdate(User user);
//	
//	public void delete(int id);
}