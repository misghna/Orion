package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.ClientDAO;
import com.sesnu.orion.dao.DuLicenseDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.Client;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;
import com.sesnu.orion.web.model.Order;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ClientDAOImpl implements ClientDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ClientDAOImpl() {
	}


	@Override
	public List<Client> listAll() {
		String hql = "from Client";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Client>) query.list();
	}



	@Override
	public Client get(long id) {
		return (Client) sessionFactory.getCurrentSession().get(Client.class, id);

	}


	@Override
	public void saveOrUpdate(Client client) {
		sessionFactory.getCurrentSession().saveOrUpdate(client);
		
	}


	@Override
	public void delete(Client client) {
		sessionFactory.getCurrentSession().delete(client);	
		
	}	
	
	
	
	
}

