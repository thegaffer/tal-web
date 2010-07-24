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

package org.tpspencer.tal.mvc.commons.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.BeanNameAware;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.commons.util.PrototypeResourceBundle;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.render.RenderModel;
import org.tpspencer.tal.mvc.view.AbstractView;
import org.tpspencer.tal.template.Compiler;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.compiler.html.HtmlCompiler;
import org.tpspencer.tal.template.compiler.js.JsCompiler;
import org.tpspencer.tal.template.core.BasicTemplateConfiguration;
import org.tpspencer.tal.template.core.xml.XmlTemplateReader;

/**
 * This class represents a view which contains its own 
 * self-configuring template configuration. The config
 * is produced by the settings inside the view.
 * 
 * @author Tom Spencer
 */
public abstract class AbstractTemplateView extends AbstractView implements BeanNameAware {

	/** The name of the view - defaults to primary beans simple name */
	private String viewName = null;
	/** The name of the bean form is formed from (usually equates to attribute in model) */
	private String viewBeanName = null;
	/** The name of the template config - defaults to templateFile (minus the xml) or package of primary + the view name */
	private String templateName = null;
	
	/** Holds the URI of a template file to load (optional) */
	private String templateFile = null;
	/** Holds a custom resource bundle name - if null tries to find one in package of primary bean and view name */
	private String resourceBase = null;
	/** Holds the classes of the primary bean to display */
	private Class<?> primaryBean = null;
	
	/** Determines if we should use the form version of the compilers */
	private boolean asForm = false;
	/** Holds the compilers, if not set a default set is used */
	private Map<String, Compiler> compilers = null;
	
	/** An additional set of model attributes that will be added to render model */
	private Map<String, String> renderAttributes = null;
	
	/** The internal template configuration set during initialisation */
	private TemplateConfiguration config = null;
	
	public void init() {
		// Defaults
		if( viewName == null ) viewName = getDefaultViewName(); 
		if( templateName == null ) templateName = getDefaultTemplateName();
		
		// Validation
		if( viewName == null ) throw new IllegalArgumentException("A template view must have a name");
		if( templateName == null ) throw new IllegalArgumentException("A template view must have a template name");
		if( primaryBean == null ) throw new IllegalArgumentException("A template view must have a primary bean set");
		
		// Model
		if( getModel() == null ) {
			setModel(createDefaultModel());
		}
		
		// Set Config
		BasicTemplateConfiguration config = new BasicTemplateConfiguration();
		config.setName(templateName);
		config.setRootTemplate(viewName);
		
		if( templateFile != null ) {
			config.setTemplates(XmlTemplateReader.getStdReader().loadTemplates(templateFile));
		}
		
		if( primaryBean != null ) {
			config.addBeanTemplate(primaryBean, true);
		}
		
		if( resourceBase != null ) config.setResourceBase(resourceBase);
		else {
			try {
				ResourceBundle.getBundle(config.getResourceName());
			}
			catch( MissingResourceException e ) {
				config.setResourceBase(PrototypeResourceBundle.class.getName());
			}
		}
		
		if( compilers == null ) {
			compilers = new HashMap<String, Compiler>();
			compilers.put("html", new HtmlCompiler(asForm));
			compilers.put("js", new JsCompiler(asForm));
		}
		config.setCompilers(compilers);
		
		preInitConfig(config);
		
		config.init();
		this.config = config;
	}

	/**
	 * Called if the views name is not set. The views name is used
	 * to get the
	 * 
	 * The views name is used for
	 * the template name. The default view name is the primary beans' 
	 * classes' simple name with an initial lower case.
	 * 
	 * <p>Derived classes may prefix, suffix or replace this altogether.</p>
	 * 
	 * @return The default view name
	 * @throws IllegalArgumentException if the name cannot be set
	 */
	protected String getDefaultViewName() {
		if( primaryBean == null ) throw new IllegalArgumentException("Cannot set a default view name if there is no primary bean class for the view");
		
		String name = primaryBean.getSimpleName();
		if( name.length() > 1 ) name = name.substring(0, 1).toLowerCase() + name.substring(1);
		else name = name.toLowerCase();
		return name;
	}
	
