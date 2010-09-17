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

package org.talframework.talui.template.core.groups;

import java.util.List;

import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.core.BaseElement;

/**
 * This special element represents messages to output in
 * the view. It has no behaviour as such and is noted
 * purely by its type.
 * 
 * @author Tom Spencer
 */
public class MessageGroup extends BaseElement implements TemplateElement {
	
	/** Holds the name of the attribute in render model holding errors */
	private String errorsAttribute = "errors";
	/** Holds the name of the attribute in render model holding warnings */
	private String warningsAttribute = "warnings";
	/** Holds the name of the attribute in render model holding messages */
	private String messagesAttribute = "messages";
	
	/** Holds the name of the property of each message object */
	private String messageProperty = "code";
	/** Holds the name of the parameter property of each message object */
	private String paramsProperty = "params";
	
	public MessageGroup() {
	}
	
	public MessageGroup(String messages) {
		this.messagesAttribute = messages;
	}

	/**
	 * Passes children on to base init
	 */
	public void init(Template template, List<TemplateElement> children) {
		super.init(children);
	}
	
	public String getType() {
		return "message-group";
	}

	/**
	 * @return the messagesAttribute
	 */
	public String getMessagesAttribute() {
		return messagesAttribute;
	}

	/**
	 * @param messagesAttribute the messagesAttribute to set
	 */
	public void setMessagesAttribute(String messagesAttribute) {
		this.messagesAttribute = messagesAttribute;
	}

	/**
	 * @return the messageProperty
	 */
	public String getMessageProperty() {
		return messageProperty;
	}

	/**
	 * @param messageProperty the messageProperty to set
	 */
	public void setMessageProperty(String messageProperty) {
		this.messageProperty = messageProperty;
	}

	/**
	 * @return the errorsAttribute
	 */
	public String getErrorsAttribute() {
		return errorsAttribute;
	}

	/**
	 * @param errorsAttribute the errorsAttribute to set
	 */
	public void setErrorsAttribute(String errorsAttribute) {
		this.errorsAttribute = errorsAttribute;
	}

	/**
	 * @return the warningsAttribute
	 */
	public String getWarningsAttribute() {
		return warningsAttribute;
	}

	/**
	 * @param warningsAttribute the warningsAttribute to set
	 */
	public void setWarningsAttribute(String warningsAttribute) {
		this.warningsAttribute = warningsAttribute;
	}

	/**
	 * @return the paramsProperty
	 */
	public String getParamsProperty() {
		return paramsProperty;
	}

	/**
	 * @param paramsProperty the paramsProperty to set
	 */
	public void setParamsProperty(String paramsProperty) {
		this.paramsProperty = paramsProperty;
	}
}
