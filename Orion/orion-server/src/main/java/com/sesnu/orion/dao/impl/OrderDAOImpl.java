package com.sesnu.orion.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Item;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.OrderView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class OrderDAOImpl implements OrderDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public OrderDAOImpl() {
	}

	@Override
	public OrderView list(long id) {
		
		return (OrderView) sessionFactory.getCurrentSession().get(OrderView.class, id);

	}
	
	@Override
	public List<OrderView> listAll() {	
		String hql = "from OrderView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<OrderView>) query.list();

	}	
	
	@Override
	public List<OrderView> list(int year,int month) {
		
		String hql = "from OrderView " + getDateRange(year,month);
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setInteger("year",year)
		.setInteger("month",month);
		return (List<OrderView>) query.list();

	}	
	
	public List<OrderView> listByIdList(List<BigInteger> ids){
		if(ids==null){
			return new ArrayList<OrderView>();
		}
		String hql = "from OrderView where id in " + ids.toString().replace("[", "(").replace("]", ")");
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<OrderView>) query.list();
	}
	
	@Override
	public List<OrderView> getOrdersByInvNo(String invNo) {
		
		String hql = "from OrderView where lower(invNo) = :invNo";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setString("invNo",invNo.toLowerCase());
		return (List<OrderView>) query.list();

	}	
	
	private String getDateRange(Integer year, int month){
		if(month == 0 ){
			return "where date_part('year',created_on) = :year";
		}else{
			return "where date_part('year',created_on) = :year and date_part('month',created_on) = :month";
		}

	}
	
	@Override
	public List<OrderView> getOrderByTime(int year,String month) {

		String hql = "from Order OrderView year = :year and month = :month";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year",year);
		query.setParameter("month",month);
		return (List<OrderView>) query.list();
	}	
	
	@Override
	public OrderView get(long id) {
		return (OrderView) sessionFactory.getCurrentSession().get(OrderView.class, id);
	}

	@Override
	public void saveOrUpdate(Order order) {
		sessionFactory.getCurrentSession().saveOrUpdate(order);				
	}

	@Override
	public void delete(long id) {
		Order itemToDelete = new Order();
		itemToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(itemToDelete);		
	}
	
	@Override
	public void deleteByTime(int year, String month) {
		String hql = "delete from Order where year = :year and month = :month";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("year",year);
		query.setParameter("month",month);
		query.executeUpdate();		
	}

	@Override
	public boolean isExists(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLatest() {
		String sql = "select concat(date_part('year',created_on),'-',date_part('month',created_on)) from Orders order by created_on desc";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		.setMaxResults(1);
		if(query.list().size()>0){
			return (String) query.list().get(0);
		}
		return null;
	}

	
	@Override
	public List<BigInteger> newOrdersList() {
		String sql = "select id from orders where id not in (select order_ref from shipping where (ata is null and eta > :today) or (ata > :today))";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setDate("today", new Date());
		if(query.list().size()>0){
			return (List<BigInteger>) query.list();
		}
		return null;
	}

	
	
}

