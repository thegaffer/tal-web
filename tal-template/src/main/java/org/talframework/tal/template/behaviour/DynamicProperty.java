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

package org.tpspencer.tal.template.behaviour;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.TemplateElement;

/**
 * This interface represents something that is a dynamic
 * property - this means the value is determined only at
 * render time (unlike a static property). 
 * 
 * <p><b>Note: </b>This is a primary behaviour, while 
 * the element can implement other supporting or property
 * behaviours it cannot implement any of the other 
 * primary behaviours.</p>
 * 
 * @author Tom Spencer
 */
public interface DynamicProperty extends TemplateElement {
	
	/**
	 * Determines if the property is always hidden. This is
	 * often used for properties that are only used for 
	 * control purposes. 
	 * 
	 * @return True if it is always hidden, false otherwise
	 */
	public boolean isHidden();
	
	/**
	 * Call to get the value of the property based on the
	 * current model. This is always returned as a string
	 * so the implementing class should deal with formatting
	 * etc. This method is called at render time.
	 * 
	 * @param model The render model with access to current node
	 * @return The value of the property on the current object
	 */
	public String getValue(RenderModel model);
}
