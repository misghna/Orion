package com.sesnu.orion.web.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.AddressBookDAO;
import com.sesnu.orion.dao.DocHandoverDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.AddressBook;
import com.sesnu.orion.web.model.DocHandover;
import com.sesnu.orion.web.model.User;

@Component
public class GmailInbox {
 
	@Autowired AddressBookDAO clientDao;
	@Autowired DocHandoverDAO docDao;
	@Autowired UserDAO userDao;
	
	public void checkConfirmation(){
		try{
			
			List<DocHandover>  unconfirmed = docDao.listUnconfirmed();		
			if(unconfirmed.size()<1) return;
			
			Store store = getStore();
	        
			store.connect("smtp.gmail.com", "eriangoappmaster@gmail.com", "wearefighters@1991");
	
	        Folder inbox = store.getFolder("inbox");
	        inbox.open(Folder.READ_ONLY);
						
	        long yday = System.currentTimeMillis() - 72*3600*1000;
	        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, new Date(yday));
      
	        Message[] messages = inbox.search(newerThan);
			for (DocHandover docHandover : unconfirmed) {
				processMessages(messages, docHandover);
			}
			
			inbox.close(true);
	        store.close();
        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public Store getStore() throws NoSuchProviderException {
 
        Properties props = new Properties();
        props.setProperty("mail.smtp.host","smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.port","465");
		props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.auth","true");
		props.setProperty("mail.smtp.port","465");

            Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("eriangoappmaster@gmail.com","wearefighters@1991");
				}
			});
            Store store = session.getStore("imaps");
            return store;
      
    }
   
    
    public void processMessages(Message[] messages,DocHandover docHandover) 
    		throws MessagingException, IOException{
          
          for (int i = 0; i < messages.length; i++) {
              String subject = messages[i].getSubject();
              if(subject.indexOf("Document Handover Confirmation")>=0){
	                String sender =  InternetAddress.toString(messages[i].getFrom());
	                String content = readMail(messages[i].getContent());
	                if(content.indexOf("eriangoappmaster@gmail.com")>0){
	                	content = content.substring(0, content.indexOf("eriangoappmaster@gmail.com"));
	                }
	                
	                if(content.indexOf("YES")>=0){
	                	String reqId = subject.substring(subject.indexOf("requestId-"), subject.length()).split("-")[1];
							if(reqId.trim().equals(docHandover.getId().toString()) &&
									!docHandover.getStatus().equals("Confirmed")){
									AddressBook address = clientDao.getByName(docHandover.getReceivedBy());
									User user = userDao.getUserByName(docHandover.getReceivedBy());
									if((address!=null && sender.indexOf(address.getEmail())>=0) || 
									   (user!=null && sender.indexOf(user.getEmail())>=0)){
										docHandover.setReceivedOn(messages[i].getReceivedDate());
										docHandover.setStatus("Confirmed");
										docDao.saveOrUpdate(docHandover);
									}							
						}
	                }

              }
             
          }
    }
    
    
	private static String readMail(Object msgContent) throws MessagingException, IOException{
		StringBuffer content=new StringBuffer();
		 if (msgContent instanceof Multipart) {
	         Multipart multipart = (Multipart) msgContent;
	         for (int j = 0; j < multipart.getCount(); j++) {
		          BodyPart bodyPart = multipart.getBodyPart(j);
		          content.append(bodyPart.getContent().toString());
	        }
	     }else{
	    	 return msgContent.toString();
	     }
		 return content.toString();
	}
	
	
	
	public void sendMail(String subject,String to, String msg) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("eriangoappmaster@gmail.com","wearefighters@1991");
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("eriangoappmaster@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(msg);			

			Transport.send(message);

			System.out.println("email sent");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
 
}