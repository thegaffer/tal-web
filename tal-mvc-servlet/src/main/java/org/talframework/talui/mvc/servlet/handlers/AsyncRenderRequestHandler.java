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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.StandardModel;
import org.talframework.talui.mvc.process.ModelLayerAttributesResolver;
import org.talframework.talui.mvc.render.BasicRenderModel;
import org.talframework.talui.mvc.servlet.RequestCoordinates;
import org.talframework.talui.mvc.servlet.util.RequestAttributeUtils;
import org.talframework.talui.template.core.TemplateConfigurationLocator;

/**
 * This handler will render a window for an async request,
 * i.e. it has been initiated by the browser and is not part
 * of a full page render request.
 * 
 * @author Tom Spencer
 */
public class AsyncRenderRequestHandler extends RenderRequestHandler {
	
	/**
	 * Always returns false
	 */
	public boolean canHandlePost() {
		return false;
	}
	
	/**
	 * Ensures there is a window to process. Also ensures that
	 * there is a model, app and page in the current request
	 * as this handler can only, erm, handle when included from
	 * the page.
	 */
	public void validate(HttpServletRequest request, RequestCoordinates coords) {
		if( coords.getWindow() == null ) throw new IllegalArgumentException("Cannot handle window render request with no window: " + coords);
	}
	
	/**
	 * Obtains the view and passes to its prepareRender method.
	 * If this returns a template then the template is included.
	 */
	@SuppressWarnings("unchecked")
	@Trace
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp, ModelLayerAttributesResolver resolver, RequestCoordinates coords) throws ServletException, IOException {
		StandardModel model = new StandardModel(resolver, false);
		// Turn off saving of model, any change is temporary (probably a pop-up window)
		resolver.setSaveMode(false);
		
		RequestAttributeUtils.saveModel(req, model);
		RequestAttributeUtils.saveCurrentApp(req, coords.getApp());
		RequestAttributeUtils.saveCurrentPage(req, coords.getPage());
		RequestAttributeUtils.saveCurrentWindow(req, coords.getWindow());
		
		WindowConfig window = coords.getWindow();
		model.pushLayer(window.getModel());
		
		View view = window.getWindow().getCurrentState(model);
		model.pushLayer(view.getModel());
		
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
		
		// Output a hidden form with windows configuration
		outputWindowModelForm(req, resp, coords.getApp(), coords.getPage(), window, window.getWindow(), model);
		
		BasicRenderModel renderModel = new BasicRenderModel();
		view.prepareRender(renderModel, model);
		
		// Add on any auto attributes to model
		if( window.getModel() != null ) {
			Collection<ModelAttribute> attrs = window.getModel().getAttributes();
			if( attrs != null ) {
				Iterator<ModelAttribute> it = attrs.iterator();
				while( it.hasNext() ) {
					ModelAttribute attr = it.next();
					if( attr.isAutoRenderAttribute() ) {
						Object val = model.getAttribute(attr.getName());
						if( val != null ) renderModel.setAttribute(attr.getName(), val);
					}
				}
			}
		}
		
		if( renderModel.getAttributes() != null ) RequestAttributeUtils.saveRenderModel(req, renderModel.getAttributes());

		dispatchView(req, resp, renderModel);
		
		// No point clearing up request as its an async render we know we end here!
	}

	/**
	 * Resolves and dispatches the view. Initially this is hard coded
	 * to support JSPs and treat all others a Web Templates
	 * 
	 * @param req
	 * @param resp
	 * @param model
	 */
	@Trace
	private void dispatchView(HttpServletRequest req, HttpServletResponse resp, BasicRenderModel model) throws IOException, ServletException {
		String template = model.getTemplate();
		
		// See if its a template
		if( TemplateConfigurationLocator.getInstance().getTemplate(template) != null ) {
			template = "/template/" + template + ".html"; 
		}
		
		req.getRequestDispatcher(template).include(req, resp);
	}
}