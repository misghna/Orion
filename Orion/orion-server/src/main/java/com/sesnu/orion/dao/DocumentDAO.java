package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.DocView;
import com.sesnu.orion.web.model.Document;




public interface DocumentDAO {
	
	public List<DocView> listByOrderRef(long orderRef);
	
	public List<DocView> listByDocType(String docType);

	public void saveOrUpdate(Document doc);

	public DocView get(long id);

	public void delete(DocView doc);


	
}