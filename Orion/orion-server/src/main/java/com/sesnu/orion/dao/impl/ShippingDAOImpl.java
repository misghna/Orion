package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.ShippingDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Shipping;
import com.sesnu.orion.web.model.ShippingView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ShippingDAOImpl implements ShippingDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ShippingDAOImpl() {
	}


	@Override
	public List<ShippingView> listByOrderId(long orderRefId) {
		String hql = "from ShippingView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<ShippingView>) query.list();
	}
	

	public List<ShippingView> listAll(){
		String hql = "from ShippingView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<ShippingView>) query.list();
	}

	@Override
	public void saveOrUpdate(Shipping ship) {
		sessionFactory.getCurrentSession().saveOrUpdate(ship);				
	}


	@Override
	public Shipping get(long id) {
		return (Shipping) sessionFactory.getCurrentSession().get(Shipping.class, id);

	}


	@Override
	public void delete(Shipping ship) {
		sessionFactory.getCurrentSession().delete(ship);		
	}



	
	
	
}

