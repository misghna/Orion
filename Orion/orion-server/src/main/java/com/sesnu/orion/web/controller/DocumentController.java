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
		}else if (state.indexOf("all")>=0){
			docs = docDao.listAll();
		}else{
			docs = docDao.listByDocType(state.replaceAll("_", " "));
		}
		return docs;
	}
	

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
	@RequestMapping(value = "/documents/image/{docName}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@PathVariable("docName") String docName, HttpServletResponse resp)
			throws IOException {
	    TCPResponse response = util.readFromS3(docName);
	    if(response.getCode()==200){
			InputStream in = (InputStream) util.readFromS3(docName).getResponse();
		    return IOUtils.toByteArray(in);
	    }else{
	    	resp.sendError(400,"error when retriving document");
	    	return null;
	    }
	}
	
	@ResponseBody
	@RequestMapping(value = "/documents/pdf/{docName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public byte[] getPdf(@PathVariable("docName") String docName, HttpServletResponse resp) 
			throws IOException {
	    TCPResponse response = util.readFromS3(docName + ".pdf");
	    if(response.getCode()==200){
			InputStream in = (InputStream) response.getResponse();
		    return IOUtils.toByteArray(in);
	    }else{
	    	System.out.println(response.getMsg());
	    	resp.sendError(400,"error when retriving document");
	    	return null;
	    }
	}
		

}
