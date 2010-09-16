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
public interface AppElement {

	/**
	 * Adds the element instance. Used if config already exists and
	 * we need to attach the element to it.
	 * 
	 * @param element The actual object this element refers to
	 */
	public void addElement(Object element);
	
	/**
	 * @return the id of the element
	 */
	public String getId();
	
	/**
	 * Returns the extra info given the name
	 * 
	 * @param name The name of the extra info required
	 * @return The extra info or null if it does not exist
	 */
	public String getInfo(String name, String def);
	
	/**
	 * @return All extra infor about the element
	 */
	public Map<String, String> getExtraInfo();
	
	/**
	 * Returns the element this instance represents
	 * 
	 * @return The element
	 */
	public Object getElement();
	
	/**
	 * Returns the element cast to the expected type.
	 * 
	 * @param expected The expected type of the element
	 * @return The element
	 */
	public <T> T getElement(Class<T> expected);
	
	/**
	 * Adds a child to this element.
	 * 
	 * @param type The type of element
	 * @param child The child element to add
	 */
	public void add(String type, AppElement child);
	
	/**
	 * Call to get all children of a particular type
	 * 
	 * @param type The type of that element
	 * @return The children of that type
	 */
	public List<AppElement> getChildren(String type);
	
	/**
	 * Call to get a specific child element.
	 * 
	 * @param type The type of element
	 * @param id The ID of that element
	 * @return The child
	 */
	public AppElement getChild(String type, String id);
	
	/**
	 * Helper to get first child of given type. Useful 
	 * when only 1 child of that type is expected.
	 * 
	 * @param type The type
	 * @return The element
	 */
	public AppElement getFirstChild(String type);
	
	/**
	 * FUTURE: Should really have an AppElement config that knows how to treat app element properly
	 * 
	 * @param specIndexes the specIndexes to set
	 */
	public void setSpecIndexes(Map<String, Integer> specIndexes);
}
