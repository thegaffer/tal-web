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

package org.tpspencer.tal.template.behaviour.supporting;

import org.tpspencer.tal.template.TemplateElement;

/**
 * This behaviour is exhibited by an element that should
 * contain a resource as its value. 
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface ResourceProperty extends TemplateElement {

	/**
	 * @return The resource to resolve and output at render time.
	 */
	public String getResource();
}
