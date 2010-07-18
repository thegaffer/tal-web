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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.behaviour.supporting.ContainerElement;
import org.tpspencer.tal.template.core.groups.SimpleGroup;
import org.tpspencer.tal.template.core.memberprops.MemberProperty;
import org.tpspencer.tal.template.core.props.BooleanProperty;
import org.tpspencer.tal.template.core.props.DateProperty;
import org.tpspencer.tal.template.core.props.NumberProperty;
import org.tpspencer.tal.template.core.props.StringProperty;

/**
 * This class represents a template that in turn holds
 * one or more groups and/or properties. Most of its
 * functionality is in the base element class as it
 * is shared.
 * 
 * @author Tom Spencer
 */
public class SimpleTemplate extends BaseElement implements Template {

	private Class<?> templateClass = null;
	private boolean autoDiscover = true;
	private boolean autoShowDiscoveredFields = false;
	private String[] ignoredFields = null;
	private String[] fieldOrder = null;
	
	/**
	 * Adds in any properties that are not covered by
	 * it's children. These are placed in a special
	 * group named "_autoProps" at the end.
	 */
	@Override
	public void init(List<TemplateElement> children) {
		String name = getName();
		if( name == null && templateClass != null ) {
			name = templateClass.getSimpleName();
			setName(name);
		}
		if( name == null ) throw new IllegalArgumentException("!!! A template must have a name or a template class");
		
		// b. If auto-discover is on, add any extra children (except ignored)
		if( children == null ) children = new ArrayList<TemplateElement>();
		if( autoDiscover && templateClass != null ) {
			addAutoFields(children);
		}
		
		// c. If fieldOrder is set, order top-level children
		children = orderFields(children);
		
		super.init(children.size() > 0 ? children : null);
	}
	
	private void addAutoFields(List<TemplateElement> children) {
		try {
			List<TemplateElement> hiddenFields = null;
			
			BeanInfo info = Introspector.getBeanInfo(templateClass);
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for( int i = 0 ; i < props.length ; i++ ) {
				if( !"class".equals(props[i].getName()) ) {
					if( !isChildPresent(children, props[i].getName()) ) {
						// Generate an auto prop for this element
						TemplateElement e = generateChild(props[i]);
						if( e != null ) {
							if( isNamedField(e) || autoShowDiscoveredFields ) children.add(e);
							else {
								if( hiddenFields == null ) hiddenFields = new ArrayList<TemplateElement>();
								hiddenFields.add(e);
							}
						}
					}
				}
			}
			
			// Add on group of hidden fields
			if( hiddenFields != null && hiddenFields.size() > 0 ) {
				SimpleGroup hiddenGroup = new SimpleGroup();
				hiddenGroup.setName("_autoHidden");
				hiddenGroup.addProperty("htmlWrapperStyle", "display: none");
				hiddenGroup.init(this, hiddenFields);
				children.add(hiddenGroup);
			}
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Unable for auto discover fields for class [" + templateClass.getName() + "]: " + e.getMessage());
		}
	}
	
