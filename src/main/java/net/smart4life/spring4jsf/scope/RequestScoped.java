package net.smart4life.spring4jsf.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Spring bean's request scoped and type safe annotation.
 * ProxyMode is set to TAGET_CLASS so spring bean annotated with @RequestScoped can be injected into smaller and bigger scopes
 * 
 * Created by ILIN02 on 07.02.2015.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface RequestScoped
{
}
