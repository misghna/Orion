package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.DocumentDAO;
import com.sesnu.orion.web.model.DocView;
import com.sesnu.orion.web.model.TCPResponse;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class DocumentController {

	
	@Autowired
	DocumentDAO docDao;

	@Autowired Util util;
	

	@RequestMapping(value = "/api/user/doc/{state}", method = RequestMethod.GET)
	public @ResponseBody List<DocView> docByTypeId(@PathVariable("state") String state,
			HttpServletResponse response) throws IOException {

		List<DocView> docs = getDocs(state);
		if(docs.size()>0){
			return docs;
		}

		response.sendError(404);
		return null;
	}
	
	private List<DocView> getDocs(String state){
		List<DocView> docs =null;
		if(state.indexOf("orderRef")>=0){
			docs = docDao.listByOrderRef(Long.parseLong(state.split("-")[1].toString()));
		}else{
			docs = docDao.listByDocType(state.replaceAll("_", " "));
		}
		return docs;
	}
//	@RequestMapping(value = "/api/user/doc/{docType}", method = RequestMethod.GET)
//	public @ResponseBody List<Document> docByDocType(@PathVariable("docType") String docType,
//			HttpServletResponse response) throws IOException {
//
//		List<Document> docs = docDao.listByDocType(docType);
//		if(docs.size()>0){
//			return docs;
//		}
//		response.sendError(404);
//		return null;
//	}
	
	
//	@RequestMapping(value = "/api/user/doc", method = RequestMethod.POST)
//	public @ResponseBody List<DocView> addItem(HttpServletResponse response,@RequestBody DocView doc)
//			throws Exception {
//		
//		doc.setUpdatedOn(Util.parseDate(new Date(),"/"));
//		doc.setId(null);
//		docDao.saveOrUpdate(doc);
//		
//		List<DocView> docs = docDao.list(doc.getOrderRef());
//		if(docs.size()>0){
//			return docs;
//		}
//		response.sendError(404);
//		return null;
//
//	}
	
	
//	@RequestMapping(value = "/api/user/doc", method = RequestMethod.PUT)
//	public @ResponseBody List<DocView> updateItem(HttpServletResponse response,
//			@RequestBody DocView doc)
//			throws Exception {
//		
//		if(docDao.get(doc.getId())==null){
//			response.sendError(400);
//			return null;
//		}
//		doc.setUpdatedOn(Util.parseDate(new Date(),"/"));
//		docDao.saveOrUpdate(doc);
//		
//		List<DocView> Documents = docDao.list(doc.getOrderRef());
//		if(Documents.size()>0){
//			return Documents;
//		}
//		response.sendError(404);
//		return null;
//
//	}
	
	

	@RequestMapping(value = "/api/user/doc/{state}/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<DocView> deleteItem(@PathVariable("id") long id,
			@PathVariable("state") String state,
			HttpServletResponse response) throws Exception {
		DocView doc = docDao.get(id);
		if(doc != null){
			docDao.delete(doc);
			List<DocView> docs = getDocs(state);
			if(docs.size()>0){
				return docs;
			}
			response.sendError(404);
			return null;
		}
		response.sendError(400);
		return null;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/documents/{docName}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getDoc(@PathVariable("docName") String docName, HttpServletResponse resp) throws IOException {
	    TCPResponse response = util.readFromS3(docName);
	    if(response.getCode()==200){
			InputStream in = (InputStream) util.readFromS3(docName).getResponse();
		    return IOUtils.toByteArray(in);
	    }else{
	    	resp.sendError(400,"error when retriving document");
	    	return null;
	    }
	}
		

}
