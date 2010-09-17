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

package org.talframework.talui.template.core.props;

import java.util.List;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.core.BaseElement;

/**
 * This class represents a single simple property
 * in an object typically represented by the template
 * that ultimately holds this property. This class
 * performs no formatting of the property value.
 * 
 * @author Tom Spencer
 *
 */
public class SimpleProperty extends BaseElement implements DynamicProperty {
	
	/** Determines if field is mandatory */
	private boolean mandatory = false;
	/** Determines if field is always hidden (if so just output as such) */
	private boolean hidden = false;
	
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
		return "prop";
	}
	
	/**
	 * @return the mandatory
	 */
	public boolean isMandatory(RenderModel model) {
		return mandatory;
	}
	
	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * Simply returns the value as a string (or null if
	 * it is not set).
	 */
	public String getValue(RenderModel model) {
		Object val = model.getCurrentNode().getProperty(getName());
		return val != null ? val.toString() : null;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
