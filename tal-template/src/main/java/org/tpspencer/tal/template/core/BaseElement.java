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

package org.tpspencer.tal.template.core;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.TemplateElement;

/**
 * This class can be used as a base for either a
 * template or a template element. It handles the
 * common attributes such as name and properties.
 * 
 * @author Tom Spencer
 */
public class BaseElement {

	/** Member holds the name */
	private String name = null;
	/** Member holds the sub-properties of the element */
	private Map<String, String> properties = null;
	/** Member holds any child elements the element may have */
	private List<TemplateElement> elements = null;
	/** Member holds the list of valid settings for this class */
	private Map<String, Method> validSettings = new HashMap<String, Method>();
	
	/**
	 * This base version of init does nothing
	 */
	public void init(List<TemplateElement> children) {
		this.elements = children != null ? new ArrayList<TemplateElement>(children) : null;
		
		try {
			BeanInfo info = Introspector.getBeanInfo(this.getClass());
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for( int i = 0 ; i < props.length ; i++ ) {
				if( !props[i].getName().equals("class") &&
						props[i].getReadMethod() != null ) {
					validSettings.put(props[i].getName(), props[i].getReadMethod());
				}
			}
		}
		catch( Exception e ) {
			// If we fail we ignore as there will be no settings!
		}
	}
	
	/**
	 * Helper for the TemplateElement.getBejaviour method. 
	 * Derived classes can override if certain behaviours are
	 * only active if certain settings have been made. The
	 * default version returns if the class implements the
	 * behaviour.
	 * 
	 * @param behaviour The behaviour to get
	 * @return The behaviour
	 */
	public <T> T getBehaviour(Class<T> behaviour) {
		if( behaviour.isInstance(this) ) return behaviour.cast(this);
		else return null;
	}
	
	/**
	 * Call to get a named property set
	 * 
	 * @param name The name of the property set
	 * @return The property set (or null)
	 */
	public Map<String, String> getPropertySet(String name) {
		if( properties == null ) return null;
		
		if( name == null ) return properties;
		
		Map<String, String> ret = null;
		Iterator<String> it = properties.keySet().iterator();
		while( it.hasNext() ) {
			String p = it.next();
			
			if( p.startsWith(name) ) {
				String key = p.substring(name.length());
				if( key == null || key.length() == 0 ) continue;
				if( key.length() > 1 ) key = key.substring(0, 1).toLowerCase() + key.substring(1);
				else key = key.toLowerCase();
				
				if( ret == null ) ret = new HashMap<String, String>();
				ret.put(key, properties.get(p));
			}
		}
		
		return ret;
	}
	
	/**
	 * Call to add a child element. The element is added
	 * after any existing child elements.
	 * 
	 * @param element The element to add
	 */
	public void addChildElement(TemplateElement element) {
		if( elements == null ) elements = new ArrayList<TemplateElement>();
		elements.add(element);
	}

	/**
	 * Determines if the element has the given setting.
	 * 
	 * @param name The name of the setting
	 * @return True if it is a valid setting (doesn't mean it's set though!)
	 */
	public boolean hasSetting(String name) {
		return validSettings.containsKey(name);
	}
	
	/**
	 * Call to get a specific setting in this template 
	 * element.
	 * 
	 * @param name The name of the property
	 * @return The value of the property (or null)
	 * @throws IllegalArgumentException if the setting is not valid
	 */
	public Object getSetting(String name) {
		if( validSettings.containsKey(name) ) {
			try {
				return validSettings.get(name).invoke(this, (Object[])null);
			}
			catch( Exception e ) {
				throw new IllegalArgumentException("The setting [" + name + "] is not accessible for the template element: " + this);
			}
		}
		
		throw new IllegalArgumentException("The setting [" + name + "] is not valid for the template element: " + this);
	}
	
	/**
	 * Call to get a specific setting in this template 
	 * element.
	 * 
	 * @param name The name of the property
	 * @param expected The expected return type
	 * @return The value of the property (or null)
	 * @throws IllegalArgumentException if the setting is not valid
	 * @throws ClassCastException if the type of the setting is not as expected
	 */
	public <T> T getSetting(String name, Class<T> expected) {
		Object setting = getSetting(name);
		return expected.cast(setting);
	}
	
	public void addProperty(String name, String value) {
		if( properties == null ) properties = new HashMap<String, String>();
		properties.put(name, value);
	}
	
	/**
	 * Helper to evaluate a parameter expression and turn it into
	 * a map. If the parameter evaluates to a string then we treat
	 * is as a comma separated name=value string. If it evaluates 
	 * to a map then it is returned. 
	 * 
	 * @param model The model
	 * @param expr The expression
	 * @return The map of parameters (or null if no parameters)
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, String> evaluateParameterExpression(RenderModel model, String expr) {
		Map<String, String> ret = null;
		if( expr != null ) {
			Object exprVal = model.evaluateExpression(expr, Object.class);
			if( exprVal instanceof Map ) {
				ret = (Map<String, String>)exprVal;
			}
			else if( exprVal != null ){
				String params = exprVal.toString();
				
				String[] split = params.split(",");
				if( split != null && split.length > 0) {
					for( int i = 0 ; i < split.length ; i++ ) {
						int index = split[i].indexOf('=');
						if( index > 0 && index < (split[i].length() - 1) ) {
							String name = split[i].substring(0, index);
							String val = split[i].substring(index + 1);
							if( ret == null ) ret = new HashMap<String, String>();
							ret.put(name, val);
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Internal helper to create a string suitable for toString
	 * for the elements, its name and its settings.
	 * 
	 * @return A string in the form "SimpleName: name=x, setting1=y ..." 
	 */
	protected String toStringHelper() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.getClass().getSimpleName()).append(": ");
		buf.append("name=").append(getName());
		if( validSettings != null ) {
			Iterator<String> it = validSettings.keySet().iterator();
			while( it.hasNext() ) {
				String setting = it.next();
				buf.append(", ").append(setting).append("=").append(getSetting(setting));
			}
		}
		return buf.toString();
	}
	
	@Override
	public String toString() {
		return toStringHelper();
	}
	
	/////////////////////////////
	// Getter/Setter
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @return the elements
	 */
	public List<TemplateElement> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<TemplateElement> elements) {
		this.elements = elements;
	}
}
