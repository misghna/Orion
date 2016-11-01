package com.sesnu.orion.servlet3;

import javax.servlet.Filter;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.sesnu.orion.config.SecurityConfig;


public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	
	public SecurityWebApplicationInitializer() {
		super(SecurityConfig.class);
	}

}
