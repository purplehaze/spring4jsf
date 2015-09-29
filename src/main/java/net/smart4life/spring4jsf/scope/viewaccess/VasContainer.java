package net.smart4life.spring4jsf.scope.viewaccess;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is container class for ViewAccessScope'd beans for one windowId (normally it is browser tab)
 * On every new non ajax request ViewAccessScoped beans requested by .xhtml pages or other beans
 * are marked as used in scope.
 * Used beans are kept in memory until other request.
 * Not used beans are deleted from scope after JSFs RENDER_RESPONSE phase.
 * 
 * Created by Roman Ilin on 06.03.2015.
 */
public class VasContainer implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(VasContainer.class);

	public static final String NAME = VasContainer.class.getName();

	/**
	 * container map(beanName, bean) for beans used in request. 
	 */
	private Map<String, Object> beansMap = new ConcurrentHashMap<String, Object>();

	/**
	 * map(beanName, callback) with callbacks for @PreDestroy methods
	 */
	private Map<String, Runnable> destroyersMap = new ConcurrentHashMap<String, Runnable>();

	/**
	 * names of beans used during current request
	 */
	private Set<String> usedBeanNames = Collections.synchronizedSet(new HashSet<String>());
	private final String windowId;
	private long lastAccess = 0;

	/**
	 *  it is a counter for actually running request which using this container (windowId)
	 *  normally it should be max 1. In case of concurrent requests only the last request should destroy unused beans
	 */
	private AtomicInteger activeRequestCnt = new AtomicInteger(0);

	private final long requestTimeout;

	public VasContainer(String windowId, int requestTimeout)
	{
		this.windowId = windowId;
		this.requestTimeout = requestTimeout;
	}

	/**
	 * on request start empty usedBeanNames container.
	 * So we mark only beans used in current request
	 */
	public void onRequestStart()
	{
		//        logger.debug("!!!!! start moveViewContainer() size={}", containerList.size());
		int cnt = activeRequestCnt.incrementAndGet();
		if(cnt == 1)
		{
			usedBeanNames.clear();
		}
		/**
		 * at this point more than one request is active
		 * If last access for this container is older than 'maxREquestWaitTime'
		 * clear unusedBeanNames and reset request counter
		 */
		else if(lastAccess < (System.currentTimeMillis() - requestTimeout))
		{
			usedBeanNames.clear();
			activeRequestCnt.set(1);
		}
		//        logger.debug("!!!!! end moveViewContainer() size={}", containerList.size());
	}

	/**
	 * by request end destroy beans which where contained in ViewAccessScope
	 * but weren't used in this request.
	 * If isDestroyUnusedBeans true than destroy beans not touched in this request.
	 * This it true for all requests with normal JSF lifecycle.
	 * But it should be false for request without RENDER_RESPONSE jsf phase
	 * 
	 * @param isDestroyUnusedBeans should beans not touched in this request be destroyed
	 */
	public void onRequestEnd(boolean isDestroyUnusedBeans)
	{
		int cnt = activeRequestCnt.decrementAndGet();
		if(cnt == 0 && isDestroyUnusedBeans)
		{
			destroyUnusedBeans();
		}
	}

	/**
	 * get ViewAccessScoped bean by name
	 * 
	 * @param name
	 * @return
	 */
	public Object get(String name)
	{
		usedBeanNames.add(name);
		lastAccess = System.currentTimeMillis();
		Object bean = beansMap.get(name);
		return bean;
	}

	/**
	 * put new bean in ViewAccessScope
	 * 
	 * @param name
	 * @param bean
	 */
	public void put(String name, Object bean)
	{
		beansMap.put(name, bean);
	}

	/**
	 * remove bean from scope and call PreDestroy callbacks on those bean
	 * 
	 * @param name
	 * @return
	 */
	public Object remove(String name)
	{
		Object bean = beansMap.remove(name);

		Runnable destroyer = destroyersMap.remove(name);
		if(destroyer != null)
		{
			destroyer.run();
		}

		return bean;
	}

	/**
	 * remove all beans from scope
	 * called on session destroy
	 */
	public void destroy()
	{
		for(String beanName : beansMap.keySet().toArray(new String[0]))
		{
			remove(beanName);
		}
		usedBeanNames.clear();
	}

	private void destroyUnusedBeans()
	{
		for(String beanName : beansMap.keySet().toArray(new String[0]))
		{
			if(!usedBeanNames.contains(beanName))
			{
				remove(beanName);
			}
		}
	}

	/**
	 * return milliseconds of last access to this scope container
	 * 
	 * @return
	 */
	public long getLastAccess()
	{
		return lastAccess;
	}

	/**
	 * returns windowId identifier of this container
	 * 
	 * @return
	 */
	public String getWindowId()
	{
		return windowId;
	}

	/**
	 * register destruction callback (PreDestroy) for one bean
	 * 
	 * @param name
	 * @param callback
	 */
	public void registerDestructionCallback(String name, Runnable callback)
	{
		destroyersMap.put(name, callback);
	}

}
