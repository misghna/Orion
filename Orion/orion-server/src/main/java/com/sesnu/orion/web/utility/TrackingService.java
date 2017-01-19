package com.sesnu.orion.web.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sesnu.orion.dao.PortFeeDAO;
import com.sesnu.orion.web.model.PortFee;

@SuppressWarnings("unchecked")
@Service
public class TrackingService {

	@Autowired PortFeeDAO portFeeDao;
	
	
	public String getShipmentTracking(String agency, String bl){
		JSONObject response = new JSONObject();
		PortFee portFee = portFeeDao.getByName(agency);
		List<String> dropList = null;
		if(portFee.getDropCols()!=null){
			dropList = Arrays.asList(portFee.getDropCols().split(","));
		}else{
			dropList = new ArrayList<String>();
		}
		
		
		WebDriver driver = new HtmlUnitDriver();		
		String url = portFee.getTrackingUrl();
		
		if(url==null){
			 response.put("type", "HTML");
			 response.put("data", "This shipping agency doesn't have webbased tracking system");
	    	return response.toJSONString();
			
		}else if(url.indexOf("BILL_OF_LOADING")<0 || portFee.getTagId()==null){
			url = url.replace("BILL_OF_LOADING", bl);
			 response.put("type", "HTML");
			 response.put("data", "Automated tracking is not set for this shipping agency, click <a target='_blank' href='" + url + "'>here</a> for manual tracking");
	    	return response.toJSONString();
		}
		
		url = url.replace("BILL_OF_LOADING", bl);
			
		driver.get(url);
		
 	    WebElement table = (new WebDriverWait(driver, 10))
	    		   .until(ExpectedConditions.elementToBeClickable(By.id(portFee.getTagId())));
		
 	    System.out.println(table.getAttribute("innerHTML"));
	 
 	    List<WebElement> allHeaders= table.findElements(By.tagName("th")); 
	  
 	    if(allHeaders.size()==0){
 			 response.put("type", "HTML");
 			 response.put("data", table.getAttribute("innerHTML"));
	    	return response.toJSONString();
	    }
	    List<WebElement> allRows = table.findElements(By.tagName("tr")); 


	    Map<Integer,String> headers = new HashMap<Integer,String>();
	    for (Integer j =0; j< allHeaders.size();j++) {
	    	WebElement h = allHeaders.get(j);
	    	if(!dropList.contains(h.getText())){	    	 
		    	headers.put(j,h.getText());
	    	}else{
	    		System.out.println("droped *****" + h.getText());
	    	}
	    }
	    
	    JSONArray data = new JSONArray();
		 for (WebElement row : allRows) { 
			 JSONObject jo = new JSONObject();
		     List<WebElement> cells = row.findElements(By.tagName("td")); 
		     for (Integer i =0; i< cells.size();i++) { 
		    	 if(headers.containsKey(i)){
			    	 WebElement cell = cells.get(i);
			    	 jo.put(headers.get(i), cell.getText());
		    	 }
		     }
		     if(!jo.isEmpty())data.add(jo);
		 }
		 response.put("type", "JSON");
		 response.put("data", data);
		 
		return response.toJSONString();
		
	}
	
	
	public String TrackHDL(String trackingId) 
			throws ClientProtocolException, IOException, ParseException{
		JSONObject resultObj = new JSONObject();
	    Long time = System.currentTimeMillis();
	    String url = "http://www.dhl.com/shipmentTracking?AWB="+trackingId+"&countryCode=g0&languageCode=en&_=" + time.toString();
	    HttpUriRequest request = new HttpGet(url);
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
	    String result = EntityUtils.toString(httpResponse.getEntity());
	    JSONParser parser= new JSONParser();
	    JSONObject mr = (JSONObject) parser.parse(result);
	    JSONObject res = new JSONObject();
	    if(mr.containsKey("results")){
	    	JSONObject jo = (JSONObject) ((JSONArray) mr.get("results")).get(0);
	    	res.put("Tracking id",jo.get("id"));
	    	res.put("Status" , ((JSONObject)jo.get("delivery")).get("status"));
	    	res.put("Origin" , ((JSONObject)jo.get("origin")).get("value"));
	    	res.put("Destination" , ((JSONObject)jo.get("destination")).get("value"));
	    	JSONObject ckpts = (JSONObject) ((JSONArray) jo.get("checkpoints")).get(0);
	    	res.put("last known location ", ckpts.get("date") + " @ " +  ckpts.get("time") + " " + ckpts.get("description"));
	    }else{
		    resultObj.put("type", "HTML");
		    resultObj.put("data", "Tracking history not found");
		    return resultObj.toJSONString();
	    }
	    JSONArray resultArray = new JSONArray();
	    resultArray.add(res);
	    resultObj.put("type", "JSON");
	    resultObj.put("data", resultArray);

	    return resultObj.toJSONString();
	}
	
	//https://www.tnt.com/api/v1/shipment?con=7465679678&locale=en_US&searchType=CON
	
}
