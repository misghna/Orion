package com.sesnu.orion.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.web.model.User;
import com.sesnu.orion.web.utility.ConfigFile;
import com.sesnu.orion.web.utility.Util;

public class RequestInterceptor extends HandlerInterceptorAdapter{
	
	//List<String> allowed = Arrays.asList(new String[]{"access_denied","login"});
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
		    throws Exception {
			
			String permissions = null;
			boolean dev=false;
			if(request.getSession().getAttribute("isDev")==null){
				dev = new ConfigFile().getProp().get("devMode").equals("true");
				request.getSession().setAttribute("isDev",dev);
			}
			
			if(dev)return true;

			if(request.getSession().getAttribute("user")!=null){				
				User user = (User) request.getSession().getAttribute("user");
				request.setAttribute("user", user);
				permissions = user.getPrivilage();
				request.setAttribute("grantedIds", request.getSession().getAttribute("grantedIds"));
				
				if(user.getRole().equals("Admin")){
					return true;
				}
			}
			
			if(request.getMethod().toLowerCase().equals("get")){
				return true;
			}
			
			if(request.getRequestURL().indexOf("login")>-1 ||
					request.getRequestURL().indexOf("open")>-1){
				return true;
			}
			
			if(permissions==null || (permissions.equals("READ ONLY")) ){
				if(request.getRequestURL().indexOf("api")>-1){
					response.sendError(403,"Method not allowed");

				}else{
					response.sendRedirect("/access_denied");
				}
				
				return false;
			}
		
			return true;
		}
	
	
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	//	response.sendError(200, "Server Error");
	}

}
