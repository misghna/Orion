package com.sesnu.orion.web.utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;


@Component
public class Util {
	
	@Autowired
	SnsConfiguration cnsConf; 
	
	static List<String> monthName= Arrays.asList(new String[]{"ALL","JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL", "AUG", "SEP", "OCT", "NOV", "DEC"});

	static SecureRandom random = new SecureRandom();	

	  public static String generateString(int len) {
	    String generated = new BigInteger(130, random).toString(32);
	    return generated.substring(0, len).toUpperCase();
	  }
	
	
	public static Long getTime(){
		return System.currentTimeMillis();
	}
	
	public static boolean passwordMatch(String real, String supplied){
		try {
			return encodePassword(supplied).equals(real);
		} catch (Exception e) {
			e.printStackTrace();
			return false;			
		}
	}
	
	public static String encodePassword(String password)
			throws Exception{
		
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

       return sb.toString();

    }

	
	public static void sendMail(String subject,String to, String msg) {
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
	
	public void sendText(String txt,String phoneNo) {
        String message = "[Anseba web] \n" + txt;
        String phoneNumber = phoneNo;
        System.out.println("sending sms to " +  phoneNo);
        Map<String, MessageAttributeValue> smsAttributes = 
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue("AnsebaWeb") //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.50") //Sets the max price to 1cent USD.
                .withDataType("Number"));
        sendSMSMessage(message, phoneNumber, smsAttributes);
	}

	
	private void sendSMSMessage(String message, 
			String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
	        PublishResult result = cnsConf.amazonSNS().publish(new PublishRequest()
	                        .withMessage(message)
	                        .withPhoneNumber(phoneNumber)
	                        .withMessageAttributes(smsAttributes));
	        System.out.println("hello" + result); // Prints the message ID.
	}
	
	public String encrypText(String clearText){
		/* Encrypt the message. */
        TextEncryptor encryptor = Encryptors.text("myAnsebaSalt2016", "73621314587861349519");      
        String encryptedText = encryptor.encrypt(clearText);

        return encryptedText;    		
	}
	
	
	public String decryptText(String encriptedTex){
        TextEncryptor decryptor = Encryptors.text("myAnsebaSalt2016", "73621314587861349519");
        String decryptedText = decryptor.decrypt(encriptedTex);
        return  decryptedText;
	}
	
	
	public static Date parseDate(String dateStr){
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	//	formatter.setTimeZone(TimeZone.getTimeZone("Gulf Time Zone"));
	    try {	
	        Date date = formatter.parse(dateStr);
	        return date;	
	    } catch (ParseException e) {
	        return null;
	    }
	}
	
	public static String parseDate(Date date){
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String reportDate = df.format(date);
		return reportDate;
	}
	
	public static String parseDate(Date date,String separator){
		DateFormat df = new SimpleDateFormat("dd" + separator + "MM" + separator + "yyyy");
		String reportDate = df.format(date);
		return reportDate;
	}
	
	
	public static int getMonth (String month){
		return monthName.indexOf(month);
	}
	 
	public static String parseError(String errMsg){
		return "*$Start$* " + errMsg + " *$End$*";
	}
}

