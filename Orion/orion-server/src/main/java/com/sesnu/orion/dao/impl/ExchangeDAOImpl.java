package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.ExchangeDAO;
import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.web.model.Currency;
import com.sesnu.orion.web.model.Exchange;
import com.sesnu.orion.web.model.PortFee;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ExchangeDAOImpl implements ExchangeDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ExchangeDAOImpl() {
	}


	
	public List<Exchange> listAll(){
		String hql = "from Exchange";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Exchange>) query.list();
	}

	@Override
	public void saveOrUpdate(Exchange curr) {
		sessionFactory.getCurrentSession().saveOrUpdate(curr);				
	}


	@Override
	public Exchange get(long id) {
		return (Exchange) sessionFactory.getCurrentSession().get(Exchange.class, id);
	}

	@Override
	public Exchange getByCurrency(Exchange curr) {
		String hql = "from Exchange where name = :name and type = :type and fromCurrency= :fromCurrency and toCurrency = :toCurrency";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("name", curr.getName())
				.setString("type", curr.getType())
				.setString("fromCurrency",curr.getFromCurrency())
				.setString("toCurrency", curr.getToCurrency());
	    List<Exchange> currency = query.list();
		if(currency.size()>0){
			return currency.get(0);
		}
		return null;
	}

	@Override
	public void delete(Exchange curr) {
		sessionFactory.getCurrentSession().delete(curr);		
	}



	@Override
	public Currency getByAbrev(String abrev) {
		String hql = "from Currency where abrevation = :abrevation";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("abrevation",abrev);
		List<Currency> currencies =  (List<Currency>) query.list();
		if(currencies.size()>0){
			return currencies.get(0);
		}
		return null;
	}



	@Override
	public List<Currency> listAllCurrencies() {
		String hql = "from Currency";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Currency>) query.list();
	}





	
	
	
}

