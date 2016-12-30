package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.ApprovalDAO;
import com.sesnu.orion.web.model.Approval;
import com.sesnu.orion.web.model.ApprovalView;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ApprovalDAOImpl implements ApprovalDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public ApprovalDAOImpl() {
	}


	@Override
	public List<ApprovalView> listByOrderRef(long orderRefId) {
		String hql = "from ApprovalView where orderRef = :orderRefId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("orderRefId",orderRefId);
		return (List<ApprovalView>) query.list();
	}
	
	@Override
	public List<ApprovalView> listByApproverName(String name) {
		String hql = "from ApprovalView where lower(name) = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setString("name",name.toLowerCase());
		return (List<ApprovalView>) query.list();
	}

	public List<ApprovalView> listAll(){
		String hql = "from ApprovalView";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<ApprovalView>) query.list();
	}
	
	public List<ApprovalView> listByTypeId(String type,long forId){
		String hql = "from ApprovalView where type= :type and forId= :forId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("type", type)
				.setLong("forId", forId);
		return (List<ApprovalView>) query.list();
	}

	@Override
	public void saveOrUpdate(Approval aprv) {
		sessionFactory.getCurrentSession().saveOrUpdate(aprv);				
	}


	@Override
	public Approval get(long id) {
		return (Approval) sessionFactory.getCurrentSession().get(Approval.class, id);

	}


	@Override
	public void delete(Approval ship) {
		sessionFactory.getCurrentSession().delete(ship);		
	}

	
	
}

