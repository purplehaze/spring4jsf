package net.smart4life.spring4jsf.scope.view;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;


/**
 * This class acts as "session-destroyed" listener, which call the destruction callbacks
 * of all view scoped beans that have not been destructed yet.
 *
 * @author Lars Grefer
 */
class SessionHelper implements DisposableBean {

	List<DestructionCallbackWrapper> getDestructionCallbackWrappers() {
		return destructionCallbackWrappers;
	}

	private List<DestructionCallbackWrapper> destructionCallbackWrappers = new LinkedList<>();

	@Override
	public void destroy() {
		this.destructionCallbackWrappers.forEach(DestructionCallbackWrapper::onSessionDestroy);
	}

	public void register(DestructionCallbackWrapper destructionCallbackWrapper) {
		cleanup();
		this.destructionCallbackWrappers.add(destructionCallbackWrapper);
	}

	public void unregister(DestructionCallbackWrapper destructionCallbackWrapper) {
		cleanup();
		this.destructionCallbackWrappers.remove(destructionCallbackWrapper);
	}

	synchronized void cleanup() {
		this.destructionCallbackWrappers.removeIf(DestructionCallbackWrapper::isCallbackCalled);
	}
	
	
}
