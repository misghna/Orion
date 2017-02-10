package com.sesnu.orion.web.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.NotificationDAO;
import com.sesnu.orion.dao.OrderDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.Notification;
import com.sesnu.orion.web.model.OrderView;
import com.sesnu.orion.web.model.User;

@Component
public class NotificationService {
	
	@Autowired UserDAO userDao;
	@Autowired OrderDAO orderDao;
	@Autowired Util util;
	@Autowired NotificationDAO notifDao;
	
	public void checkNotification() throws ParseException{
        List<Notification> notifs = notifDao.listUnnotified();
        if(notifs.size()<1) return;
       
        Map<String,List<User>> notifUsers=null; String msg =null;
    
        for (Notification notif : notifs) {
        	switch(notif.getType()){
        	case "Payment":
        		notifUsers = getNotifUsers("Payment",notif.getName());		
        		msg = "\n The payment for "  + notif.getName() + " has been processed ";
        		emailNotification(notifUsers.get("Email"), msg,notif.getOrderRef());
        		smsNotification(notifUsers.get("SMS/Text"), msg,notif.getOrderRef());
        		notif.setNotified(true);
        		notifDao.saveOrUpdate(notif);
        		break;
        	case "Document":
        		notifUsers = getNotifUsers("Document",notif.getName());		
        		msg = "\n The Document for "  + notif.getName() + " has been uploaded ";
        		emailNotification(notifUsers.get("Email"), msg,notif.getOrderRef());
        		smsNotification(notifUsers.get("SMS/Text"), msg,notif.getOrderRef());
        		notif.setNotified(true);
        		notifDao.saveOrUpdate(notif);
        		break;
        		default : break;
        	}
		}
	}
	
//	public void sendPaymentNotif(Payment pay) throws ParseException{		
//		Map<String,List<User>> notifUsers = getNotifUsers("Payment",pay.getName());		
//		String  msg = "\n The payment for "  + pay.getName() + " has been processed ";
//		emailNotification(notifUsers.get("Email"), msg,pay.getOrderRef());
//		smsNotification(notifUsers.get("SMS/Text"), msg,pay.getOrderRef());
//	}
//	
//	public void sendDocumentNotif(Document doc) throws ParseException{		
//		Map<String,List<User>> notifUsers = getNotifUsers("Document",doc.getType());		
//		String  msg = "\n The payment for "  + doc.getType() + " has been processed ";
//		emailNotification(notifUsers.get("Email"), msg,doc.getOrderRef());
//		smsNotification(notifUsers.get("SMS/Text"), msg,doc.getOrderRef());
//	}
	
	private Map<String,List<User>> getNotifUsers(String type,String name) throws ParseException{
		List<User> users = userDao.list();
		Map<String,List<User>> notifMap = new HashMap<String,List<User>>();
		List<User> emailNotifUsers = new ArrayList<User>();
		List<User> smsNotifUsers = new ArrayList<User>();
		for (User user : users) {
			JSONParser parser = new JSONParser();
			if(user.getNotification()!=null) {	
				JSONObject notif = (JSONObject) parser.parse(user.getNotification());			
				if(notif.containsKey(type)){
					JSONArray payNotifList = (JSONArray) notif.get(type);
					if(payNotifList.contains(name)){
						if(deliveryMethod(notif,"Email")){
							emailNotifUsers.add(user);
						}else if(deliveryMethod(notif,"SMS/Text")){
							smsNotifUsers.add(user);
						}
					}
				}
			}
		}
		
		notifMap.put("Email", emailNotifUsers);
		notifMap.put("SMS/Text", smsNotifUsers);
		
		return notifMap;
	}
	
	private boolean deliveryMethod(JSONObject notif,String dm){
		
		if(notif.containsKey("DeliveryMethod")){
			JSONArray deliveryMethod = (JSONArray) notif.get("DeliveryMethod");
			return deliveryMethod.contains(dm);
		}
		return false;
	}

	private void emailNotification(List<User> users, String mainMsg,Long orderRef){
		OrderView order = orderDao.get(orderRef);
		StringBuilder msg = new StringBuilder();
		msg.append("Hello  USER_NAME,\n\n ");
		msg.append("Notification for : Inv No. "  + order.getInvNo() + " Bill of loading : " + order.getBl());
		msg.append("\n " + mainMsg);
		msg.append("\n\n you are getting this notification, becase you set it in your notification profile.");
		msg.append("\n\n Do not replay to this email, this is an automated message.\n\n Thank you!!");
		StringBuffer receiver = new StringBuffer();
		for (User user : users) {
			receiver.append(user.getEmail() + ",");			
		}
		String allRecipients = receiver.toString();
		allRecipients = allRecipients.substring(0,allRecipients.length()-1);
		Util.sendMail("EriAngo Notification", allRecipients, msg.toString());		
	}
	
	private void smsNotification(List<User> users, String mainMsg,Long orderRef){
		OrderView order = orderDao.get(orderRef);
		StringBuilder msg = new StringBuilder();
		msg.append("Hello  USER_NAME,\n\n ");
		msg.append("Notification for : Inv No. "  + order.getInvNo() + " Bill of loading : " + order.getBl());
		msg.append("\n " + mainMsg);
		StringBuffer receiver = new StringBuffer();
		for (User user : users) {
			if(user.getPhone()!=null){
				util.sendText(msg.toString(), user.getPhone());
			}					
		}
	}
}
