package com.sesnu.orion.web.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.lowagie.text.DocumentException;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.TCPResponse;
import com.sesnu.orion.web.model.User;




@Component
public class Util {
	
	@Autowired
	SnsConfiguration cnsConf; 
	
	@Autowired
	ConfigFile conf;
	
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
		sendMail(subject,to,msg,null,null);
	}
	
	public static void sendMail(String subject,String to, String msg,String cc) {
		sendMail(subject,to,msg,cc,null);      
	}
	
	public static void sendMail(String subject,String to, String msg,String cc,String path) {
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
			if(cc!=null){message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(cc));}
			message.setSubject(subject);
			message.setText(msg);

			
			
			if(path!=null){
		        MimeBodyPart messageBodyPart = new MimeBodyPart();
		        Multipart multipart = new MimeMultipart();
		        messageBodyPart = new MimeBodyPart();
		        String file = "/tmp/" + path;
		        String fileName = path;
		        DataSource source = new FileDataSource(file);
		        messageBodyPart.setDataHandler(new DataHandler(source));
		        messageBodyPart.setFileName(fileName);
		        multipart.addBodyPart(messageBodyPart);
		        message.setContent(multipart);
			}
			
			Transport.send(message);
			
			System.out.println("email sent");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public void sendText(String txt,String phoneNo) {
        String message = "[Anseba limitada] \n" + txt;
        String phoneNumber = phoneNo;
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

	
	public TCPResponse readFromS3(String key) throws IOException {
      AmazonS3 s3client = new AmazonS3Client(cnsConf.getAwsCred());
      try {
	    S3Object s3object = s3client.getObject(new GetObjectRequest(
	    		cnsConf.getBucketName(), key));
	    InputStream in = s3object.getObjectContent();

	    return new TCPResponse("Ok",200,in);
	  }catch(AmazonServiceException ase){
		  return new TCPResponse(ase.getMessage(),ase.getStatusCode(),null);
	  }

	}
	
	
	public TCPResponse writeToS3(byte[] bytes, String fileName){
        AmazonS3 s3client = new AmazonS3Client(cnsConf.getAwsCred());
        try {
            InputStream stream = new ByteArrayInputStream(bytes);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(bytes.length);
 //           meta.setContentType("application/pdf");
            s3client.putObject(cnsConf.getBucketName(), fileName, stream, meta);
            s3client.setObjectAcl(cnsConf.getBucketName(), fileName, CannedAccessControlList.PublicRead);
            return new TCPResponse("Ok",200,null);
         } catch (AmazonServiceException ase) {
            return new TCPResponse(ase.getMessage(),ase.getStatusCode(),null);
        } catch (AmazonClientException ace) {
            return new TCPResponse(ace.getMessage(),null,null);
        }
	}
	
	public TCPResponse deleteS3File(String keyName){
        AmazonS3 s3client = new AmazonS3Client(cnsConf.getAwsCred());
        try {
        	s3client.deleteObject(new DeleteObjectRequest(cnsConf.getBucketName(), keyName));
        		return new TCPResponse("Ok",200,null);
	        } catch (AmazonServiceException ase) {
	        	return new TCPResponse(ase.getMessage(),ase.getStatusCode(),null);
	        } catch (AmazonClientException ace) {
	        	return new TCPResponse(ace.getMessage(),null,null);
	        }
	}
	
	private void sendSMSMessage(String message, 
			String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
	        PublishResult result = cnsConf.amazonSNS().publish(new PublishRequest()
	                        .withMessage(message)
	                        .withPhoneNumber(phoneNumber)
	                        .withMessageAttributes(smsAttributes));
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
	
	public static String getBuildTime(ServletContext context) throws IOException{
		InputStream input = context.getResourceAsStream("/META-INF/MANIFEST.MF");
		BufferedReader reader=new BufferedReader(new InputStreamReader(input, "UTF-8"));
		String line = "";
		while ((line = reader.readLine()) != null) {
		    if(line.indexOf("Build-Time")>=0){
		    	return line;
		    }
		}
		return "";

	}
	
	public User getDevUser(UserDAO userDao){
		return userDao.getUserByEmail(conf.getProp().get("devEmail").toString());
	}
	
	public String convertToPdf(String html) throws DocumentException, IOException{
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		String fileNameWithPath = "/tmp/report_" + generateString(10) + "_" + getTime().toString() + ".pdf";
		FileOutputStream fos = new FileOutputStream( fileNameWithPath );
		renderer.createPDF( fos );
		fos.close();
		return fileNameWithPath;
	}
	
	public static String parseCurrency(Double amount){
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String moneyString = formatter.format(amount);
		return moneyString;
	}
	
	
	public User getActiveUser(HttpServletRequest request,UserDAO userDao){
		User user=null;
		HttpSession session = request.getSession();			
		if(request.getSession().getAttribute("user")!=null){			
			 user = (User) session.getAttribute("user");		
		}else if(conf.getProp().get("devMode").equals("true"))	{
			 user = getDevUser(userDao);
		}
		return user;
	}
	
		
	public JSONObject getExchangeRates(String base, Set<String> currencies) 
			throws ClientProtocolException, IOException, org.json.simple.parser.ParseException{
		StringBuffer sb = new StringBuffer();
		for (String curr : currencies) {
			sb.append(base + curr + "=X+");
		}
		String url = "http://finance.yahoo.com/d/quotes.csv?e=.csv&f=sl1d1t1&s="+ sb.toString();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url.replaceAll(" ", ""));
		
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));
		
		String line = "";
		JSONObject ex = new JSONObject();
		while ((line = rd.readLine()) != null) {
			String[] exchange = line.split(",");
			ex.put(exchange[0].toString().replace("\"", "").replace("=X", ""), exchange[1]);
		}
		return ex;
		
	}
	
}

