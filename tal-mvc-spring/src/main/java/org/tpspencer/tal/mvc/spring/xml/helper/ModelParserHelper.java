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

package org.tpspencer.tal.mvc.spring.xml.helper;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.util.xml.DomUtils;
import org.tpspencer.tal.mvc.model.ConfigModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.ResolvedModelAttribute;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;
import org.w3c.dom.Element;

/**
 * Simple class that creates a model if there is one
 * from the given element.
 * 
 * @author Tom Spencer
 */
public class ModelParserHelper {
	
	/**
	 * Call to create a model if one if held against given
	 * element. The element is first scanned for a child
	 * called 'model'. If there is one it's children are
	 * analysed for attributes.
	 * 
	 * @param modelName The name to apply to the model
	 * @param e The element that may contain a model
	 * @return The BeanDefinition representing the model
	 */
	public static BeanDefinition createModel(String modelName, Element e) {
		BeanDefinition ret = null;
		
		Element modelElement = ParserHelper.getChildElement(e, "model");
		if( modelElement != null ) {
			ManagedList attributes = createModelAttributes(modelElement);
			
			if( attributes != null ) {
				BeanDefinitionBuilder model = BeanDefinitionBuilder.rootBeanDefinition(ModelConfiguration.class);
				model.addConstructorArgValue(modelName);
				model.addConstructorArgValue(attributes);
				ret = model.getBeanDefinition();
			}
		}
		
		return ret;
	}
	
	/**
	 * Call to parse the given element for any model attributes
	 * it contains and return them in a Spring managed list of
	 * model attribute bean definitions.
	 */
	@SuppressWarnings("unchecked") // Because of the Spring classes being JDK 1.4!
	public static ManagedList createModelAttributes(Element element) {
		List<Element> children = DomUtils.getChildElementsByTagName(element, new String[]{"simpleAttribute", "resolvedAttribute", "configAttrubute"});
		if( children == null || children.size() == 0 ) return null;
		
		ManagedList attributes = new ManagedList();
		Iterator<Element> it = children.iterator();
		while( it.hasNext() ) {
			Element e = it.next();
			String name = e.getLocalName();
			
			if( "simpleAttribute".equals(name) ) attributes.add(parseSimple(e));
			if( "resolvedAttribute".equals(name) ) attributes.add(parseResolved(e));
			if( "configAttrubute".equals(name) ) attributes.add(parseConfig(e));
		}
		
		return attributes;
	}
	
	/**
	 * Helper to define a simple model attribute bean
	 */
	private static BeanDefinition parseSimple(Element element) {
		BeanDefinitionBuilder attr = BeanDefinitionBuilder.rootBeanDefinition(SimpleModelAttribute.class);
		
		String name = element.getAttribute("name");
		String defRef = ParserHelper.getAttribute(element, "default-ref");
		String defValue = ParserHelper.getAttribute(element, "default-value");
		
		// Constructor
		attr.addConstructorArgValue(name);
		
		// Properties
		parseAttributeBase(element, attr);
		if( defRef != null ) attr.addPropertyReference("defaultValue", defRef);
		else if( defValue != null ) attr.addPropertyValue("defaultValue", defValue);
		
		return attr.getBeanDefinition();
	}
	
	/**
	 * Helper to define a resolved model attribute bean
	 */
	private static BeanDefinition parseResolved(Element element) {
		BeanDefinitionBuilder attr = BeanDefinitionBuilder.rootBeanDefinition(ResolvedModelAttribute.class);
		
		String name = element.getAttribute("name");
		String resolver = element.getAttribute("resolver");
		String param = ParserHelper.getAttribute(element, "parameter");
		String defRef = ParserHelper.getAttribute(element, "default-ref");
		String defValue = ParserHelper.getAttribute(element, "default-value");
		
		// Constructor
		attr.addConstructorArgValue(name);
		attr.addConstructorArgReference(resolver);
		attr.addConstructorArgValue(param);
		if( defRef != null ) attr.addConstructorArgReference(defRef);
		else attr.addConstructorArgValue(defValue);
		
		// Properties
		parseAttributeBase(element, attr);
		
		return attr.getBeanDefinition();
	}

	/**
	 * Helper to define a config model attribute bean
	 */
	private static BeanDefinition parseConfig(Element element) {
		BeanDefinitionBuilder attr = BeanDefinitionBuilder.rootBeanDefinition(ConfigModelAttribute.class);
		
		String name = element.getAttribute("name");
		String resolver = element.getAttribute("resolver");
		String param = ParserHelper.getAttribute(element, "parameter");
		String defRef = ParserHelper.getAttribute(element, "default-ref");
		String defValue = ParserHelper.getAttribute(element, "default-value");
		
		// Constructor
		attr.addConstructorArgValue(name);
		attr.addConstructorArgReference(resolver);
		attr.addConstructorArgValue(param);
		if( defRef != null ) attr.addConstructorArgReference(defRef);
		else attr.addConstructorArgValue(defValue);
		
		// Properties
		parseAttributeBase(element, attr);
	
		return attr.getBeanDefinition();
	}
	
	/**
	 * Helper to add in common model attributes
	 */
	private static BeanDefinition parseAttributeBase(Element element, BeanDefinitionBuilder attr) {
		String alias = ParserHelper.getAttribute(element, "alias");
		String type = ParserHelper.getAttribute(element, "type");
		String flash = ParserHelper.getAttribute(element, "flash");
		String eventable = ParserHelper.getAttribute(element, "eventable");
		String clearOnAction = ParserHelper.getAttribute(element, "auto-clear");
		String aliasable = ParserHelper.getAttribute(element, "aliasable");
		String aliasExpected = ParserHelper.getAttribute(element, "alias-expected");
		
		// Properties
		if( alias != null ) attr.addPropertyValue("aliases", alias);
		if( type != null ) attr.addPropertyValue("type", type);
		if( flash != null ) attr.addPropertyValue("flash", flash);
		if( eventable != null ) attr.addPropertyValue("eventable", eventable);
		if( clearOnAction != null ) attr.addPropertyValue("clearOnAction", clearOnAction);
		if( aliasable != null ) attr.addPropertyValue("aliasable", aliasable);
		if( aliasExpected != null ) attr.addPropertyValue("aliasExpected", aliasExpected);
		
		return attr.getBeanDefinition();
	}
}
