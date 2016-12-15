package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.web.model.Document;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class DocumentController {

	
	@Autowired
	DocumentDAO docDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/doc/orderRef/{orderRef}", method = RequestMethod.GET)
	public @ResponseBody List<Document> docByOrderRef(@PathVariable("orderRef") long orderRef,
			HttpServletResponse response) throws IOException {

		List<Document> docs = docDao.list(orderRef);
		if(docs.size()>0){
			return docs;
		}
		response.sendError(404);
		return null;
	}
	
	@RequestMapping(value = "/api/user/doc/{docType}", method = RequestMethod.GET)
	public @ResponseBody List<Document> docByDocType(@PathVariable("docType") String docType,
			HttpServletResponse response) throws IOException {

		List<Document> docs = docDao.listByDocType(docType);
		if(docs.size()>0){
			return docs;
		}
		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/doc", method = RequestMethod.POST)
	public @ResponseBody List<Document> addItem(HttpServletResponse response,@RequestBody Document doc)
			throws Exception {
		
		doc.setUpdatedOn(Util.parseDate(new Date(),"/"));
		doc.setId(null);
		docDao.saveOrUpdate(doc);
		
		List<Document> docs = docDao.list(doc.getOrderRef());
		if(docs.size()>0){
			return docs;
		}
		response.sendError(404);
		return null;

	}
	
	
	@RequestMapping(value = "/api/user/doc", method = RequestMethod.PUT)
	public @ResponseBody List<Document> updateItem(HttpServletResponse response,
			@RequestBody Document doc)
			throws Exception {
		
		if(docDao.get(doc.getId())==null){
			response.sendError(400);
			return null;
		}
		doc.setUpdatedOn(Util.parseDate(new Date(),"/"));
		docDao.saveOrUpdate(doc);
		
		List<Document> Documents = docDao.list(doc.getOrderRef());
		if(Documents.size()>0){
			return Documents;
		}
		response.sendError(404);
		return null;

	}
	
	

	@RequestMapping(value = "/api/user/doc/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<Document> deleteItem(@PathVariable("id") long id,
			HttpServletResponse response) throws Exception {
		Document doc = docDao.get(id);
		long orderRef= doc.getOrderRef();
		if(doc != null){
			docDao.delete(doc);
			List<Document> docs = docDao.list(orderRef);
			if(docs.size()>0){
				return docs;
			}
		}
		response.sendError(400);
		return null;
	}
	
		

}
