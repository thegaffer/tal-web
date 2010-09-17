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

package org.talframework.talui.mvc.config;

/**
 * This class represents the configuration for how
 * to apply an event. This object contains the details
 * that turn a changed model event into an action on
 * the window that holds the event.
 * 
 * @author Tom Spencer
 */
public final class EventConfig {

	/** Member holds the attribute to event on */
	private final String attr;
	/** Member holds the action to fire if event occurs */
	private final String action;
	/** Member holds the name of the input param to pass new value in as */
	private final String newValueName;
	/** Member holds the name of the input param to pass old value in as (Optional) */
	private final String oldValueName;
	
	/**
	 * Constructs an EventConfig that only supplied the new value
	 * as an input into the given action.
	 * 
	 * @param attr
	 * @param action
	 * @param newValueName
	 */
	public EventConfig(String attr, String action, String newValueName) {
		if( attr == null ) throw new IllegalArgumentException("Attribute for event config must be supplied");
		if( action == null ) throw new IllegalArgumentException("Action for event config must be supplied");
		if( newValueName == null ) throw new IllegalArgumentException("New Value Attribute name for event config must be supplied");
		
		this.attr = attr;
		this.action = action;
		this.newValueName = newValueName;
		this.oldValueName = null;
	}
	
	/**
	 * Constructs an EventConfig that supplies both the old and the
	 * new value as attributes to the given action.
	 * 
	 * @param attr The attribute to event on
	 * @param action The name of the action to fire
	 * @param newValueName The name of the attribute to supply new value as in input
	 * @param oldValueName The name of the attribute to supply old value as in input
	 */
	public EventConfig(String attr, String action, String newValueName, String oldValueName) {
		if( attr == null ) throw new IllegalArgumentException("Attribute for event config must be supplied");
		if( action == null ) throw new IllegalArgumentException("Action for event config must be supplied");
		if( newValueName == null ) throw new IllegalArgumentException("New Value Attribute name for event config must be supplied");
		
		this.attr = attr;
		this.action = action;
		this.newValueName = newValueName;
		this.oldValueName = oldValueName;
	}
	
	/**
	 * @return the attr
	 */
	public String getAttr() {
		return attr;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return the newValueName
	 */
	public String getNewValueName() {
		return newValueName;
	}

	/**
	 * @return the oldValueName
	 */
	public String getOldValueName() {
		return oldValueName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("EventConfig [");
		buf.append("name=").append(attr);
		buf.append(", action=").append(action);
		buf.append(", new=").append(newValueName);
		if( oldValueName != null ) buf.append(", old=").append(oldValueName);
		buf.append("]");

		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + ((attr == null) ? 0 : attr.hashCode());
		result = prime * result
				+ ((newValueName == null) ? 0 : newValueName.hashCode());
		result = prime * result
				+ ((oldValueName == null) ? 0 : oldValueName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventConfig other = (EventConfig) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (attr == null) {
			if (other.attr != null)
				return false;
		} else if (!attr.equals(other.attr))
			return false;
		if (newValueName == null) {
			if (other.newValueName != null)
				return false;
		} else if (!newValueName.equals(other.newValueName))
			return false;
		if (oldValueName == null) {
			if (other.oldValueName != null)
				return false;
		} else if (!oldValueName.equals(other.oldValueName))
			return false;
		return true;
	}
}
