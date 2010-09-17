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

package org.talframework.talui.mvc.spring.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.talframework.talui.mvc.spring.xml.helper.ControllerMapParserHelper;
import org.talframework.talui.mvc.spring.xml.helper.EventMapParserHelper;
import org.talframework.talui.mvc.spring.xml.helper.ModelParserHelper;
import org.talframework.talui.mvc.window.SimpleWindow;
import org.w3c.dom.Element;

public class SimpleWindowBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	/**
	 * Simply returns the AppConfig class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return SimpleWindow.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("id");
		String view = element.getAttribute("view");
		
		BeanDefinition model = ModelParserHelper.createModel(name + "Model", element);
		ManagedMap controllers = ControllerMapParserHelper.createControllerMap(element);
		ManagedList events = EventMapParserHelper.createEventList(element);
		
		// Constructor
		bean.addPropertyValue("name", name);
		bean.addPropertyReference("defaultView", view);
		if( controllers != null ) bean.addPropertyValue("controllers", controllers);
		if( model != null ) bean.addPropertyValue("model", model);
		if( events != null ) bean.addPropertyValue("events", events);
		
		bean.setInitMethodName("init");
	}
}
