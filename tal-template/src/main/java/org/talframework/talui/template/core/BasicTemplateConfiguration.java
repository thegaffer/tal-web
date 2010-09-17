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

package org.talframework.talui.template.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.talui.template.Compiler;
import org.talframework.talui.template.Renderer;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateConfiguration;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.core.groups.CompositeGroup;
import org.talframework.talui.template.core.groups.FormGroup;
import org.talframework.talui.template.core.groups.GridGroup;
import org.talframework.talui.template.core.groups.MessageGroup;
import org.talframework.talui.template.core.groups.SimpleGroup;
import org.talframework.talui.template.core.memberprops.MemberProperty;
import org.talframework.talui.template.core.props.CommandProperty;

/**
 * This class implements the template configuration interface.
 * This class is intended to be setup manually rather then reading
 * any configuration. It contains some logic to create some 
 * standard templates.
 * 
 * @author Tom Spencer
 */
public class BasicTemplateConfiguration implements TemplateConfiguration {
	
	/** The name of the config - typically a Java package/class style */
	private String name = null;
	/** Base name for the resource bundle for template - if null uses the config name */
	private String resourceBase = null;
	
	/** Member determines if all renderers should be constructed at init time, default is true */
	private boolean initRenderers = true;
	
	/** The name of the root template */
	private String rootTemplate = null;
	/** The default model object */
	private String defaultModelObject = null;
	
	/** A map of all templates */
	private Map<String, Template> templates = new HashMap<String, Template>();
	/** Member holds the configured compilers, keyed by render type */
	private Map<String, Compiler> compilers = null;
	
	/** Member holds the cached renders keyed by render type */
	private Map<String, Renderer> renderers = new HashMap<String, Renderer>();
	
	/**
	 * Call to initialise the configuration after setup. All
	 * templates are now fixed - or at least any added will
	 * probably not be reflected in rendered output.
	 */
	public void init() {
		if( name == null ) throw new IllegalArgumentException("A template configuration must have a name, typically a class name");
		if( !this.templates.containsKey(rootTemplate) ) throw new IllegalArgumentException("The root template [" + rootTemplate + "] does not exist in the template file");
		
		if( initRenderers ) {
			Iterator<String> it2 = compilers.keySet().iterator();
			while( it2.hasNext() ) {
				String renderType = it2.next();
				Compiler c = compilers.get(renderType);
				Renderer r = c.compile(this);
				renderers.put(renderType, r);
			}
		}
		
		// Register so we can be found in the template servlet
		TemplateConfigurationLocator.getInstance().setTemplate(name, this);
	}

	/**
	 * Gets the renderer if it exists, compiling it if necc
	 * 
	 * @throws IllegalArgumentException if the renderer does not exist
	 */
	public Renderer getRenderer(String renderType) {
		if( compilers == null || !compilers.containsKey(renderType) ) throw new IllegalArgumentException("The render type does not exist in the configuration: " + renderType);
		
		Renderer ret = renderers.get(renderType);
		if( ret == null && !initRenderers ) {
			ret = compilers.get(renderType).compile(this);
			renderers.put(renderType, ret);
		}
		
		return ret;
	}
	
	/**
	 * Safely determines if the renderer exists
	 */
	public boolean hasRenderer(String renderType) {
		if( compilers == null ) return false;
		else return compilers.containsKey(renderType);
	}
	
	/**
	 * Simply returns the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the name without leading /
	 */
	public String getResourceName() {
		if( resourceBase != null ) return resourceBase;
		if( name.charAt(0) == '/' ) return name.substring(1);
		else return name;
	}

	/**
	 * Simply gets the root template
	 */
	public Template getMainTemplate() {
		return getTemplate(rootTemplate);
	}

	/**
	 * Gets the given template
	 * 
	 * @Throws IllegalArgumentException if the template does not exist
	 */
	public Template getTemplate(String name) {
		Template ret = null;
		ret = templates != null ? templates.get(name) : null;
		
		if( ret == null ) throw new IllegalArgumentException("The given template [" + name + "] does not exist in this configuration");
		return ret;
	}
	
	/**
	 * Returns the default model object
	 */
	public String getDefaultModelObject() {
		return defaultModelObject;
	}
	
