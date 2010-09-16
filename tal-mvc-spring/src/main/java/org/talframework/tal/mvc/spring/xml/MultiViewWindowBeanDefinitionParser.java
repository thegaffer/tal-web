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
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.tpspencer.tal.mvc.spring.xml.helper.ActionMapParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ControllerMapParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.EventMapParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ModelParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ViewMapParserHelper;
import org.tpspencer.tal.mvc.window.MultiViewWindow;
import org.w3c.dom.Element;

public class MultiViewWindowBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	/**
	 * Simply returns the MultiViewWindow class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return MultiViewWindow.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("id");
		String defaultView = element.getAttribute("default-view");
		
		ManagedMap views = ViewMapParserHelper.createViewMap(element);
		BeanDefinition model = ModelParserHelper.createModel(name + "Model", element);
		ManagedMap controllers = ControllerMapParserHelper.createControllerMap(element);
		ManagedMap actionMappings = ActionMapParserHelper.createActionMappings(element);
		ManagedList events = EventMapParserHelper.createEventList(element);
		
		// Constructor
		bean.addPropertyValue("name", name);
		bean.addPropertyReference("defaultView", defaultView);
		if( views != null ) bean.addPropertyValue("views", views);
		if( controllers != null ) bean.addPropertyValue("controllers", controllers);
		if( actionMappings != null ) bean.addPropertyValue("actionMappings", actionMappings);
		if( model != null ) bean.addPropertyValue("model", model);
		if( events != null ) bean.addPropertyValue("events", events);
		
		bean.setInitMethodName("init");
	}
}
