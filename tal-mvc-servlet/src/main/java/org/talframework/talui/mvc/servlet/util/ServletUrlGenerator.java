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

package org.talframework.talui.mvc.servlet.util;

import java.util.Iterator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.servlet.RequestCoordinates;
import org.talframework.talui.mvc.servlet.UrlGenerator;
import org.talframework.talui.util.htmlhelper.UrlCharStripper;

/**
 * This class implements the url generator for a normal
 * servlet environment.
 * 
 * @author Tom Spencer
 */
public class ServletUrlGenerator implements UrlGenerator {
	
	/** Member holds the request object */
	private final HttpServletRequest request;
	/** A helper string buffer to use */
	private StringBuilder buf = null;
	
	/**
	 * Private constructor to create a generator based
	 * on the request
	 * 
	 * @param request The request object to use.
	 */
	private ServletUrlGenerator(HttpServletRequest request) {
		this.request = request;
	}
	
	/**
	 * This static method provides access to the url
	 * generator. We only want to create the url generator
	 * once per request so this method stores the generator
	 * in the request and then checks there first before
	 * creating again.
	 * 
	 * @param request The request
	 * @param app The app request is dealing with
	 * @param coords The request coordinates (set for each request)
	 * @return The url generator
	 */
	public static UrlGenerator getUrlGenerator(HttpServletRequest request) {
		ServletUrlGenerator generator = (ServletUrlGenerator)request.getAttribute("_urlGenerator");
		if( generator == null ) {
			generator = new ServletUrlGenerator(request);
			request.setAttribute("_urlGenerator", generator);
		}
		
		return generator;
	}
	
	public static String generateSelfPageUrl(UrlGenerator generator, RequestCoordinates coords) {
		return generator.generateCustomUrl("page", coords.getApp().getName(), coords.getPage().getName(), null, null, null);
	}
	
	public static String generatePageUrl(UrlGenerator generator, RequestCoordinates coords, String page) {
		return generator.generateCustomUrl("page", coords.getApp().getName(), page, null, null, null);
	}
	
	public static String generateSelfActionUrl(UrlGenerator generator, RequestCoordinates coords, String action, Map<String, String> params) {
		return generator.generateCustomUrl("action", coords.getApp().getName(), coords.getPage().getName(), coords.getWindow().getName(), action, params);
	}
	
	public static String generateActionUrl(UrlGenerator generator, RequestCoordinates coords, String page, String window, String action, Map<String, String> params) {
		return generator.generateCustomUrl("action", coords.getApp().getName(), page, window, action, params);
	}
	
	public static String generateAsyncActionUrl(UrlGenerator generator, RequestCoordinates coords, String action, Map<String, String> params) {
		return generator.generateCustomUrl("asyncAction", coords.getApp().getName(), coords.getPage().getName(), coords.getWindow().getName(), action, params);
	}
	
	public static String generateAsyncRenderUrl(UrlGenerator generator, RequestCoordinates coords, Map<String, String> params) {
		return generator.generateCustomUrl("asyncRender", coords.getApp().getName(), coords.getPage().getName(), coords.getWindow().getName(), null, params);
	}
	
	/**
	 * Generates a url in the form /context/resource
	 */
	public String generateResourceUrl(String resource) {
		StringBuilder buf = getEmptyBuffer();
		buf.append(request.getContextPath());
		buf.append('/');
		buf.append(resource);
		return buf.toString();
	}

	/**
	 * Generates a url in the form /context/resource?params
	 */
	public String generateResourceUrl(String resource, Map<String, String> params) {
		StringBuilder buf = getEmptyBuffer();
		buf.append(request.getContextPath());
		buf.append('/');
		buf.append(resource);
		appendParameters(params, false);
		return buf.toString();
	}

	/**
	 * Generates a url in the form /context/app/page
	 */
	public String generatePageUrl(String page, Map<String, String> params) {
		RequestCoordinates coords = getRequestCoords();
		
		StringBuilder buf = getEmptyBuffer();
		buf.append(request.getContextPath());
		buf.append('/');
		buf.append(coords.getApp().getName());
		buf.append('/');
		buf.append("page");
		buf.append('/');
		buf.append(page);
		appendParameters(params, true);
		return buf.toString();
	}
	
	/**
	 * Generates a url in the form 
	 * /context/app/currentPage/currentWindow/action?params
	 */
	public String generateActionUrl(String action, Map<String, String> params) {
		RequestCoordinates coords = getRequestCoords();
		
		StringBuilder buf = getEmptyBuffer();
		buf.append(request.getContextPath());
		buf.append('/');
		buf.append(coords.getApp().getName());
		buf.append('/');
		buf.append("action");
		buf.append('/');
		buf.append(coords.getPage().getName());
		buf.append('/');
		buf.append(coords.getWindow().getName());
		buf.append('/');
		buf.append(action);
		appendParameters(params, true);
		return buf.toString();
	}
	
