package net.smart4life.spring4jsf.config;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.ServletContextAware;

import com.sun.faces.config.ConfigureListener;

/**
 * Created by roman on 29.01.2015.
 */
@Configuration
@ConditionalOnClass(FacesContext.class)
// @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebConfig implements ServletContextAware {
	
	@Autowired
	private Environment env;

	@Bean
	public ServletRegistrationBean<FacesServlet> servletRegistrationBean() {
		FacesServlet servlet = new FacesServlet();
		ServletRegistrationBean<FacesServlet> servletRegistrationBean = new ServletRegistrationBean<>(servlet, "*.xhtml", "*.jsf");
		servletRegistrationBean.setLoadOnStartup(1);
		return servletRegistrationBean;
	}

	@Bean
	public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
		return new ServletListenerRegistrationBean<>(new ConfigureListener());
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
		servletContext.setInitParameter("javax.faces.CLIENT_WINDOW_MODE", "url");
		servletContext.setInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME, env.getProperty(ProjectStage.PROJECT_STAGE_PARAM_NAME, ProjectStage.Production.name()));
		servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");
		// servletContext.setInitParameter("javax.faces.PARTIAL_STATE_SAVING_METHOD", "true");
		servletContext.setInitParameter("primefaces.THEME", "bluesky");
		servletContext.setInitParameter("primefaces.FONT_AWESOME", "true");
		
		servletContext.setInitParameter("org.omnifaces.EXCEPTION_TYPES_TO_IGNORE_IN_LOGGING", "javax.faces.application.ViewExpiredException,java.lang.IndexOutOfBoundsException");

	}
}
