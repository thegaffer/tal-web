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
 * the property holds a text value. This interface then
 * allows the compiler/renderer to get at the various
 * validation settings for the property.
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface TextProperty extends DynamicProperty {
	
	/**
	 * @return True if the field should be treated as a longer memo/multi-line field
	 */
	public boolean isMemo();
	
	/**
	 * @return The maximum length this string can be
	 */
	public Number getMaxLength(RenderModel model);

	/**
	 * @return The regular expression that validates the field (may be null)
	 */
	public String getValidationExpression(RenderModel model);
	
	/**
	 * @return Determines if the property is mandatory
	 */
	public boolean isMandatory(RenderModel model);
}
