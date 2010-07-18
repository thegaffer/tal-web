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
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.tpspencer.tal.mvc.config.AppConfig;
import org.tpspencer.tal.mvc.spring.xml.helper.EventMapParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ModelParserHelper;
import org.tpspencer.tal.mvc.spring.xml.helper.ParserHelper;
import org.w3c.dom.Element;

public class AppConfigBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	/**
	 * Simply returns the AppConfig class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return AppConfig.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("id");
		bean.addConstructorArgValue(name);
		
		String modelRef = ParserHelper.getAttribute(element, "model-ref");
		if( modelRef != null ) {
			bean.addDependsOn(modelRef);
			bean.addConstructorArgReference(modelRef);
		}
		else {
			BeanDefinition model = ModelParserHelper.createModel(name + "Model", element);
			if( model != null ) bean.addConstructorArgValue(model);
		}
		
		// Add in pages
		ManagedList pageRefs = new ManagedList();
		String page = element.getAttribute("pages");
		String[] pages = page.split(",");
		for( int i = 0 ; i < pages.length ; i++ ) {
			String pageName = pages[i].trim();
			bean.addDependsOn(pageName);
			pageRefs.add(new RuntimeBeanReference(pages[i].trim()));
		}
		bean.addPropertyValue("pages", pageRefs);
		
		// Add in events
		ManagedList events = EventMapParserHelper.createPageEventList(element);
		if( events != null ) bean.addPropertyValue("events", events);
		
		bean.setInitMethodName("init");
	}
}
