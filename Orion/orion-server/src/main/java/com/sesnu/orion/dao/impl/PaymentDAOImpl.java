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
	
	
	public List getPaymentHistogram(int year,String dest){
		String destQry ="";
		if(!dest.equals("All")){
			String hql = "select distinct id from Order where destinationPort = :destinationPort";
			Query query = sessionFactory.getCurrentSession().createQuery(hql)
			.setString("destinationPort",dest);
			List<Long> destIds = (List<Long>) query.list();
			if(destIds.size()>0){
				destQry = " and orderRef in " + destIds.toString().replace("[", "(").replace("]", ")");
			}else{
				return null;
			}
			
		}
		
		String hql = "select sum(paymentAmount) as amount, month,curr from PayView where year = :year " + destQry + " group by month,curr";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setInteger("year",year);
		return query.list();
	}

	


	
	
	
}

