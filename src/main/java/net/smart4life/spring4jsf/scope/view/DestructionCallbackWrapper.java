package net.smart4life.spring4jsf.scope.view;

import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Wrapper around the {@link ViewScope#registerDestructionCallback(String, Runnable) destruction callback} of
 * view scoped beans which acts as listener for {@link javax.faces.event.PreDestroyViewMapEvent view-map destruction} and
 * session destruction.
 *
 * @author Lars Grefer
 * @see ViewScope#registerDestructionCallback(String, Runnable)
 */
class DestructionCallbackWrapper implements SystemEventListener {
	private static final Logger log = LoggerFactory.getLogger(DestructionCallbackWrapper.class);
	
	private final String beanName;

	private Runnable callback;
	private boolean callbackCalled;

	DestructionCallbackWrapper(String beanName, Runnable callback) {
		Assert.hasText(beanName, "beanName must not be null or empty");
		Assert.notNull(callback, "callback must not be null");
		this.beanName = beanName;
		this.callback = callback;
	}

	@Override
	public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
		doRunCallback(Source.VIEW);
	}

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}

	void onSessionDestroy() {
		doRunCallback(Source.SESSION);
	}

	private synchronized void doRunCallback(Source source) {
		if (!isCallbackCalled()) {
			log.info(source.getLogPattern(), getBeanName());
			this.callback.run();
			this.callbackCalled = true;
			this.callback = null;
		}
	}
	
	String getBeanName() {
		return beanName;
	}

	Runnable getCallback() {
		return callback;
	}

	boolean isCallbackCalled() {
		return callbackCalled;
	}

	private enum Source {
		VIEW("Calling destruction callbacks for bean {} because the view map is destroyed"),
		SESSION("Calling destruction callbacks for bean {} because the session is destroyed");

		private final String logPattern;
		
		private Source(String logPattern){
			this.logPattern = logPattern;
		}

		String getLogPattern() {
			return logPattern;
		}
	}
}
