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

package org.tpspencer.tal.template.render.elements.html;

import java.io.IOException;
import java.io.Writer;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.elements.SimpleRenderElementBase;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

public class Script extends SimpleRenderElementBase {

	private final String resource;
	
	public Script(String resource) {
		this.resource = resource;
	}
	
	/**
	 * Renders out the script entry
	 */
	public void render(RenderModel model) throws IOException {
		String url = getUrl(model);
		
		GenericElement elem = model.getGenericElement();
		elem.reset("script");
		elem.addAttribute("src", url, false);
		elem.addAttribute("type", "text/javascript", false);
		
		Writer writer = model.getWriter();
		elem.write(writer, false);
		elem.writeTerminate(writer);
		writer.append("\n");
	}
	
	/**
	 * Generates the URL, the default version just gets the
	 * URL generator to generate a resource based URL.
	 * 
	 * @param model The render model
	 * @return The url
	 */
	public String getUrl(RenderModel model) {
		return model.getUrlGenerator().generateResourceUrl(resource);
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}
	
	@Override
	public String toString() {
		return "Script: res=" + getResource();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
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
		Script other = (Script) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}
}
