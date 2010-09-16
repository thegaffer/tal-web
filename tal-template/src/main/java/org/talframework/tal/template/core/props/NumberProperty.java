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

package org.tpspencer.tal.template.core.props;

import java.text.NumberFormat;

import org.tpspencer.tal.template.RenderModel;

/**
 * Extends simple property to provide common handling
 * for a number property.
 * 
 * @author Tom Spencer
 */
public class NumberProperty extends SimpleProperty implements org.tpspencer.tal.template.behaviour.property.NumberProperty {

	/** Holds the min value the number can take on */
	private Number minimum = null;
	/** Holds the max value the number can take on */
	private Number maximum = null;
	/** Holds the decimal places to show */
	private Integer decimalPlaces = null;
	
	/**
	 * Formats the string as appropriate
	 */
	@Override
	public String getValue(RenderModel model) {
		String ret = null;
		Object val = model.getCurrentNode().getProperty(getName());
		
		// Does this work if primitive!?!
		if( val != null && val instanceof Number ) {
			if( model.getLocale() != null ) {
				ret = NumberFormat.getInstance(model.getLocale()).format(val);
			}
			else {
				ret = NumberFormat.getInstance().format(val);
			}
		}
		
		else if( val != null ) {
			ret = val.toString();
		}
		
		else {
			ret = null;
		}
		
		return ret;
	}
	
	@Override
	public String getType() {
		return "number-prop";
	}
	
	public Number getMinimum(RenderModel model) {
		return getMinimum();
	}
	
	public Number getMaximum(RenderModel model) {
		return getMaximum();
	}
	
	public Number getDecimalPlaces(RenderModel model) {
		return getDecimalPlaces();
	}
	
	/**
	 * @return the minimum
	 */
	public Number getMinimum() {
		return minimum;
	}

	/**
	 * @param minimum the minimum to set
	 */
	public void setMinimum(Number minimum) {
		this.minimum = minimum;
	}

	/**
	 * @return the maximum
	 */
	public Number getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(Number maximum) {
		this.maximum = maximum;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}
}
