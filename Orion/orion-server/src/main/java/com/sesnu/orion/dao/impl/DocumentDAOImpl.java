package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.web.model.DocView;
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
	public List<DocView> listByOrderRef(long orderRefId) {
		String hql = "from DocView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<DocView>) query.list();
	}
	
	@Override
	public List<DocView> listByDocType(String docType) {
		String hql = "from DocView where type = :docType";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setParameter("docType",docType);
		return (List<DocView>) query.list();
	}
	
	@Override
	public List<Document> listByDocTypeOrderRef(long orderRef,String docType) {
		String hql = "from Document where orderRef= :orderRef and type = :docType";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setParameter("docType",docType)
		.setParameter("orderRef",orderRef);		
		return (List<Document>) query.list();
	}

	@Override
	public List<DocView> listAll() {
		String hql = "from DocView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<DocView>) query.list();
	}
	
	@Override
	public void saveOrUpdate(Document doc) {
		sessionFactory.getCurrentSession().saveOrUpdate(doc);				
	}


	@Override
	public DocView get(long id) {
		return (DocView) sessionFactory.getCurrentSession().get(DocView.class, id);

	}


	@Override
	public void delete(DocView doc) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("delete from doc where id= :id");
		query.setParameter("id", doc.getId());
		query.executeUpdate();
	}



	
	
	
}

