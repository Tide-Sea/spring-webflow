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

import jakarta.faces.context.FacesContext;

import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.RequestContext;

/**
 * A {@link FlowExecutionListener} that creates a {@link FlowFacesContext}
 * instance when a flow request is submitted and releases it when the request
 * has been processed.
 *
 * @author Rossen Stoyanchev
 */
public class FlowFacesContextLifecycleListener implements FlowExecutionListener {

	public static final String DEFAULT_FACES_CONTEXT =
			FlowFacesContextLifecycleListener.class.getName() + ".DEFAULT_FACES_CONTEXT";


	/**
	 * Creates a new instance of {@link FlowFacesContext} that is then available for the duration of the request.
	 * @param context the current flow request context
	 */
	public void requestSubmitted(RequestContext context) {

		FacesContext facesContext = getRequestFacesContext(context);
		if (facesContext != null) {
			// FacesContext already created, just wrap it (sets "current" instance internally)
			new FlowFacesContext(context, facesContext);
			return;
		}

		FlowFacesContext.newInstance(context, FlowLifecycle.newInstance());
	}

	/**
	 * Releases the current {@link FlowFacesContext} instance.
	 * @param context the source of the event
	 */
	public void requestProcessed(RequestContext context) {

		if (getRequestFacesContext(context) != null) {
			return;
		}

		FacesContext.getCurrentInstance().release();
	}


	private FacesContext getRequestFacesContext(RequestContext context) {
		return (FacesContext) context.getExternalContext().getRequestMap().get(DEFAULT_FACES_CONTEXT);
	}

}
