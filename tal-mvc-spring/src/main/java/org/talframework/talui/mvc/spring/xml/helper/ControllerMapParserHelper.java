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

package org.talframework.talui.mvc.spring.xml.helper;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * This class contains helpers to create a controller
 * map bean definition.
 * 
 * @author Tom Spencer
 */
public class ControllerMapParserHelper {
	
	/**
	 * Call to generate a Spring managed map of bean 
	 * definitions for each controller mapping inside
	 * an embedded controllers element from given element.
	 * If there are no controllers the return is null.
	 * 
	 * @param element The window element
	 * @return The map if there are any proxy attributes
	 */
	@SuppressWarnings("unchecked") // Because of the Spring classes being JDK 1.4!
	public static ManagedMap createControllerMap(Element element) {
		Element holdingElement = ParserHelper.getChildElement(element, "controllers");
		if( holdingElement == null ) return null;
		
		List<Element> children = DomUtils.getChildElementsByTagName(holdingElement, new String[]{"controllerMapping"});
		if( children == null || children.size() == 0 ) return null;
		
		ManagedMap controllers = new ManagedMap();
		Iterator<Element> it = children.iterator();
		while( it.hasNext() ) {
			Element e = it.next();
			if( "controllerMapping".equals(e.getLocalName()) ) {
				String action = e.getAttribute("action");
				String controller = e.getAttribute("controller");
				
				controllers.put(action, new RuntimeBeanReference(controller));
			}
		}
		
		return controllers;
	}
}
