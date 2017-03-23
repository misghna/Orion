package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.AddressBookDAO;
import com.sesnu.orion.dao.DuLicenseDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;
import com.sesnu.orion.web.model.Order;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AddressBookDAOImpl implements AddressBookDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public AddressBookDAOImpl() {
	}

	
	@Override
	public List<AddressBook> listAllDest() {
		String hql = "from AddressBook where type = 'Client' or type = 'WH'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<AddressBook>) query.list();
	}
	
	@Override
	public List<AddressBook> listAllFWAgents() {
		String hql = "from AddressBook where type = 'For. Agent'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<AddressBook>) query.list();
	}

	@Override
	public List<AddressBook> listAll() {
		String hql = "from AddressBook";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<AddressBook>) query.list();
	}

	
	@Override
	public AddressBook getByName(String name) {
		String hql = "from AddressBook where name = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("name", name);
		if(query.list().size()>0){
			return (AddressBook) query.list().get(0);
		}
		return null;
	}

	@Override
	public List<AddressBook> getByType(String type) {
		String hql = "from AddressBook where type = :type";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("type", type);
		if(query.list().size()>0){
			return (List<AddressBook>) query.list();
		}
		return null;
	}

	@Override
	public AddressBook get(long id) {
		return (AddressBook) sessionFactory.getCurrentSession().get(AddressBook.class, id);
	}


	@Override
	public void saveOrUpdate(AddressBook client) {
		sessionFactory.getCurrentSession().saveOrUpdate(client);
		
	}


	@Override
	public void delete(AddressBook client) {
		sessionFactory.getCurrentSession().delete(client);	
		
	}	
	
	
	
	
}

