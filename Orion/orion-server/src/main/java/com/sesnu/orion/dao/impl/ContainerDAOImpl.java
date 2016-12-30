package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.ContainerDAO;
import com.sesnu.orion.web.model.Container;
import com.sesnu.orion.web.model.ContainerView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ContainerDAOImpl implements ContainerDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ContainerDAOImpl() {
	}


	@Override
	public List<ContainerView> listByOrderId(long orderRefId) {
		String hql = "from ContainerView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<ContainerView>) query.list();
	}
	

	public List<ContainerView> listAll(){
		String hql = "from ContainerView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<ContainerView>) query.list();
	}

	@Override
	public void saveOrUpdate(Container ship) {
		sessionFactory.getCurrentSession().saveOrUpdate(ship);				
	}


	@Override
	public Container get(long id) {
		return (Container) sessionFactory.getCurrentSession().get(Container.class, id);

	}


	@Override
	public void delete(Container ship) {
		sessionFactory.getCurrentSession().delete(ship);		
	}



	
	
	
}

