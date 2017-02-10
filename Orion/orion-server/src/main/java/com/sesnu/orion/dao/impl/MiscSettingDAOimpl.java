package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.web.model.MiscSetting;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.PortFee;
@SuppressWarnings("unchecked")
@Transactional
@Repository
public class MiscSettingDAOimpl implements MiscSettingDAO {
	
	@Autowired
    private SessionFactory sessionFactory;

	public MiscSettingDAOimpl() {
	}

	@Override
	public List<MiscSetting> listAll() {
		String hql = "from MiscSetting";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<MiscSetting>) query.list();
	}

	@Override
	public void saveOrUpdate(MiscSetting setting) {
		sessionFactory.getCurrentSession().saveOrUpdate(setting);				
	}


	@Override
	public MiscSetting get(long id) {
		return (MiscSetting) sessionFactory.getCurrentSession().get(MiscSetting.class, id);
	}

	@Override
	public void delete(MiscSetting setting) {
		sessionFactory.getCurrentSession().delete(setting);
		
	}

	@Override
	public MiscSetting getByName(String name) {
		String hql = "from MiscSetting where name = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("name", name);
		if(query.list().size()>0){
			return (MiscSetting) query.list().get(0);
		}	
		return null;
	}

	@Override
	public List<String> getTableNamesWithOrderRef() {
		String sql = "SELECT * FROM information_schema.columns WHERE  table_schema = 'public' AND column_name IN ( 'order_ref' )";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		return query.list();
		
	}
	

	


	



	
	
	
}

