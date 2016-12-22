package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.PaymentDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.PayView;
import com.sesnu.orion.web.model.Payment;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class PaymentDAOImpl implements PaymentDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public PaymentDAOImpl() {
	}


	@Override
	public List<PayView> listByOrderRef(long orderRefId) {
		String hql = "from PayView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<PayView>) query.list();
	}

	@Override
	public List<PayView> listAll() {
		String hql = "from PayView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<PayView>) query.list();
	}

	@Override
	public void saveOrUpdate(Payment pay) {
		sessionFactory.getCurrentSession().saveOrUpdate(pay);				
	}


	@Override
	public Payment get(long id) {
		return (Payment) sessionFactory.getCurrentSession().get(Payment.class, id);

	}


	@Override
	public void delete(Payment pay) {
		sessionFactory.getCurrentSession().delete(pay);		
	}



	
	
	
}

