package com.sesnu.orion.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.sesnu.orion.web.utility.Util;



@Component
public class AuthProvider implements AuthenticationProvider  {

	private @Autowired HttpServletRequest request;
	@Autowired
	UserDAO userDao;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		System.out.println("credentials" + email + "-" + password);

		if(email ==null || password ==email){
			return null;
		}
		
		User user = userDao.getUserByEmail(email);
		
		
		if(user==null){
			return null; // Bad emailAddress
		}
		

		
		if (email.equals(user.getEmail()) && Util.passwordMatch(user.getPassphrase(), password)) {
				if(request.getSession()!=null){
					request.getSession().setAttribute("user", user);
				}
			    request.setAttribute("user", user);
				List<GrantedAuthority> grantedAuths = new ArrayList<>();
				GrantedAuthority ga = new SimpleGrantedAuthority("SECURE");
				grantedAuths.add(ga);           
				return new UsernamePasswordAuthenticationToken(email, password, grantedAuths);
		}
		
		return null;
		

	}




	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}



}

