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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.spring.xml.helper.ModelParserHelper;
import org.w3c.dom.Element;

public class ModelBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	/**
	 * Simply returns the AppConfig class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ModelConfiguration.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String name = element.getAttribute("id");
		bean.addConstructorArgValue(name);
		
		ManagedList attrs = ModelParserHelper.createModelAttributes(element);
		bean.addConstructorArgValue(attrs);
	}
}
