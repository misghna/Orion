package com.sesnu.orion.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sesnu.orion.dao.InvFormatDAO;
import com.sesnu.orion.web.model.Invoice;
import com.sesnu.orion.web.model.InvoiceFormat;

@Transactional
@Repository
public class InvFormatDAOImpl implements InvFormatDAO {

	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	public void saveOrUpdate(InvoiceFormat invFormat) {
		sessionFactory.getCurrentSession().saveOrUpdate(invFormat);

	}
	
	@Override
	public void saveInvoice(Invoice invoice) {
		sessionFactory.getCurrentSession().saveOrUpdate(invoice);
	}

	@Override
	public Invoice getInvoice(String invNo, String invoiceType){
		String hql = "from Invoice where invNo = :invNo and invoiceType = :invoiceType";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("invNo", invNo)
				.setString("invoiceType", invoiceType);
		List<Invoice> inv = (List<Invoice>) query.list();
		if(inv.size()>0) return inv.get(0);
		return null;
	}
	
	
	@Override
	public InvoiceFormat get(long id) {
		return (InvoiceFormat) sessionFactory.getCurrentSession().get(InvoiceFormat.class, id);
	}

	@Override
	public void delete(InvoiceFormat invFormat) {
		sessionFactory.getCurrentSession().delete(invFormat);	

	}

	@Override
	public List<InvoiceFormat> listAll() {
		String hql = "from InvoiceFormat";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (List<InvoiceFormat>) query.list();
	}

	@Override
	public InvoiceFormat get(String invType, String exporter) {
		String hql = "from InvoiceFormat where invoiceType = :invoiceType and exporter = :exporter";
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setString("invoiceType", invType)
				.setString("exporter", exporter);
		List<InvoiceFormat> result = (List<InvoiceFormat>) query.list();
		if (result.size()>0){
			return result.get(0);
		}
		return null;
	}

}
