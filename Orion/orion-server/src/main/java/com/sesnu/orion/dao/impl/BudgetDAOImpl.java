package com.sesnu.orion.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BudgetDAO;
import com.sesnu.orion.dao.SalesPlanDAO;
import com.sesnu.orion.web.model.Budget;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.SalesPlan;
import com.sesnu.orion.web.model.SalesView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class BudgetDAOImpl implements BudgetDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public BudgetDAOImpl() {
	}

	@Override
	public List<Budget> list(int year,String month) {
		String hql = null;Query query = null;
		if(month.equals("next6")){
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int mon1 = cal.get(Calendar.MONTH);
			int year1 = cal.get(Calendar.YEAR);
			cal.add(Calendar.MONTH, 6);
			int mon2 = cal.get(Calendar.MONTH);
			int year2 = cal.get(Calendar.YEAR);
			hql = "from SalesView where ";
			if (year1==year2){
				hql += "year =" + year1 + " and mon >=" + mon1 + " and mon <="+ mon2;
			}else{
				hql += "(year =" + year1 + " and mon >=" + mon1 + " and mon <=12) or (year =" + year2 + " and mon >=1 and mon <="+mon2+")";
			}
			System.out.println(hql);
			query = sessionFactory.getCurrentSession().createQuery(hql);
		}else{
			hql = "from Budget where year = :year and month = :month";
			query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("year",year);
			query.setParameter("month",month);
		}
		
		return (List<Budget>) query.list();

	}	
	
	@Override
	public List<Budget> getPlanByTime(int year,String month) {

		String hql = "from Budget where year = :year and month = :month";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year",year);
		query.setParameter("month",month);
		return (List<Budget>) query.list();
	}	
	
	@Override
	public Budget get(long id) {
		return (Budget) sessionFactory.getCurrentSession().get(Budget.class, id);
	}

	@Override
	public void saveOrUpdate(Budget budget) {
		sessionFactory.getCurrentSession().saveOrUpdate(budget);				
	}

	@Override
	public void delete(long id) {
		Budget itemToDelete = new Budget();
		itemToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(itemToDelete);		
	}
	
	@Override
	public void deleteByTime(int year, String month) {
		String hql = "delete from Budget where year = :year and month = :month";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year",year);
		query.setParameter("month",month);
		query.executeUpdate();		
	}

	@Override
	public boolean isExists(Budget budget) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLatest() {
		String hql = "select CONCAT(year,'-', month) from Budget order by year desc,mon desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setMaxResults(1);
		if(query.list().size()>0){
		return (String) query.list().get(0);
		}
		return null;
	}

	
	
}

