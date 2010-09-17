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

package org.talframework.talui.template.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Renderer;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateConfiguration;
import org.talframework.talui.template.render.ModelRenderer;
import org.talframework.talui.template.render.TemplateRenderer;
import org.talframework.talui.template.render.elements.special.EmptyElement;

/**
 * This class is a generic render compiler. An instance of
 * this class is configured with a number of render templates.
 * These guys are registered against the model template, group
 * and property types and know how to wrap the model element.
 * The generic render compiler then steps through the input 
 * template and uses the render templates to produce the 
 * compiled renderer.
 * 
 * @author Tom Spencer
 */
public class SimpleGenericCompiler implements GenericCompiler {
	private static final Log logger = LogFactory.getLog(SimpleGenericCompiler.class);
	
	/** If true the compiler will always recurse all templates in compilation */
	private boolean recurseTemplates = false;
	/** Holds the inbuilt styles that apply for any compliation */
	private String[] compilerStyles = null;
	/** The mold to use for any templates */
	private TemplateRenderMold mold = null;
	
	/** Member holds the current styles */
	private String[] styles = new String[0];
	/** Member holds the current template styles */
	private String[] templateStyles = new String[0];
	
	/** Member holds the model templates to be compiled */
	private Map<String, Template> modelTemplates = null;
	/** Member holds the render elements against each template */
	private Map<String, RenderElement> renderedTemplates = null;
	
	/**
	 * Obtains the root set of render templates and renders the 
	 * template with it. Any other templates needed by this one
	 * are rendered as well. The map of render templates produced
	 * is returned.
	 */
	public Renderer compile(TemplateConfiguration config) {
		// reset run elements
		this.modelTemplates = config.getTemplates();
		this.renderedTemplates = new HashMap<String, RenderElement>();
		this.styles = new String[0];
		this.templateStyles = new String[0];
		
		Renderer ret = null;
		if( recurseTemplates ) {
			Iterator<String> it = modelTemplates.keySet().iterator();
			while( it.hasNext() ) {
				String name = it.next();
				Template template = modelTemplates.get(name);
				compileTemplate(template, null, null);
			}
			
			ret = new TemplateRenderer(renderedTemplates);
		}
		
		// Start with only the 1 template
		else {
			if( logger.isDebugEnabled() ) logger.debug(">>> Starting template compilation: " + config.getMainTemplate());
			RenderElement renderedRootTemplate = compileTemplate(config.getMainTemplate(), null, null);
			if( logger.isDebugEnabled() ) logger.debug("<<< Ending template compilation: " + config.getMainTemplate());
			
			ret = new ModelRenderer(swapRootTemplate(config, renderedRootTemplate));
		}
		
		return ret;
	}
	
	/**
	 * This method can be called to swap in the new root template
	 * because create the ModelRenderer. The HTML Compiler is known
	 * to do this to generate all the applicable call-backs for
	 * JS and CSS. Default is to return root unchanged.
	 * 
	 * @param root The current root element
	 * @return The new root element
	 */
	public RenderElement swapRootTemplate(TemplateConfiguration config, RenderElement root) {
		return root;
	}
	
	/**
	 * Just returns the styles member.
	 */
	public String[] getStyles() {
		return styles;
	}
	
	public String[] getTemplateStyles() {
		return templateStyles;
	}
	
	/**
	 * Determines if style is set of not (either perm or template)
	 */
	public boolean isStyle(String style) {
		return isStyle(style, false);
	}
	
