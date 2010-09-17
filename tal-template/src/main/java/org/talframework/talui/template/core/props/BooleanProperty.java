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

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.property.CheckedProperty;

/**
 * This class extends simple property to indicate a
 * boolean field whose value is true or false. This
 * can be because it is a boolean field, or because
 * if matches a certain value.
 * 
 * @author Tom Spencer
 */
public class BooleanProperty extends SimpleProperty implements CheckedProperty {

	/** The value if the property is true */
	private String trueValue = null;
	
	@Override
	public String getType() {
		return "bool-prop";
	}
	
	/**
	 * Simply returns the true value
	 */
	public String getCheckedValue() {
		return trueValue;
	}
	
	/**
	 * Returns the string true or false based on the
	 * value of the property.
	 */
	@Override
	public String getValue(RenderModel model) {
		Object val = model.getCurrentNode().getProperty(getName());
		
		String ret = "false";
		if( val != null && val instanceof Boolean ) {
			if( ((Boolean)val).booleanValue() ) ret = "true";
		}
		else if( val != null ) {
			if( ret.toString().equals(trueValue) ) ret = "true";
		}
		
		return ret;
	}
	
	/**
	 * @return the trueValue
	 */
	public String getTrueValue() {
		return trueValue;
	}

	/**
	 * @param trueValue the trueValue to set
	 */
	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}
}
