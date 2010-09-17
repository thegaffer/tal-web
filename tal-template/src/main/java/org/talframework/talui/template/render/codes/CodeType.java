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

package org.talframework.talui.template.render.codes;


/**
 * This interface represents a set of codes. Typically this
 * is used where a property can take on one of a set of 
 * values - often shown as a drop-down. The codes may be
 * static and or they may be dynamic. The code type instance
 * is requested at run time, typically through the singleton
 * {@link SingletonCodeTypeLookup} class.
 * 
 * @author Tom Spencer
 */
public interface CodeType {
	
	/**
	 * @return The name of this code type - i.e. countryCodes etc
	 */
	public String getType();

	/**
	 * Call to get the codes in this type. The order will
	 * be a natural order determined by the implementation
	 * of this class.
	 * 
	 * @return The codes
	 */
	public String[] getCodes();
	
	/**
	 * Call to convert the code into a description.
	 * 
	 * @param code The code to convert description of
	 * @return The description of the code (or code if no description)
	 */
	public String getCodeDescription(String code);
}
