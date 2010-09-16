/*
 * Copyright 2009 Thomas Spencer
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

package org.tpspencer.tal.mvc.render;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the render model interface by
 * providing a simple mechanism to create it and get at
 * the attributes.
 * 
 * @author Tom Spencer
 */
public final class BasicRenderModel implements RenderModel {
	
	/** Member holds the template for the view */
	private String template = null;
	/** Member holds the optional map of render attributes */
	private Map<String, Object> attributes = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.render.RenderModel#getTemplate()
	 */
	public String getTemplate() {
		return template;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.render.RenderModel#setTemplate(java.lang.String)
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
	/**
	 * @return The render attribute map
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.render.RenderModel#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if( attributes == null ) return null;
		return attributes.get(name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.render.RenderModel#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		if( attributes == null ) return;
		attributes.remove(name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.render.RenderModel#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object attr) {
		if( attributes == null ) attributes = new HashMap<String, Object>();
		attributes.put(name, attr);
	}
	
	@Override
	public String toString() {
		return "BasicRenderModel: template=" + template + ", attributes=" + attributes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicRenderModel other = (BasicRenderModel) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		return true;
	}
}
