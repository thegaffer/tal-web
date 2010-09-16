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

import org.tpspencer.tal.template.RenderModel;

/**
 * This behaviour indicates an element that should refer to
 * another page for more information. This is typically used
 * when rendering HTML to provide an active title that defers
 * to another page - or to provide a contextual link in an
 * XML document where the reader can see more information.
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface ReferenceElement {
	
	/**
	 * Call to get the URL that holds reference information.
	 * 
	 * @param model The render model
	 * @return The reference
	 */
	public String getReferenceUrl(RenderModel model);
}
