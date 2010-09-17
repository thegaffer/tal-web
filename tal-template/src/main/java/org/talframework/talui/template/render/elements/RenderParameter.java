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

package org.talframework.talui.template.render.elements;

import org.talframework.talui.template.RenderModel;

/**
 * A Render Parameter is a value used by the RenderElement at
 * render time. Simple render parameters are just a value, but
 * they may be resolved at render time as a message, an 
 * expression, a dynamic value. Some parameters are known by
 * the render elements what kind of value they are, whereas
 * some are known by the compilers. This interface abstracts
 * the decision away so that it does not matter.
 * 
 * @author Tom Spencer
 */
public interface RenderParameter {

	/**
	 * Call to get the value of the parameter at render time.
	 * 
	 * @param model The current render model
	 * @return The value of the parameter
	 */
	public String getValue(RenderModel model);
}
