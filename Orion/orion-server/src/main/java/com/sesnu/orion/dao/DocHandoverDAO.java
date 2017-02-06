package com.sesnu.orion.dao;

import java.util.List;

import com.sesnu.orion.web.model.DocHandover;




public interface DocHandoverDAO {
	
	public List<DocHandover> listByOrderBl(long bl);
	
	public List<DocHandover> listAll();
	
	public void saveOrUpdate(DocHandover doc);

	public DocHandover get(long id);

	public void delete(DocHandover doc) ;
		
	public List<DocHandover> listUnconfirmed();
	


	
}