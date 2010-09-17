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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.RenderModel;

/**
 * This class should be the base class for any render 
 * element that supports child elements. Specifically
 * this class will support adding the children at
 * compile time and then call each child at render
 * time asking it to compile itself. It calls an 
 * internal preChildrenRender before doing this and a
 * postChildrenRender afterwards. Neither of which 
 * need to be implemented, but can be. 
 * 
 * @author Tom Spencer
 */
public abstract class AbstractRenderElement implements RenderElement {
	
	/** The child elements to render in between this element */
	private List<RenderElement> children = null;
	
	/**
	 * Adds the element to the list of children
	 */
	public void addElement(RenderElement element) {
		if( element == null ) throw new IllegalArgumentException("You cannot add a null element to a render element");
		
		if( children == null ) children = new ArrayList<RenderElement>();
		children.add(element);
	}

	public void render(RenderModel model) throws IOException {
		if( preRender(model) ) {
			renderChildren(model);
			postRender(model);
		}
	}
	
	/**
	 * Helper to render the children of this element.
	 * 
	 * @param model The render model
	 */
	protected void renderChildren(RenderModel model) throws IOException {
		if( children != null ) {
			int ln = children.size();
			for( int i = 0 ; i < ln ; i++ ) {
				children.get(i).render(model);
			}
		}
	}
	
	/**
	 * Called at the start of rendering to a) perform any
	 * output before the child template elements and b) to 
	 * conditionally stop processing.
	 * 
	 * @param model The render model
	 * @return True if rendering should continue to children, false otherwise
	 */
	protected boolean preRender(RenderModel model) throws IOException {
		return true;
	}
	
	protected void postRender(RenderModel model) throws IOException {
	}
}
