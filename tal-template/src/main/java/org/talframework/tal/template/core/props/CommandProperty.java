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

import java.util.Map;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.behaviour.CommandElement;

/**
 * A simple action property represents an action that
 * can be performed, typically on the current object.
 * Unlike the extended action property no parameters 
 * can be passed in a simple action property instead
 * it assumes that the rest of the object will be passed
 * in.
 * 
 * @author Tom Spencer
 */
public class CommandProperty extends SimpleProperty implements CommandElement {

	/** The action to perform */
	private String action = null;
	
	public CommandProperty() {
		
	}
	
	public CommandProperty(String name, String action) {
		setName(name);
		this.action = action;
		super.init(null);
	}
	
	/**
	 * Simply returns "prop" as the basic type of property
	 */
	public String getType() {
		return "command-prop";
	}
	
	/**
	 * Evaluates the action using the model
	 */
	public String getAction(RenderModel model) {
		return action;
	}
	
	/**
	 * There are never parameters in the base
	 */
	public Map<String, String> getActionParameters(RenderModel model) {
		return null;
	}
	
	/**
	 * For a button it is not a dynamic property so we just
	 * return its name.
	 */
	@Override
	public String getValue(RenderModel model) {
		return getName();
	}
	
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
}
