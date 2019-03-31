package net.smart4life.spring4jsf.scope.flash;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.*;

/**
 * Created by roman on 07.02.2015.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(value = FlashScope.SCOPE_FLASH, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface FlashScoped {
}
