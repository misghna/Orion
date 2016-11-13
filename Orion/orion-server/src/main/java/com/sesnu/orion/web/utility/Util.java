package com.sesnu.orion.web.utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;


@Component
public class Util {
	
	static SecureRandom random = new SecureRandom();	

	  public static String generateString() {
	    return new BigInteger(130, random).toString(32);
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
	
}

