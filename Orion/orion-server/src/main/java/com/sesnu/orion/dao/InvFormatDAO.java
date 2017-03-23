package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Invoice;
import com.sesnu.orion.web.model.InvoiceFormat;




public interface InvFormatDAO {

	
	public void saveOrUpdate(InvoiceFormat bid);
	
	public InvoiceFormat get(long id);
	
	public InvoiceFormat get(String invType,String exporter);
	
	public void delete(InvoiceFormat bid);

	public List<InvoiceFormat> listAll();
	
	public void saveInvoice(Invoice invoice);
	
	public Invoice getInvoice(String invNo, String invoiceType);

	
}