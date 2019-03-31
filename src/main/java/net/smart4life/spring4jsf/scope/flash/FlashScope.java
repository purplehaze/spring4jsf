package net.smart4life.spring4jsf.scope.flash;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


public class FlashScope implements Scope {
	
	/**
	 * Scope identifier for view scope: "flash".
	 */
	public static final String SCOPE_FLASH = "flash";

    @Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		Object flashScopedBean = flash.get(name);

		if (flashScopedBean == null) {
            flashScopedBean = objectFactory.getObject();
			flash.put(name, flashScopedBean);
		}

		return flashScopedBean;
	}

    @Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// Unsupported feature
	}

    @Override
	public Object remove(String name) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

		return flash.remove(name);
	}

    @Override
	public Object resolveContextualObject(String key) {
		// Unsupported feature
		return null;
	}

    @Override
	public String getConversationId() {
		// Unsupported feature
		return null;
	}

}