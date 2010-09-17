/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.template.Renderer;
import org.talframework.talui.template.TemplateConfiguration;
import org.talframework.talui.template.core.TemplateConfigurationLocator;
import org.talframework.talui.template.render.SimpleRenderModel;
import org.talframework.talui.template.render.SpringRenderNodeFactory;
import org.talframework.talui.template.render.UrlGenerator;
import org.talframework.talui.template.render.apacheel.ApacheELExpressionEvaluator;

/**
 * This class is the base template servlet. It will serve up web templates
 * using a target renderer. The idea is that the template servlet is
 * totally standalone - however, it does require some help to this class
 * is an abstract class that must be derived from (the simplest way is
 * to connect this to a Spring world so an optional SpringTemplateServlet
 * is provided in this project).
 * 
 * <p>Essentially the template servlet will get a request such as ...</p>
 * 
 * <p><code>[template]/templateName.renderer[?namespace=xyz][otherParams]</code></p>
 * 
 * <p>Where ...</p>
 * <ul>
 * <li>template - The name of this servlet in web.xml, could be anything
 * <li>templateName - The name of the template config to use.
 * <li>renderer - The precise renderer to use
 * <li>namespace - The namesace to prefix ID's with
 * <li>otherParams - Used to generate requests on ourself
 * </ul>
 * 
 * <p>The servlet is initially invoked via a include from another servlet.
 * Much the way that a front controller will dispatch to a JSP. In this
 * case the namespace will be as an attribute and there will be no other
 * parameters.</p>
 * 
 * <p>However, often the templates generate requests back on themselves.
 * The most obvious is a JS or CSS request. Here the namespace and 
 * other parameters will be set. The other parameters are added by the
 * derived class and are typically there to be able to find the config
 * or setup the URL generator.</p>
 * 
 * <p>The derived class has the following jobs</p>
 * <ul>
 * <li>Find/get the URL Generator that generates URLs for the target env
 * <li>Find/get the Template Config given the templates name
 * <li>Find/get the Namespace to use
 * </ul>
 * 
 * <p>The default of all of these is to look in the request attributes
 * for _urlGenerator, templateConfig and namepsace. With the exception of
 * the latter, if they are not present then the servlet fails.</p>
 * 
 * @author Tom Spencer
 */
