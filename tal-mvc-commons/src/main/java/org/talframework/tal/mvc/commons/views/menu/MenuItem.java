/*
 * Copyright 2009 Thomas Spencer
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

package org.tpspencer.tal.mvc.commons.views.menu;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple class holding the details of any menu item
 * in a menu view.
 * 
 * @author Tom Spencer
 */
public final class MenuItem {
	
	private final String name;
	private final String labelResource;
	private final String action;
	private final String nameParameter;
	
	/**
	 * Constructs a simple menu item with a name and
	 * an action only
	 */
	public MenuItem(String name, String action) {
		this.name = name;
		this.labelResource = null;
		this.action = action;
		this.nameParameter = null;
	}
	
	/**
	 * Constructs a full menu item with a name, label,
	 * action and the parameter to submit the name of
	 * the menu item in with on the action.
	 *  
	 * @param name
	 * @param label
	 * @param action
	 * @param nameParameter
	 */
	public MenuItem(String name, String label, String action, String nameParameter) {
		this.name = name;
		this.labelResource = label;
		this.action = action;
		this.nameParameter = nameParameter;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the labelResource
	 */
	public String getLabelResource() {
		return labelResource;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the nameParameter
	 */
	public String getNameParameter() {
		return nameParameter;
	}
	
	/**
	 * @return The map of parameters, if applicable
	 */
	public Map<String, String> getParams() {
		Map<String, String> ret = null;
		if( nameParameter != null ) {
			ret = new HashMap<String, String>();
			ret.put(nameParameter, name);
		}
		return ret;
	}
}
