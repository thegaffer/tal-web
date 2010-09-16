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

package org.tpspencer.tal.mvc.spring.xml.helper;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * This class contains helpers to create a controller
 * map bean definition.
 * 
 * @author Tom Spencer
 */
public class ActionMapParserHelper {
	
	/**
	 * Internal helper to produce the map of action mappings
	 * given any <mvc:resultMapping> child elements.
	 * 
	 * @param element The window element
	 * @return The map if there are any action mappings attributes
	 */
	@SuppressWarnings("unchecked") // Because of the Spring classes being JDK 1.4!
	public static ManagedMap createActionMappings(Element element) {
		Element holdingElement = ParserHelper.getChildElement(element, "results");
		if( holdingElement == null ) return null;
		
		List<Element> children = DomUtils.getChildElementsByTagName(holdingElement, new String[]{"resultMapping"});
		if( children == null || children.size() == 0 ) return null;
		
		ManagedMap actionMappings = new ManagedMap();
		Iterator<Element> it = children.iterator();
		while( it.hasNext() ) {
			Element e = it.next();
			if( "resultMapping".equals(e.getLocalName()) ) {
				String result = e.getAttribute("result");
				String view = e.getAttribute("view");
				
				actionMappings.put(result, view);
			}
		}
		
		return actionMappings;
	}
}
