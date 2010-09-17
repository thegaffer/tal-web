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

package org.talframework.talui.template.behaviour.property;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.DynamicProperty;


/**
 * This interface extends the dynamic property to indicate
 * the property holds a code. By the use of a code type
 * these are typically converted into a human description
 * in the rendering. This interface allows the compilers
 * to get this information to create the relevant render
 * elements.
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface CodedProperty extends DynamicProperty {
	
	/**
	 * The code value for this field
	 * 
	 * @param model The render model
	 * @return The model
	 */
	public String getCodeValue(RenderModel model);
	
	/**
	 * Call to get the type of code this property will hold.
	 * 
	 * @return The name of the code type.
	 */
	public String getCodeType(RenderModel model);
	
	/**
	 * Call to get the URL that will obtain the code values
	 * 
	 * @param model The model
	 * @return The url
	 */
	public String getSearchUrl(RenderModel model);
	
	/**
	 * Determines if the coded property is unbounded - can
	 * the user invent their own codes and the available
	 * codes are just a guidance?
	 * 
	 * @return True if the field should be treated as unbounded.
	 */
	public boolean isUnbounded();
	
	/**
	 * Determines if the list of values are dynamic, i.e. will
	 * depend on the value of other fields or external factors.
	 * 
	 * @return True if the list of values if dynamic
	 */
	public boolean isDynamic();
	
}
