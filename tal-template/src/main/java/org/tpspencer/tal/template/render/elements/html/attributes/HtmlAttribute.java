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

package org.tpspencer.tal.template.render.elements.html.attributes;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

/**
 * This interface represents an attribute against an HTML
 * element. Various flavours of this interface exist to
 * add attributes to the element that are given. Classes
 * that derive from AbstractHtmlElement add instances of
 * this class based on the properties they represent.
 * 
 * @author Tom Spencer
 */
public interface HtmlAttribute {
	
	/**
	 * @return The name of the HTML attribute
	 */
	public String getName();

	/**
	 * Called to add the attribute to the given HTML element.
	 * 
	 * @param model The render model
	 * @param elem The element to add the attribute to
	 */
	public void addAttribute(RenderModel model, GenericElement elem);
}
