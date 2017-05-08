package com.sesnu.orion.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.AccessHistoryDAO;
import com.sesnu.orion.web.model.AccessHistory;
import com.sesnu.orion.web.model.AccessView;
import com.sesnu.orion.web.model.Location;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AccessHistoryDAOImpl implements AccessHistoryDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public AccessHistoryDAOImpl() {
	}

	@Override
	public List<AccessHistory> listByUserId(long userId) {
		String hql = "from AccessHistory where userId = :userId";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
		.setLong("userId",userId);
		return (List<AccessHistory>) query.list();
	}

	@Override
	public void save(AccessHistory access) {
		if(access.getIp().startsWith("0:")) return;
		access.setAccessTime(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(access);			
	}

	@Override
	public void update(AccessHistory access) {
		
		sessionFactory.getCurrentSession().saveOrUpdate(access);	
		
	}
	
	@Override
	public List<AccessHistory> getUnCoordinated() {
		String hql = "from AccessHistory where lat is null and lng is null";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<AccessHistory>) query.list();
	}

	@Override
	public AccessHistory get(long id) {
		return (AccessHistory) sessionFactory.getCurrentSession().get(AccessHistory.class, id);
	}

	@Override
	public void delete(AccessHistory access) {
		sessionFactory.getCurrentSession().delete(access);	
		
	}

	@Override
	public List<AccessHistory> listAll() {
		String hql = "from AccessHistory";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<AccessHistory>) query.list();
	}

	@Override
	public List<Location> listAllLocations() {
		String hql = "from Locations";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Location>) query.list();
	}

	@Override
	public void saveLocation(Location loc) {
		loc.setUpdatedOn(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(loc);		
	}

	@Override
	public List<Location> listFailedLocations() {
		String hql = "from Location where lat is null and lng is null and trials < 4";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Location>) query.list();
	}

	@Override
	public List<String> listNewIps() {
		String sql = "select distinct ip from access_history where ip not in (select distinct ip from locations where (lat is not null and lng is not null) or trials < 3)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		return (List<String>) query.list();
	}

	@Override
	public List<AccessView> getAllAccess() {
		String hql = "from AccessView where lat is not null and lng is not null";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<AccessView>) query.list();
	}

	@Override
	public List<AccessView> getAccessByUser(String userName) {
		String hql = "from AccessView where fullname = :fullname and lat is not null and lng is not null";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("fullname", userName);
		return (List<AccessView>) query.list();
	}

	

	
	
}

