package net.smart4life.spring4jsf.config.tomcat;

import lombok.Getter;
import org.apache.catalina.Context;

import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;

/**
 * Jsf Tomcat Context customizer to capture context.
 *
 * @author Marcelo Fernandes
 */
public class JsfTomcatContextCustomizer implements TomcatContextCustomizer {

	@Getter
	private Context context;

	@Override
	public void customize(Context context) {
		this.context = context;
	}
}
