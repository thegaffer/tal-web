/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.behaviour;

import org.talframework.talui.template.TemplateElement;

/**
 * This interface represents a template element that itself
 * as a member property. A member property is an object or
 * collection/array of objects. As opposed to the simply
 * dynamic property that just computes to a single value.
 * 
 * <p><b>Note: </b>This is a primary behaviour, while 
 * the element can implement other supporting or property
 * behaviours it cannot implement any of the other 
 * primary behaviours.</p>
 * 
 * @author Tom Spencer
 */
public interface MemberProperty extends TemplateElement {
	
	/**
	 * @return True if the type of property (array, map, coll or object) is known
	 */
	public boolean isTypeKnown();

	/**
	 * @return True if the property represents a primitive array
	 */
	public boolean isArray();
	
	/**
	 * @return True if the property represents a collection
	 */
	public boolean isCollection();
	
	/**
	 * @return True if the property represents a map
	 */
	public boolean isMap();
	
	/**
	 * @return The single template to use for member (or each element of member) at render time
	 */
	public String getTemplate();
}
