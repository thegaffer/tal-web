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

package org.tpspencer.tal.template.compiler.html;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateElementRenderMold;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;
import org.tpspencer.tal.template.render.elements.RenderParameter;
import org.tpspencer.tal.template.render.elements.html.Div;
import org.tpspencer.tal.template.render.elements.special.CollectionElement;
import org.tpspencer.tal.template.render.elements.special.MapElement;

/**
 * This mold specifically works against a message group element.
 * It generates a HTML elements to output a wrapping div which
 * then holds a span for each message
 * 
 * @author Tom Spencer
 */
public class MessagesElementMold implements TemplateElementRenderMold {

	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		String errorsAttribute = element.getSetting("errorsAttribute", String.class);
		String warningsAttribute = element.getSetting("warningsAttribute", String.class);
		String messagesAttribute = element.getSetting("messagesAttribute", String.class);
		String codeProperty = element.getSetting("messageProperty", String.class);
		String paramsProperty = element.getSetting("paramsProperty", String.class);
		
		RenderElement errors = produceMessageElements(errorsAttribute, "error", codeProperty, paramsProperty);
		RenderElement warnings = produceMessageElements(warningsAttribute, "warning", codeProperty, paramsProperty);
		RenderElement messages = produceMessageElements(messagesAttribute, "message", codeProperty, paramsProperty);
		
		Div outerDiv = new Div("messages");
		outerDiv.addStyleClass("messages");
		outerDiv.setIgnoreExpr("${empty " + errorsAttribute + " && empty " + warningsAttribute + " && empty " + messagesAttribute + "}");
		
		if( errors != null ) outerDiv.addElement(errors);
		if( warnings != null ) outerDiv.addElement(warnings);
		if( messages != null ) outerDiv.addElement(messages);
		
		return outerDiv;
	}
	
	private RenderElement produceMessageElements(String messages, String innerClass, String codeProperty, String paramsProperty) {
		if( messages == null ) return null;

		// Div to hold each message
		Div inner = new Div("message", new MessageParameter(codeProperty, paramsProperty));
		inner.addStyleClass(innerClass);
		
		/* Iterate over each message */
		CollectionElement coll = new CollectionElement((String)null, inner);
		coll.setShowIfNull(false);
		
		/* Iterate each message group inside each element of the map */ 
		MapElement map = new MapElement(messages, coll);
		map.setKeyIfNull(null);
		
		return map;
	}
	
	/**
	 * Helper class to convert the current node, expected to
	 * represent a message, to a locale specific value
	 * 
	 * @author Tom Spencer
	 */
	private class MessageParameter implements RenderParameter {
		private final String codeProperty;
		private final String paramsProperty;
		
		/**
		 * Constructs message parameter with custom names for the
		 * code and params property
		 * 
		 * @param code The code property
		 * @param params The parameters property
		 */
		public MessageParameter(String code, String params) {
			if( code != null && code.equals("code") ) codeProperty = null;
			else codeProperty = code;
			
			if( params != null && params.equals("params") ) paramsProperty = null;
			else paramsProperty = code;
		}
		
		/**
		 * Converts the message to a string for display
		 */
		public String getValue(RenderModel model) {
			String message = (String)model.getCurrentNode().getProperty(codeProperty != null ? codeProperty : "code");
			Object[] params = (Object[])model.getCurrentNode().getProperty(paramsProperty != null ? paramsProperty : "params");
			
			if( params != null ) return model.getMessage(message, message, params);
			else return model.getMessage(message, message);
		}
	}
}
