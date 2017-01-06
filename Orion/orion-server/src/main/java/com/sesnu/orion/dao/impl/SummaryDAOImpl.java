package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.SummaryDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Summary;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SummaryDAOImpl implements SummaryDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public SummaryDAOImpl() {
	}


	
	@Override
	public List<Summary> listAll(String state) {	
		String hql = "from Summary";
		if(state.equals("active")){
			hql = hql + " where progress < 100";
		}
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Summary>) query.list();

	}	
	
	@Override
	public List<Summary> list(int year,int month) {
		
		String hql = "from Or ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setInteger("year",year)
		.setInteger("month",month);
		return (List<Summary>) query.list();

	}



	@Override
	public List<Summary> getOrderByTime(int year, String month) {
		// TODO Auto-generated method stub
		return null;
	}	
	

	
	
}

