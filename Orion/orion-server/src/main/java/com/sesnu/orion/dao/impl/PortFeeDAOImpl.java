package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.web.model.PortFee;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class PortFeeDAOImpl implements PortFeeDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public PortFeeDAOImpl() {
	}

	public List<String> listAllAgency(){
		String hql = "select agency from PortFee order by agency asc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<String>) query.list();
	}
	
	public List<PortFee> listAll(){
		String hql = "from PortFee";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<PortFee>) query.list();
	}

	@Override
	public void saveOrUpdate(PortFee ship) {
		sessionFactory.getCurrentSession().saveOrUpdate(ship);				
	}


	@Override
	public PortFee get(long id) {
		return (PortFee) sessionFactory.getCurrentSession().get(PortFee.class, id);

	}


	@Override
	public void delete(PortFee ship) {
		sessionFactory.getCurrentSession().delete(ship);		
	}

	@Override
	public PortFee getByName(String agency) {
		String hql = "from PortFee where lower(agency) = :agency";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("agency", agency.toLowerCase());
	    List<PortFee> portFees = query.list();
		if(portFees.size()>0){
			return portFees.get(0);
		}
		return null;
	}



	
	
	
}