	/**
	 * Determines if the field is named in the field order 
	 * (if so its visible, otherwise hidden).
	 * 
	 * @param e The element
	 * @return True if its named, false otherwise
	 */
	private boolean isNamedField(TemplateElement e) {
		if( fieldOrder == null || fieldOrder.length == 0 ) return false;
		
		boolean ret = false;
		for( int i = 0 ; i < fieldOrder.length ; i++ ) {
			if( fieldOrder[i].equals(e.getName()) ) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * Determines if the given child is within the the list of
	 * children (or contained by one of them!)
	 * 
	 * @param children The child elements
	 * @param prop The property to look for
	 * @return True if it exists, false otherwise
	 */
	private boolean isChildPresent(List<TemplateElement> children, String prop) {
		boolean ret = false;
		Iterator<TemplateElement> it = children.iterator();
		while( it.hasNext() ) {
			TemplateElement e = it.next();
			if( prop.equals(e.getName()) ) {
				ret = true;
				break;
			}
			else if( e instanceof ContainerElement ) {
				ret = isChildPresent(((ContainerElement)e).getElements(), prop);
				if( ret ) break;
			}
		}
		
		return ret;
	}
	
	/**
	 * Internal helper to generate a sensible property given
	 * the properties type.
	 * 
	 * @param prop The property
	 * @return The generated template element
	 */
	private TemplateElement generateChild(PropertyDescriptor prop) {
		Class<?> type = prop.getPropertyType();
		
		TemplateElement ret = null;
		if( isStringField(type) ) {
			StringProperty p = new StringProperty();
			p.setName(prop.getName());
			p.init(this, null);
			ret = p;
		}
		else if( isNumberField(type) ) {
			NumberProperty p = new NumberProperty();
			p.setName(prop.getName());
			p.init(this, null);
			ret = p;
		}
		else if( isDateField(type) ) {
			DateProperty p = new DateProperty();
			p.setName(prop.getName());
			p.init(this, null);
			ret = p;
		}
		else if( isBooleanField(type) ) {
			BooleanProperty p = new BooleanProperty();
			p.setName(prop.getName());
			p.init(this, null);
			ret = p;
		}
		/*else if( isSimpleArrayField(type) ) {
			// Add in support for simple array fields - MVC-13
		}*/
		
		else {
			MemberProperty p = new MemberProperty();
			p.setName(prop.getName());
			p.setMemberType(type);

			// Hope there is a template of the same name as bean!
			p.setTemplate(type.getSimpleName());
			
			ret = p;
		}
		
		return ret;
	}
	
	/**
	 * Externally available helper to determine any inner bean
	 * classes inside this template. This helps a client who is
	 * constructing a template dynamically to get the inner beans
	 * and ensure the logic matches that of this class internally.
	 * 
	 * @return The list of inner beans (not simple props) found, or null
	 */
	public List<Class<?>> getInnerBeans() {
		try {
			List<Class<?>> innerBeans = new ArrayList<Class<?>>();
			
			BeanInfo info = Introspector.getBeanInfo(templateClass);
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for( int i = 0 ; i < props.length ; i++ ) {
				Class<?> type = props[i].getPropertyType();
				if( !props[i].getName().equals("class") && 
						!isStringField(type) &&
						!isNumberField(type) &&
						!isDateField(type) &&
						!isBooleanField(type) &&
						!isSimpleArrayField(type) ) {
					innerBeans.add(type);
				}
			}
			
			return innerBeans.size() > 0 ? innerBeans : null;
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Unable for auto discover fields for class [" + templateClass.getName() + "]: " + e.getMessage());
		}
	}
	
	private boolean isStringField(Class<?> type) {
		if( String.class.isAssignableFrom(type) ||
				char.class.isAssignableFrom(type) ) {
			return true;
		}
		
		return false;
	}
	
	private boolean isNumberField(Class<?> type) {
		if( Number.class.isAssignableFrom(type) ||
				double.class.isAssignableFrom(type) ||
				float.class.isAssignableFrom(type) ||
				long.class.isAssignableFrom(type) ||
				int.class.isAssignableFrom(type) ||
				short.class.isAssignableFrom(type) ||
				byte.class.isAssignableFrom(type) ) {
			return true;
		}
		
		return false;
	}
	
	private boolean isDateField(Class<?> type) {
		if( Date.class.isAssignableFrom(type) ) {
			return true;
		}
		
		return false;
	}
	
	private boolean isBooleanField(Class<?> type) {
		if( Boolean.class.isAssignableFrom(type) ||
				boolean.class.isAssignableFrom(type) ) {
			return true;
		}
		
		return false;
	}
	
	private boolean isSimpleArrayField(Class<?> type) {
		if( type.isArray() ) {
			if( !isStringField(type.getComponentType()) &&
					!isNumberField(type.getComponentType()) &&
					!isDateField(type.getComponentType()) &&
					!isBooleanField(type.getComponentType()) &&
					!isSimpleArrayField(type.getComponentType()) ) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Internal helper to order fields around a field order
	 * if it is present.
	 * 
	 * @param children The existing children
	 * @return The new orderer children
	 * @throws IllegalArgumentException If the order contains a field we don't know
	 */
	private List<TemplateElement> orderFields(List<TemplateElement> children) {
		if( fieldOrder == null || fieldOrder.length == 0 ) return children;
		
		Map<String, TemplateElement> childMap = new HashMap<String, TemplateElement>();
		Iterator<TemplateElement> it = children.iterator();
		while( it.hasNext() ) {
			TemplateElement e = it.next();
			childMap.put(e.getName(), e);
		}
		
		List<TemplateElement> ret = new ArrayList<TemplateElement>();
		for( int i = 0 ; i < fieldOrder.length ; i++ ) {
			TemplateElement e = childMap.get(fieldOrder[i]);
			if( e == null ) throw new IllegalArgumentException("The field [" + fieldOrder[i] + "] listed in field order does not exist in template: " + getName());
			ret.add(e);
			childMap.remove(fieldOrder[i]);
		}
		
		// Add on any unnamed fields that are left in any order
		if( childMap.size() > 0 ) {
			Iterator<TemplateElement> it2 = childMap.values().iterator();
			while( it2.hasNext() ) {
				ret.add(it2.next());
			}
		}
		
		return ret;
	}
	
	/**
	 * Simply returns the template class
	 */
	public Class<?> getTemplateClass() {
		return templateClass;
	}

	/**
	 * @param templateClass The class this template is a template for
	 */
	public void setTemplateClass(Class<?> templateClass) {
		this.templateClass = templateClass;
	}

	/**
	 * @return the autoDiscover
	 */
	public boolean isAutoDiscover() {
		return autoDiscover;
	}

	/**
	 * @param autoDiscover the autoDiscover to set
	 */
	public void setAutoDiscover(boolean autoDiscover) {
		this.autoDiscover = autoDiscover;
	}

	/**
	 * @return the ignoredFields
	 */
	public String[] getIgnoredFields() {
		return ignoredFields;
	}

	/**
	 * @param ignoredFields the ignoredFields to set
	 */
	public void setIgnoredFields(String[] ignoredFields) {
		this.ignoredFields = ignoredFields;
	}

	/**
	 * @return the fieldOrder
	 */
	public String[] getFieldOrder() {
		return fieldOrder;
	}

	/**
	 * @param fieldOrder the fieldOrder to set
	 */
	public void setFieldOrder(String[] fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	/**
	 * @return the autoShowDiscoveredFields
	 */
	public boolean isAutoShowDiscoveredFields() {
		return autoShowDiscoveredFields;
	}

	/**
	 * @param autoShowDiscoveredFields the autoShowDiscoveredFields to set
	 */
	public void setAutoShowDiscoveredFields(boolean autoShowDiscoveredFields) {
		this.autoShowDiscoveredFields = autoShowDiscoveredFields;
	}

	@Override
	public String toString() {
		return "SimpleTemplate: name=" + getName() + ", class=" + templateClass; 
	}
}