	public String generatePageUrl(String page, String target, String action, Map<String, String> params) {
		if( target == null ) {
			return generatePageUrl(page, params);
		}
		else {
			return generateCustomUrl("action", null, page, target, action, params);
		}
	}

	/**
	 * Validates the request and then creates the URL
	 */
	public String generateCustomUrl(String type, String app, String page, String window, String action, Map<String, String> params) {
		if( type == null ) throw new IllegalArgumentException("!!! Request type is needed for a custom url");
		if( app == null && RequestAttributeUtils.getCurrentApp(request) == null ) throw new IllegalArgumentException("!!! App (or current app in the request) is needed for a custom url");
		if( page == null && RequestAttributeUtils.getCurrentPage(request) == null ) throw new IllegalArgumentException("!!! Page (or current page in the request) is needed for a custom url");
		if( action != null && window == null && RequestAttributeUtils.getCurrentWindow(request) == null ) throw new IllegalArgumentException("!!! Window (or current window in the request) is needed for a custom url with an action");
		
		if( app == null ) app = RequestAttributeUtils.getCurrentApp(request).getName();
		if( page == null ) page = RequestAttributeUtils.getCurrentPage(request).getName();
		if( action != null && window == null ) window = RequestAttributeUtils.getCurrentWindow(request).getName();
		
		StringBuilder buf = getEmptyBuffer();
		buf.append(request.getContextPath());
		buf.append("/").append(app);
		buf.append("/").append(type);
		buf.append("/").append(page);
		if( window != null ) buf.append("/").append(window);
		if( action != null ) buf.append("/").append(action);
		appendParameters(params, true);
		
		return buf.toString();
	}
	
	public String generateTemplateUrl(String templateName, String renderType) {
		RequestCoordinates coords = getRequestCoords();
		
		StringBuilder buf = getEmptyBuffer();
		buf.append(request.getContextPath());
		buf.append("/").append("template");
		buf.append("/").append(templateName);
		buf.append(".").append(renderType);
		buf.append("?").append("app=").append(coords.getApp().getName());
		if( coords.getPage() != null ) buf.append("&page=").append(coords.getPage().getName());
		if( coords.getWindow() != null ) buf.append("&window=").append(coords.getWindow().getName());
		return buf.toString();
	}
	
	/**
	 * Helper to append parameters to the current buffer.
	 * 
	 * @param params The parameters
	 */
	private void appendParameters(Map<String, String> params, boolean escape) {
		if( params == null || params.size() == 0 ) return;
		
		buf.append('?');
		Iterator<String> it = params.keySet().iterator();
		boolean first = true;
		while( it.hasNext() ) {
			String k = it.next();
			String v = params.get(k);
			if( !first ) buf.append('&');
			buf.append(k);
			buf.append('=');
			if( escape ) UrlCharStripper.strip(v, buf);
			else buf.append(v);
			first = false;
		}
	}
	
	/**
	 * @return The empty string buffer (either a new one or an emptied one)
	 */
	private StringBuilder getEmptyBuffer() {
		if( buf == null ) buf = new StringBuilder();
		else buf.setLength(0);
		return buf;
	}
	
	/**
	 * Internal helper to get the request coordinates.
	 * 
	 * @return The current coordinates
	 */
	private RequestCoordinates getRequestCoords() {
		RequestCoordinates coords = RequestAttributeUtils.getRequestCoordinates(request);
		if( coords != null ) return coords;
		
		// See if in request (although above should be true if this is so)
		coords = new RequestCoordinates();
		coords.setApp(RequestAttributeUtils.getCurrentApp(request));
		if( coords.getApp() != null ) {
			coords.setPage(RequestAttributeUtils.getCurrentPage(request));
			coords.setWindow(RequestAttributeUtils.getCurrentWindow(request));
			return coords;
		}
		
		// Finally see if names are params
		String appName = request.getParameter("app");
		if( appName != null ) {
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			coords.setApp((AppConfig)ctx.getBean(appName));
			if( coords.getApp() == null ) throw new IllegalArgumentException("The Servlet URL generator has been accessed illegally. The app parameter is not a valid app in the Spring config");
			coords.setPage(request.getParameter("page"));
			coords.setWindow(request.getParameter("window"));
			return coords;
		}
		
		// If we get here something is wrong
		throw new IllegalArgumentException("The Servlet URL generator has been accessed illegally. There is no concept of current app, page or window to be found");
	}
}
