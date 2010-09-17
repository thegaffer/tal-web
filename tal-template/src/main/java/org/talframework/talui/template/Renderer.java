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

package org.talframework.talui.template;

import java.io.IOException;


/**
 * A template renderer is something that is capable of
 * rendering out a template from the source information
 * given to it.
 * 
 * @author Tom Spencer
 */
public interface Renderer {
	
	/**
	 * Determines if the renderer iterates around the model
	 * from a root template, or iterates around the template
	 * regardless of the model.
	 * 
	 * @return True if it is a model renderer
	 */
	public boolean isModelRenderer();

	/**
	 * Call to perform the rendering into the writer.
	 * The render model is provided to get access to
	 * dynamic elements.
	 * 
	 * @param model The dynamic render model for this render run
	 */
	public void render(RenderModel model) throws IOException;
}
