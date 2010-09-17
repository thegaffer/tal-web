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

/**
 * This group is a specialised group that represents a form.
 * This is not generally used inside templates, but only when
 * we want to actually output a form. Usually this is done
 * from a special template.
 * 
 * @author Tom Spencer
 */
public class FormGroup extends SimpleGroup {

	/** Holds the action to submit form into */
	private String action = null;
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getType() {
		return "form-group";
	}
	
	@Override
	public String toString() {
		return "FormGroup: name=" + getName() + ", action=" + getAction();
	}
}
