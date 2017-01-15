package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.DuLicenseDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.DuLicense;
import com.sesnu.orion.web.model.DuLicenseView;
import com.sesnu.orion.web.model.Order;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DuLicenseDAOImpl implements DuLicenseDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public DuLicenseDAOImpl() {
	}


	@Override
	public List<DuLicenseView> listAll() {
		String hql = "from DuLicenseView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<DuLicenseView>) query.list();
	}
	


	@Override
	public List<DuLicenseView> listByOrderId(long orderRef) {
		String hql = "from DuLicenseView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRef);
		return (List<DuLicenseView>) query.list();
	}


	@Override
	public DuLicense get(long id) {
		return (DuLicense) sessionFactory.getCurrentSession().get(DuLicense.class, id);

	}


	@Override
	public void saveOrUpdate(DuLicense lic) {
		sessionFactory.getCurrentSession().saveOrUpdate(lic);
		
	}


	@Override
	public void delete(DuLicense lic) {
		sessionFactory.getCurrentSession().delete(lic);	
		
	}	
	
	
	
	
}

