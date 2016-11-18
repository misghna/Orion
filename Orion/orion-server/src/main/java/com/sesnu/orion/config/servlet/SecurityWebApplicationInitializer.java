package com.sesnu.orion.config.servlet;

import javax.servlet.Filter;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.sesnu.orion.config.SecurityConfig;


public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	
	public SecurityWebApplicationInitializer() {
		super(SecurityConfig.class);
	}

}
