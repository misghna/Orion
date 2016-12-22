package com.sesnu.orion.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.ItemDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.utility.Util;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ItemDAOImpl implements ItemDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ItemDAOImpl() {
	}
	
	@Override
	public List<Item> list(String rev) {
		Date date = null;
		
		if(rev.equals("latest")){
			List<String> revisions = getRevisions();
			if(revisions.size()>0){
				rev = revisions.get(0);
			}
		}
		
		date = Util.parseDate(rev);
		System.out.println("**********************************"  +   date.toString());
		String hql = "from Item where revision = :rev";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setDate("rev",date);
		
		List<Item> itemList = (List<Item>) query.list();

		return itemList;
	}
	
	@Override
	public Item get(long id) {
		return (Item) sessionFactory.getCurrentSession().get(Item.class, id);
	}
	

	@Override
	public List<String> getRevisions() {
		String hql = "select distinct revision from Item order by revision desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);		
		List<Date> itemList = (List<Date>) query.list();
		List<String> revList = new ArrayList<String>();	
		for (Date date : itemList) {
			revList.add(Util.parseDate(date));
		}		
		return revList;
	}
	
	@Override
	public List<String> getNameList() {
		String hql = "select distinct name from Item order by name asc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);		
		return  (List<String>) query.list();
	}
	
	@Override
	public List<String> getNameBrandList() {
		String hql = "select distinct concat('(', i.id, ') ',  i.name, ' [' , i.brand ,']') as fullname from Item i order by fullname asc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);		
		return  (List<String>) query.list();
	}
	
	@Override
	public List<String> getBrandList() {
		String hql = "select distinct brand from Item order by brand asc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);		
		return  (List<String>) query.list();
	}

	@Override
	public List<Item> getItemByName(String name) {
		String hql = "from Item where name= :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("name", name);
		
		List<Item> list = query.list();
		
		if (list != null && !list.isEmpty()) {
			return list;
		}
		
		return null;
	}

	@Override
	public Item getItemByHsCode(long hsCode) {
		String hql = "from Item where hscode= :hscode";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("hscode",hsCode);
			
		List<Item> itemList = (List<Item>) query.list();
		
		if (itemList != null && !itemList.isEmpty()) {
			return itemList.get(0);
		}
		
		return null;
	}


	@Override
	public boolean isExists(String name, String brand) {
		String hql = "from Item where lower(brand)= :brand or lower(name)= :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("brand",brand);
		query.setParameter("name",name.toLowerCase());
			
		List<Item> itemList = (List<Item>) query.list();
		
		if (itemList != null && !itemList.isEmpty()) {
			System.out.println("===========> " +itemList.get(0).getId() + "  " +   itemList.size());
			return itemList.size()>0;
		}
		return false;
	}

	@Override
	public void saveOrUpdate(Item item) {
		sessionFactory.getCurrentSession().saveOrUpdate(item);		
	}
	
	@Override
	@Transactional
	public void delete(long id) {
		Item itemToDelete = new Item();
		itemToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(itemToDelete);
	}

	@Override
	public void deleteByRev(String rev) {
		String hql = "delete from Item where revision= :rev";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		Date revDate = Util.parseDate(rev);
		query.setParameter("rev",revDate);
		query.executeUpdate();				
	}
	
}

