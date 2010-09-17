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

package org.talframework.talui.template.render;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.RenderNode;

/**
 * This class implements the render node factory interface using
 * SpringRenderNodes that use the bean access mechanisms of
 * Spring to get at the property values.
 * 
 * <p>Note this class is purely static, so a single instance of it
 * is provided on the classloader.</p>
 * 
 * @author Tom Spencer
 */
public final class SpringRenderNodeFactory implements RenderNodeFactory {
	private static final SpringRenderNodeFactory INSTANCE = new SpringRenderNodeFactory();
	
	/**
	 * Hidden constructor, does nothing just prevents lots of
	 * instances.
	 */
	private SpringRenderNodeFactory() {
	}
	
	/**
	 * @return The single instance of the SpringRenderNodeFactory
	 */
	public static RenderNodeFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * Creates a SpringRenderNode supplying the named object either from
	 * the current node or from the model if the current node is null.
	 */
	public RenderNode getNode(RenderModel model, RenderNode current, String name, int index) {
		Object bean = null;
		String nodeName = null;
		String nodeId = null;
		
		// New node, get from the model
		if( current == null ) {
			bean = model.getObject(name);
			nodeName = name;
			nodeId = name;
		}
		
		// Indexed node
		else if( index >= 0 ) {
			BeanWrapper wrapper = new BeanWrapperImpl(new IndexableObject(current.getObject()));
			bean = wrapper.getPropertyValue("object[" + name + "]");
			
			nodeName = model.getTempBuffer().append(current.getName()).append('[').append(name).append(']').toString();
			nodeId = model.getTempBuffer().append(current.getId()).append('[').append(name).append(']').toString();
		}
		
		// Standard node relative to current node
		else {
			bean = current.getProperty(name);
			nodeName = model.getTempBuffer().append(current.getName()).append('.').append(name).toString();
			nodeId = model.getTempBuffer().append(current.getId()).append('-').append(name).toString();
		}
		
		return new SpringRenderNode(current, nodeName, nodeId, index, bean);
	}
	
	/**
	 * Helper class to wrap the bean if its known to be
	 * a array, collection or map.
	 * 
	 * @author Tom Spencer
	 */
	private class IndexableObject {
		private final Object object;
		public IndexableObject(Object obj) {
			this.object = obj;
		}
		
		@SuppressWarnings("unused")
		public Object getObject() {
			return object;
		}
	}
}
