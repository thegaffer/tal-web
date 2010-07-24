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
 * This class represents a configuration attribute in
 * a model. A config model attribute is one which is
 * determined via a lookup on a SimpleModelResolver.
 * Typically this represents some static value, often
 * set in a property file or similar.
 * 
 * @author Tom Spencer
 */
public final class ConfigModelAttribute extends BaseModelAttribute {
	
	/** Member holds resolver for the attribute (if there is one) */
	private final SimpleModelResolver resolver;
	/** Member holds an object that can help the resolver get the attribute */
	private final Object parameter;
	
	/**
	 * Constructs an attribute that is held externally to the 
	 * UI and is obtained via the use of a model resolver. This
	 * type of attribute is never held in any storage.
	 *  
	 * @param name The name of the attribute
	 * @param resolver The resolver that is capable of getting it
	 * @param parameter An optional parameter the resolver can use
	 * @param defaultValue The default value if the resolver returns null
	 */
	public ConfigModelAttribute(String name, SimpleModelResolver resolver, Object parameter, Object defaultValue) {
		super(name);
		
		if( resolver == null ) throw new IllegalArgumentException("A resolved model attribute must have a resolver");
		
		this.resolver = resolver;
		this.parameter = parameter;
		setDefaultValue(defaultValue);
		if( defaultValue == null ) setType(String.class); // Treat as string by default
		setFlash(true);
	}

	/**
	 * Passes control to resolver
	 */
	public Object getValue(Model model) {
		return resolver.getModelAttribute(getName(), this.parameter);
	}
	
	/**
	 * @return the resolver
	 */
	public SimpleModelResolver getResolver() {
		return resolver;
	}

	/**
	 * @return the parameter
	 */
	public Object getParameter() {
		return parameter;
	}
	
	/**
	 * Config attributes can never be non-flash
	 */
	@Override
	public void setFlash(boolean flash) {
		if( !flash ) throw new IllegalArgumentException("You cannot set a config model attribute to be non-flash");
		super.setFlash(true);
	}
	
	/**
	 * Is alwayes resolved
	 */
	public boolean isResolved() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("ConfigModelAttribute [");
		buf.append("name=").append(getName());
		buf.append(", resolver=").append(resolver);
		if( parameter != null ) buf.append(", param=").append(parameter);
		if( getDefaultValue() != null ) buf.append(", default=").append(getDefaultValue());
		if( isEventable() ) buf.append(", eventable");
		buf.append(']');
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result
				+ ((resolver == null) ? 0 : resolver.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigModelAttribute other = (ConfigModelAttribute) obj;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}
}
