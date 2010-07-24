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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.tpspencer.tal.mvc.Model;

/**
 * This class implements the model interface to provide access
 * to only a single model. It also does not save model attributes.
 * 
 * @author Tom Spencer
 */
public class SimpleModel implements Model {

	/** Member holds the model configuration */
	private ModelConfiguration model;
	/** Member holds the attributes of the model */
	private Map<String, Object> attrs;
	/** Determines if getting attribute via a resolver is ok */
	private boolean canUseResolver = true;
	
	/**
	 * Construct a simple model
	 * 
	 * @param model The model itself
	 * @param attrs The attributes
	 */
	public SimpleModel(ModelConfiguration model, Map<String, Object> attrs) {
		this.model = model;
		this.attrs = attrs;
	}
	
	/**
	 * Simply returns the model attribute if it exists
	 */
	public Object getAttribute(String name) {
		Object ret = null;
		
		if( model.hasAttribute(name) ) {
			ModelAttribute attr = model.getAttribute(name);
				
			if( attrs != null && attrs.containsKey(name) ) {
				ret = attrs.get(name);
			}
			else {
				// Attribute does not exist, get it
				ret = getNonLocalAttribute(attr);
				if( ret != null ) {
					if( attrs == null ) {
						attrs = new HashMap<String, Object>();
					}
					
					attrs.put(name, ret);
				}
				else {
					ret = attr.getDefaultValue(this);
				}
			}
		}
		else {
			throw new UnsupportedModelAttributeException(name);
		}
		
		return ret;
	}
	
	/**
	 * Specialised version of the getAttribute method that checks
	 * the attribute is of the expected type. Saves casting in
	 * client code, but still returns a ClassCaseException if the
	 * attribute does not match. 
	 * 
	 * @param <T> The class to expect
	 * @param name The name of the attribute
	 * @param expected The expected type of the attribute
	 * @return The attribute value
	 * @throws ClassCastException is thrown if attribute is not of correct type
	 */
	public <T> T getAttribute(String name, Class<T> expected) {
		Object ret = getAttribute(name);
		if( ret != null ) {
			if( !expected.isInstance(ret) ) throw new ClassCastException("Attribute [" + name + "] is not of the expected type: " + expected.getName());
			return expected.cast(ret);
		}
		
		return null;
	}
	
	/**
	 * Works through the layers in reverse order to set the attribute
	 */
	public void setAttribute(String name, Object value) {
		if( model.hasAttribute(name) ) {
			if( attrs == null ) attrs = new HashMap<String, Object>();
			Object oldValue = attrs.get(name);
			
			// Give attribute chance to adjust/convert
			value = model.getAttribute(name).setValue(this, oldValue, value);
			attrs.put(name, value);
		}
		else {
			throw new UnsupportedModelAttributeException(name);
		}
	}
	
	/**
	 * Works through the layers in reverse order to remove the attribute
	 */
	public void removeAttribute(String name) {
		if( model.hasAttribute(name) ) {
			if( attrs != null ) attrs.remove(name);
		}
		else {
			throw new UnsupportedModelAttributeException(name);
		}
	}
	
	/**
	 * Determines if we are holding a value for the attribute
	 */
	public boolean containsValueFor(String name) {
		boolean ret = false;
		if( model.hasAttribute(name) ) {
			ModelAttribute attr = model.getAttribute(name);
			if( !attr.isResolved() ) ret = attrs.containsKey(name);
			else ret = true;
		}
		return ret;
	}
	
	/**
	 * This private method gets the value of the attribute
	 * from the ModelAttribute description. It will pass the
	 * model unless we have already inside getting another
	 * model attribute already. 
	 * 
	 * @param attribute The attribute we need to get
	 * @return The attributes value
	 */
	private Object getNonLocalAttribute(ModelAttribute attribute) {
		Object ret = null;
		
		boolean useResolver = canUseResolver;
		if( useResolver ) canUseResolver = false;
		ret = attribute.getValue(useResolver ? this : null);
		if( useResolver ) canUseResolver = true;
		
		return ret;
	}
	
	//////////////////////////////////////////
	// Map Operations
	
	/**
	 * Provides size in terms of total number of possible
	 * attributes throughout layer, some may be null
	 */
	public int size() {
		return model.getAttributes().size();
	}
	
	/**
	 * Only returns true if there are no layers or
	 * attributes in the layers (which is unlikely!)
	 */
	public boolean isEmpty() {
		if( size() == 0 ) return true;
		else return false;
	}
	
	/**
	 * Determines if the model contains the specified
	 * attribute.
	 * 
	 * @param key The name of the attribute to check for
	 * @return True if it exists, false otherwise
	 */
	public boolean containsKey(Object key) {
		return model.hasAttribute(key.toString());
	}
	
	/**
	 * Iterates the layers to find the object if it has
	 * been set (i.e. not default values)
	 */
	public boolean containsValue(Object value) {
		return attrs != null ? attrs.containsValue(value) : false;
	}
    
	/**
	 * Gets the object at specified key from model
	 */
	public Object get(Object key) {
		String k = key.toString();
		return getAttribute(k);
	}
	
	/**
	 * Sets the value of the given attribute in model
	 */
	public Object put(String key, Object value) {
		String k = key.toString();
		Object current = attrs != null ? attrs.get(key) : null;
		setAttribute(k, value);
		return current;
	}
	
	/**
	 * Puts all attributes in input into the model
	 */
    public void putAll(Map<? extends String, ? extends Object> map) {
    	Iterator<? extends String> it = map.keySet().iterator();
    	while( it.hasNext() ) {
    		String k = it.next();
    		put(k, map.get(k));
    	}
    }
	
	/**
	 * Removes the attribute from model setting it back to
	 * its default value.
	 */
	public Object remove(Object key) {
		String k = key.toString();
		Object current = attrs != null ? attrs.get(key) : null;
		removeAttribute(k);
		return current;
	}
	
	/**
	 * Returns all 'set' attributes in a set
	 */
	public Set<String> keySet() {
		return attrs != null ? attrs.keySet() : null;
	}

	/**
	 * Returns all 'set' attributes in a list
	 */
	public Collection<Object> values() {
		return attrs != null ? attrs.values() : null;
	}
	
	/**
     * Clearing all attributes is not supported in model
     * (many attributes are persistently available)
     */
    public void clear() {
    	throw new UnsupportedOperationException("You cannot clear a model");
    }

    /**
     * Gettings the entry set of a model is not supported.
     * There is no 'real' map backing it up.
     */
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException("You cannot get the entry set of a model, it is not a real map!");
	}
}
