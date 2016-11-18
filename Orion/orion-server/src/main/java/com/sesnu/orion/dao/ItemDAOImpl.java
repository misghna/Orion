package com.sesnu.orion.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.User;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ItemDAOImpl implements ItemDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ItemDAOImpl() {
	}
	
	@Override
	@Transactional
	public List<Item> list() {
		
		return sessionFactory.getCurrentSession()
				.createCriteria(Item.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}
	
	@Override
	@Transactional
	public void delete(long id) {
		Item itemToDelete = new Item();
		itemToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(itemToDelete);
	}



	
	@Override
	public List<Item> getItemByProductName(String productName) {
//		String hql = "from Item where email= :email";
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setParameter("email", email);
//		
//		List<?> list = query.list();
//		
//		if (list != null && !list.isEmpty()) {
//			return (User) list.get(0);
//		}
		
		return null;
	}

	@Override
	public Item getItemByProductHsCode(long hsCode) {
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
	public Item get(long id) {
		return (Item) sessionFactory.getCurrentSession().get(Item.class, id);
	}

	@Override
	public void saveOrUpdate(Item item) {
		sessionFactory.getCurrentSession().saveOrUpdate(item);		
	}
}