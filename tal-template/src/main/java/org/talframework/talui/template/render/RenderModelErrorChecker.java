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

package org.talframework.talui.template.render;

import org.talframework.talui.template.RenderModel;

/**
 * This interface represents an object that understands the
 * errors and warnings that may exist in the render model.
 * An instance of this should be created and passed to the
 * the {@link SimpleRenderModel} (or other render model 
 * implementation that holds this).
 * 
 * @author Tom Spencer
 */
public interface RenderModelErrorChecker {
	
	/**
	 * Called by the render model to initialise it against
	 * the current render model. Typically this will 
	 * extract out the errors to work with them more 
	 * efficiently.
	 * 
	 * @param model The render model
	 */
	public void initialise(RenderModel model);

	/**
	 * Call to check if there are any errors against the given
	 * ID and return them if there are.
	 * 
	 * @param errors The errors object
	 * @param id The ID to check if there are errors against
	 * @return The errors
	 */
	public boolean isErrors(String id);
	
}
