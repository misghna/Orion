package com.sesnu.orion.servlet3;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.sesnu.orion.config.CorsFilter;
import com.sesnu.orion.config.SessionListener;
import com.sesnu.orion.config.SpringWebConfig;


public class SpringWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        
        appContext.register(SpringWebConfig.class);
        
        servletContext.addListener(new SessionListener());
        
        servletContext.addListener(new RequestContextListener());
        
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "SpringDispatcher", new DispatcherServlet(appContext));
        dispatcher.setInitParameter("dispatchOptionsRequest", "true");
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
        
	}

//	@Override
//    protected void customizeRegistration(Dynamic registration) {
//        registration.setInitParameter("dispatchOptionsRequest", "true");
//    }
	
	@Override
	  protected Filter[] getServletFilters() {
	    return new Filter[] {
	      new CorsFilter()
	    };
	  }
	
	@Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
	

}