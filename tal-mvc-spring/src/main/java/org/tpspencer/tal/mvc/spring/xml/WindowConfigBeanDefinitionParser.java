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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.tpspencer.tal.mvc.config.WindowConfig;
import org.tpspencer.tal.mvc.spring.xml.helper.ParserHelper;
import org.w3c.dom.Element;

public class WindowConfigBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	/**
	 * Simply returns the AppConfig class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return WindowConfig.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("id");
		String namespace = ParserHelper.getAttribute(element, "namespace");
		String window = ParserHelper.getAttribute(element, "window");
		
		// Constructor
		bean.addConstructorArgValue(name);
		bean.addConstructorArgReference(window);
		
		// Properties
		if( namespace != null && namespace.length() > 0 ) 
			bean.addPropertyValue("namespace", namespace);
	}
}
