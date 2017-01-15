package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.Order;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class BidDAOImpl implements BidDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public BidDAOImpl() {
	}


	@Override
	public List<BidView> list(long orderRefId) {
		String hql = "from BidView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<BidView>) query.list();
	}
	

	@Override
	public void saveOrUpdate(Bid bid) {
		sessionFactory.getCurrentSession().saveOrUpdate(bid);				
	}


	@Override
	public Bid get(long id) {
		return (Bid) sessionFactory.getCurrentSession().get(Bid.class, id);

	}


	@Override
	public void delete(Bid bid) {
		sessionFactory.getCurrentSession().delete(bid);		
	}


	@Override
	public List<Bid> getBidWinner(long orderRefId) {
		String hql = "from Bid where orderRef = :orderRefId and selected = true";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<Bid>) query.list();
	}


	@Override
	public List<Bid> setBidWinner(long bidId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<BidView> listAll() {
		String hql = "from BidView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<BidView>) query.list();
	}	
	
	
	
	
}

