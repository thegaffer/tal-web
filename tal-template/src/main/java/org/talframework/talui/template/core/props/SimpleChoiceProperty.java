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
import org.talframework.talui.template.behaviour.property.CodedProperty;
import org.talframework.talui.template.render.codes.CodeType;
import org.talframework.talui.template.render.codes.CodeTypeFactoryLocator;

/**
 * Extends simple property for a simple choices field.
 * Here the actual property can take on one of a set
 * of pre-defined values. See also ChoiceProperty for
 * a more complex unbounded choice property.
 * 
 * @author Tom Spencer
 */
public class SimpleChoiceProperty extends SimpleProperty implements CodedProperty {

	/** The type of codes this field holds */
	private String codeType = null;
	
	@Override
	public void init(Template template, List<TemplateElement> children) {
		if( codeType == null ) throw new IllegalArgumentException("A simple choice must have a code type set");
		
		super.init(template, children);
	}
	
	/**
	 * Just returns the code type
	 */
	public String getCodeType(RenderModel model) {
		return codeType;
	}
	
	/**
	 * Overridden to convert the value to a description
	 */
	@Override
	public String getValue(RenderModel model) {
		String ret = super.getValue(model);
		CodeType type = CodeTypeFactoryLocator.getCodeType(codeType, model, null);
		String desc = type.getCodeDescription(getValue(model));
		if( desc != null ) ret = desc;
		return ret;
	}
	
	/**
	 * Returns the raw value
	 */
	public String getCodeValue(RenderModel model) {
		return super.getValue(model);
	}
	
	/**
	 * Always null
	 */
	public String getSearchUrl(RenderModel model) {
		return null;
	}
	
	/**
	 * Simply returns the code type
	 */
	public String getCodeType() {
		return codeType;
	}
	
	/**
	 * @param codeType The new code type
	 */
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	/**
	 * Simple choices are never unbounded
	 */
	public boolean isUnbounded() {
		return false;
	}
	
	/**
	 * Simple choices are never dynamic
	 */
	public boolean isDynamic() {
		return false;
	}
	
	@Override
	public String getType() {
		return "simple-choice-prop";
	}
}
