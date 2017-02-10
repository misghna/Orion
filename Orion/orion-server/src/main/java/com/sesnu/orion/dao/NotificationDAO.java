package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Notification;




public interface NotificationDAO {
	
	public List<Notification> listByOrderBl(long bl);
	
	public List<Notification> listAll();
	
	public void saveOrUpdate(Notification doc);

	public Notification get(long id);

	public void delete(Notification doc) ;
		
	public List<Notification> listUnnotified();
	


	
}