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
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.model.StandardModel;
import org.talframework.talui.mvc.process.ModelLayerAttributesResolver;
import org.talframework.talui.mvc.servlet.MVCRequestHandler;
import org.talframework.talui.mvc.servlet.RequestCoordinates;
import org.talframework.talui.mvc.servlet.model.CookieModelAttributeResolver;
import org.talframework.talui.mvc.servlet.util.RequestAttributeUtils;

/**
 * This class aids the dispatching servlet to render
 * an entire page.
 * 
 * @author Tom Spencer
 */
public class PageRequestHandler implements MVCRequestHandler {
	
	/**
	 * Always returns false
	 */
	public boolean canHandlePost() {
		return false;
	}
	
	/**
	 * Nothing to do as coordinates will always have a page
	 */
	public void validate(HttpServletRequest request, RequestCoordinates coords) {
	}
	
	/**
	 * Simply forwards request to the page template
	 */
	@SuppressWarnings("unchecked")
	@Trace
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp, ModelLayerAttributesResolver resolver, RequestCoordinates coords) throws ServletException, IOException {
		StandardModel model = null;
		
		try {
			model = new StandardModel(new CookieModelAttributeResolver(req, resp), true);
			model.pushLayer(coords.getApp().getModel());
			model.pushLayer(coords.getPage().getModel());
			
			RequestAttributeUtils.saveModel(req, model);
			RequestAttributeUtils.saveCurrentApp(req, coords.getApp());
			RequestAttributeUtils.saveCurrentPage(req, coords.getPage());
			
			// Update the model with any parameters in request if they are valid
			Enumeration e = req.getParameterNames();
			if( e != null ) {
				while( e.hasMoreElements() ) {
					String attr = (String)e.nextElement();
					if( model.containsKey(attr) ) {
						String val = req.getParameter(attr);
						if( val != null && val.length() != 0 ) model.setAttribute(attr, val);
					}
				}
			}
			
			String template = coords.getPage().getTemplate();
			req.getRequestDispatcher(template).forward(req, resp);
		}
		finally {
			if( model != null ) {
				model.popLayer(coords.getPage().getModel());
				model.popLayer(coords.getApp().getModel());
			}
		}
	}
}
