package com.sesnu.orion.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.GenericDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.utility.Util;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class GenericDAOImpl implements GenericDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public GenericDAOImpl() {
	}
	
	@Override
	public List<?> list(String tableName, String rev) {
		Date date = null;
		
		if(rev.equals("latest")){
			List<String> revisions = getRevisions(tableName);
			if(revisions.size()>0){
				rev = revisions.get(0);
			}
		}
		
		date = Util.parseDate(rev);
		String hql = "from "+tableName+" where revision = :rev";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setDate("rev",date);
		
		return query.list();

	}
	
	@Override
	public Item get(String tableName, long id) {
		return (Item) sessionFactory.getCurrentSession().get(Item.class, id);
	}
	

	@Override
	public List<String> getRevisions(String tableName) {
		String hql = "select distinct revision from "+tableName+" order by revision desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);		
		List<Date> itemList = (List<Date>) query.list();
		List<String> revList = new ArrayList<String>();	
		for (Date date : itemList) {
			revList.add(Util.parseDate(date));
		}		
		return revList;
	}
	

	@Override
	public List<Item> getItemByName(String tableName, String name) {
		String hql = "from "+tableName+" where name= :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("name", name);
		
		List<Item> list = query.list();
		
		if (list != null && !list.isEmpty()) {
			return list;
		}
		
		return null;
	}

	@Override
	public Item getItemByHsCode(String tableName, long hsCode) {
		String hql = "from "+tableName+" where hscode= :hscode";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("hscode",hsCode);
			
		List<Item> itemList = (List<Item>) query.list();
		
		if (itemList != null && !itemList.isEmpty()) {
			return itemList.get(0);
		}
		
		return null;
	}


	@Override
	public boolean isExists(String tableName, String name, long hsCode) {
		String hql = "from "+tableName+" where hscode= :hscode or lower(name)= :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("hscode",hsCode);
		query.setParameter("name",name.toLowerCase());
			
		List<Item> itemList = (List<Item>) query.list();
		
		if (itemList != null && !itemList.isEmpty()) {
			System.out.println("===========> " +itemList.get(0).getId() + "  " +   itemList.size());
			return itemList.size()>0;
		}
		return false;
	}

	@Override
	public void saveOrUpdate(String tableName, Object item) {
		sessionFactory.getCurrentSession().saveOrUpdate(item);		
	}
	
	@Override
	@Transactional
	public void delete(String tableName, long id) {
		Item itemToDelete = new Item();
		itemToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(itemToDelete);
	}
}

