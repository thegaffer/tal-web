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

package org.talframework.talui.mvc.model;

/**
 * This class represents an event that has occured in 
 * the model. Events are tracked against eventable 
 * attributes in the model. Windows and States can
 * register themselves to be recipients of events,
 * which is taken care of by the outer dispatcher.
 * 
 * <p>Events carry the old and new values together 
 * with a description of the attribute that has 
 * changed and the name of the layer the attribute
 * is contained within.</p>
 * 
 * @author Tom Spencer
 */
public final class ModelEvent {
	private final String source;
	private final ModelConfiguration configuration;
	private final ModelAttribute attribute;
	private final String eventName;
	private final Object oldValue;
	private final Object newValue;
	
	/**
	 * Constructs a new ModelEvent
	 * 
	 * @param source The source of the event (normally window name)
	 * @param configuration The configuration the attribute is within
	 * @param attribute The attribute that has changed
	 * @param oldValue The old value (may be null of course)
	 * @param newValue The new value (may be null of course)
	 */
	public ModelEvent(String source, ModelConfiguration configuration, ModelAttribute attribute, Object oldValue, Object newValue) {
		this.source = source;
		this.configuration = configuration;
		this.attribute = attribute;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.eventName = configuration.getName() + "." + attribute.getName();
	}

	/**
	 * @return the configuration
	 */
	public ModelConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @return the attribute
	 */
	public ModelAttribute getAttribute() {
		return attribute;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}
	
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * @return The fully qualified event name (model.attr)
	 */
	public String getEventName() {
		return eventName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result
				+ ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result
				+ ((newValue == null) ? 0 : newValue.hashCode());
		result = prime * result
				+ ((oldValue == null) ? 0 : oldValue.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		ModelEvent other = (ModelEvent) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (newValue == null) {
			if (other.newValue != null)
				return false;
		} else if (!newValue.equals(other.newValue))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}
