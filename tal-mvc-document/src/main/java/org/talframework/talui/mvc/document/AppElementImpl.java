/*
 * Copyright 2010 Thomas Spencer
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

package org.tpspencer.tal.mvc.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Every part of an application that is documentable is recorded
 * in an application element. This allows us to seperately record
 * extra information about each element of the application for
 * documenting purposes.
 * 
 * @author Tom Spencer
 */
public final class AppElementImpl implements AppElement {

	/** Holds the ID of the element (bean name or bean name + accessor) */
	private final String id;
	/** Holds the element we refer to */
	private Object element;
	/** Holds extra information about this element */
	private final Map<String, String> extraInfo;
	/** Holds child elements, keyed first by type, then by name */
	private Map<String, List<AppElement>> children = null;
	/** Holds the keys for the specifications */
	private Map<String, Integer> specIndexes = null;
	
	public AppElementImpl(String id, Object element, Map<String, String> info) {
		this.id = id;
		this.element = element;
		this.extraInfo = info;
	}
	
	/**
	 * Adds the element instance. Used if config already exists and
	 * we need to attach the element to it.
	 * 
	 * @param element The actual object this element refers to
	 */
	public void addElement(Object element) {
		this.element = element;
	}
	
	/**
	 * @return the id of the element
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the extra info given the name
	 * 
	 * @param name The name of the extra info required
	 * @return The extra info or null if it does not exist
	 */
	public String getInfo(String name, String def) {
		if( name.startsWith("Nos[") && name.endsWith("]") ) {
			String type = name.substring(4, (name.length() - 1));
			if( children != null && children.containsKey(type) ) return Integer.toString(children.get(type).size());
			else return "0";
		}
		else if( name.equals("Nos") ) {
			int count = 0;
			if( children != null ) {
				Iterator<String> it = children.keySet().iterator();
				while( it.hasNext() ) {
					count += children.get(it.next()).size();
				}
			}
			return Integer.toString(count);
		}
		
		if( extraInfo != null ) return extraInfo.containsKey(name) ? extraInfo.get(name) : def;
		return def;
	}
	
	/**
	 * Returns the element cast to the expected type.
	 * 
	 * @param expected The expected type of the element
	 * @return The element
	 */
	public <T> T getElement(Class<T> expected) {
		return expected.cast(element);
	}
	
	/**
	 * Adds a child to this element.
	 * 
	 * @param type The type of element
	 * @param child The child element to add
	 */
	public void add(String type, AppElement child) {
		if( children == null ) children = new HashMap<String, List<AppElement>>();
		
		List<AppElement> elems = null;
		if( children.containsKey(type) ) {
			elems = children.get(type);
		}
		else {
			elems = new ArrayList<AppElement>();
			children.put(type, elems);
		}
		
		// Add spec if configured
		if( specIndexes != null && specIndexes.containsKey(type) ) {
			Integer i = specIndexes.get(type);
			Map<String, String> info = child.getExtraInfo();
			info.put("Spec", i.toString() + "." + Integer.toString(elems.size() + 1));
		}
		
		elems.add(child);
	}
	
	/**
	 * Call to get all children of a particular type
	 * 
	 * @param type The type of that element
	 * @return The children of that type
	 */
	public List<AppElement> getChildren(String type) {
		if( children != null ) return children.get(type);
		return null;
	}
	
	/**
	 * Call to get a specific child element.
	 * 
	 * @param type The type of element
	 * @param id The ID of that element
	 * @return The child
	 */
	public AppElement getChild(String type, String id) {
		AppElement ret = null;
		
		if( children != null && children.containsKey(type) ) {
			List<AppElement> elems = children.get(type);
			Iterator<AppElement> it = elems.iterator();
			while( it.hasNext() ) {
				AppElement e = it.next();
				if( id.equals(e.getId()) ) {
					ret = e;
					break;
				}
			}
		}
		
		return ret;
	}
	
	public AppElement getFirstChild(String type) {
		AppElement ret = null;
		
		if( children != null && children.containsKey(type) ) {
			List<AppElement> elems = children.get(type);
			if( elems != null ) ret = elems.get(0);
		}
		
		return ret;
	}
	
	/////////////////////////////////////////////////////
	// Additional Getter/Setter
	
	/**
	 * @return the element
	 */
	public Object getElement() {
		return element;
	}
	
	/**
	 * @return the extraInfo
	 */
	public Map<String, String> getExtraInfo() {
		return extraInfo;
	}

	/**
	 * @return the specIndexes
	 */
	public Map<String, Integer> getSpecIndexes() {
		return specIndexes;
	}

	/**
	 * @param specIndexes the specIndexes to set
	 */
	public void setSpecIndexes(Map<String, Integer> specIndexes) {
		this.specIndexes = specIndexes;
	}
}
