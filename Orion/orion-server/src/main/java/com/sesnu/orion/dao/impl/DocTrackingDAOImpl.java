package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.DocTrackingDAO;
import com.sesnu.orion.web.model.DocTracking;
import com.sesnu.orion.web.model.DocTrackingView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DocTrackingDAOImpl implements DocTrackingDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public DocTrackingDAOImpl() {
	}


	@Override
	public List<DocTrackingView> listAll() {
		String hql = "from DocTrackingView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<DocTrackingView>) query.list();
	}
	


	@Override
	public List<DocTrackingView> listByOrderId(long orderRef) {
		String hql = "from DocTrackingView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRef);
		return (List<DocTrackingView>) query.list();
	}


	@Override
	public DocTracking get(long id) {
		return (DocTracking) sessionFactory.getCurrentSession().get(DocTracking.class, id);

	}


	@Override
	public void saveOrUpdate(DocTracking docTrack) {
		sessionFactory.getCurrentSession().saveOrUpdate(docTrack);
		
	}


	@Override
	public void delete(DocTracking docTrack) {
		sessionFactory.getCurrentSession().delete(docTrack);	
		
	}


	
	
	
}

