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

import java.util.HashMap;
import java.util.Map;

import org.talframework.talui.mvc.Controller;
import org.talframework.util.beans.BeanComparison;

/**
 * This configuration class represents a UI application.
 * An application might consist of a single page with a
 * single window on that page, or it might consist of
 * several pages each with one or more windows on it.
 * It using an external Portal then the app might only
 * be a part of what is displayed to the user.
 * 
 * @author Tom Spencer
 */
public final class AppConfig implements MVCConfig {

	/** The name of the app */
	private final String name;
	/** The app-wide model */
	private ModelLayerConfig model;
	/** The common page events that apply across the app */
	private Map<String, PageEventConfig> events;
	/** The controllers that the app contains */
	private Map<String, Controller> controllers;
	
	/**
	 * Constructs an app config with no model
	 * 
	 * @param name The name of the app (required)
	 */
	public AppConfig(String name) {
		if( name == null ) throw new IllegalArgumentException("You must supply a name to an app config");
		
		this.name = name;
	}
	
	/**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Always returns null for the app
     */
    public MVCConfig getParent() {
        return null;
    }
	
	/**
     * @return the model
     */
    public ModelLayerConfig getModel() {
        return model;
    }
    
    /**
     * @param model The model for the app
     */
    public void setModel(ModelLayerConfig model) {
        this.model = model;
    }

    /**
     * @return the events
     */
    public Map<String, PageEventConfig> getEvents() {
        return events;
    }

    /**
     * Setter for the events field
     *
     * @param events the events to set
     */
    public void setEvents(Map<String, PageEventConfig> events) {
        this.events = events;
    }
    
    /**
     * Finds an event config given the event
     *  
     * @param event The event
     * @return The page event config to fire
     */
    public PageEventConfig findEvent(String event) {
        PageEventConfig ret = null;
        if( events != null && events.containsKey(event) ) ret = events.get(event);
        return ret;
    }
    
    /**
     * Call to add an event to the app.
     * 
     * @param result The result upon which this page event should fire
     * @param config The page event to fire
     */
    public void addEvent(String result, PageEventConfig config) {
        if( this.events == null ) this.events = new HashMap<String, PageEventConfig>();
        this.events.put(result, config);
    }

    /**
     * @return the controllers
     */
    public Map<String, Controller> getControllers() {
        return controllers;
    }

    /**
     * Setter for the controllers field
     *
     * @param controllers the controllers to set
     */
    public void setControllers(Map<String, Controller> controllers) {
        this.controllers = controllers;
    }
    
    /**
     * Finds a controller given the action (if there is one).
     * 
     * @param action The action to perform
     * @return The controller
     */
    public Controller findController(String action) {
        Controller ret = null;
        if( controllers != null && controllers.containsKey(action) ) ret = controllers.get(action);
        return ret;
    }
    
    /**
     * Call to add a controller to the app.
     * 
     * @param action The action this controller should fire on
     * @param controller The controller to use
     */
    public void addController(String action, Controller controller) {
        if( this.controllers == null ) this.controllers = new HashMap<String, Controller>();
        this.controllers.put(action, controller);
    }

    
    ////////////////////////////////////////////////////////////
    // Standard
    
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((events == null) ? 0 : events.hashCode());
		result = prime * result + ((controllers == null) ? 0 : controllers.hashCode());
        return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    AppConfig other = BeanComparison.basic(this, obj);
	    boolean ret = other != null;
	    if( ret && other != this ) {
	        ret = BeanComparison.equals(ret, this.name, other.name);
	        ret = BeanComparison.equals(ret, this.model, other.model);
	        ret = BeanComparison.equals(ret, this.events, other.events);
	        ret = BeanComparison.equals(ret, this.controllers, other.controllers);
	    }
	       
	    return ret;
	}
}
