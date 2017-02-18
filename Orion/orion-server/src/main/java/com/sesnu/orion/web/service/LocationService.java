package com.sesnu.orion.web.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.AccessHistoryDAO;
import com.sesnu.orion.web.model.Location;
import com.sesnu.orion.web.utility.ConfigFile;

@Component
public class LocationService {

	@Autowired AccessHistoryDAO accessDoa;
	@Autowired HttpService http;
	@Autowired ConfigFile config;
	
	
	
		public void updateLocation() throws UnsupportedOperationException, IOException, ParseException{
			Date date = new Date();
			String locationApi = config.getProp().getProperty("ipLocationUrl");
			List<String> ips = accessDoa.listNewIps();
			if(ips.size()<1)return;
		//	System.out.println("checking locations " + ips.size());
			for (String ip : ips) {
				HttpResponse response = http.get(locationApi + "/" + ip);
				if(response.getStatusLine().getStatusCode()==200){
					JSONObject result = http.getJSONContent(response);
					Location loc = new Location(ip,result.get("latitude").toString(),result.get("longitude").toString(),date,1);
					accessDoa.saveLocation(loc);
				}
			}
		}
	
}