	//////////////////////////////////////////////////
	// Helper functions
	
	/**
	 * Call to add a template based on the given bean.
	 * The template is not added if there is already a template
	 * with the same name.
	 * 
	 * @param beanClass The class to create a template for
	 * @param includeChildren If true then create templates for any bean properties of object
	 */
	public Template addBeanTemplate(Class<?> beanClass, boolean includeChildren) {
		SimpleTemplate template = new SimpleTemplate();
		template.setTemplateClass(beanClass);
		template.setAutoDiscover(true);
		template.setAutoShowDiscoveredFields(true);
		template.init(null);
		
		if( !templates.containsKey(template.getName()) ) {
			templates.put(template.getName(), template);
			
			if( includeChildren ) {
				List<Class<?>> innerBeans = template.getInnerBeans();
				if( innerBeans != null ) processBeans(innerBeans);
			}
		}
		
		return template;
	}
	
	/**
	 * Internal helper to add a list of inner beans as templates
	 * to this config.
	 * 
	 * @param beans The beans
	 */
	private void processBeans(List<Class<?>> beans) {
		if( beans == null || beans.size() == 0 ) return;
		
		Iterator<Class<?>> it = beans.iterator();
		while( it.hasNext() ) {
			addBeanTemplate(it.next(), true);
		}
	}
	
	/**
	 * Adds in a simple template to create a form that will then
	 * point to the template for the given bean class.
	 * 
	 * @param formName The formName
	 * @param beanClass The bean class
	 * @return The template (which will have been added to config)
	 */
	@SuppressWarnings("serial")
	public Template addFormTemplate(String formName, String beanName, final boolean asForm, Class<?> beanClass, String action, final List<String> commands) {
		if( formName.equals(beanName) ) throw new IllegalArgumentException("The form name and the forms bean name are identical, suggest overridding bean name: " + beanName);
		if( templates.containsKey(formName) ) return null; // Already present!
		
		SimpleTemplate template = new SimpleTemplate();
		template.setName(formName);
		
		List<TemplateElement> children = new ArrayList<TemplateElement>();
		
		// Create the Buttons group
		CompositeGroup grp = new CompositeGroup();
		grp.setName("commands");
		grp.setProperties(new HashMap<String, String>(){{
			put("htmlWrapperStyleClass", asForm ? "form-buttons" : "view-actions");
		}});
		Iterator<String> it = commands.iterator();
		while( it.hasNext() ) {
			String cmd = it.next();
			children.add(new CommandProperty(cmd, asForm ? cmd : action));
		}
		grp.init(template, children); 
		children.clear();
		
		// Add in member prop pointing to template
		MemberProperty member = new MemberProperty();
		member.setName(beanName);
		member.setTemplate(beanClass.getSimpleName());
		children.add(member);
		children.add(grp);
		
		// Main form group
		if( asForm ) {
			FormGroup formGroup = new FormGroup();
			formGroup.setName(formName);
			formGroup.setAction(action);
			formGroup.init(template, children);
			
			children.clear();
			
			MessageGroup messages = new MessageGroup(); 
			messages.init(template, null);
			
			children.add(messages); 
			children.add(formGroup);
		}
		// Or as not a form
		else {
			SimpleGroup simpleGroup = new SimpleGroup();
			simpleGroup.setName(formName);
			simpleGroup.setProperties(new HashMap<String, String>(){{
				put("htmlWrapperStyleClass", "view");
			}});
			simpleGroup.init(template, children);
			
			children.clear();
			
			MessageGroup messages = new MessageGroup(); 
			messages.init(template, null);
			
			children.add(messages);
			children.add(simpleGroup);
		}
		
		template.init(children);
		
		templates.put(template.getName(), template);
		return template;
	}

