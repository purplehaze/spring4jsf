/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package net.smart4life.spring4jsf.scope.view;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PreDestroyViewMapEvent;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Implementation of view scope.
 *
 * @author Marcelo Fernandes
 * @author Lars Grefer
 */
public class ViewScope implements Scope {

	/**
	 * Scope identifier for view scope: "view".
	 */
	public static final String SCOPE_VIEW = "view";

	private final BeanFactory beanFactory;
	
	public ViewScope(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object get(String name, ObjectFactory objectFactory) {
		return getViewRoot()
				.getViewMap()
				.computeIfAbsent(name, k -> objectFactory.getObject());
	}

	@Override
	public Object remove(String name) {

		UIViewRoot viewRoot = getViewRoot();
		Object bean = viewRoot.getViewMap().remove(name);

		viewRoot
				.getViewListenersForEventClass(PreDestroyViewMapEvent.class)
				.stream()
				.filter(systemEventListener -> systemEventListener instanceof DestructionCallbackWrapper)
				.map(systemEventListener -> (DestructionCallbackWrapper) systemEventListener)
				.filter(destructionCallbackWrapper -> destructionCallbackWrapper.getBeanName().equals(name))
				.findFirst()
				.ifPresent(destructionCallbackWrapper -> {
					viewRoot.unsubscribeFromViewEvent(PreDestroyViewMapEvent.class, destructionCallbackWrapper);
					getSessionHelper().unregister(destructionCallbackWrapper);
				});

		return bean;
	}

	@Override
	public String getConversationId() {
		return getViewRoot().getViewId();
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		DestructionCallbackWrapper wrapper = new DestructionCallbackWrapper(name, callback);

		getViewRoot().subscribeToViewEvent(PreDestroyViewMapEvent.class, wrapper);
		getSessionHelper().register(wrapper);
	}

	@Override
	public Object resolveContextualObject(String key) {
		RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
		return attributes.resolveReference(key);
	}

	@NonNull
	private UIViewRoot getViewRoot() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {
			throw new IllegalStateException("No FacesContext found.");
		}

		UIViewRoot viewRoot = facesContext.getViewRoot();
		if (viewRoot == null) {
			throw new IllegalStateException("No ViewRoot found");
		}

		return viewRoot;
	}

	private SessionHelper getSessionHelper() {
		return this.beanFactory.getBean(SessionHelper.class);
	}

}