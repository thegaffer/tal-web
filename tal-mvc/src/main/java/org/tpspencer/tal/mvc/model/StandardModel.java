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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.process.ModelAttributeResolver;

/**
 * This class implements the {@link Model} interfaice, but it
 * supports loading of multiple model configurations which are
 * traversed in (reverse) order for the attributes as they are
 * requested. The StandardModel can also record any changes
 * made to the model during it's lifetime (all changes are 
 * logged against a source). Finally the caller can retreive
 * any attributes stored in order to save them away (which is
 * not done automatically).
 * 
 * @author Tom Spencer
 */
public class StandardModel implements Model {
	
	/** Member holds the model layer resolver */
	private final ModelAttributeResolver resolver;
	/** Member holds the model layers that make up the model */
	private List<ModelConfiguration> layers = new ArrayList<ModelConfiguration>();
	/** Member holds a map of model attributes */
	private Map<ModelConfiguration, Map<String, Object> > modelAttributes = null;
	/** Member holds the events generated from changed model elements */
	private List<ModelEvent> events = null;
	/** Determines if getting attribute via a resolver is ok */
	private boolean canUseResolver = true;
	/** Holds the name of the source to stamp any event with (typically a window name) */
	private String source = null;
	/** If set then auto clear attributes will be removed */
	private boolean autoClear = false;

	/**
	 * Constructs a standard model with the given layers which are
	 * analysed in reverse order.
	 * 
	 * @param layers The layers of the model
	 */
	public StandardModel(ModelAttributeResolver resolver, boolean recordEvents) {
		this.resolver = resolver;
		if( recordEvents ) events = new ArrayList<ModelEvent>();
		else events = null;
	}
	
	/**
	 * @return The resolver
	 */
	public ModelAttributeResolver getResolver() {
		return resolver;
	}
	
	/**
	 * @return All model attribute maps, keyed by model config, used by model
	 */
	public Map<ModelConfiguration, Map<String, Object> > getModelAttributes() {
		return modelAttributes;
	}
	
	/**
	 * Call to push a model config as the first model config
	 * to check when resolving attributes. If the model is 
	 * null or has no attributes then this becomes a no-op
	 * 
	 * @param model The model to push
	 */
	public void pushLayer(ModelConfiguration model) {
		if( model == null || model.getAttributes() == null ) return;
		
		// If auto clear, clear down if first time we see this model
		if( autoClear && 
				(modelAttributes == null || !modelAttributes.containsKey(model)) ) {
			Map<String, Object> modelAttrs = resolver.getModelAttributes(model);
			if( modelAttrs != null ) {
				Collection<ModelAttribute> attrs = model.getAttributes();
				Iterator<ModelAttribute> it = attrs.iterator();
				while( it.hasNext() ) {
					ModelAttribute a = it.next();
					if( a.isClearOnAction() ) {
						modelAttrs.remove(a.getName());
					}
				}
				
				if( modelAttributes == null ) modelAttributes = new HashMap<ModelConfiguration, Map<String,Object>>();
				modelAttributes.put(model, modelAttrs);
			}
		}
		
		this.layers.add(0, model);
	}
	
	/**
	 * Call to pop the given model configuration from the
	 * model. If the model config provided is not at the
	 * head it is not removed. This does not generated any
	 * warning.
	 * 
	 * @param model The model to pop
	 * @return The attributes in that layer (they have been saved)
	 */
	public Map<String, Object> popLayer(ModelConfiguration model) {
		if( layers == null ) return null;
		if( model == null || model.getAttributes() == null ) return null;
		
		ModelConfiguration layer = this.layers.size() > 0 ? this.layers.get(0) : null;
		if( layer != null && layer.equals(model) ) {
			this.layers.remove(0);
			return modelAttributes != null ? modelAttributes.get(model) : null;
		}
		else {
			throw new IllegalArgumentException("Illegal model passed in to push layer - that valid layer [" + model + "] is not at the top of the pile: " + layers);
		}
	}
	
