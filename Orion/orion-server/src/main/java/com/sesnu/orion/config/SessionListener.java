package com.sesnu.orion.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.sesnu.orion.web.utility.ConfigFile;

public class SessionListener implements HttpSessionListener {

	private Integer sessionTime;
	
	static int SESSION_TIME_OUT = 1; // minutes
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	//	System.out.println("session is created");
		event.getSession().setMaxInactiveInterval(getSessionTime()*60);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		//System.out.println("==== Session is destroyed ==== id -> " + event.getSession().getId());
	}

	
	private Integer getSessionTime(){
		
		if(sessionTime==null){
			ConfigFile conf = new ConfigFile();
			sessionTime = Integer.parseInt(conf.getProp().get("sessionMins").toString());
			return sessionTime;
		}
		return sessionTime;
	}
}
