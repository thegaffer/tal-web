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

public final class StyleImport extends SimpleRenderElementBase {
	
	private String cssResource = null;
	
	public StyleImport(String css) {
		this.cssResource = css;
	}

	public void render(RenderModel model) throws IOException {
		Writer writer = model.getWriter();
		
		GenericElement elem = model.getGenericElement();
		elem.reset("style");
		elem.addAttribute("type", "text/css", false);
		elem.write(writer, false);
		
		StringBuilder buf = model.getTempBuffer();
		buf.append("\n").append("@import \"");
		buf.append(model.getUrlGenerator().generateResourceUrl(cssResource));
		buf.append("\";\n");
		writer.append(buf.toString());
		
		elem.writeTerminate(writer);
		writer.append("\n");
	}
	
	@Override
	public String toString() {
		return "StyleImport: css=" + cssResource;
	}

	/**
	 * @return the cssResource
	 */
	public String getCssResource() {
		return cssResource;
	}

	/**
	 * @param cssResource the cssResource to set
	 */
	public void setCssResource(String cssResource) {
		this.cssResource = cssResource;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cssResource == null) ? 0 : cssResource.hashCode());
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
		StyleImport other = (StyleImport) obj;
		if (cssResource == null) {
			if (other.cssResource != null)
				return false;
		} else if (!cssResource.equals(other.cssResource))
			return false;
		return true;
	}
}