	/**
	 * Determines if style is set of not (either perm or template)
	 */
	public boolean isStyle(String style, boolean nonTemplateOnly) {
		if( this.compilerStyles != null ) {
			for( int i = 0 ; i < this.compilerStyles.length ; i++ ) {
				if( this.compilerStyles[i].equals(style) ) return true;
			}
		}
		
		for( int i = 0 ; i < this.styles.length ; i++ ) {
			if( this.styles[i].equals(style) ) return true;
		}
		
		if( !nonTemplateOnly && this.templateStyles != null ) {
			for( int i = 0 ; i < this.templateStyles.length ; i++ ) {
				if( this.templateStyles[i].equals(style) ) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Adds the given style to end of array
	 */
	public boolean addStyle(String style) {
		if( style == null ) return false;
		
		boolean ret = isStyle(style);
		
		ArrayList<String> styles = new ArrayList<String>();
		
		for( int i = 0 ; i < this.styles.length ; i++ ) {
			if( !style.equals(this.styles[i]) ) styles.add(this.styles[i]);
		}
		
		styles.add(style);
		
		this.styles = styles.toArray(new String[styles.size()]);
		return ret;
	}
	
	/**
	 * Removes style from array
	 */
	public void removeStyle(String style) {
		if( style == null ) return;
		
		ArrayList<String> styles = new ArrayList<String>();
		
		for( int i = 0 ; i < this.styles.length ; i++ ) {
			if( !style.equals(this.styles[i]) ) styles.add(this.styles[i]);
		}
		
		if( styles.size() == 0 ) this.styles = new String[0];
		else this.styles = styles.toArray(new String[styles.size()]);
	}
	
	/**
	 * Adds the given style to end of array
	 */
	public boolean addTemplateStyle(String style) {
		if( style == null ) return false;
		
		boolean ret = isStyle(style, true);
		
		ArrayList<String> styles = new ArrayList<String>();
		
		for( int i = 0 ; i < this.templateStyles.length ; i++ ) {
			if( !style.equals(this.templateStyles[i]) ) styles.add(this.templateStyles[i]);
		}
		
		styles.add(style);
		
		this.templateStyles = styles.toArray(new String[styles.size()]);
		return ret;
	}
	
	/**
	 * Removes style from array
	 */
	public void removeTemplateStyle(String style) {
		if( style == null ) return;
		
		ArrayList<String> styles = new ArrayList<String>();
		
		for( int i = 0 ; i < this.templateStyles.length ; i++ ) {
			if( !style.equals(this.templateStyles[i]) ) styles.add(this.templateStyles[i]);
		}
		
		if( styles.size() == 0 ) this.templateStyles = new String[0];
		else this.templateStyles = styles.toArray(new String[styles.size()]);
	}
	
	/**
	 * @return The current style name by adding all styles together (suffixed .)
	 */
	public String getStyleName() {
		if( this.styles.length == 0 ) return null;
		
		StringBuilder ret = new StringBuilder();
		for( int i = 0 ; i < this.styles.length ; i++ ) {
			ret.append(this.styles[i]);
			ret.append('.');
		}
		
		return ret.toString();
	}
	
	/**
	 * First this method finds the template (throwing if it does not
	 * exist). Then it sees if we have already rendered this template.
	 * If so returns it's name, otherwise starts compiling it.
	 */
	public RenderElement compileTemplate(String templateName, String[] styles, String[] templateStyles) {
		Template template = modelTemplates.get(templateName);
		if( template == null ) throw new IllegalArgumentException("!!! Cannot compile template as it does not exist in set of model templates: " + templateName);
		
		return compileTemplate(template, styles, templateStyles);
	}
	
	/**
	 * First this method finds the template (throwing if it does not
	 * exist). Then it sees if we have already rendered this template.
	 * If so returns it's name, otherwise starts compiling it.
	 */
	public RenderElement compileTemplate(Template template, String[] styles, String[] innerTemplateStyles) {
		// Set the new styles
		List<String> removeStyles = new ArrayList<String>();
		if( styles != null ) {
			for( int i = 0 ; i < styles.length ; i++ ) {
				if( !isStyle(styles[i], true) ) {
					addStyle(styles[i]);
					removeStyles.add(styles[i]);
				}
			}
		}
		String[] tempStyles = this.templateStyles;
		this.templateStyles = innerTemplateStyles;
		if( this.templateStyles == null ) this.templateStyles = new String[0];
		
		// Actually compiler
		RenderElement ret = null;
		try {
			String name = template.getName();
			String style = getStyleName();
			if( style != null ) name = style + name;
			
			ret = renderedTemplates.get(name);
			if( ret == null ) {
				ret = createTemplateElement(template);
				renderedTemplates.put(name, ret);
				if( logger.isDebugEnabled() ) logger.debug("\tCreated render element for template [" + name + "]: " + ret);
			
				RenderElement root = mold.compile(this, template);
				ret.addElement(root);
			}
		}
		finally {
			// Remove any styles
			Iterator<String> it = removeStyles.iterator();
			while( it.hasNext() ) {
				removeStyle(it.next());
			}
			
			// Replace the template styles
			this.templateStyles = tempStyles;
		}
		
		return ret;
	}
	
	public RenderElement getTemplate(String name) {
		return renderedTemplates.get(name);
	}
	
	public void addTemplate(String name, RenderElement elem) {
		renderedTemplates.put(name, elem);
	}
	
	/**
	 * This method can be overridden by any derived class to set
	 * the render element to use for any template. The default
	 * is an empty render element.
	 * 
	 * @param template The template we are comiling
	 * @return The render element for template
	 */
	protected RenderElement createTemplateElement(Template template) {
		return new EmptyElement();
	}
	
	//////////////////////////////////////////
	// Getters/Setters

	/**
	 * @return the mold
	 */
	public TemplateRenderMold getMold() {
		return mold;
	}

	/**
	 * @param mold the mold to set
	 */
	public void setMold(TemplateRenderMold mold) {
		this.mold = mold;
	}

	/**
	 * @return the modelTemplates
	 */
	public Map<String, Template> getModelTemplates() {
		return modelTemplates;
	}

	/**
	 * @param modelTemplates the modelTemplates to set
	 */
	public void setModelTemplates(Map<String, Template> modelTemplates) {
		this.modelTemplates = modelTemplates;
	}

	/**
	 * @return the renderedTemplates
	 */
	public Map<String, RenderElement> getRenderedTemplates() {
		return renderedTemplates;
	}

	/**
	 * @return the compilerStyles
	 */
	public String[] getCompilerStyles() {
		return compilerStyles;
	}

	/**
	 * @param compilerStyles the compilerStyles to set
	 */
	public void setCompilerStyles(String[] compilerStyles) {
		this.compilerStyles = compilerStyles;
	}

	/**
	 * @return the recurseTemplates
	 */
	public boolean isRecurseTemplates() {
		return recurseTemplates;
	}

	/**
	 * @param recurseTemplates the recurseTemplates to set
	 */
	public void setRecurseTemplates(boolean recurseTemplates) {
		this.recurseTemplates = recurseTemplates;
	}
}
