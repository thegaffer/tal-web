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
import org.talframework.talui.template.behaviour.property.TextProperty;

/**
 * This class extends simple property to provide
 * some formatting to a string property.
 * 
 * @author Tom Spencer
 */
public class StringProperty extends SimpleProperty implements TextProperty {

	/** If true will put the string into proper case */
	private boolean properCase = false;
	/** If true will trim leading whitespace */
	private boolean trimLeading = false;
	/** Holds a reg expr for formatting the string */
	private String format = null;
	/** Holds the maximum length of the string */
	private Integer maxLength = null;
	
	@Override
	public String getType() {
		return "text-prop";
	}
	
	/**
	 * Simple string prop is not a memo
	 */
	public boolean isMemo() {
		return false;
	}
	
	/**
	 * Returns the format set
	 */
	public String getValidationExpression(RenderModel model) {
		return format;
	}
	
	/**
	 * Returns the internal max length
	 */
	public Number getMaxLength(RenderModel model) {
		return getMaxLength();
	}
	
	/**
	 * @return the properCase
	 */
	public boolean isProperCase() {
		return properCase;
	}
	/**
	 * @param properCase the properCase to set
	 */
	public void setProperCase(boolean properCase) {
		this.properCase = properCase;
	}
	/**
	 * @return the trimLeading
	 */
	public boolean isTrimLeading() {
		return trimLeading;
	}
	/**
	 * @param trimLeading the trimLeading to set
	 */
	public void setTrimLeading(boolean trimLeading) {
		this.trimLeading = trimLeading;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength.intValue();
	}
}
