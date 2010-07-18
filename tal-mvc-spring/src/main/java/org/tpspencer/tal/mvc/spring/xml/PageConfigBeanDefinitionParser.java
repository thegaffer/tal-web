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

package org.tpspencer.tal.mvc.spring.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.spring.xml.helper.EventMapParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ModelParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ParserHelper;
import org.w3c.dom.Element;

public class PageConfigBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	/**
	 * Simply returns the PageConfig class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return PageConfig.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("id");
		String overrideName = ParserHelper.getAttribute(element, "name");
		String parent = ParserHelper.getAttribute(element, "parent");
		String template = ParserHelper.getAttribute(element, "template");
		String modelRef = ParserHelper.getAttribute(element, "model-ref");
		String window = ParserHelper.getAttribute(element, "windows");
		
		if( parent != null ) bean.setParentName(parent);
		if( parent == null && name != null ) bean.addPropertyValue("name", name);
		if( overrideName != null ) bean.addPropertyValue("name", overrideName);
		if( template != null ) bean.addPropertyValue("template", template);
				
		if( modelRef != null ) {
			bean.addDependsOn(modelRef);
			bean.addPropertyReference("model", modelRef);
		}
		else {
			BeanDefinition model = ModelParserHelper.createModel(name + "Model", element);
			if( model != null ) bean.addPropertyValue("model", model);
		}
		
		// Add in windows
		if( window != null ) {
			ManagedMap windowRefs = new ManagedMap();
			String[] windows = window.split(",");
			for( int i = 0 ; i < windows.length ; i++ ) {
				String key = windows[i].trim();
				String windowName = key;
				
				int index = key.indexOf('=');
				if( index > 0 && index < (key.length() - 1) ) {
					windowName = key.substring(0, index);
				}
				
				bean.addDependsOn(windowName);
				windowRefs.put(key, new RuntimeBeanReference(windowName));
			}
			
			if( parent == null ) bean.addPropertyValue("windowMap", windowRefs);
			else bean.addPropertyValue("additionalWindows", windowRefs);
		}

		// Add in events
		ManagedList events = EventMapParserHelper.createPageEventList(element);
		if( events != null ) bean.addPropertyValue("events", events);
	}
}
