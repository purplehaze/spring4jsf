package net.smart4life.spring4jsf.scope.viewaccess;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.web.jsf.FacesContextUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * JSF Listener to manage ViewAccessScope request lifecycle
 * 
 * Created by Roman Ilin on 28.02.2015.
 */
@Slf4j
public class ViewAccessScopeListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent event) {
		// we consider full requests only (no ajax partial request)
		if (isPartialRequest()) {
			return;
		}

		VasContainer container = getContainer();
		if (container != null) {
			if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
				container.onRequestEnd(true);
			} else if (isResponseComplete()) {
				container.onRequestEnd(false);
			}
		}
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// we consider full requests only (no ajax partial request)
		if (isPartialRequest()) {
			return;
		}

		if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {
			VasContainer container = getContainer();
			container.onRequestStart();
		}
	}

	private boolean isPartialRequest() {
		return FacesContext.getCurrentInstance().getPartialViewContext().isPartialRequest();
	}

	private boolean isResponseComplete() {
		boolean isResponseComplete = false;
		try {
			isResponseComplete = FacesContext.getCurrentInstance().getResponseComplete();
		} catch (IllegalStateException e) {
			log.error("An exception occured while evaluating response completeness", e);
		}
		return isResponseComplete;
	}

	private VasContainer getContainer() {
		VasContainer container = null;
		try {
			VasWindowsContainer vasWindowsContainer = FacesContextUtils
					.getWebApplicationContext(FacesContext.getCurrentInstance())
					.getBean(VasWindowsContainer.class);
			container = vasWindowsContainer.getVasContainer();
		} catch (BeanCreationException e) {
			log.warn("can not get VasWindowsContainer from session. Probably session has been invalidated", e);
		}

		return container;
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