	/**
	 * Internal helper to get the model attributes for the 
	 * given model configuration. If these have previously
	 * been obtained they are stored locally, otherwise we
	 * use the model attribute resolver.
	 * 
	 * @param model
	 * @return
	 */
	private Map<String, Object> getModelAttributes(ModelConfiguration model) {
		Map<String, Object> ret = null;
		if( modelAttributes != null ) {
			ret = modelAttributes.get(model);
		}
		
		if( ret == null ) {
			ret = resolver.getModelAttributes(model);
			if( ret != null ) {
				if( modelAttributes == null ) modelAttributes = new HashMap<ModelConfiguration, Map<String,Object>>();
				modelAttributes.put(model, ret);
			}
		}
		
		return ret;
	}
	
	/**
	 * Helper to save model attributes away locally
	 * 
	 * @param model The model the attributes are for
	 * @param attrs The attributes
	 */
	private void saveModelAttributes(ModelConfiguration model, Map<String, Object> attrs) {
		if( modelAttributes == null ) modelAttributes = new HashMap<ModelConfiguration, Map<String,Object>>();
		modelAttributes.put(model, attrs);
	}
	
	/**
	 * @return The events raised during the interaction
	 */
	public List<ModelEvent> getEvents() {
		return events;
	}
	
	/**
	 * Call to clear all events, new events are recorded
	 * in another list.
	 */
	public void clearEvents() {
		if( this.events != null ) this.events = new ArrayList<ModelEvent>();
	}
	
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * @return the autoClear
	 */
	public boolean isAutoClear() {
		return autoClear;
	}

	/**
	 * @param autoClear the autoClear to set
	 */
	public void setAutoClear(boolean autoClear) {
		this.autoClear = autoClear;
	}

	/**
	 * Works through the layers in reverse order
	 */
	public Object getAttribute(String name) {
		Object ret = null;
		boolean found = false;
		
		Iterator<ModelConfiguration> it = this.layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration layer = it.next();
			if( layer.hasAttribute(name) ) {
				ModelAttribute attr = layer.getAttribute(name);
				
				found = true;
				Map<String, Object> attributes = getModelAttributes(layer);
				if( attributes != null && attributes.containsKey(attr.getName()) ) {
					ret = attributes.get(attr.getName());
					break;
				}
				
				// Attribute does not exist, get it
				ret = getNonLocalAttribute(attr);
				if( ret != null ) {
					if( attributes == null ) {
						attributes = new HashMap<String, Object>();
						saveModelAttributes(layer, attributes);
					}
					
					attributes.put(attr.getName(), ret);
					break;
				}
				
				// Finally get default (but, don't store it!)
				else {
					ret = attr.getDefaultValue(this);
					break;
				}
			}
		}
		
		if( !found ) {
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
		Iterator<ModelConfiguration> it = this.layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration layer = it.next();
			if( layer.hasAttribute(name) ) {
				ModelAttribute attr = layer.getAttribute(name);
				
				Map<String, Object> attributes = getModelAttributes(layer);
				Object oldValue = null;
				if( attributes == null ) {
					attributes = new HashMap<String, Object>();
					saveModelAttributes(layer, attributes);
				}
				else {
					oldValue = attributes.get(attr.getName());
				}
				
				value = attr.setValue(this, oldValue, value);
				attributes.put(attr.getName(), value);
				
				// Raise event if appropriate
				if( events != null ) {
					if( attr.isEventable() ) {
						events.add(new ModelEvent(
								source,
								layer,
								attr,
								oldValue,
								value));
					}
				}
				
				return;
			}
		}
		
