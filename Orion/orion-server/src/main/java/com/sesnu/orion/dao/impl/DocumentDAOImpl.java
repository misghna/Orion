package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.web.model.Document;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DocumentDAOImpl implements DocumentDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public DocumentDAOImpl() {
	}


	@Override
	public List<Document> list(long orderRefId) {
		String hql = "from Document where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<Document>) query.list();
	}
	
	@Override
	public List<Document> listByDocType(String docType) {
		String hql = "from Document where type = :docType";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setParameter("docType",docType);
		return (List<Document>) query.list();
	}


	@Override
	public void saveOrUpdate(Document doc) {
		sessionFactory.getCurrentSession().saveOrUpdate(doc);				
	}


	@Override
	public Document get(long id) {
		return (Document) sessionFactory.getCurrentSession().get(Document.class, id);

	}


	@Override
	public void delete(Document doc) {
		sessionFactory.getCurrentSession().delete(doc);		
	}



	
	
	
}

