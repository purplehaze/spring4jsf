package net.smart4life.spring4jsf.scope.viewaccess;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * ViewAccessScope windows container. It holds one ViewAccessScope container @see VasContainer per windowId (browser tab)
 * 
 * Created by Roman Ilin on 12.03.2015.
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Slf4j
public class VasWindowsContainer implements Serializable
{

	private static final int DEFAULT_REQUEST_TIMEOUT = 10000;
	private static final int DEFAULT_MAX_WINDOWS = 16;
	private static final long serialVersionUID = 1L;
	private Integer maxWindows;
	private int requestTimeout;

	private Map<String, VasContainer> windowContainerMap = new ConcurrentHashMap<>();

	@Autowired
	private Environment env;

	@PostConstruct
	private void init()
	{
		String maxWindowsStr = env.getProperty(ViewAccessScope.MAX_WINDOWS_PARAM);
		try
		{
			if(maxWindowsStr == null || maxWindowsStr.isEmpty())
			{
				maxWindows = DEFAULT_MAX_WINDOWS;
			}
			else
			{
				maxWindows = Integer.valueOf(maxWindowsStr);
			}

		}
		catch(Exception e)
		{
			log.error("An Exception occured while evaluating the max Number of Windows: ", e);
			maxWindows = DEFAULT_MAX_WINDOWS;
		}

		String requestTimeoutStr = env.getProperty(ViewAccessScope.REQUEST_TIMEOUT_PARAM);
		try
		{
			if(requestTimeoutStr == null || requestTimeoutStr.isEmpty())
			{
				requestTimeout = DEFAULT_REQUEST_TIMEOUT;
			}
			else
			{
				requestTimeout = Integer.valueOf(requestTimeoutStr);
			}
		}
		catch(Exception e)
		{
			log.error("An Exception occured while evaluating the request timeout: ", e);
			requestTimeout = DEFAULT_REQUEST_TIMEOUT;
		}

		log.info("ViewAccessScopeWindowsContainer initialized with maxWindows={}", maxWindows);
	}

	public VasContainer getVasContainer()
	{
		String wid = FacesContext.getCurrentInstance().getExternalContext().getClientWindow().getId();
		VasContainer container = windowContainerMap.get(wid);

		if(container == null)
		{
			/**
			 * because of concurrency size of windowContainerMap could be greater than maxWindows
			 * but this will be handled and the oldest entries will be deleted in next request
			 * @see VasWindowsContainer#destroyOldestContainer()
			 */
			if(windowContainerMap.size() >= maxWindows)
			{
				destroyOldestContainer();
			}

			container = new VasContainer(wid, requestTimeout);
			windowContainerMap.put(wid, container);
		}

		return container;
	}

	private void destroyOldestContainer()
	{
		synchronized(windowContainerMap)
		{
			TreeMap<Long, String> accessKeyMap = new TreeMap<>();
			for(VasContainer vasContainer : windowContainerMap.values())
			{
				accessKeyMap.put(vasContainer.getLastAccess(), vasContainer.getWindowId());
			}

			while(windowContainerMap.size() > maxWindows - 1)
			{
				long lastAccessToRemove = accessKeyMap.firstEntry().getKey();
				String widToRemove = accessKeyMap.firstEntry().getValue();
				VasContainer vasContainer = windowContainerMap.get(widToRemove);
				vasContainer.destroy();
				windowContainerMap.remove(widToRemove);
				accessKeyMap.remove(lastAccessToRemove);
			}
		}
	}

	@PreDestroy
	public void preDestroy()
	{
		for(VasContainer vasContainer : windowContainerMap.values())
		{
			vasContainer.destroy();
		}
	}
}
