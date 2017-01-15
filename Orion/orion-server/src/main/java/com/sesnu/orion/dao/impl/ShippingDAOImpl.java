package com.sesnu.orion.dao.impl;

import java.math.BigInteger;
import java.util.Date;
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
	
	@Override
	public List<ShippingView> listByBL(String bl) {
		String hql = "from ShippingView where lower(bl) = :bl";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setString("bl",bl.toLowerCase());
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

	public List<BigInteger> inTransitList(){
		String sql = "select order_ref from shipping where (ata is null and eta > :today) or (ata > :today)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setDate("today", new Date());
		if(query.list().size()>0){
			return (List<BigInteger>) query.list();
		}
		return null;
	}
	
	public List<BigInteger> inPortList(){
		String sql = "select order_ref as t from shipping where (ata is null and (eta - :today)<=0 and (eta - :today)>=-5) or "
				+ "((ata - :today)<=0 and (ata - :today)>=-5)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setDate("today", new Date());
		if(query.list().size()>0){
			return (List<BigInteger>) query.list();
		}
		return null;
	}
	
	public List<BigInteger> inTerminalList(){
		String sql = "	select order_ref from shipping where ((ata is null and (eta - :today)<-5) or  (ata - :today)<-5) "
				+ "and order_ref not in (select order_ref from payment where name ='Terminal')";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setDate("today", new Date());
		if(query.list().size()>0){
			return (List<BigInteger>) query.list();
		}
		return null;
	}
	
	@Override
	public BigInteger InTransitCount(long itemId) {
		String sql = "SELECT count(*) from orders o left join shipping s on o.id=s.order_ref "
				+ "where o.item_id = :itemId and s.bl is not null and ((s.ata is not null and s.ata < :today) or  (s.eta < :today))";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		.setLong("itemId",itemId)
		.setDate("today", new Date());		
		return  (BigInteger) query.list().get(0);
	}
	
	@Override
	public BigInteger InPortCount(long itemId) {
		String sql = "SELECT count(*) from orders o left join shipping s on o.id=s.order_ref where o.item_id = :itemId and s.bl is not null"
				+ " and ((s.ata is not null and s.ata - :today <= 0 and s.ata - :today >=-5) or  (s.eta - :today <= 0 and s.eta - :today >=-5))";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		.setLong("itemId",itemId)
		.setDate("today", new Date());
		return  (BigInteger) query.list().get(0);
	}

	@Override
	public BigInteger InTerminalCount(long itemId) {
		String sql = "SELECT count(*) from orders o left join shipping s on o.id=s.order_ref where o.item_id = :itemId and s.bl is not null"
				+ " and ((s.ata is not null and s.ata - :today < -5) or  (s.eta - :today < -5)) and o.id not in (select order_ref from payment where name ='Terminal')";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
		.setLong("itemId",itemId)
		.setDate("today", new Date());
		return  (BigInteger) query.list().get(0);
	}
	
}

