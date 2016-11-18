package com.sesnu.orion.config.servlet;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.WebApplicationContext;
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
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
   //     dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "SpringDispatcher",dispatcherServlet );
        
     //   dispatcher.setInitParameter("throwExceptionIfNoHandlerFound", "true");

        dispatcher.setInitParameter("dispatchOptionsRequest", "true");
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
	}

	@Override
    protected DispatcherServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        DispatcherServlet ds = new DispatcherServlet(servletAppContext);
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }
	
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