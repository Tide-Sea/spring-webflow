/*
 * Copyright 2004-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.faces.webflow;

import jakarta.faces.FacesException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;
import jakarta.faces.lifecycle.Lifecycle;
import jakarta.faces.lifecycle.LifecycleFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.faces.support.LifecycleWrapper;

/**
 * Custom {@link Lifecycle} for Spring Web Flow that only executes the APPLY_REQUEST_VALUES through INVOKE_APPLICATION
 * phases.
 * <p>
 * This Lifecycle does not execute the RESTORE_VIEW phase since view creation and restoration are now handled by the
 * {@link JsfViewFactory}.
 * </p>
 * 
 * @author Jeremy Grelle
 * @author Phillip Webb
 */
public class FlowLifecycle extends LifecycleWrapper {

	private static final Log logger = LogFactory.getLog(FlowLifecycle.class);

	private final Lifecycle wrapped;

	public static Lifecycle newInstance() {
		LifecycleFactory lifecycleFactory = JsfUtils.findFactory(LifecycleFactory.class);
		Lifecycle defaultLifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
		return new FlowLifecycle(defaultLifecycle);

	}

	FlowLifecycle(Lifecycle wrapped) {
		this.wrapped = wrapped;
	}

	public Lifecycle getWrapped() {
		return this.wrapped;
	}

	/**
	 * Executes APPLY_REQUEST_VALUES through INVOKE_APPLICATION.
	 */
	public void execute(FacesContext context) throws FacesException {
		logger.debug("Executing view post back lifecycle");
		for (int p = PhaseId.APPLY_REQUEST_VALUES.getOrdinal(); p <= PhaseId.INVOKE_APPLICATION.getOrdinal(); p++) {
			PhaseId phaseId = PhaseId.VALUES.get(p);
			if (!skipPhase(context, phaseId)) {
				context.setCurrentPhaseId(phaseId);
				invokePhase(context, phaseId);
			}
		}
	}

	private boolean skipPhase(FacesContext context, PhaseId phaseId) {
		if (context.getResponseComplete()) {
			return true;
		} else {
			return context.getRenderResponse();
		}
	}

	private void invokePhase(FacesContext context, PhaseId phaseId) {
		JsfUtils.notifyBeforeListeners(phaseId, this, context);
		if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
			logger.debug("Processing decodes");
			context.getViewRoot().processDecodes(context);
		} else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
			logger.debug("Processing validators");
			context.getViewRoot().processValidators(context);
		} else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
			logger.debug("Processing model updates");
			context.getViewRoot().processUpdates(context);
		} else {
			logger.debug("Processing application");
			context.getViewRoot().processApplication(context);
		}
		JsfUtils.notifyAfterListeners(phaseId, this, context);
	}
}
