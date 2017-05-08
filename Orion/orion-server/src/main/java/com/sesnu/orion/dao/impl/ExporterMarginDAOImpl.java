package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.ExporterMarginDAO;
import com.sesnu.orion.web.model.ExporterQuote;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ExporterMarginDAOImpl implements ExporterMarginDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ExporterMarginDAOImpl() {
	}

	@Override
	public void saveOrUpdate(ExporterQuote exMargin) {
		sessionFactory.getCurrentSession().saveOrUpdate(exMargin);			
	}

	@Override
	public ExporterQuote get(long id) {
		return (ExporterQuote) sessionFactory.getCurrentSession().get(ExporterQuote.class, id);
	}

	@Override
	public ExporterQuote getByOrderRef(long orderRefId) {
		String hql = "from ExporterQuote where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		List list = query.list();
		if(list.size()>0){
			return (ExporterQuote) list.get(0);
		}
		return null;
	}

	@Override
	public void delete(ExporterQuote exMargin) {
		sessionFactory.getCurrentSession().delete(exMargin);	
		
	}

	@Override
	public List<ExporterQuote> listAll() {
		String hql = "from ExporterQuote";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		List list = query.list();
		return list;
	}




	
	
	
	
}

