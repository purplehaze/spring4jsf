package net.smart4life.spring4jsf.scope.flash;

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
 * which registers the {@link FlashScope} scope.
 *
 * @author Lars Grefer
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({FacesContext.class, UIViewRoot.class})
public class FlashScopeAutoConfiguration {

	@Bean
	public static CustomScopeConfigurer flashScopeConfigurer(WebApplicationContext applicationContext) {
		CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
		customScopeConfigurer.addScope(FlashScope.SCOPE_FLASH, new FlashScope());
		return customScopeConfigurer;
	}

}
