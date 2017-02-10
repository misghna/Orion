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
import com.sesnu.orion.dao.NotificationDAO;
import com.sesnu.orion.web.model.DocHandover;
import com.sesnu.orion.web.model.DocView;
import com.sesnu.orion.web.model.Document;
import com.sesnu.orion.web.model.Notification;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class NotificationDAOImpl implements NotificationDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public NotificationDAOImpl() {
	}


	@Override
	public List<Notification> listAll() {
		String hql = "from Notification";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Notification>) query.list();
	}
	
	
	@Override
	public List<Notification> listUnnotified() {
		String hql = "from Notification where notified = false";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Notification>) query.list();
	}


	@Override
	public List<Notification> listByOrderBl(long bl) {
		String hql = "from Notification where bl= :bl";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setParameter("bl",bl);
		return (List<Notification>) query.list();
	}
	




	@Override
	public void saveOrUpdate(Notification doc) {
		sessionFactory.getCurrentSession().saveOrUpdate(doc);
		
	}


	@Override
	public Notification get(long id) {
		return (Notification) sessionFactory.getCurrentSession().get(Notification.class, id);

	}


	@Override
	public void delete(Notification doc) {
		sessionFactory.getCurrentSession().delete(doc);
		
	}



	
	
	
}

