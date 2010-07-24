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

package org.tpspencer.tal.mvc.model;

import org.tpspencer.tal.mvc.Model;


/**
 * This class contains the configuration of a model 
 * attribute. This includes its name, its resolver,
 * its lifecycle and an optional parameter to be passed
 * to the resolver.
 * 
 * @author Tom Spencer
 */
public final class SimpleModelAttribute extends BaseModelAttribute {
	
	/**
	 * Constructs a simple attribute held in the model. This
	 * type of attribute typically represents some navigational
	 * state and is set by the actions in response to the users
	 * actions.
	 * 
	 * @param name The name of the attribute
	 */
	public SimpleModelAttribute(String name) {
		super(name);
		setType(String.class); // By default attrs are just strings
	}
	
	public SimpleModelAttribute(String name, Class<?> type) {
		super(name);
		setType(type);
	}

	/**
	 * Value is always null on a simple attribute
	 */
	public Object getValue(Model model) {
		return null;
	}
	
	/**
	 * Is always not resolved
	 */
	public boolean isResolved() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("SimpleModelAttribute [");
		buf.append("name=").append(getName());
		if( getDefaultValue() != null ) buf.append(", default=").append(getDefaultValue());
		if( isFlash() ) buf.append(", flash");
		if( isEventable() ) buf.append(", eventable");
		if( !isSimple() ) buf.append(", notSimple");
		if( isClearOnAction() ) buf.append(", clearOnAction");
		buf.append(']');
		return buf.toString();
	}
}
