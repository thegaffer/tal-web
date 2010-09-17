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
 * This class extends simple property to indicate a
 * memo field (multiline text)
 * 
 * @author Tom Spencer
 */
public class MemoProperty extends SimpleProperty implements TextProperty {

	/** Holds the maximum length of the string */
	private Integer maxLength = null;
	
	@Override
	public String getType() {
		return "memp-prop";
	}
	
	public boolean isMemo() {
		return true;
	}
	
	public String getValidationExpression(RenderModel model) {
		return null;
	}
	
	/**
	 * @return the maxLength
	 */
	public Number getMaxLength(RenderModel model) {
		return maxLength;
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
		this.maxLength = maxLength;
	}
}
