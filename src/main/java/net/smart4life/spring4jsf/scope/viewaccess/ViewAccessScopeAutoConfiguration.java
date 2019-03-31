package net.smart4life.spring4jsf.scope.viewaccess;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;

/**
 * An {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration AutoConfiguration}
 * which registers the {@link ViewAccessScope} scope.
 *
 * @author Lars Grefer
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({FacesContext.class, UIViewRoot.class})
public class ViewAccessScopeAutoConfiguration {

	@Bean
	public static CustomScopeConfigurer viewAccessScopeConfigurer(WebApplicationContext applicationContext) {
		CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
		customScopeConfigurer.addScope(ViewAccessScope.NAME, new ViewAccessScope());
		return customScopeConfigurer;
	}

	@Bean
	public static VasWindowsContainer vasWindowsContainer() {
		return new VasWindowsContainer();
	}
}