	/**
	 * Called to get the default template name if it is not supplied.
	 * If there is a template file the name defaults to template file
	 * minus the xml. Otherwise it goes to package or primary bean +
	 * the view name.
	 * 
	 * <p>Derived classes may prefix, suffix or replace this altogether.</p>
	 * 
	 * @return The default template name.
	 */
	protected String getDefaultTemplateName() {
		String ret = null;
		
		if( templateFile != null ) {
			int index = templateFile.lastIndexOf(".xml");
			if( index > 0 ) ret = templateFile.substring(0, index);
			else ret = templateFile;
		}
		else {
			if( this.viewName == null ) throw new IllegalArgumentException("Cannot set a default template name if there is no template or name for the view");
			if( primaryBean == null ) throw new IllegalArgumentException("Cannot set a default template name if there is no template or primary bean class for the view");
			
			String viewName = this.viewName;
			if( viewName.length() > 1 ) viewName = viewName.substring(0, 1).toUpperCase() + viewName.substring(1);
			else viewName = viewName.toUpperCase();
			
			ret = primaryBean.getPackage().getName().replace('.', '/') + "/" + viewName;	
		}

		// Strip the slash at beginning if there is one
		if( ret != null && ret.length() > 1 && ret.charAt(0) == '/' ) ret = ret.substring(1);
		return ret;
	}
	
	/**
	 * This method is called just before initialisation the config
	 * to allow the derived class to setup any other settings on
	 * the TemplateConfig 
	 * 
	 * @param config The template config
	 */
	protected abstract void preInitConfig(BasicTemplateConfiguration config);
	
	/**
	 * Overrideable method to create a default model when
	 * one is not supported. The default does nothing.
	 * 
	 * @return The model to create
	 */
	protected ModelConfiguration createDefaultModel() {
		return null;
	}
	
	/**
	 * Sets the template and if there is a bean named after the view
	 * in the model it adds it (it does this by called getBean so 
	 * derived classes can override this method if they require
	 */
	public void prepareRender(RenderModel renderModel, Model model) {
		renderModel.setTemplate(config.getName());
		
		// Add the primary model on
		Object bean = getBean(model);
		if( bean != null ) renderModel.setAttribute(getViewBeanName(), bean);

		// Add on any extra attributes
		if( renderAttributes != null ) {
			Iterator<String> it = renderAttributes.keySet().iterator();
			while( it.hasNext() ) {
				String name = it.next();
				Object val = model.get(name);
				if( val != null ) renderModel.setAttribute(renderAttributes.get(name), val);
			}
		}
	}
	
	/**
	 * Overrideable helper to get the primary bean to 
	 * place in the render model. The default is to 
	 * simply get from the model
	 * 
	 * @param model The model
	 * @return The bean
	 */
	protected Object getBean(Model model) {
		Object ret = null;
		if( getViewBeanName() != null && model.containsValueFor(getViewBeanName()) ) {
			ret = model.get(getViewBeanName());
		}
		return ret;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	/**
	 * @return Returns the name of the bean
	 */
	public String getViewBeanName() {
		return viewBeanName;
	}
	
	/**
	 * @param viewBeanName The new view bean name
	 */
	public void setViewBeanName(String viewBeanName) {
		this.viewBeanName = viewBeanName;
	}
	
	/**
	 * Called from Spring, set the view name automatically.
	 */
	public void setBeanName(String name) {
		if( viewName == null ) viewName = name;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public Class<?> getPrimaryBean() {
		return primaryBean;
	}

	public void setPrimaryBean(Class<?> primaryBean) {
		this.primaryBean = primaryBean;
	}

	public Map<String, Compiler> getCompilers() {
		return compilers;
	}

	public void setCompilers(Map<String, Compiler> compilers) {
		this.compilers = compilers;
	}

	/**
	 * @return the asForm
	 */
	public boolean isAsForm() {
		return asForm;
	}

	/**
	 * @param asForm the asForm to set
	 */
	public void setAsForm(boolean asForm) {
		this.asForm = asForm;
	}

	/**
	 * @return the resourceBase
	 */
	public String getResourceBase() {
		return resourceBase;
	}

	/**
	 * @param resourceBase the resourceBase to set
	 */
	public void setResourceBase(String resourceBase) {
		this.resourceBase = resourceBase;
	}

	/**
	 * @return the renderAttributes
	 */
	public Map<String, String> getRenderAttributes() {
		return renderAttributes;
	}

	/**
	 * @param renderAttributes the renderAttributes to set
	 */
	public void setRenderAttributes(Map<String, String> renderAttributes) {
		this.renderAttributes = renderAttributes;
	}

	/**
	 * @return the config
	 */
	public TemplateConfiguration getConfig() {
		return config;
	}
}
