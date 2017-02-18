package com.sesnu.orion.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.sesnu.orion.dao.AccessHistoryDAO;
import com.sesnu.orion.dao.UserDAO;
import com.sesnu.orion.web.model.AccessHistory;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.Util;



@Component
public class AuthProvider implements AuthenticationProvider  {

	private @Autowired HttpServletRequest request;
	private @Autowired HttpServletResponse response;
	private @Autowired AccessHistoryDAO accessDao;
	@Autowired
	UserDAO userDao;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		System.out.println("credentials" + email + "-" + password);
		AccessHistory accessHistory = new AccessHistory(0l,request.getRemoteHost(),"denied");
		if(email ==null || password ==email){
			accessDao.save(accessHistory);
			return null;
		}
		
		User user = userDao.getUserByEmail(email);
		
		
		if(user==null){
			accessDao.save(accessHistory);
			request.setAttribute("error", "User not found");
			return null;
		}else{
			accessHistory.setUserId(user.getId());
		}
		
		if(user.getAccess()==null){
			accessDao.save(accessHistory);
			throw new BadCredentialsException("insuficient Access");
		}
		
		
		if (email.equals(user.getEmail()) && Util.passwordMatch(user.getPassphrase(), password)) {
		    	
				accessHistory.setStatus("granted");
				accessDao.save(accessHistory);
				
				if(request.getSession()!=null){
					request.getSession().setAttribute("user", user);
					List<Long> grandtedIds = userDao.getAllowedOrderIds(user.getAccess());
					if(grandtedIds.size()==0){
						grandtedIds.add(0l);
					}
					request.getSession().setAttribute("grantedIds",grandtedIds);
				}
				
				List<GrantedAuthority> grantedAuths = new ArrayList<>();
				GrantedAuthority ga = new SimpleGrantedAuthority(user.getRole());
				grantedAuths.add(ga);
				if(user.getRole().equals("Admin")){
					GrantedAuthority ga2 = new SimpleGrantedAuthority("User");
					grantedAuths.add(ga2);
				}
				return new UsernamePasswordAuthenticationToken(email, password, grantedAuths);
		}
		
		return null;
		

	}




	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}



}

