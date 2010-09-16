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

package org.tpspencer.tal.template.behaviour;

import java.util.Map;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.TemplateElement;

/**
 * This interface represents an element that in turns
 * represents a command. This may be treated as a link
 * or a button, but regardless it 
 * 
 * <p><b>Note: </b>This is a primary behaviour, while 
 * the element can implement other supporting or property
 * behaviours it cannot implement any of the other 
 * primary behaviours.</p>
 * 
 * @author Tom Spencer
 */
public interface CommandElement extends TemplateElement {
	
	/**
	 * Call to get the action this command should invoke.
	 * 
	 * @param model The current render model
	 * @return The raw action to invoke 
	 */
	public String getAction(RenderModel model);
	
	/**
	 * Call to get the parameters that should be passed in
	 * the command.
	 * 
	 * @param model The current render model
	 * @return The parameters (if any)
	 */
	public Map<String, String> getActionParameters(RenderModel model);
}
