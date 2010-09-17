/*
 * Copyright 2009 Thomas Spencer
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

package org.talframework.talui.mvc.model;

import org.talframework.talui.mvc.Model;

/**
 * This interface represents a class that can produce the default
 * value of a model attribute.
 * 
 * @author Tom Spencer
 */
public interface DefaultModelResolver {

	/**
	 * @return The type of object that will be returned
	 */
	public Class<?> getType();
	
	/**
	 * Called when we must provide the default object. The
	 * model is provided in case the default value is 
	 * dependent on other model attributes. However, at 
	 * least initially there is no protection from a 
	 * recursive call here, so beware!
	 * 
	 * @param model The current model
	 * @return The default value
	 */
	public Object getDefault(Model model);
}
