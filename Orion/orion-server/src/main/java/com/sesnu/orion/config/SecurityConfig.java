package com.sesnu.orion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.sesnu.orion.web.utility.ConfigFile;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@ComponentScan("com.sesnu.orion")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	AuthProvider authProvider;
	@Autowired
	ConfigFile config;
	

	
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		boolean isDevMode =false;
		if(config.getProp().get("devMode").equals("true")){
			isDevMode=true;
		}
			if(isDevMode){
			     http.authorizeRequests()
			    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			    .antMatchers(HttpMethod.GET,"/**").permitAll() 
		        .antMatchers("/api/xyz/**").access("hasRole('SECURE')")	
				.and().csrf().disable().httpBasic()
			 	.and().exceptionHandling().accessDeniedPage("/logout");	
			
			}else{
			     http.authorizeRequests()
			    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		        .antMatchers("/api/user/**").access("hasRole('User')")	
		        .antMatchers("/api/admin/**").access("hasRole('Admin')")
		        .antMatchers("/api/open/**").permitAll()
		        .antMatchers("/api/*").permitAll()
				.and().csrf().disable().httpBasic()
			 	.and().exceptionHandling().accessDeniedPage("/login");	
			}

	}

	
}



