package net.smart4life.spring4jsf.scope.view;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

/**
 * An {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration AutoConfiguration}
 * which registers the {@link ViewScope} scope.
 *
 * @author Lars Grefer
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({FacesContext.class, UIViewRoot.class})
public class ViewScopeAutoConfiguration {

	@Bean
	public static CustomScopeConfigurer viewScopeConfigurer(WebApplicationContext applicationContext) {
		CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
		customScopeConfigurer.addScope(ViewScope.SCOPE_VIEW, new ViewScope(applicationContext));
		return customScopeConfigurer;
	}

	@Bean
	@SessionScope
	public SessionHelper viewScopeSessionHelper() {
		return new SessionHelper();
	}
}
