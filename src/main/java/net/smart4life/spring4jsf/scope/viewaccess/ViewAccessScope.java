package net.smart4life.spring4jsf.scope.viewaccess;

import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * ViewAccessScope implementation of springs Scope interface.
 * This should be activated on initialization of spring container
 * 
 * @author Roman Ilin
 *
 */
public class ViewAccessScope implements Scope
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ViewAccessScope.class);

	public static final String NAME = "viewAccess";
	public static final String MAX_WINDOWS_PARAM = "viewAccessScope.max.windows";
	public static final String REQUEST_TIMEOUT_PARAM = "viewAccessScope.request.timeout";

	/**
	 * get ViewAccessScoped bean from scope or create a new one
	 */
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory)
	{
		//		LOGGER.debug("get() viewAccessScoped bean={}", name);

		VasContainer container = getContainer();
		Object bean = container.get(name);

		if(bean == null)
		{
			//			LOGGER.debug("create by factory viewAccessScoped bean={}", name);
			bean = objectFactory.getObject();
			container.put(name, bean);
		}

		return bean;
	}

	/**
	 * find VasContainer by browser tab windowId
	 * 
	 * @return
	 */
	private VasContainer getContainer()
	{
		VasWindowsContainer vasWindowsContainer = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance()).getBean(VasWindowsContainer.class);
		VasContainer container = vasWindowsContainer.getVasContainer();

		return container;
	}

	/**
	 * register PreDestroy callbacks
	 */
	@Override
	public void registerDestructionCallback(String name, Runnable callback)
	{
		VasContainer container = getContainer();
		container.registerDestructionCallback(name, callback);
	}

	/**
	 * remove bean from scope by bean's name
	 */
	@Override
	public Object remove(String name)
	{
		//		LOGGER.debug("remove viewAccessScope bean={}", name);
		VasContainer container = getContainer();
		return container.remove(name);
	}

	@Override
	public Object resolveContextualObject(String key)
	{
		// Unsupported feature
		return null;
	}

	@Override
	public String getConversationId()
	{
		// Unsupported feature
		return null;
	}

}