		throw new UnsupportedModelAttributeException(name);
	}
	
	/**
	 * Works through the layers in reverse order to remove the attribute
	 */
	public void removeAttribute(String name) {
		Iterator<ModelConfiguration> it = this.layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration layer = it.next();
			if( layer.hasAttribute(name) ) {
				ModelAttribute attr = layer.getAttribute(name);
				
				Map<String, Object> attributes = getModelAttributes(layer);
				Object oldValue = null;
				if( attributes != null ) {
					oldValue = attributes.get(attr.getName());
					attributes.remove(attr.getName());
				}
				
				// Raise an event
				if( events != null ) {
					if( attr.isEventable() ) {
						events.add(new ModelEvent(
								source,
								layer,
								attr,
								oldValue,
								null));
					}
				}
				
				return;
			}
		}
		
		throw new UnsupportedModelAttributeException(name);
	}
	
	public boolean containsValueFor(String name) {
		boolean ret = false;
		
		Iterator<ModelConfiguration> it = this.layers.iterator();
		while( it.hasNext() && !ret ) {
			ModelConfiguration layer = it.next();
			if( layer.hasAttribute(name) ) {
				ModelAttribute attr = layer.getAttribute(name);
				if( !attr.isResolved() ) {
					Map<String, Object> attributes = getModelAttributes(layer);
					ret = attributes != null && attributes.containsKey(attr.getName());
				}
				else {
					ret = true;
				}
				
				break;
			}
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
	
	/**
	 * Internal helper to get a stored attribute by name. 
	 * 
	 * @param name The name of the attribute
	 * @return The stored attribute
	 */
	private Object getStoredAttribute(String name) {
		if( layers == null || name == null ) return false;
		
		Object ret = null;
		Iterator<ModelConfiguration> it = layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration config = it.next();
			ModelAttribute attr = config.getAttribute(name);
			if( attr != null ) {
				Map<String, Object> attrs = modelAttributes != null ? modelAttributes.get(config) : null;
				if( attrs != null && attrs.containsKey(attr.getName()) ) {
					ret = attrs.get(attr.getName());
					break;
				}
			}
		}
		
		return ret;
	}
	
	//////////////////////////////////////////
	// Map Operations
	
	/**
	 * Provides size in terms of total number of possible
	 * attributes throughout layer, some may be null
	 */
	public int size() {
		if( layers == null ) return 0;
		
		int ret = 0;
		Iterator<ModelConfiguration> it = layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration layer = it.next();
			ret += layer.getAttributes().size();
		}
		
		return ret;
	}
	
	/**
	 * Only returns true if there are no layers or
	 * attributes in the layers (which is unlikely!)
	 */
	public boolean isEmpty() {
		if( layers == null ) return true;
		else if( size() == 0 ) return true;
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
		if( layers == null || key == null ) return false;
		
		String k = key.toString();
		
		boolean ret = false;
		Iterator<ModelConfiguration> it = layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration layer = it.next();
			if( layer.hasAttribute(k) ) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	/**
	 * Iterates the layers to find the object if it has
	 * been set (i.e. not default values)
	 */
	public boolean containsValue(Object value) {
		if( modelAttributes == null ) return false;
		
		boolean ret = false;
		Iterator<Map<String, Object>> it = modelAttributes.values().iterator();
		while( it.hasNext() ) {
			Map<String, Object> attrs = it.next();
			if( attrs != null && attrs.containsValue(value) ) {
				ret = true;
				break;
			}
		}
		
		return ret;
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
		Object current = getStoredAttribute(k);
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
		Object current = getStoredAttribute(k);
		removeAttribute(k);
		return current;
	}
	
	/**
	 * Returns all 'set' attributes in a set
	 */
	public Set<String> keySet() {
		if( layers == null ) return new HashSet<String>();
		
		HashSet<String> ret = new HashSet<String>();
		Iterator<ModelConfiguration> it = layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration config = it.next();
			Map<String, Object> attrs = modelAttributes != null ? modelAttributes.get(config) : null;
			if( attrs != null ) {
				ret.addAll(attrs.keySet());
			}
		}
		
		return ret;
	}

	/**
	 * Returns all 'set' attributes in a list
	 */
	public Collection<Object> values() {
		if( layers == null ) return new ArrayList<Object>();
		
		ArrayList<Object> ret = new ArrayList<Object>();
		Iterator<ModelConfiguration> it = layers.iterator();
		while( it.hasNext() ) {
			ModelConfiguration config = it.next();
			Map<String, Object> attrs = modelAttributes != null ? modelAttributes.get(config) : null;
			if( attrs != null ) {
				ret.addAll(attrs.values());
			}
		}
		
		return ret;
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
