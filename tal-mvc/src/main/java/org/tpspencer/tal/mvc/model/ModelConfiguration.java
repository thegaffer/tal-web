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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A model configuration holds all the details about the attributes
 * a model contains. Only those attributes inside the model 
 * configuration can be retrieved by the actions and states. The 
 * model configuration, therefore, is the primary control over the
 * UI.
 * 
 * <p>The model presented to the actions and states is actually 
 * made up of several model configurations as the configuration
 * applies at a level. There is an application wide model 
 * configuration, a page wide configuration, a window wide one,
 * a state configuration and optionally a temporary transaction
 * model.</p>
 * 
 * @author Tom Spencer
 */
public final class ModelConfiguration {

	/** Member holds the name of the configuration */
	private final String name;
	/** Member holds the attributes in this model configuration */
	private final Map<String, ModelAttribute> modelAttributes;
	 
	/**
	 * Constructs a ModelConfiguration will all its attributes.
	 * 
	 * @param name The name of the configuration
	 * @param modelAttributes The attributes for the model
	 */
	public ModelConfiguration(String name, List<ModelAttribute> modelAttributes) {
		if( modelAttributes == null ) throw new IllegalArgumentException("You must supply attributes to the model configuration");
		
		this.name = name;
		this.modelAttributes = new HashMap<String, ModelAttribute>();
		
		Iterator<ModelAttribute> it = modelAttributes.iterator();
		while( it.hasNext() ) {
			ModelAttribute attr = (ModelAttribute)it.next();
			this.modelAttributes.put(attr.getName(), attr);
		}
	}
	
	/**
	 * This method adds an attribute to the model. It is useful only
	 * during initialisation. Adding attributes to a model in a running
	 * app is not allowed. This class may detect initialisation and
	 * prevent this from happeneing in the future.
	 * 
	 * @param attr The attribute to add
	 */
	public void addAttribute(ModelAttribute attr) {
		this.modelAttributes.put(attr.getName(), attr);
	}

	/**
	 * Call to clone the model configuration. This is done by
	 * many holding objects to ensure their copy of a potentially
	 * shared configuration is private.
	 */
	public ModelConfiguration clone() {
		return new ModelConfiguration(this.name, new ArrayList<ModelAttribute>(modelAttributes.values()));
	}
	
	/**
	 * Determines if this model configuration has a particular 
	 * attribute
	 * 
	 * @param name The name of the attribute
	 * @return True if the model can contain this attribute
	 */
	public boolean hasAttribute(String name) {
		return modelAttributes.containsKey(name);
	}
	
	/**
	 * Call to get the ModelAttribute instance that describes
	 * a given attribute.
	 * 
	 * @param name The name of the attribute
	 * @return The ModelAttribute instance (or null if it does not exist)
	 */
	public ModelAttribute getAttribute(String name) {
		return (ModelAttribute)modelAttributes.get(name);
	}

	/**
	 * @return the modelAttributes
	 */
	public Collection<ModelAttribute> getAttributes() {
		return modelAttributes.values();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Call to add a model attribute into the map of
	 * attributes in the config under one of its aliases.
	 * 
	 * @param name The name of the attribute to alias
	 * @param alias The alias to use (must be one of the attributes aliases)
	 */
	public void addAlias(String name, String alias) {
		ModelAttribute attr = modelAttributes.get(name);
		if( attr == null ) throw new IllegalArgumentException("You cannot add an alias [" + alias + "] for an attribute [" + name + "] that does not exist in configuration");
		
		if( modelAttributes.get(alias) != null ) throw new IllegalArgumentException("You cannot add an alias [" + alias + "] that already is in the model configuration");
		
		// Check valid
		String[] aliases = attr.getAliases();
		boolean validAlias = false;
		if( aliases != null ) {
			for( int i = 0 ; i < aliases.length ; i++ ) {
				if( aliases[i].equals(alias) ) {
					validAlias = true;
					break;
				}
			}
		}
		
		if( !validAlias ) throw new IllegalArgumentException("You cannot add an alias [" + alias + "] for an attribute [" + name + "] that does not have that alias name: " + attr);
		
		modelAttributes.put(alias, attr);
	}
	
	/**
	 * Call to attempt to merge the given attribute with any 
	 * in this model.
	 * 
	 * @param attr The attribute to try and merge
	 * @return The name of the core attribute if changed
	 */
	public String mergeAttribute(ModelAttribute attr) {
		if( modelAttributes == null ) return null;
		if( !attr.isAliasable() ) return null;
		
		String ret = null;
		Iterator<ModelAttribute> it = modelAttributes.values().iterator();
		while( it.hasNext() ) {
			ModelAttribute a = it.next();
			
			// Determine if name matches or is alias
			String matchName = determineAttributeMatchName(attr.getName(), a);
			
			// TODO: Other matches (eventable, flash etc)
			
			// Determine if same type
			if( matchName != null ) {
				if( attr.getType().isAssignableFrom(a.getType()) ) {
					if( !modelAttributes.containsKey(matchName) ) {
						modelAttributes.put(matchName, a);
						ret = a.getName();
						break;
					}
					else if( modelAttributes.get(matchName).equals(a) ) {
						ret = a.getName();
						break;
					}
					
					// TODO: Is it a failure if it matches name of diff attr?
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Helper to determine if name matches either the attributes name
	 * or one of its alias names.
	 * 
	 * @param name The name of the attribute
	 * @param attr The attribute to match with
	 * @return The name if it matches
	 */
	private String determineAttributeMatchName(String name, ModelAttribute attr) {
		if( name.equals(attr.getName()) ) return name;
		
		String ret = null;
		String[] aliases = attr.getAliases();
		if( aliases != null && aliases.length > 0 ) {
			for( int i = 0 ; i < aliases.length ; i++ ) {
				if( name.equals(aliases[i]) ) {
					ret = aliases[i];
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Call to remove an attribute from config. This is often
	 * done if the attribute is aliased to a higher level model
	 * configuration (window to page for instance). The caller
	 * should have copied the model config in the first instance
	 * in case it is shared.
	 * 
	 * @param name The attribute to remove
	 */
	public void removeAttribute(String name) {
		modelAttributes.remove(name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ModelConfiguration [name=" + name + ", attrs=" + modelAttributes + "]"; 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modelAttributes == null) ? 0 : modelAttributes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelConfiguration other = (ModelConfiguration) obj;
		if (modelAttributes == null) {
			if (other.modelAttributes != null)
				return false;
		} else if (!modelAttributes.equals(other.modelAttributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
