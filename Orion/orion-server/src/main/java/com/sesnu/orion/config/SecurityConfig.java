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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
@ComponentScan("com.sesnu.orion")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	AuthProvider authProvider;
	

	
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		System.out.println("trying to run authentication ....");
		auth.authenticationProvider(authProvider);
//        auth
//        .inMemoryAuthentication()
//            .withUser("admin").password("nimda").roles("ADMIN");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

			     http.authorizeRequests()
			    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		        .antMatchers("/loginPage").permitAll() 
		        .antMatchers("/api/**").access("hasRole('SECURE')")	
		        .antMatchers("/*").permitAll() 
				.and().csrf().disable().httpBasic()
			 	.and().exceptionHandling().accessDeniedPage("/logout");	

	}

	
}



