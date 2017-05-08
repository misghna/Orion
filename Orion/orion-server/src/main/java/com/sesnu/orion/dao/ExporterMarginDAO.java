package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.ExporterQuote;




public interface ExporterMarginDAO {
	

	public void saveOrUpdate(ExporterQuote exMargin);

	public ExporterQuote get(long id);
	
	public ExporterQuote getByOrderRef(long orderRefId);
	
	public void delete(ExporterQuote exMargin);

	public List<ExporterQuote> listAll();



	
}