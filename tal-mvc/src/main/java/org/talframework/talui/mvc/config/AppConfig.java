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

package org.tpspencer.tal.mvc.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tpspencer.tal.mvc.model.ModelConfiguration;

/**
 * This configuration class represents a UI application.
 * An application might consist of a single page with a
 * single window on that page, or it might consist of
 * several pages each with one or more windows on it.
 * It using an external Portal then the app might only
 * be a part of what is displayed to the user.
 * 
 * <p>The main purpose of this object is to carry the
 * configuration of pages (and in turn windows). It also
 * carries the application-wide model configuration.</p>
 * 
 * @author Tom Spencer
 */
public final class AppConfig {

	/** The name of the app */
	private final String name;
	/** The app-wide model */
	private ModelConfiguration model;
	/** The pages inside the app (must be 1) */
	private Map<String, PageConfig> pages = null;
	/** The common page events that apply across the app */
	private Map<String, PageEventConfig> events = null;
	
	/**
	 * Constructs an app config with no model
	 * 
	 * @param name The name of the app (required)
	 */
	public AppConfig(String name) {
		if( name == null ) throw new IllegalArgumentException("You must supply a name to an app config");
		
		this.name = name;
		this.model = null;
	}
	
	/**
	 * Constructs a new AppConfig object
	 * 
	 * @param name The name of the app (required)
	 * @param model The app-wide model configuration (optional)
	 */
	public AppConfig(String name, ModelConfiguration model) {
		if( name == null ) throw new IllegalArgumentException("You must supply a name to an app config");
		
		this.name = name;
		this.model = model;
	}
	
	/**
	 * Initialises the app. This step includes merging the models
	 * across pages and windows and then validating it is set up
	 * correctly.
	 */
	public void init() {
		if( pages == null || pages.size() == 0 ) throw new IllegalArgumentException("You have not created any pages in the " + name + " app");
		
		if( model != null ) model = model.clone();
		
		Map<String, PageConfig> newPages = new HashMap<String, PageConfig>();
		Iterator<String> it = pages.keySet().iterator();
		while( it.hasNext() ) {
			String n = it.next();
			PageConfig p = pages.get(n);
			newPages.put(n, p.init(model));
		}
		this.pages = newPages;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the configuration
	 */
	public ModelConfiguration getModel() {
		return model;
	}

	/**
	 * @return the pages of the app
	 */
	public Collection<PageConfig> getPages() {
		return pages != null ? pages.values() : null;
	}
	
	/**
	 * Sets the pages that the app contains. When adding
	 * each page is cloned so that when the app is 
	 * initialised the page model is adjusted for this
	 * particular application.
	 * 
	 * @param pages The pages for the app
	 */
	public void setPages(Collection<PageConfig> pages) {
		if( pages == null || pages.size() == 0 ) throw new IllegalArgumentException("You have not created any pages in the " + name + " app");
		
		this.pages = new HashMap<String, PageConfig>();
		Iterator<PageConfig> it = pages.iterator();
		while( it.hasNext() ) {
			PageConfig config = it.next();
			if( this.pages.containsKey(config.getName()) ) throw new IllegalArgumentException("You are inserting a page with the same name [" + config.getName() + "] twice into the app: " + name);
			this.pages.put(config.getName(), config);
		}
	}

	/**
	 * Helper to get a page configuration from the app
	 * given its name. 
	 * 
	 * @param name The page to get
	 * @return The page
	 * @throws IllegalArgumentException if page is not known
	 */
	public PageConfig getPage(String name) {
		PageConfig ret = pages != null ? pages.get(name) : null;
		if( ret == null ) throw new IllegalArgumentException("The page requested is not valid: " + name);
		return ret;
	}
	
	/**
	 * @return the events
	 */
	public PageEventConfig getEvent(String result) {
		if( this.events == null ) return null;
		else return this.events.get(result);
	}
	
	/**
	 * @return The page events
	 */
	public Collection<PageEventConfig> getEvents() {
		return events != null ? events.values() : null;
	}

	/**
	 * @param events the page events to set
	 */
	public void setEvents(Collection<PageEventConfig> events) {
		if( events == null ) {
			this.events = null;
			return;
		}
		
		Iterator<PageEventConfig> it = events.iterator();
		this.events = new HashMap<String, PageEventConfig>();
		while( it.hasNext() ) {
			PageEventConfig e = it.next();
			this.events.put(e.getResult(), e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppConfig [name=" + name + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pages == null) ? 0 : pages.hashCode());
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
		AppConfig other = (AppConfig) obj;
		if (events == null) {
			if (other.events != null)
				return false;
		} else if (!events.equals(other.events))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pages == null) {
			if (other.pages != null)
				return false;
		} else if (!pages.equals(other.pages))
			return false;
		return true;
	}
}
