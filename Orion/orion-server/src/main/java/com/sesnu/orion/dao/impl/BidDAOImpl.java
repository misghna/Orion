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
import com.sesnu.orion.web.model.PayView;
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
	public Bid getBidWinner(long orderRefId) {
		String hql = "from Bid where orderRef = :orderRefId and selected = true";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		if(query.list().size()>0){
			return (Bid) query.list().get(0);
		}
		return null;
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
	
	public List getItemCostHistogram(int year,String dest){
//		String hql = "select sum(totalBid), to_char(estDueDate, 'MM') as month,currency from Bid";
//		hql += " where EXTRACT(year FROM 'estDueDate') = :year and approval = 'Approved'";
//		if(!dest.equals("ALL")){
//			hql += " and order_ref in (select id from Order where destinationPort= :desPort)";		 
//		}
//		hql += " group by month, currency order by month asc";
//		
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		query.setInteger("year", year);
//		
//		if(!dest.equals("ALL")){
//			query.setString("desPort", dest);
//		}
		
		String sql = "select sum(total_bid), to_char(estimated_due_date, 'MM') as month,currency from bid"; 
		sql += " where EXTRACT(year FROM \"estimated_due_date\") = :year and approval = 'Approved'";
		if(!dest.equals("All")){
			sql += " and order_ref in (select id from orders where destination_port= :destPort)";
		}
		sql += " group by month, currency";
		sql += " order by month asc";
	
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setInteger("year", year);
		
		if(!dest.equals("All")){
			query.setString("destPort", dest);
		}
		return query.list();
	}
	

	
	
}

