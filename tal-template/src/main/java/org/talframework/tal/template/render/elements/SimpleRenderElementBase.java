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

package org.tpspencer.tal.template.render.elements;

import org.tpspencer.tal.template.RenderElement;

/**
 * This class is a base class for any render element that does
 * not support have embedded child elements. It should only
 * be used for render elements that map to ModelProperty's.
 * A runtime exception is generated if we try and add a child
 * element at compile time.
 * 
 * @author Tom Spencer
 */
public abstract class SimpleRenderElementBase implements RenderElement {

	/**
	 * Throws an illegal argument exception
	 */
	public void addElement(RenderElement element) {
		throw new IllegalArgumentException("!!! Cannot add a child [" + element + "] to this entity as it does not support child render elements: " + this.getClass());
	}
}
