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

import java.util.Arrays;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.model.def.FixedDefaultResolver;
import org.talframework.talui.mvc.model.def.SimpleDefaultResolver;

/**
 * This abstract class is a basic implementation for any kind
 * of {@link ModelAttribute}. It holds all the base flags that
 * any model attribute has leaving the derived class to handle
 * only what it requires.
 * 
 * @author Tom Spencer
 */
public abstract class BaseModelAttribute implements ModelAttribute {

	/** Member holds the name of the attribute */
	private final String name;
	/** Member holds any aliases for this attribute */
	private String[] aliases = null;
	/** Member holds the type of the attribute */
	private Class<?> type = null;
	/** Member holds if the attribute is simple (as determined from type) */
	private boolean simple = false;
	/** Member holds the default value to be supplied */
	private DefaultModelResolver defaultValue = null;
	/** Member determines if the attribute should raise an event when changed */
	private boolean eventable = false;
	/** Member determines if the attribute should be discarded at end of action request */
	private boolean flash = false;
	/** Member determines if the attribute should be cleared when an action is received */
	private boolean clearOnAction = false;
	/** Member determines if attribute should be cleared after first render */
	private boolean clearOnRender = false;
	/** Member determines if attribute should always be added to render model */
	private boolean autoRenderAttribute = false;
	/** Member determines if the attribute is aliasable */
	private boolean aliasable = true;
	/** Member determines if an alias is expected for this attribute */
	private boolean aliasExpected = false;
	
	/**
	 * Simple constructor taking in the attributes name.
	 * 
	 * @param name The attributes name
	 */
	public BaseModelAttribute(String name) {
		this.name = name;
	}
	
	/**
	 * @return the aliases
	 */
	public String[] getAliases() {
		return aliases;
	}
	
	/**
	 * @param aliases the aliases to set
	 */
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
	
	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
		
		// Set the type
		if( String.class.equals(type) ) simple = true;
		else if( Number.class.isAssignableFrom(type) ) simple = true;
		else if( type.isPrimitive() ) simple = true;
		else simple = false;
	}
	
	/**
	 * Determines if simple based on type (not a directly settable attr)
	 */
	public boolean isSimple() {
		return simple;
	}
	
	/**
	 * By default we just allow the insertion of the new value.
	 * Derived classes can override to perform some conversion.
	 * 
	 * TODO: We could perform some basic conversion here?!?
	 */
	public Object setValue(Model model, Object currentValue, Object newValue) {
		return newValue;
	}
	
	/**
	 * @return the defaultValue
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * @return the defaultValue
	 */
	public Object getDefaultValue(Model model) {
		return defaultValue != null ? defaultValue.getDefault(model) : null;
	}
	
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(Object defaultValue) {
		if( defaultValue == null ) this.defaultValue = null;
		else if( defaultValue instanceof DefaultModelResolver ) this.defaultValue = (DefaultModelResolver)defaultValue;
		else if( defaultValue instanceof Class<?> ) this.defaultValue = new SimpleDefaultResolver((Class<?>)defaultValue); 
		else this.defaultValue = new FixedDefaultResolver(defaultValue);
		
		if( this.defaultValue != null ) setType(this.defaultValue.getType());
	}
	
	/**
	 * @return the eventable
	 */
	public boolean isEventable() {
		return eventable;
	}
	
	/**
	 * @param eventable the eventable to set
	 */
	public void setEventable(boolean eventable) {
		this.eventable = eventable;
	}
	
	/**
	 * @return true if the attribute is a flash attribute
	 */
	public boolean isFlash() {
		return flash;
	}
	
	/**
	 * @param flash The flash setting
	 */
	public void setFlash(boolean flash) {
		this.flash = flash;
	}
	
	/**
	 * @return the clearOnAction
	 */
	public boolean isClearOnAction() {
		return clearOnAction;
	}
	
	/**
	 * @param clearOnAction the clearOnAction to set
	 */
	public void setClearOnAction(boolean clearOnAction) {
		this.clearOnAction = clearOnAction;
	}
	
	/**
	 * @return the clearOnRender
	 */
	public boolean isClearOnRender() {
		return clearOnRender;
	}

	/**
	 * @param clearOnRender the clearOnRender to set
	 */
	public void setClearOnRender(boolean clearOnRender) {
		this.clearOnRender = clearOnRender;
	}

	/**
	 * @return the autoRenderAttribute
	 */
	public boolean isAutoRenderAttribute() {
		return autoRenderAttribute;
	}

	/**
	 * @param autoRenderAttribute the autoRenderAttribute to set
	 */
	public void setAutoRenderAttribute(boolean autoRenderAttribute) {
		this.autoRenderAttribute = autoRenderAttribute;
	}

	/**
	 * @return the aliasable
	 */
	public boolean isAliasable() {
		return aliasable;
	}
	
	/**
	 * @param aliasable the aliasable to set
	 */
	public void setAliasable(boolean aliasable) {
		this.aliasable = aliasable;
	}
	
	/**
	 * @return the aliasExpected
	 */
	public boolean isAliasExpected() {
		return aliasExpected;
	}
	
	/**
	 * @param aliasExpected the aliasExpected to set
	 */
	public void setAliasExpected(boolean aliasExpected) {
		this.aliasExpected = aliasExpected;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aliasExpected ? 1231 : 1237);
		result = prime * result + (aliasable ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(aliases);
		result = prime * result + (clearOnAction ? 1231 : 1237);
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + (eventable ? 1231 : 1237);
		result = prime * result + (flash ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		BaseModelAttribute other = (BaseModelAttribute) obj;
		if (aliasExpected != other.aliasExpected)
			return false;
		if (aliasable != other.aliasable)
			return false;
		if (!Arrays.equals(aliases, other.aliases))
			return false;
		if (clearOnAction != other.clearOnAction)
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (eventable != other.eventable)
			return false;
		if (flash != other.flash)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
