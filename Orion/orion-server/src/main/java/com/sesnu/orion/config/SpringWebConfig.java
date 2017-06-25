package com.sesnu.orion.config;
 
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.sesnu.orion.dao.MiscSettingDAO;
import com.sesnu.orion.web.utility.ConfigFile;
import com.sesnu.orion.web.utility.Util;


@EnableScheduling
@EnableWebMvc
@Configuration
@ComponentScan(basePackages="com.sesnu.orion", useDefaultFilters = false, 
includeFilters = @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION))
@EnableTransactionManagement
public class SpringWebConfig extends WebMvcConfigurerAdapter {
 
	@Autowired Util util;
	@Autowired ConfigFile conf;
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/*").addResourceLocations("/resources/");
		registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor());
    }
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	@Bean
	public MultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(10000000);
	    return multipartResolver;
	}
	
    @Bean
    public CronJob myCron() {
        return new CronJob();
    }
    
    
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
 
	@Bean(name = "dataSource")
    public DataSource getDataSource() {
		Properties prop = conf.getProp();
		
    	BasicDataSource dataSource = new BasicDataSource();
    	dataSource.setDriverClassName("org.postgresql.Driver");
    	dataSource.setUrl("jdbc:postgresql://" + prop.getProperty("dbUrl"));
    	dataSource.setUsername(prop.getProperty("dbUsername"));
    //	dataSource.setPassword("ansebagroup2016");
    	dataSource.setPassword(util.decryptText(prop.getProperty("dbPassword").trim()));
    	
    	return dataSource;
    }
    
    
    private Properties getHibernateProperties() {
    	Properties properties = new Properties();
    	properties.put("hibernate.show_sql", "true");
    	properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    	properties.put("hibernate.temp.use_jdbc_metadata_defaults","false");
    	properties.put("hibernate.c3p0.min_size",5);
    	properties.put("hibernate.c3p0.max_size",10);
    	properties.put("hibernate.c3p0.timeout",300);
    	properties.put("hibernate.c3p0.max_statements",50);
    	properties.put("hibernate.c3p0.idle_test_period",3000);
    //	properties.put("hibernate.connection.pool_size",10);
    	return properties;
    }
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setEntityInterceptor(new AccessInterceptor());
        sessionFactory.setDataSource(getDataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.sesnu.orion" });
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
     }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").
        		allowedOrigins("*").
        		allowedMethods("GET", "POST","DELETE", "OPTIONS", "PUT")
                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers")
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
                .allowCredentials(true).maxAge(3600);
    }
    

    
}