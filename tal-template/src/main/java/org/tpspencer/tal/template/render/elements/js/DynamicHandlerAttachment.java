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

package org.tpspencer.tal.template.render.elements.js;

import java.io.IOException;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.elements.SimpleRenderElementBase;

/**
 * This Javascript RenderElement simply attaches a Javascript
 * handlers to elements identified by their property and the
 * role of the element inside this wrapper to apply to.
 * 
 * @author Tom Spencer
 */
public class DynamicHandlerAttachment extends SimpleRenderElementBase {

	private final String propertyName;
	private final String roleName;
	private final String event;
	private final String handler;
	
	public DynamicHandlerAttachment(String prop, String role, String event, String handler) {
		this.propertyName = prop;
		this.roleName = role;
		this.event = event;
		this.handler = handler;
	}
	
	/**
	 * Renders out the following:
	 * 
	 * <p><code><pre>
	 * dynamicOnLoad(function() {
	 *   var input = {
	 *     propertyName : "propertyName",
	 *     roleName : "roleName",
	 *     eventName : "eventName",
	 *     handlerName : "handlerName"
	 *   };
	 * 
	 *   dynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);<br />
	 * });
	 * </pre></code></p>
	 */
	public void render(RenderModel model) throws IOException {
		StringBuilder buf = model.getTempBuffer();
		buf.append("dynamicOnLoad(function() {").append("\n");
		
		buf.append("\tvar input = {\n");
		buf.append("\t\tpropertyName : \"").append(propertyName).append("\",\n");
		if( roleName != null ) buf.append("\t\troleName : \"role-").append(roleName).append("\",\n");
		buf.append("\t\teventName : \"").append(event).append("\",\n");
		buf.append("\t\thandlerName : \"").append(handler).append("\"\n");
		buf.append("\t};").append("\n");
		
		buf.append("\tdynamicHandlerAttach(input.propertyName, input.roleName, input.eventName, input.handlerName);\n");
		buf.append("});\n\n");
		
		model.getWriter().write(buf.toString());
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @return the handler
	 */
	public String getHandler() {
		return handler;
	}
}