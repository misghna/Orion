package com.sesnu.orion.web.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component
public class HttpService {
	
	public HttpResponse get(String url) 
			throws UnsupportedOperationException, IOException{
		HttpClient clinet = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

//		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		return response;
	}

	public String getContent(HttpResponse response) 
			throws IOException{
		
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();			
	}
	
	public JSONObject getJSONContent(HttpResponse response) 
			throws IOException, ParseException{
		String result = getContent(response);
		JSONParser parser = new JSONParser();
		JSONObject jResult = (JSONObject) parser.parse(result);
		return jResult;
	}
	
}
