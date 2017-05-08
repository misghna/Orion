package com.sesnu.orion.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.StatusDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderStat;
import com.sesnu.orion.web.model.Required;
import com.sesnu.orion.web.model.Status;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatusDAOImpl implements StatusDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public StatusDAOImpl() {
	}


	@Override
	public List<Status> list(long orderRefId) {
		String hql = "from Status where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<Status>) query.list();
	}


	@Override
	public List<Status> list(long orderRef, String type) {
		String hql = "from Status where orderRef = :orderRefId and type = :type";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRef)
		.setString("type", type);
		return (List<Status>) query.list();
	}
	
	@Override
	public List<String> listName(long orderRef, String type) {
		String hql = "select name from Status where orderRef = :orderRefId and type = :type";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRef)
		.setString("type", type);
		return (List<String>) query.list();
	}


	@Override
	public List<Required> listRequired(String type,String itemType) {
		String hql = "from Required where type = :type and (itemType = :itemType or itemType = 'all') order by id desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setString("itemType",itemType)
		.setString("type", type);
		return (List<Required>) query.list();
	}
	
	@Override
	public List<Required> listRequiredByType(String type) {
		String hql = "from Required where type = :type order by id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setString("type", type);
		return (List<Required>) query.list();
	}
	
	@Override
	public List<Required> listAllRequired() {
		String hql = "from Required order by type";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Required>) query.list();
	}


	@Override
	public List<OrderStat> listOrderStat(long orderRef) {
		String hql = "from OrderStat where orderRef = :orderRef";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRef",orderRef);
		return (List<OrderStat>) query.list();
	}

	
}

