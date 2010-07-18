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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Contains some general utility methods for parsing
 * MVC beans.
 * 
 * @author Tom Spencer
 */
public class ParserHelper {

	/**
	 * Helper to get an attribute if it exists or return null.
	 * (Normal getAttribute returns an empty string if the
	 * attr does not exist).
	 * 
	 * @param name The name of the attribute
	 * @return The value or null if it does not exist
	 */
	public static String getAttribute(Element e, String name) {
		if( e.hasAttribute(name) ) return e.getAttribute(name);
		return null;
	}
	
	/**
	 * Helper to get an attribute if it exists or return null.
	 * (Normal getAttribute returns an empty string if the
	 * attr does not exist).
	 * 
	 * @param name The name of the attribute
	 * @return The value or null if it does not exist
	 */
	public static boolean getBooleanAttribute(Element e, String name) {
		String val = getAttribute(e, name);
		return Boolean.parseBoolean(val);
	}
	
	/**
	 * Helper to get the first child element with given name.
	 * Useful where you expect only one of a given element inside
	 * child.
	 * 
	 * @param parent The parent element
	 * @param element The name of the element to get
	 * @return The first element of given name (or null).
	 */
	public static Element getChildElement(Element parent, String element) {
		NodeList nodes = parent.getChildNodes();
		if( nodes == null || nodes.getLength() == 0 ) return null;
		
		Element ret = null;
		for( int i = 0 ; i < nodes.getLength() ; i++ ) {
			Node child = nodes.item(i);
			if( child.getNodeType() == Node.ELEMENT_NODE &&
					child.getLocalName().equals(element) ) {
				ret = (Element)child;
				break;
			}
		}
		
		return ret;
	}
}
