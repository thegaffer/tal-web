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

package org.tpspencer.tal.template.render;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.RenderNode;

/**
 * This interface represents a service that can create
 * render nodes inside the render model. The 
 * SimpleRenderModel uses an instance of this class to
 * get the nodes each time it is asked to push and pop.
 * The resulting instance needs to fully implement the
 * RenderNode instance.
 * 
 * @author Tom Spencer
 */
public interface RenderNodeFactory {

	/**
	 * Called to get a render node given the input (see
	 * below).
	 * 
	 * @param model The render model (from here implementor can get the base model objects)
	 * @param current The current node (if there is one)
	 * @param name The name, key or index (relative to current node) of the new node
	 * @param index If the name is a key or index, the integer index for the node
	 * @return The new node
	 */
	public RenderNode getNode(RenderModel model, RenderNode current, String name, int index);
}
