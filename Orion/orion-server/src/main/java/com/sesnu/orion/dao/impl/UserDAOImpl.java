package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.User;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public UserDAOImpl() {
	}
	
	@Override
	@Transactional
	public List<User> list() {
		
		return sessionFactory.getCurrentSession()
				.createCriteria(User.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@Override
	@Transactional
	public List<String> getApprovers(String type) {
		String hql = "select fullname from User where approver like :type";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("type", "%" + type + "%");		
		List<?> list = query.list();		
		if (list != null && !list.isEmpty()) {
			return (List<String>) list;
		}		
		return null;
	}
	
	@Override
	@Transactional
	public User getUserByEmail(String email) {
		String hql = "from User where lower(email)= :email";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("email", email.toLowerCase());
		
		List<?> list = query.list();
		
		if (list != null && !list.isEmpty()) {
			return (User) list.get(0);
		}

		return null;
	}
	
	@Override
	@Transactional
	public void saveOrUpdate(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}


	
	@Override
	@Transactional
	public void delete(long id) {
		User userToDelete = new User();
		userToDelete.setId(id);
		sessionFactory.getCurrentSession().delete(userToDelete);
	}

	@Override
	@Transactional
	public User get(long id) {
		String hql = "from User where id= :id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("id", id);
			
		List<User> listUser = (List<User>) query.list();
		
		if (listUser != null && !listUser.isEmpty()) {
			return listUser.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public List<Long> getAllowedOrderIds(String importers) {
		importers = importers.replace("[", "(").replace("]", ")").replace("\"", "'");
		String sql = "select id from orders where importer in " + importers;
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		return query.list();
		
	}
	
}