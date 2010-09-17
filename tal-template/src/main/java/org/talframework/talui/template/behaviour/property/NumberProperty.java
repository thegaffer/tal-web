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
 * the property holds a number. This interface is used by 
 * the compilers mostly to extract additional validation 
 * & formatting information from the property.
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface NumberProperty extends DynamicProperty {
	
	/**
	 * @return The static minimum this property can be
	 */
	public Number getMinimum(RenderModel model);
	
	/**
	 * @return The static maximum this property can be
	 */
	public Number getMaximum(RenderModel model);
	
	/**
	 * @return The number of decimal places to show if applicable
	 */
	public Number getDecimalPlaces(RenderModel model);
}
