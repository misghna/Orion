package com.sesnu.orion.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sesnu.orion.dao.AddressBookDAO;
import com.sesnu.orion.dao.DocHandoverDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.model.DocHandover;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.GmailInbox;
import com.sesnu.orion.web.utility.Util;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class DocHandoverController {

	
	@Autowired
	DocHandoverDAO docDao;
	@Autowired UserDAO userDao;
	@Autowired Util util;
	@Autowired AddressBookDAO clientDao;
	@Autowired GmailInbox gmailInbox;	

	@RequestMapping(value = "/api/user/docHandover/all", method = RequestMethod.GET)
	public @ResponseBody List<DocHandover> getAll(
			HttpServletResponse response) throws IOException {

		gmailInbox.checkConfirmation();
		List<DocHandover> docs = docDao.listAll();
		if(docs.size()>0){			
			return docs;
		}

		response.sendError(404);
		return null;
	}
	
	
	@RequestMapping(value = "/api/user/docHandover", method = RequestMethod.POST)
	public @ResponseBody List<DocHandover> addItem(HttpServletResponse response,
			HttpServletRequest request,
			@RequestBody DocHandover doc)
			throws Exception {
		
		doc.setId(null);
		User user = util.getActiveUser(request,userDao);
		String agentName = doc.getReceivedBy().equals("My Self")?doc.getReceivedFrom():doc.getReceivedBy();
		if(doc.getReceivedBy().equals("My Self")){
			doc.setReceivedBy(user.getFullname());
		}
		if(doc.getReceivedFrom().equals("My Self")){
			doc.setReceivedFrom(user.getFullname());
		}
		doc.setStatus("Not Confirmed");
		docDao.saveOrUpdate(doc);
		String bl = doc.getBl()==null? "NA":doc.getBl();
		AddressBook client = clientDao.getByName(agentName);
		String subject = "Document Handover Confirmation for BL-" + bl + "/ requestId-" + doc.getId();
		String msg = "Hello " +  doc.getReceivedBy() + "\n if you receive the original documents listed below, please 'Replay to All' only the word 'YES' \n" + 
					"\nBill of loading No - " + bl + "\nSubmitted by - " + doc.getReceivedFrom() + "\nSubmitted on -" + (new Date()).toGMTString() + 
					"\n ***************Documents List********************** \n";
		
		String [] docList = doc.getDocs().split(",");
		for (String docName : docList) {
			msg = msg + "\n " + docName;
		}
		util.sendMail(subject, client.getEmail(), msg,user.getEmail());
		List<DocHandover> docs =  docDao.listAll();

		if(docs.size()>0){
			return docs;
		}
		response.sendError(404);
		return null;
		
	}

	
	@RequestMapping(value = "/api/user/docHandover/markAsReturned/{id}", method = RequestMethod.PUT)
	public @ResponseBody List<DocHandover> markAsReturned(HttpServletResponse response,
			HttpServletRequest request,@PathVariable("id") long id)
			throws Exception {
		DocHandover doc = docDao.get(id);
		if(doc == null ){
			response.sendError(400,Util.parseError("Bad document format, try again"));
			return null;
		}

		User user = util.getActiveUser(request,userDao);
		doc.setReturnedTo(user.getFullname());
		doc.setReturnedOn(new Date());
		doc.setStatus("Returned");
		docDao.saveOrUpdate(doc);

		String bl = doc.getBl()==null? "NA":doc.getBl();
		AddressBook client = clientDao.getByName(doc.getReceivedBy());
		String subject = "Document Return Confirmation for BL-" + bl + "/ requestId-" + doc.getId();
		String msg = "Hello " +  doc.getReceivedBy() + "\n This is a confirmation email for your reference that I have received the documents listed below. \n" + 
					"\nBill of loading No - " + bl + "\n Returned by - " + client.getName() + "\n Returned to - "  + user.getFullname() + "\nReturned on -" + (new Date()).toGMTString() + 
					"\n \n ***************Documents List********************** \n";
		
		String [] docList = doc.getDocs().split(",");
		for (String docName : docList) {
			msg = msg + "\n " + docName;
		}
		util.sendMail(subject, client.getEmail(), msg,user.getEmail());
		List<DocHandover> docs =  docDao.listAll();

		if(docs.size()>0){
			return docs;
		}
		response.sendError(404);
		return null;
		
	}
	
	
	
	
	@RequestMapping(value = "/api/user/docHandover/{id}", method = RequestMethod.DELETE)
	
	public @ResponseBody List<DocHandover> deleteItem(@PathVariable("id") Long id,
			HttpServletResponse response) throws Exception {
		DocHandover doc = docDao.get(id);
		if(!doc.getStatus().equals("Not Confirmed")){
			response.sendError(400,Util.parseError("this record can not be deleted, because it has been already confirmed by the recipient."));
			return null;
		}
		if(doc != null){
			docDao.delete(doc);
			List<DocHandover> docs = docDao.listAll();
			if(docs.size()>0){
				return docs;
			}
			response.sendError(404);
			return null;
		}
		response.sendError(400);
		return null;
	}
	
	

		

}
