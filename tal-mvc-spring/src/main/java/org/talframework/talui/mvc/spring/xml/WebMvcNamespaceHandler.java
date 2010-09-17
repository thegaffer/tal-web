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

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * This class is the namespace handler for the customer
 * mvc XML elements that simplify the construction of a
 * UI app based on the Web MVC framework
 * 
 * @author Tom Spencer
 */
public class WebMvcNamespaceHandler extends NamespaceHandlerSupport {

	/**
	 * Adds in our beans
	 */
	public void init() {
		// Config
		registerBeanDefinitionParser("app", new AppConfigBeanDefinitionParser());
		registerBeanDefinitionParser("page", new PageConfigBeanDefinitionParser());
		registerBeanDefinitionParser("window", new WindowConfigBeanDefinitionParser());
		registerBeanDefinitionParser("model", new ModelBeanDefinitionParser());
		
		// Controller
		registerBeanDefinitionParser("genericController", new GenericControllerBeanDefinitionParser());
		
		// View
		registerBeanDefinitionParser("templateView", new TemplateViewBeanDefinitionParser());
		
		// Window
		registerBeanDefinitionParser("simpleWindow", new SimpleWindowBeanDefinitionParser());
		registerBeanDefinitionParser("complexWindow", new MultiViewWindowBeanDefinitionParser());
	}
}
