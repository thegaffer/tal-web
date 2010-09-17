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
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.talframework.talui.mvc.controller.GenericController;
import org.talframework.talui.mvc.spring.xml.helper.ParserHelper;
import org.w3c.dom.Element;

public class GenericControllerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	/**
	 * Simply returns the AppConfig class
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return GenericController.class;
	}
	
	/**
	 * Overridden to set the Apps name to the ID
	 * 
	 * FUTURE: Not sure we even want this anymore as compilers setup directly, if we do this has to change
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String controller = ParserHelper.getAttribute(element, "controller");
		
		String subAction = ParserHelper.getAttribute(element, "sub-action");
		String errorAttribute = ParserHelper.getAttribute(element, "error-attribute");
		//String binder = ParserHelper.getAttribute(element, "binder");
		
		// Properties
		bean.addPropertyReference("controller", controller);
		if( subAction != null ) bean.addPropertyValue("subAction", subAction);
		if( errorAttribute != null ) bean.addPropertyValue("errorsModelAttribute", errorAttribute);
		//if( binder != null ) bean.addPropertyReference("binder", binder);
		
		bean.setInitMethodName("init");
	}
}
