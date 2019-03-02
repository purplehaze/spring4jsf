package net.smart4life.spring4jsf.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.smart4life.spring4jsf.scope.viewaccess.ViewAccessScope;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Annotation for gui controller implementation classes
 * This activate controller class in ViewAccessScope
 * 
 * Created by Roman Ilin on 07.02.2015.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(value = ViewAccessScope.NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface ViewAccessScoped
{
}
