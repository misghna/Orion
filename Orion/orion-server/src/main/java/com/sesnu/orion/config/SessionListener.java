package com.sesnu.orion.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	static int SESSION_TIME_OUT = 10; // minutes
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		System.out.println("session is created");
		event.getSession().setMaxInactiveInterval(SESSION_TIME_OUT*60);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("==== Session is destroyed ====");
	}

}
