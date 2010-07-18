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

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.template.RenderNode;

/**
 * This class uses Spring (specifically the bean access elements)
 * to implement the RenderNode functionality.
 * 
 * @author Tom Spencer
 */
public final class SpringRenderNode implements RenderNode {

	private final RenderNode parent;
	private final String name;
	private final String id;
	private final Object bean;
	private BeanWrapper wrapper = null;
	private final int index;
	
	public SpringRenderNode(RenderNode parent, String name, String id, int index, Object bean) {
		this.parent = parent;
		this.name = name;
		this.id = id;
		this.bean = bean;
		this.index = index;
	}
	
	/**
	 * Simply returns the parent node
	 */
	public RenderNode getParentNode() {
		return parent;
	}
	
	/**
	 * Returns the name adding on the key if there is one.
	 */
	public String getName() {
		return name; // formIndex(name);
	}
	
	public String getId() {
		return id; // formIndex(id);
	}
	
	/**
	 * Returns the name of the node
	 */
	public String getRootName() {
		return name;
	}
	
	/**
	 * Returns the bean directly
	 */
	public Object getObject() {
		return bean;
	}
	
	/**
	 * Uses the bean wrapper to get the property. This
	 * creates the bean wrapper if neccessary.
	 */
	public Object getProperty(String name) {
		if( bean == null ) return null;
		if( wrapper == null ) wrapper = new BeanWrapperImpl(bean);
		
		return wrapper.isReadableProperty(name) ? wrapper.getPropertyValue(name) : null;
	}
	
	/**
	 * @return True if we are an indexed object
	 */
	public boolean isIndex() {
		return index >= 0;
	}
	
	/**
	 * Returns the index
	 */
	public int getIndex() {
		return index;
	}
}
