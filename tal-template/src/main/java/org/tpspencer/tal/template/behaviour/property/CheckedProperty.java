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

package org.tpspencer.tal.template.behaviour.property;

import org.tpspencer.tal.template.behaviour.DynamicProperty;

/**
 * This interface represents a checked property, which is
 * a bit like a coded property with only two values that
 * is represented as a checkbox or similar.
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface CheckedProperty extends DynamicProperty {

	/**
	 * Called to get the value to submit if checked
	 * 
	 * @return Called to get the value if checked
	 */
	public String getCheckedValue();
}
