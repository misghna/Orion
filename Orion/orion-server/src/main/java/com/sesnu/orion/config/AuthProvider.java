package com.sesnu.orion.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.User;



@Component
public class AuthProvider implements AuthenticationProvider  {

	private @Autowired HttpServletRequest request;
	@Autowired
	UserDAO userDao;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		System.out.println("credentials" + name + "-" + password);

		if(name ==null || password ==null){
			return null;
		}
		
		User user = userDao.getUserByName(name);
		
		
		if(user==null){
			return null; // Bad username
		}
		

		
		if (name.equals(user.getUsername()) && password.equals(user.getPassphrase())) {
				if(request.getSession()!=null){
					request.getSession().setAttribute("user", user);
				}
			    request.setAttribute("user", user);
				List<GrantedAuthority> grantedAuths = new ArrayList<>();
				GrantedAuthority ga = new SimpleGrantedAuthority("SECURE");
				grantedAuths.add(ga);           
				return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
		}
		
		return null;
		

	}




	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}



}