public class WebTemplateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Log logger = LogFactory.getLog(WebTemplateServlet.class);
	
	/** The configured URL Generator Factory */
	private UrlGeneratorFactory urlGeneratorFactory = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		String factoryClass = config.getInitParameter("urlGeneratorFactoryClass");
		if( factoryClass == null ) throw new ServletException("The template servlet requires a urlGeneratorFactoryClass setting");
		
		try {
			Class<?> cls = Class.forName(factoryClass);
			urlGeneratorFactory = (UrlGeneratorFactory)cls.newInstance();
		}
		catch( Exception e ) {
			throw new ServletException("The template servlet requires a valid urlGeneratorFactoryClass setting: " + e.getMessage(), e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = getTemplatePath(req);
		if( logger.isDebugEnabled() ) logger.debug(">>> Starting Template request: " + path);
		
		String name = getTemplateName(path);
		String renderType = getRenderType(path);
		
		UrlGenerator urlGenerator = urlGeneratorFactory.getUrlGenerator(req);
		TemplateConfiguration config = getTemplateConfig(req, name);
		
		String namespace = getNamespace(req);
		
		if( !config.hasRenderer(renderType) ) throw new IllegalArgumentException("The template servlet has not been provided with a valid render for config: " + renderType);
		Renderer renderer = config.getRenderer(renderType);
		
		SimpleRenderModel renderModel = new SimpleRenderModel(resp.getWriter(), urlGenerator);
		renderModel.setEvaluator(new ApacheELExpressionEvaluator());
		if( namespace != null ) renderModel.setNamespace(namespace);
		renderModel.setLocale(req.getLocale());
		renderModel.setUser(req.getUserPrincipal());
		renderModel.setBundle(config.getResourceName());
		
		// Add in the model if we are a model renderer
		Map<String, Object> model = null;
		if( renderer.isModelRenderer() ) {
			model = getModel(req);
			if( model != null ) renderModel.setModel(model);
			renderModel.setNodeFactory(SpringRenderNodeFactory.getInstance());
		}
		
		try {
			renderer.render(renderModel);
		}
		catch( Exception ex ) {
			logger.error("!!! Failure during template render: " + ex.getMessage());
			if( logger.isDebugEnabled() ) {
				StringBuilder buf = new StringBuilder();
				buf.append("Template Parameters: ");
				
				buf.append("\n\t**** Request Attributes ****");
				Enumeration e = req.getAttributeNames();
				while( e.hasMoreElements() ) {
					String k = (String)e.nextElement();
					buf.append("\n\t").append(k).append("=").append(req.getAttribute(k));
				}
				buf.append("\n\t**** End Contents ****");
				
				buf.append("\n\t**** Request Attributes ****");
				e = req.getParameterNames();
				while( e.hasMoreElements() ) {
					String k = (String)e.nextElement();
					buf.append("\n\t").append(k).append("=").append(req.getParameter(k));
				}
				buf.append("\n\t**** End Contents ****");
				
				if( model != null ) {
					buf.append("\n\t**** Model Attributes ****");
					Iterator<String> it = model.keySet().iterator();
					while( it.hasNext() ) {
						String k = it.next();
						buf.append("\n\t").append(k).append("=").append(model.get(k));
					}
					buf.append("\n\t**** End Contents ****");
				}
				
				logger.debug(buf.toString());
				
				ex.printStackTrace();
			}
			
			throw new ServletException(ex);
		}
		
		if( logger.isDebugEnabled() ) logger.debug("<<< Ending Template request: " + name);
	}
	
	/**
	 * Call to get the model to render with. Default it to
	 * just get it from the request
	 * 
	 * @param req The request
	 * @return The model
	 * @throws IllegalArgumentException If the model is not found
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getModel(HttpServletRequest req) {
		Map<String, Object> model = (Map<String, Object>)req.getAttribute("renderModel");
		//if( model == null ) throw new IllegalArgumentException("The template servlet cannot find the renderModel, you must re-direct to this template");
		return model;
	}
	
	/**
	 * The default will turn to the template configuration locator to get 
	 * the config requested. If this fails then it will turn the request
	 * to see if there is a "templateConfig" inside the model. If it finds
	 * it it will add it to the locator for future reference, though this
	 * has a detrimental effect on the performance of the locator.
	 * 
	 * @param req The request
	 * @return The template config
	 * @throws IllegalArgumentException If the config is not found
	 */
	protected TemplateConfiguration getTemplateConfig(HttpServletRequest req, String name) {
		name = stripPath(name);
		TemplateConfiguration config = TemplateConfigurationLocator.getInstance().getTemplate(name);
		if( config == null ) {
			Map<String, Object> model = getModel(req);
			config = (TemplateConfiguration)model.get("templateConfig");
			if( config != null ) TemplateConfigurationLocator.getInstance().setTemplateWhenRunning(name, config);
		}
		
		if( config == null ) throw new IllegalArgumentException("The template servlet cannot find the templateConfig inside the renderModel: " + name);
		return config;
	}
	
	/**
	 * Call to get the namespace. Default is to get this from the
	 * request. This method warns if it is not present.
	 * 
	 * @param req The request
	 * @return The namespace if any
	 */
	protected String getNamespace(HttpServletRequest req) {
		String namespace = (String)req.getAttribute("namespace");
		if( namespace == null ) namespace = req.getParameter("namespace");
		
		if( namespace == null ) logger.debug("No namespace for template");
		return namespace;
	}
	
	/**
	 * Call to get the template path, which is the path info for
	 * request. We first see if we have been included from another
	 * template by checking the request attribute. If that is null
	 * we get the path from the request directly. 
	 * 
	 * @param req 
	 * @return
	 */
	public String getTemplatePath(HttpServletRequest req) {
		String path = (String)req.getAttribute("javax.servlet.include.path_info");
		if( path == null ) path = req.getPathInfo();
		if( path == null ) throw new IllegalArgumentException("Failed to find path from request, this is like a config or major error");
		
		return path;
	}
	
	/**
	 * Gets the specific template name
	 * 
	 * @param path The full path containing template name and renderer
	 * @return The template name
	 */
	public String getTemplateName(String path) {
		int index = path.lastIndexOf('.');
		if( index < 0 ) throw new IllegalArgumentException("Unable to determine template name given path: " + path);
		return path.substring(0, index);
	}
	
	/**
	 * Gets the specific render type 
	 * 
	 * @param path The full path containing template name and renderer
	 * @return The render type
	 */
	public String getRenderType(String path) {
		int index = path.lastIndexOf('.');
		if( index < 0 ) throw new IllegalArgumentException("Unable to determine render type given path: " + path);
		return path.substring(index + 1);
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
