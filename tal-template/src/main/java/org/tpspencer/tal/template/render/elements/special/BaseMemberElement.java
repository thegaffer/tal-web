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

package org.tpspencer.tal.template.render.elements.special;

import java.io.IOException;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;

/**
 * This class is the base for all member elements. All member
 * elements simply delegate processing to a render element that
 * represents another template. However, typically before doing
 * this the current node in the render model is manipulated.
 * This manipulation is in the derived classes, but the raw call
 * of the embedded render element is done inside this base class
 * to provide a consistent way to hook into this and change 
 * what is rendered.
 * 
 * 
 * @author Tom Spencer
 */
public abstract class BaseMemberElement implements RenderElement {

	private final String name;
	private final RenderElement template;
	
	public BaseMemberElement(String name, RenderElement template) {
		this.name = name;
		this.template = template;
	}
	
	/**
	 * This method is called after the derived class has manipulated
	 * the render models current node. The default version just
	 * delegates to the embedded template render element. Derived
	 * classes can hook into this to change what is output.
	 * 
	 * @param model The render model
	 * @throws IOException
	 */
	protected void renderTemplate(RenderModel model) throws IOException {
		template.render(model);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the template
	 */
	public RenderElement getTemplate() {
		return template;
	}
}
