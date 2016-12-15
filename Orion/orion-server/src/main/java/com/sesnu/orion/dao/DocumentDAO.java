package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.Document;




public interface DocumentDAO {
	
	public List<Document> list(long orderRef);
	
	public List<Document> listByDocType(String docType);

	public void saveOrUpdate(Document doc);

	public Document get(long id);

	public void delete(Document doc);


	
}