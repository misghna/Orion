package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.BidDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.TerminalDAO;
import com.sesnu.orion.web.model.Bid;
import com.sesnu.orion.web.model.BidView;
import com.sesnu.orion.web.model.Order;
import com.sesnu.orion.web.model.Terminal;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TerminalDAOImpl implements TerminalDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public TerminalDAOImpl() {
	}



	@Override
	public void saveOrUpdate(Terminal bid) {
		sessionFactory.getCurrentSession().saveOrUpdate(bid);				
	}


	@Override
	public Terminal get(long id) {
		return (Terminal) sessionFactory.getCurrentSession().get(Terminal.class, id);

	}


	@Override
	public void delete(Terminal term) {
		sessionFactory.getCurrentSession().delete(term);		
	}



	@Override
	public List<Terminal> listAll() {
		String hql = "from Terminal";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<Terminal>) query.list();
	}



	@Override
	public Terminal getByName(String name) {
		String hql = "from Terminal where name = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
					.setString("name", name);
		List<Terminal> ts = query.list();
		if(ts.size()>0){
			return ((Terminal)ts.get(0));
		}
		return null;
	}	
	
	
	
	
	
}

