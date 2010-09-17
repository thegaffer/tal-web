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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.talui.mvc.Window;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;

/**
 * This simple class represents the configuration of
 * a window. A window config is a sepeparate entity 
 * to the window itself for a number of reasons.
 * 
 * <ul>
 * <li>The window is a code artefact that may be 
 * developed entirely independently of the apps and
 * pages it will sit on.</li>
 * <li>The windows model is combined with the app
 * and page model so that attributes are shared.</li>
 * <li>The windows pure model can also be augmented
 * with window config specific model attributes,
 * particularly config attributes which make the 
 * window act differently.</li>
 * <li>The window could be assigned a different
 * namespace in each place it is used.</li>
 * </ul>
 * 
 * FUTURE: Attributes in the WindowConfig itself overridding the default ones in the Window
 * 
 * @author Tom Spencer
 */
public final class WindowConfig {

	/** The name of the window */
	private final String name;
	/** The namespace for the window (defaults to name) */
	private String namespace;
	/** The window itself */
	private final Window window;
	/** The derived model for the window (only set in the init operation) */
	private final ModelConfiguration model;
	/** The windows events converted held in a map against the app/page attribute name */
	private final Map<String, EventConfig> events;
	
	/**
	 * Constructs a window config givens its name, model and window.
	 * The namespace of the window will match the name unless its
	 * set afterwards.
	 * 
	 * @param page The page the window belongs to (required)
	 * @param name The name of the window (required)
	 * @param model The model for the window (optional)
	 * @param window The window itself (required)
	 */
	public WindowConfig(String name, Window window) {
		if( name == null ) throw new IllegalArgumentException("You must supply a name to a window config");
		if( window == null ) throw new IllegalArgumentException("You must supply a window for the window config");
		
		this.name = name;
		this.namespace = name;
		this.window = window;
		this.model = null;
		this.events = null;
	}
	
	private WindowConfig(String name, String namespace, ModelConfiguration model, Window window, Map<String, EventConfig> events) {
		this.name = name;
		this.namespace = namespace;
		this.model = model;
		this.window = window;
		this.events = events;
	}
	
	/**
	 * Initialises the window, merging with the application model
	 * and the page model and adjusting events in the window to 
	 * the correct model.
	 * 
	 * @param appModel The app model to merge with
	 * @param pageModel The page model to merge with
	 */
	public WindowConfig init(ModelConfiguration appModel, ModelConfiguration pageModel) {
		ModelConfiguration newModel = null;
		Map<String, EventConfig> newEvents = null;
		List<EventConfig> windowEvents = window.getEvents() != null ? new ArrayList<EventConfig>(window.getEvents()) : null;
		
		// Adjust to model to merge with app layer
		ModelConfiguration model = window.getModel();
		if( model != null && model.getAttributes() != null ) {
			List<ModelAttribute> adjustedModel = new ArrayList<ModelAttribute>();
			Iterator<ModelAttribute> it = model.getAttributes().iterator();
			while( it.hasNext() ) {
				ModelAttribute attr = it.next();
				
				String aliasedAs = appModel != null ? appModel.mergeAttribute(attr) : null;
				boolean alisedInApp = aliasedAs != null;
				aliasedAs = (aliasedAs == null && pageModel != null) ? pageModel.mergeAttribute(attr) : aliasedAs;
				
				// See if there is an event on this attribute
				Iterator<EventConfig> eventIt = windowEvents != null ? windowEvents.iterator() : null;
				while( eventIt != null && eventIt.hasNext() ) {
					EventConfig e = eventIt.next();
					if( e.getAttr().equals(attr.getName()) ) {
						if( aliasedAs == null ) throw new IllegalArgumentException("An event is on the window attr [" + attr.getName() + "], but this attribute is not present or aliased in the app or page");
						
						if( newEvents == null ) newEvents = new HashMap<String, EventConfig>();
						String name = alisedInApp ? appModel.getName() : pageModel.getName();
						name += "." + aliasedAs;
						newEvents.put(name, e);
						it.remove();
						break;
					}
				}
				
				if( aliasedAs == null ) {
					if( attr.isAliasExpected() ) throw new IllegalArgumentException("The attribute [" + attr.getName() + "] in the [" + model.getName() + "] model expects to be aliased and is not!");
					adjustedModel.add(attr);
				}
			}
		
			if( adjustedModel.size() > 0 ) newModel = new ModelConfiguration(model.getName(), adjustedModel);
		}
		
		// For any events that remain, map them to the page or app
		Iterator<EventConfig> eventIt = windowEvents != null ? windowEvents.iterator() : null;
		while( eventIt != null && eventIt.hasNext() ) {
			EventConfig e = eventIt.next();
			String name = determineEventKey(e.getAttr(), appModel, pageModel);
			if( name != null ) {
				if( newEvents == null ) newEvents = new HashMap<String, EventConfig>();
				newEvents.put(name, e);
			}
			else {
				throw new IllegalArgumentException("The window event [" + e.getAttr() + "] does not match any app or page model attribute");
			}
		}
		
		return new WindowConfig(
				this.name,
				this.namespace,
				newModel,
				this.window,
				newEvents);
	}
	
	/**
	 * Helper to determine if an event is present in app or page
	 * and return its full key
	 * 
	 * @param eventName The events name
	 * @param appModel The app model
	 * @param pageModel The page model
	 * @return The events full key
	 */
	private String determineEventKey(String eventName, ModelConfiguration appModel, ModelConfiguration pageModel) {
		String ret = null;
		
		ModelAttribute attr = pageModel != null ? pageModel.getAttribute(eventName) : null;
		if( attr != null ) {
			ret = pageModel.getName() + "." + attr.getName();
		}
		else {
			attr = appModel != null ? appModel.getAttribute(eventName) : null;
			if( attr != null ) {
				ret = appModel.getName() + "." + attr.getName();
			}
		}
		
		return ret;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the window
	 */
	public Window getWindow() {
		return window;
	}
	
	/**
	 * @return The windows true model
	 */
	public ModelConfiguration getModel() {
		return model;
	}
	
	/**
	 * Called to get the event configuration for the given
	 * event.
	 * 
	 * @param event The event that has occured
	 * @return The event configuration matching it.
	 */
	public EventConfig getEventConfig(String event) {
		if( this.events == null ) return null;
		else return this.events.get(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("WindowConfig [");
		buf.append("name=").append(name);
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
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
		WindowConfig other = (WindowConfig) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		return true;
	}
}
