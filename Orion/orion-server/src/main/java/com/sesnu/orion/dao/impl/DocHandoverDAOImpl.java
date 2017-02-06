package com.sesnu.orion.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.DocHandoverDAO;
import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.web.model.DocHandover;
import com.sesnu.orion.web.model.DocView;
import com.sesnu.orion.web.model.Document;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DocHandoverDAOImpl implements DocHandoverDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public DocHandoverDAOImpl() {
	}


	@Override
	public List<DocHandover> listAll() {
		String hql = "from DocHandover";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<DocHandover>) query.list();
	}
	
	
	@Override
	public List<DocHandover> listUnconfirmed() {
		String hql = "from DocHandover where status = 'Not Confirmed'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<DocHandover>) query.list();
	}


	@Override
	public List<DocHandover> listByOrderBl(long bl) {
		String hql = "from DocHandover where bl= :bl";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setParameter("bl",bl);
		return (List<DocHandover>) query.list();
	}
	

	@Override
	public void saveOrUpdate(DocHandover doc) {
		sessionFactory.getCurrentSession().saveOrUpdate(doc);
		
	}


	@Override
	public DocHandover get(long id) {
		return (DocHandover) sessionFactory.getCurrentSession().get(DocHandover.class, id);

	}


	@Override
	public void delete(DocHandover doc) {
		sessionFactory.getCurrentSession().delete(doc);
		
	}



	
	
	
}

