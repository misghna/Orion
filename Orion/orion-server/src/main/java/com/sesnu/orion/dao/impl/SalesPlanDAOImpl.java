package com.sesnu.orion.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SalesPlanDAOImpl implements SalesPlanDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public SalesPlanDAOImpl() {
	}

	@Override
	public List<SalesView> list(int year,String month) {
		String hql = null;Query query = null;
		if(month.equals("next3")){
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int mon = cal.get(Calendar.MONTH);int mon3 = mon+3;
			int year2 = cal.get(Calendar.YEAR);
			hql = "from SalesView where year = " + year2 + " and mon >= " + mon + " and mon <" + mon3 ;
			query = sessionFactory.getCurrentSession().createQuery(hql);
		}else{
			hql = "from SalesView where year = :year and month = :month";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("year",year);
			query.setParameter("month",month);
		}
		
		return (List<SalesView>) query.list();

	}	
	
	@Override
	public List<SalesPlan> getPlanByTime(int year,String month) {

		String hql = "from SalesPlan where year = :year and month = :month";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year",year);
		query.setParameter("month",month);
		return (List<SalesPlan>) query.list();
	}	
	
	@Override
	public SalesPlan get(long id) {
		return (SalesPlan) sessionFactory.getCurrentSession().get(SalesPlan.class, id);
	}

	@Override
	public void saveOrUpdate(SalesPlan salesPlan) {
		sessionFactory.getCurrentSession().saveOrUpdate(salesPlan);				
	}

	@Override
	public void delete(long id) {
		SalesPlan itemToDelete = new SalesPlan();
		itemToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(itemToDelete);		
	}
	
	@Override
	public void deleteByTime(int year, String month) {
		String hql = "delete from SalesPlan where year = :year and month = :month";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year",year);
		query.setParameter("month",month);
		query.executeUpdate();		
	}

	@Override
	public boolean isExists(SalesPlan salesPlan) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLatest() {
		String hql = "select CONCAT(year,'-', month) from SalesPlan order by year desc,mon desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setMaxResults(1);
		if(query.list().size()>0){
		return (String) query.list().get(0);
		}
		return null;
	}

	
	
}

