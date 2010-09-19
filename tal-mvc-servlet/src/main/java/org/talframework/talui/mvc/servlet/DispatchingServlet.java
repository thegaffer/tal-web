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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.talframework.tal.aspects.annotations.HttpTrace;
import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.servlet.error.ServletExceptionResolver;
import org.talframework.talui.mvc.servlet.error.StandardExceptionResolver;
import org.talframework.talui.mvc.servlet.handlers.ActionRequestHandler;
import org.talframework.talui.mvc.servlet.handlers.AsyncActionRequestHandler;
import org.talframework.talui.mvc.servlet.handlers.AsyncRenderRequestHandler;
import org.talframework.talui.mvc.servlet.handlers.PageRequestHandler;
import org.talframework.talui.mvc.servlet.handlers.RenderRequestHandler;
import org.talframework.talui.mvc.servlet.model.CookieModelAttributeResolver;
import org.talframework.talui.mvc.servlet.util.RequestAttributeUtils;
import org.talframework.talui.mvc.servlet.util.ServletUrlGenerator;

/**
 * This is the main WebMVC servlet. This servlet then 
 * handles all requests and dispatches them to the 
 * appropriate sub-servlet based on the path. The  
 * requests are:</p>
 * 
 * <p><b>Page Request</b><br />
 * If the path is /app/page/pageName then this will cause
 * the servlet to render the entire page specified in
 * the url.</p>
 * 
 * <p><b>Window Render Request</b><br />
 * If the path is /app/render/pageName/windowName then 
 * this will cause the servlet to render the window
 * specified in the url in its current view/state. Note
 * that it is safe to include this page as the special
 * javax.servlet.include attributes are inspected.</p>
 * 
 * <p><b>Action Request</b><br />
 * If the path is /app/action/pageName/window/action then 
 * this will cause the servlet to invoke the action on 
 * the given window and then redirect the browser back
 * to the same page or a different one.</p>
 *  
 * @author Tom Spencer
 */
public class DispatchingServlet extends HttpServlet {
	private final static long serialVersionUID = 1L;
	
	/** Member holds the application config, obtained at startup */
	protected WebApplicationContext ctx = null;
	
	/** Member holds the exception resolver */
	private ServletExceptionResolver exceptionResolver = null;
	/** Member holds the request handlers */
	private Map<String, MVCRequestHandler> handlers = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		
		exceptionResolver = new StandardExceptionResolver();
		
		handlers = new HashMap<String, MVCRequestHandler>();
		handlers.put("action", new ActionRequestHandler());
		handlers.put("page", new PageRequestHandler());
		handlers.put("render", new RenderRequestHandler());
		handlers.put("asyncAction", new AsyncActionRequestHandler());
		handlers.put("asyncRender", new AsyncRenderRequestHandler());

		super.init(config);
	}
	
	@Override
	@HttpTrace
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestCoordinates coords = null;
		try {
			coords = getRequestCoordinates(req);
			
			MVCRequestHandler handler = handlers.get(coords.getRequestType());
			if( handler == null ) throw new IllegalArgumentException("No request handler for given coordinates: " + coords);
			
			handler.validate(req, coords);
			handler.handleRequest(req, resp, new CookieModelAttributeResolver(req, resp), coords);
		}
		catch( Exception e ) {
			getExceptionResolver().handleException(req, resp, e);
		}
	}
	
	@Override
	@HttpTrace
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestCoordinates coords = null;
		
		try {
			coords = getRequestCoordinates(req);
			
			MVCRequestHandler handler = handlers.get(coords.getRequestType());
			if( handler == null || !handler.canHandlePost() ) throw new IllegalArgumentException("No request handler (or handler cannot handle POST) for given coordinates: " + coords);
			
			handler.validate(req, coords);
			handler.handleRequest(req, resp, new CookieModelAttributeResolver(req, resp), coords);
		}
		catch( Exception e ) {
			getExceptionResolver().handleException(req, resp, e);
		}
	}
	
	/**
	 * Internal helper to get the request coordinates. This method
	 * checks if we are an inner request and the coordinates are 
	 * already set in the attributes - if so it uses them. Failing
	 * that it gets the from the path....
	 * 
	 * <p><code>appName/requestType/pageName[/windowName[/action]]</code></p>
	 * 
	 * @param req The request
	 * @return The request coordinates
	 */
	@Trace
	private RequestCoordinates getRequestCoordinates(HttpServletRequest req) {
		RequestCoordinates coords = RequestAttributeUtils.getRequestCoordinates(req);
		
		String servlet = stripPath(req.getServletPath());
		String path = stripPath(req.getPathInfo());
		// Existing request, get paths as includes
		if( coords != null ) {
			servlet = stripPath((String)req.getAttribute("javax.servlet.include.servlet_path"));
			path = stripPath((String)req.getAttribute("javax.servlet.include.path_info"));
		}
		// New request, save request coords instance and url generator
		else {
			coords = new RequestCoordinates();
			RequestAttributeUtils.saveRequestCoordinates(req, coords);
			
			// Also add in the url generator at this point
			ServletUrlGenerator.getUrlGenerator(req);
		}
		
		String[] pathElements = path.split("/");
		coords.reset();
		
		coords.setApp((AppConfig)ctx.getBean(servlet));
		if( pathElements.length > 0 ) coords.setRequestType(pathElements[0]);
		if( pathElements.length > 1 ) coords.setPage(pathElements[1]);
		if( pathElements.length > 2 ) coords.setWindow(pathElements[2]);
		if( pathElements.length > 3 ) coords.setAction(pathElements[3]);
		
		if( coords.getApp() == null ) throw new InvalidCoordinatesException(servlet, path, "No App found");
		if( coords.getPage() == null ) throw new InvalidCoordinatesException(servlet, path, "No Page found");
		if( coords.getRequestType() == null ) throw new InvalidCoordinatesException(servlet, path, "No request type found");
		
		return coords;
	}
	
	/**
	 * @return the exceptionResolver
	 */
	public ServletExceptionResolver getExceptionResolver() {
		return exceptionResolver;
	}

	/**
	 * @param exceptionResolver the exceptionResolver to set
	 */
	public void setExceptionResolver(ServletExceptionResolver exceptionResolver) {
		this.exceptionResolver = exceptionResolver;
	}
	
	/**
	 * Internal helper to strip a leading / from a path
	 * 
	 * @param path The input path that potentially has a leading /
	 * @return The path without the leading /
	 */
	private String stripPath(String path) {
		if( path != null && path.charAt(0) == '/' ) path = path.substring(1);
		return path;
	}
}
