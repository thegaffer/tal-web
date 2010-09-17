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

package org.talframework.talui.template.render.elements.js;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.render.elements.RenderParameter;
import org.talframework.talui.template.render.elements.SimpleRenderElementBase;

/**
 * The render element generates the Javascript neccessary to
 * attach a particular type of field (identified by its wrapper
 * type and the roleName of the inner field) to dynamic JS
 * events. 
 * 
 * @author Tom Spencer
 */
public class DynamicFieldAttachment extends SimpleRenderElementBase {
	
	/** The type of the wrapper that the element to apply dijit to is within */
	private final String wrapperType;
	/** The role name of the field within the above template wrapper to apply to */
	private final String roleName;
	/** The type of field */
	private final String type;
	/** The attributes for the attachment */
	private DynamicFieldAttribute[] attributes = null;
	
	/**
	 * Standard constructor
	 * 
	 * @param wrapperType The type of the wrapper to apply dijit within
	 * @param roleName The role of the actual element to apply dijit to
	 * @param dijitType The dijit type
	 */
	public DynamicFieldAttachment(String wrapperType, String roleName, String dijitType) {
		this.wrapperType = wrapperType;
		this.roleName = roleName;
		this.type = dijitType;
	}

	/**
	 * Renders out a javascript block to attach the Dijit
	 */
	public void render(RenderModel model) throws IOException {
		StringBuilder buf = model.getTempBuffer();
		buf.append("dynamicOnLoad(function() {").append("\n");
		
		buf.append("\tvar input = {\n");
		buf.append("\t\twrapperType : \"").append(wrapperType).append("\",").append("\n");
		buf.append("\t\troleName : \"").append(roleName).append("\"");
		if( attributes != null && attributes.length > 0 ) {
			boolean first = true;
			for( int i = 0 ; i < attributes.length ; i++ ) {
				String name = attributes[i].getName();
				String val = attributes[i].getVal(model);
				if( val != null ) {
					if( first ) {
						buf.append(",\n\t\tattributes : {\n");
					}
					else {
						buf.append(",\n");
					}
					buf.append("\t\t\t");
					buf.append(name).append(" : \"").append(val).append("\"");
					first = false;
				}
			}
			if( !first ) buf.append(" }");
		}
		
		buf.append("\n\t};").append("\n");
		buf.append("\tdynamicFieldAttach_").append(type).append("(input.wrapperType, input.roleName, input.attributes);\n");
		buf.append("});\n\n");
		
		model.getWriter().write(buf.toString());
	}
	
	/**
	 * @return The type of attribute to apply to
	 */
	public String getAttribute() {
		return wrapperType;
	}

	/**
	 * @return The DIJIT type to apply
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Adds an attribute to the list of other custom widhet
	 * attributes. No check is made to ensure this is unique!
	 * 
	 * @param name The name of the attribute
	 * @param attr The parameter to resolve at render time.
	 */
	public void addAttribute(String name, RenderParameter attr) {
		if( name == null ) throw new IllegalArgumentException("Cannot add a non-named attribute to the widget: " + attr);
		if( attr == null ) throw new IllegalArgumentException("Cannot add null attribute to the widget: " + name);
		
		int ln = this.attributes != null ? this.attributes.length : 0;
		DynamicFieldAttribute[] attrs = new DynamicFieldAttribute[ln+1];
		if( this.attributes != null ) System.arraycopy(this.attributes, 0, attrs, 0, ln);
		attrs[ln] = new DynamicFieldAttribute(name, attr);
		this.attributes = attrs;
	}

	/**
	 * Sets all the Dojo parameters based on settings.
	 * 
	 * @param attrs The attributes
	 */
	public void setAttributes(Map<String, RenderParameter> attrs) {
		this.attributes = null;
		
		if( attrs == null || attrs.size() == 0 ) return;
		
		this.attributes = new DynamicFieldAttribute[attrs.size()];
		int next = 0;
		Iterator<String> it = attrs.keySet().iterator();
		while( it.hasNext() ) {
			String attr = it.next();
			RenderParameter val = attrs.get(attr);
			this.attributes[next] = new DynamicFieldAttribute(attr, val);
			++next;
		}
	}

	/**
	 * Helper class to efficiently store the attributes
	 * 
	 * @author Tom Spencer
	 */
	private class DynamicFieldAttribute {
		private final String name;
		private final RenderParameter val;
		
		public DynamicFieldAttribute(String name, RenderParameter val) {
			this.name = name;
			this.val = val;
		}
		
		public String getName() {
			return name;
		}
		
		public String getVal(RenderModel model) {
			return val.getValue(model);
		}
	}
}
