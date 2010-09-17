/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.talui.mvc.servlet;

import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.WindowConfig;

/**
 * This class holds the coordinates of any incoming request.
 * These are:
 * 
 * <ul>
 * <li>appName - The name of the application being accessed</li>
 * <li>requestType - What the servlet should perform (see below)</li>
 * <li>pageName - The name of the page to operate on</li>
 * <li>windowName - The optional name of the window to operate on</li>
 * <li>action - The optional action to perform</li>
 * </ul>
 * 
 * <p>The request type is specific to the servlet the is processing
 * the request and should not be used outside of this context.</p>
 * 
 * TODO: Requires a unit test
 *  
 * @author Tom Spencer
 */
public final class RequestCoordinates {
	
	/** The request type */
	private String requestType;
	/** The app we are working on */
	private AppConfig app;
	/** The page we are working on */
	private PageConfig page;
	/** The window we are working on */
	private WindowConfig window;
	/** The action we are working on */
	private String action;
	
	public RequestCoordinates() {
		
	}
	
	public RequestCoordinates(
			String type,
			AppConfig app,
			PageConfig page,
			WindowConfig window,
			String action) {
		this.requestType = type;
		this.app = app;
		this.window = window;
		this.page = page;
		this.action = action;
	}
	
	public void reset() {
		this.requestType = null;
		this.app = null;
		this.window = null;
		this.page = null;
		this.action = null;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @return the app
	 */
	public AppConfig getApp() {
		return app;
	}

	/**
	 * @return the page
	 */
	public PageConfig getPage() {
		return page;
	}

	/**
	 * @return the window
	 */
	public WindowConfig getWindow() {
		return window;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @param app the app to set
	 */
	public void setApp(AppConfig app) {
		this.app = app;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(PageConfig page) {
		this.page = page;
	}
	
	public void setPage(String page) {
		if( app != null && page != null ) this.page = this.app.getPage(page);
		else this.page = null;
	}

	/**
	 * @param window the window to set
	 */
	public void setWindow(WindowConfig window) {
		this.window = window;
	}
	
	public void setWindow(String window) {
		if( page != null && window != null ) this.window = this.page.getWindow(window);
		else this.window = null;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
}
