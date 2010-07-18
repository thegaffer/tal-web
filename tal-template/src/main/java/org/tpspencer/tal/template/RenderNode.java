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

package org.tpspencer.tal.template;

/**
 * This class represents the current node we are
 * rendering. Not all rendering supports outputting
 * through a model, but when it does the render model
 * holds a node to indicate where we are. From here
 * the client can get hold of the object represented
 * by the current node.
 * 
 * @author Tom Spencer
 */
public interface RenderNode {
	
	/**
	 * @return The parent to this node (or null if no parent)
	 */
	public RenderNode getParentNode();

	/**
	 * @return The full name of the current node (including ancestors)
	 */
	public String getName();
	
	/**
	 * @return The full ID of the current node (including ancestors)
	 */
	public String getId();
	
	/**
	 * @return The object at the current node
	 */
	public Object getObject();
	
	/**
	 * @param name The name of the property off current object to get
	 * @return The value of that property
	 */
	public Object getProperty(String name);
	
	/**
	 * The root name of where we are in model ignoring the
	 * index/key. i.e. if we are obj.member[3] then this method
	 * returns "obj.member".
	 * 
	 * @return The root name of where we are
	 */
	public String getRootName();
	
	/**
	 * @return True if the node represents a key or index of its parent
	 */
	public boolean isIndex();
	
	/**
	 * @return The index of this node from its parent (if parent is a map this is the number of elements in the map we have traversed in a row)
	 */
	public int getIndex();
}
