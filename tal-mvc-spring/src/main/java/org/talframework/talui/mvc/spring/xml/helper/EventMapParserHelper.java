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

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.util.xml.DomUtils;
import org.talframework.talui.mvc.config.EventConfig;
import org.talframework.talui.mvc.config.PageEventConfig;
import org.w3c.dom.Element;

/**
 * This class contains helpers to create a controller
 * map bean definition.
 * 
 * @author Tom Spencer
 */
public class EventMapParserHelper {
	
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
	public static ManagedList createEventList(Element element) {
		Element holdingElement = ParserHelper.getChildElement(element, "events");
		if( holdingElement == null ) return null;
		
		List<Element> children = DomUtils.getChildElementsByTagName(holdingElement, new String[]{"event"});
		if( children == null || children.size() == 0 ) return null;
		
		ManagedList events = new ManagedList();
		Iterator<Element> it = children.iterator();
		while( it.hasNext() ) {
			Element e = it.next();
			if( "event".equals(e.getLocalName()) ) {
				String attr = e.getAttribute("attr");
				String action = e.getAttribute("action");
				String newName = e.getAttribute("newValueName");
				String oldName = ParserHelper.getAttribute(e, "oldValueName");
				
				BeanDefinitionBuilder event = BeanDefinitionBuilder.rootBeanDefinition(EventConfig.class);
				event.addConstructorArgValue(attr);
				event.addConstructorArgValue(action);
				event.addConstructorArgValue(newName);
				if( oldName != null ) event.addConstructorArgValue(oldName);
				
				events.add(event.getBeanDefinition());
			}
		}
		
		return events;
	}
	
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
	public static ManagedList createPageEventList(Element element) {
		Element holdingElement = ParserHelper.getChildElement(element, "pageEvents");
		if( holdingElement == null ) return null;
		
		List<Element> children = DomUtils.getChildElementsByTagName(holdingElement, new String[]{"pageEvent", "selectEvent", "backEvent"});
		if( children == null || children.size() == 0 ) return null;
		
		ManagedList events = new ManagedList();
		Iterator<Element> it = children.iterator();
		while( it.hasNext() ) {
			Element e = it.next();
			if( "pageEvent".equals(e.getLocalName()) ) {
				BeanDefinitionBuilder event = BeanDefinitionBuilder.rootBeanDefinition(PageEventConfig.class);
				event.addConstructorArgValue(e.getAttribute("result"));
				event.addConstructorArgValue(e.getAttribute("page"));
				event.addConstructorArgValue(e.getAttribute("attributes"));
				
				// Optional
				String window = ParserHelper.getAttribute(e, "window");
				String action = ParserHelper.getAttribute(e, "action");
				if( window != null && action != null ) {
					event.addConstructorArgValue(window);
					event.addConstructorArgValue(action);
				}
				else if( window != null || action != null ) {
					throw new IllegalArgumentException("A page event must define both a window and an action or neither");
				}
				
				String selectWindow = ParserHelper.getAttribute(e, "selectWindow");
				String selectAction = ParserHelper.getAttribute(e, "selectAction");
				if( selectWindow != null && selectAction != null ) {
					event.addPropertyValue("selectWindow", selectWindow);
					event.addPropertyValue("selectAction", selectAction);
				}
				else if( selectWindow != null || selectAction != null ) {
					throw new IllegalArgumentException("A page event must define both a select window and a select action or neither");
				}
				
				boolean passThrough = ParserHelper.getBooleanAttribute(e, "passThrough");
				if( passThrough ) event.addPropertyValue("passThrough", true);
				
				events.add(event.getBeanDefinition());
			}
			if( "selectEvent".equals(e.getLocalName()) ) {
				BeanDefinitionBuilder event = BeanDefinitionBuilder.rootBeanDefinition(PageEventConfig.class);
				event.addConstructorArgValue(e.getAttribute("result"));
				event.addConstructorArgValue(e.getAttribute("attributes"));
				
				events.add(event.getBeanDefinition());
			}
			if( "backEvent".equals(e.getLocalName()) ) {
				BeanDefinitionBuilder event = BeanDefinitionBuilder.rootBeanDefinition(PageEventConfig.class);
				event.addConstructorArgValue(e.getAttribute("result")); 
				
				events.add(event.getBeanDefinition());	
			}
		}
		
		return events;
	}
}
