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

package org.tpspencer.tal.mvc.servlet.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.tpspencer.tal.mvc.config.AppConfig;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.config.WindowConfig;
import org.tpspencer.tal.mvc.model.StandardModel;
import org.tpspencer.tal.mvc.servlet.RequestCoordinates;

/**
 * This helper class is used by the servlet helpers to
 * store and retrive common request attributes. By using
 * this class they are protected from casting and ensuring
 * they use the right name! The common attribute names are
 * made public though.
 * 
 * @author Tom Spencer
 */
public class RequestAttributeUtils {

	public final static String COORDS = "_requestCoords";
	public final static String MODEL = "model";
	public final static String APP = "currentApp";
	public final static String PAGE = "currentPage";
	public final static String WINDOW = "currentWindow";
	public final static String NAMESPACE = "namespace";
	
	public final static String TEMPLATE_CONFIG = "templateConfig";
	public final static String RENDER_MODEL = "renderModel";
	
	/**
	 * Call to get the request coordinates
	 * 
	 * @param req The current request
	 * @return The coordinates
	 */
	public static RequestCoordinates getRequestCoordinates(HttpServletRequest req) {
		return (RequestCoordinates)req.getAttribute(COORDS);
	}
	
	/**
	 * Call to set the request coords on the current request
	 * 
	 * @param req The current request
	 * @param coords The coordinates
	 */
	public static void saveRequestCoordinates(HttpServletRequest req, RequestCoordinates coords) {
		req.setAttribute(COORDS, coords);
	}
	
	/**
	 * Returns the model if on the request
	 */
	public static StandardModel getModel(HttpServletRequest req) {
		return (StandardModel)req.getAttribute(MODEL);
	}
	
	/**
	 * Saves the model onto the request
	 * 
	 * @param req The request
	 * @param model The model to save
	 */
	public static void saveModel(HttpServletRequest req, StandardModel model) {
		req.setAttribute(MODEL, model);
	}
	
	/**
	 * @return The current app from the request.
	 */
	public static AppConfig getCurrentApp(HttpServletRequest req) {
		return (AppConfig)req.getAttribute(APP);
	}
	
	/**
	 * Call to save the current app in the request
	 */
	public static void saveCurrentApp(HttpServletRequest req, AppConfig app) {
		req.setAttribute(APP, app);
	}
	
	/**
	 * @return The current page from the request.
	 */
	public static PageConfig getCurrentPage(HttpServletRequest req) {
		return (PageConfig)req.getAttribute(PAGE);
	}
	
	/**
	 * Call to save the current page in the request
	 */
	public static void saveCurrentPage(HttpServletRequest req, PageConfig page) {
		req.setAttribute(PAGE, page);
	}
	
	/**
	 * @return The current window from the request.
	 */
	public static WindowConfig getCurrentWindow(HttpServletRequest req) {
		return (WindowConfig)req.getAttribute(WINDOW);
	}
	
	/**
	 * Call to save the current window in the request
	 */
	public static void saveCurrentWindow(HttpServletRequest req, WindowConfig window) {
		req.removeAttribute(NAMESPACE);
		if( window == null ) {
			req.removeAttribute(WINDOW);
		}
		else {
			req.setAttribute(WINDOW, window);
			if( window.getNamespace() != null ) req.setAttribute(NAMESPACE, window.getNamespace());
		}
	}
	
	/**
	 * Call to remove the current window from the request
	 */
	public static void clearCurrentWindow(HttpServletRequest req) {
		req.removeAttribute(WINDOW);
		req.removeAttribute(NAMESPACE);
	}

	/**
	 * Call to get the render model
	 * 
	 * @param req The current request
	 * @return The return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRenderModel(HttpServletRequest req) {
		return (Map<String, Object>)req.getAttribute(RENDER_MODEL);
	}
	
	/**
	 * Call to save the render model
	 */
	public static void saveRenderModel(HttpServletRequest req, Map<String, Object> renderModel) {
		req.setAttribute(RENDER_MODEL, renderModel);
	}

	/**
	 * Call to clear the render model
	 */
	public static void clearRenderModel(HttpServletRequest req) {
		req.removeAttribute(RENDER_MODEL);
	}
}