	/**
	 * Helper to form a special table template 
	 * 
	 * @param name The base name of the table and name of array of elements 
	 * @param elementTemplate The name of the template to refer to (each row)
	 * @param rowActions The actions against each row (if any)
	 * @param tableActions The actions below the table
	 * @param showPaging Shows the paging results
	 * @return The generated template
	 */
	@SuppressWarnings("serial")
	public Template addTableTemplate(String tableName, String name, String[] headings, String elementTemplate, List<TemplateElement> rowActions, List<TemplateElement> tableActions, boolean showPaging) {
		if( tableName.equals(name) ) throw new IllegalArgumentException("The table name and the tables bean name are identical, suggest overridding bean name: " + name);
		if( templates.containsKey(tableName) ) return null; // Already present!
		
		List<TemplateElement> children = new ArrayList<TemplateElement>();
		
		String innerTemplate = elementTemplate;
		if( rowActions != null ) {
			SimpleTemplate template = new SimpleTemplate();
			template.setName(name + "Row");
			
			TemplateProp prop = new TemplateProp();
			prop.setName("result");
			prop.setTemplate(elementTemplate);
			prop.init(template, null);
			
			SimpleGroup actionGroup = new SimpleGroup();
			actionGroup.setName("actions");
			actionGroup.setProperties(new HashMap<String, String>(){{
				put("htmlWrapperStyleClass", "row-actions");
			}});
			actionGroup.init(template, rowActions);
			
			children.add(prop);
			children.add(actionGroup);
			template.init(children);
			templates.put(template.getName(), template);
			children.clear();
			
			innerTemplate = template.getName();
		}
		
		SimpleTemplate template = new SimpleTemplate();
		template.setName(tableName);
		
		// Table Member Element
		MemberProperty member = new MemberProperty();
		member.setName(name);
		member.setTemplate(innerTemplate);
		member.init(template, null);
		
		// Table Group
		GridGroup tableGroup = new GridGroup();
		tableGroup.setName(tableName);
		tableGroup.setHeadings(headings);
		children.add(member);
		tableGroup.init(template, children);
		children.clear();
		
		// Actions Group
		SimpleGroup actionGroup = null;
		if( tableActions != null ) {
			actionGroup = new SimpleGroup();
			actionGroup.setName("actions");
			actionGroup.setProperties(new HashMap<String, String>(){{
				put("htmlWrapperStyleClass", "table-actions");
			}});
			actionGroup.init(template, tableActions);
		}
		
		children.add(tableGroup);
		if( actionGroup != null ) children.add(actionGroup);
		template.init(children);
		templates.put(template.getName(), template);
		
		return template;
	}
	
	//////////////////////////////////////////////////
	// Getters / Setters

	/**
	 * @return the initRenderers
	 */
	public boolean isInitRenderers() {
		return initRenderers;
	}

	/**
	 * @param initRenderers the initRenderers to set
	 */
	public void setInitRenderers(boolean initRenderers) {
		this.initRenderers = initRenderers;
	}

	/**
	 * @return the rootTemplate
	 */
	public String getRootTemplate() {
		return rootTemplate;
	}

	/**
	 * @param rootTemplate the rootTemplate to set
	 */
	public void setRootTemplate(String rootTemplate) {
		this.rootTemplate = rootTemplate;
	}

	/**
	 * @return the templates
	 */
	public Map<String, Template> getTemplates() {
		return templates;
	}

	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(Map<String, Template> templates) {
		this.templates = templates;
	}
	
	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(List<Template> templates) {
		if( templates == null ) {
			this.templates = null;
			return;
		}
		
		this.templates = new HashMap<String, Template>();
		Iterator<Template> it = templates.iterator();
		while( it.hasNext() ) {
			Template t = it.next();
			this.templates.put(t.getName(), t);
		}
	}

	/**
	 * @return the compilers
	 */
	public Map<String, Compiler> getCompilers() {
		return compilers;
	}

	/**
	 * @param compilers the compilers to set
	 */
	public void setCompilers(Map<String, Compiler> compilers) {
		this.compilers = compilers;
	}

	/**
	 * @return the renderers
	 */
	public Map<String, Renderer> getRenderers() {
		return renderers;
	}

	/**
	 * @param defaultModelObject the defaultModelObject to set
	 */
	public void setDefaultModelObject(String defaultModelObject) {
		this.defaultModelObject = defaultModelObject;
	}

	/**
	 * @param name The new name for the config
	 */
	public void setName(String name) {
		this.name = name;
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
}
