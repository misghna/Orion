package com.sesnu.orion.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.web.model.User;

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
		
//		List<User> listUser = (List<User>) sessionFactory.getCurrentSession()
//				.createCriteria(User.class)
//				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//
//		return listUser;
		return sessionFactory.getCurrentSession()
				.createCriteria(User.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
	}

	@Override
	@Transactional
	public User getUserByName(String username) {
		String hql = "from User where username= :username";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("username", username);
		
		@SuppressWarnings("unchecked")
		List<?> list = query.list();
		
		if (list != null && !list.isEmpty()) {
			return (User) list.get(0);
		}
		
		return null;
	}
	
//	@Override
//	@Transactional
//	public void saveOrUpdate(User user) {
//		sessionFactory.getCurrentSession().saveOrUpdate(user);
//	}
//
//	@Override
//	@Transactional
//	public void delete(int id) {
//		User userToDelete = new User();
//		userToDelete.setId(id);
//		sessionFactory.getCurrentSession().delete(userToDelete);
//	}
//
//	@Override
//	@Transactional
//	public User get(int id) {
//		String hql = "from User where id=" + id;
//		Query query = sessionFactory.getCurrentSession().createQuery(hql);
//		
//		@SuppressWarnings("unchecked")
//		List<User> listUser = (List<User>) query.list();
//		
//		if (listUser != null && !listUser.isEmpty()) {
//			return listUser.get(0);
//		}
//		
//		return null;
//	}
}