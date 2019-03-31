package net.smart4life.spring4jsf.config.tomcat;

import org.apache.catalina.Context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Creating tomcat auto configuration to enable jsf read facelets at integration
 * tests.
 *
 * @author Marcelo Fernandes
 */
@Configuration
@ConditionalOnClass(Context.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TomcatAutoConfiguration {

	private JsfTomcatContextCustomizer customizer = new JsfTomcatContextCustomizer();

	@Bean
	public JsfTomcatApplicationListener jsfTomcatApplicationListener() {
		return new JsfTomcatApplicationListener(this.customizer.getContext());
	}

	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> jsfTomcatFactoryCustomizer() {
		return factory -> factory.addContextCustomizers(this.customizer);
	}
}
