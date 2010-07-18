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

package org.tpspencer.tal.mvc.document.compiler;


/**
 * An instance of this class obtains a property from 
 * the source object using a given strategy.
 * 
 * @author Tom Spencer
 */
public interface PropertyAccessor {

	/**
	 * Call to get the property from the given object
	 * 
	 * @param compiler The compiler
	 * @param type The type of object
	 * @param object The instance itself
	 * @param name The name of the object relative to its parent
	 * @return The property
	 */
	public String getProperty(AppCompiler compiler, Class<?> type, Object object, String name);
}
