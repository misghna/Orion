package com.sesnu.orion.web.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class ConfigFile {
	
	
	public Properties getProp(){
		
		InputStream inputStream=null;
		Properties prop = new Properties();

		String propFileName = "config.properties";
		String productionPath = "/usr/share/tomcat8/conf/";
		
		try {

			File f = new File(productionPath + propFileName);
			
			if(f.exists() && !f.isDirectory()) { // check if its production
				System.out.println("loading from config file from " + productionPath);
				inputStream = new FileInputStream(f);
			
			}else{// dev
				System.out.println("loading from config file from resources ");
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			}		
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return prop;
		
	}
	
	
	
	public String getFile(String fileName){
		InputStream inputStream=null;
		
		try {

			inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);					
				}
				return sb.toString();
			} else {
				throw new FileNotFoundException("file '" + fileName + "' not found in resources");
			}

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return "";
		}
	
}
