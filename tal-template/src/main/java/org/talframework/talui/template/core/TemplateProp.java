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

package org.talframework.talui.template.core;

import java.util.List;

import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.InnerTemplateElement;

/**
 * This element is not really a property as such. It is
 * used to insert the properties of another element at
 * the point where the template prop is encountered.
 * At render time although control delegates over to the
 * template, the node does not not change. It is very
 * often used to decorate around another template with
 * buttons or links.
 * 
 * @author Tom Spencer
 */
public class TemplateProp extends BaseElement implements InnerTemplateElement {
	
	private String template = null;

	/**
	 * Passes children on to base init
	 */
	public void init(Template template, List<TemplateElement> children) {
		super.init(children);
	}
	
	/**
	 * Simply returns "prop" as the basic type of property
	 */
	public String getType() {
		return "template-prop";
	}
	
	@Override
	public String toString() {
		return "TemplateProp: template=" + getTemplate();
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
}
