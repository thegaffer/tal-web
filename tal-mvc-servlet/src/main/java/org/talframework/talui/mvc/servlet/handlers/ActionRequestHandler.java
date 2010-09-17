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

package org.talframework.talui.mvc.servlet.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.config.PageEventConfig;
import org.talframework.talui.mvc.input.InputModel;
import org.talframework.talui.mvc.input.WebInputModel;
import org.talframework.talui.mvc.model.StandardModel;
import org.talframework.talui.mvc.process.ActionProcessor;
import org.talframework.talui.mvc.process.ModelLayerAttributesResolver;
import org.talframework.talui.mvc.servlet.MVCRequestHandler;
import org.talframework.talui.mvc.servlet.RequestCoordinates;
import org.talframework.talui.mvc.servlet.UrlGenerator;
import org.talframework.talui.mvc.servlet.util.RequestLogUtils;
import org.talframework.talui.mvc.servlet.util.ServletUrlGenerator;

/**
 * This servlet helper handles action requests on a window.
 * It's main handleRequest method will dispatch the request
 * on to Web MVCs internal action processor and then
 * form a url and send a redirect to the relevant page after
 * the action has processed.
 * 
 * @author Tom Spencer
 */
public class ActionRequestHandler implements MVCRequestHandler {
	private final static Log logger = LogFactory.getLog(ActionRequestHandler.class);
	
	/**
	 * Always returns true
	 */
	public boolean canHandlePost() {
		return true;
	}
	
	/**
	 * Ensures there is an action and window
	 */
	public void validate(HttpServletRequest request, RequestCoordinates coords) {
		if( coords.getWindow() == null ) throw new IllegalArgumentException("Cannot handle action request with no window: " + coords);
		if( coords.getAction() == null ) throw new IllegalArgumentException("Cannot handle action request with no action: " + coords);
	}
	
	/**
	 * Internal method to handle a doGet or a doPost. This
	 * method determines the page, window and action that has
	 * been invoked and passes on to the performAction method.
	 */
	@SuppressWarnings("unchecked")
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp, ModelLayerAttributesResolver resolver, RequestCoordinates coords) throws ServletException, IOException {
		RequestLogUtils.debugRequestParameters(req, logger);
		
		String forward = null; // The page to redirect to
		
		InputModel input = new WebInputModel(req.getParameterMap());
		
		ActionProcessor proc = new ActionProcessor(coords.getApp(), coords.getPage(), resolver);
		PageEventConfig pageEvent = proc.processAction(input, coords.getWindow(), coords.getAction());
		
		UrlGenerator urlGenerator = ServletUrlGenerator.getUrlGenerator(req);
		if( pageEvent != null ) {
			if( logger.isTraceEnabled() ) logger.trace("\tFiring page event: " + pageEvent);
			
			StandardModel model = new StandardModel(resolver, false);
			model.pushLayer(coords.getApp().getModel());
			model.pushLayer(coords.getPage().getModel());
			
			switch(pageEvent.getType()) {
			case PageEventConfig.TYPE_NORMAL:
				if( pageEvent.getAction() != null ) forward = ServletUrlGenerator.generateActionUrl(urlGenerator, coords, pageEvent.getPage(), pageEvent.getWindow(), pageEvent.getAction(), getEventActionParameters(pageEvent, model));
				else forward = ServletUrlGenerator.generatePageUrl(urlGenerator, coords, pageEvent.getPage());
				break;
				
			case PageEventConfig.TYPE_SELECT:
				String selectPage = "page"; // get the select page 
				String selectWindow = "window"; // get the select window
				String selectAction = "action"; // get the select action
				forward = ServletUrlGenerator.generateActionUrl(urlGenerator, coords, selectPage, selectWindow, selectAction, getEventActionParameters(pageEvent, model));
				break;
				
			case PageEventConfig.TYPE_BACK:
				String backPage = "back"; // Get the back page
				forward = ServletUrlGenerator.generatePageUrl(urlGenerator, coords, backPage);
				break;
			}
		}
		else {
			forward = urlGenerator.generateCustomUrl("page", coords.getApp().getName(), coords.getPage().getName(), null, null, null);
		}
		
		RequestLogUtils.debugSessionAttributes(req, logger);
		if( logger.isTraceEnabled() ) logger.trace("\tRedirecting to page: " + forward);
		resp.sendRedirect(forward);
	}
	
	/**
	 * Private helper to form a map of model attributes to
	 * be passed to action on another page given the list
	 * of model attributes to copy in the PageEvent config.
	 * 
	 * @param event The event
	 * @param model The current model (app and page layers)
	 */
	private Map<String, String> getEventActionParameters(PageEventConfig event, Model model) {
		Map<String, String> ret = null;
		String[] attrs = event.getModelAttributes();
		if( attrs != null && attrs.length > 0 ) {
			ret = new HashMap<String, String>();
			for( int i = 0 ; i < attrs.length ; i++ ) {
				String k = attrs[i];
				Object v = model.getAttribute(k);
				
				if( v != null ) ret.put(k, v.toString());
			}
			
			if( ret.size() == 0 ) ret = null;
		}
		
		return ret;
	}
}
