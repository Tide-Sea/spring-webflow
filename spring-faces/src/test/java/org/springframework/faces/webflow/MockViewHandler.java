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

import java.io.IOException;
import java.util.Locale;

import jakarta.faces.FacesException;
import jakarta.faces.application.ViewHandler;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;

public class MockViewHandler extends ViewHandler {
	private UIViewRoot createViewRoot;

	private UIViewRoot restoreViewRoot;

	public Locale calculateLocale(FacesContext context) {
		return null;
	}

	public String calculateRenderKitId(FacesContext context) {
		return null;
	}

	public UIViewRoot createView(FacesContext context, String viewId) {
		return this.createViewRoot;
	}

	public void initView(FacesContext context) throws FacesException {
		// do nothing
	}

	/**
	 * Set the view root that this mock is supposed to create
	 * @param createViewRoot the view to set.
	 */
	public void setCreateView(UIViewRoot createViewRoot) {
		this.createViewRoot = createViewRoot;
	}

	/**
	 * Set the view root that this mock is supposed to restore
	 * @param restoreViewRoot the view to set.
	 */
	public void setRestoreView(UIViewRoot restoreViewRoot) {
		this.restoreViewRoot = restoreViewRoot;
	}

	public String getActionURL(FacesContext context, String viewId) {
		return null;
	}

	public String getResourceURL(FacesContext context, String path) {
		return null;
	}

	@Override
	public String getWebsocketURL(FacesContext context, String channel) {
		return null;
	}

	/**
	 * Really simple implementation to exercise rendering and state saving
	 */
	public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
		context.getViewRoot().encodeAll(context);

	}

	public UIViewRoot restoreView(FacesContext context, String viewId) {
		return this.restoreViewRoot;
	}

	public void writeState(FacesContext context) {
	}
}
