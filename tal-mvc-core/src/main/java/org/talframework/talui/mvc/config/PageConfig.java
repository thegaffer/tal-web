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
 * This configuration class represents a page inside an 
 * application. An application may contain one or more 
 * pages. The page contains the {@link WindowConfig} 
 * that make up the page (may be one or more). The page 
 * may also have it's own {@link ModelLayerConfig}.
 * 
 * @author Tom Spencer
 */
public final class PageConfig implements MVCConfig {

    /** The app this page belongs to */
    private final AppConfig app;
	/** The name of the page */
	private final String name;
	/** The template for the page */
	private final String template;
	/** The page-wide model */
    private ModelLayerConfig model;
    /** The windows on the page */
    private Map<String, WindowConfig> windows = null;
    /** The page events that apply on this page */
    private Map<String, PageEventConfig> events = null;
    /** The controllers that the page contains */
    private Map<String, Controller> controllers = null;
	
	/**
	 * Constructs a new page without any model configuration
	 * 
	 * @param app The app the page belongs to (required)
	 * @param name The name of the page (required)
	 */
	public PageConfig(AppConfig app, String name, String template) {
	    if( app == null ) throw new IllegalArgumentException("You must supply an app to an page config");
	    if( name == null ) throw new IllegalArgumentException("You must supply a name to an page config");
	    
	    this.app = app;
		this.name = name;
		this.template = template;
	}
	
	/**
	 * @return the app the page belongs to
	 */
	public AppConfig getApp() {
        return app;
    }
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Always returns the app for a page
	 */
	public MVCConfig getParent() {
	    return getApp();
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	
	/**
     * @return the model
     */
    public ModelLayerConfig getModel() {
        return model;
    }
    
    /**
     * @param model The model to use in the page
     */
    public void setModel(ModelLayerConfig model) {
        this.model = model;
    }

    /**
     * @return the windows
     */
    public Map<String, WindowConfig> getWindows() {
        return windows;
    }
    
    /**
     * Finds a window given its name
     * 
     * @param window The window to find
     * @return The window
     */
    public WindowConfig findWindow(String window) {
        WindowConfig ret = null;
        if( windows != null && windows.containsKey(window) ) ret = windows.get(window);
        return ret;
    }
    
    /**
     * Setter for the windows field
     *
     * @param windows the windows to set
     */
    public void setWindows(Map<String, WindowConfig> windows) {
        this.windows = windows;
    }
    
    /**
     * Call to add a new window to this page.
     * 
     * @param window The window
     */
    public void addWindow(WindowConfig window) {
        if( this.windows == null ) this.windows = new HashMap<String, WindowConfig>();
        this.windows.put(window.getName(), window);
    }

    /**
     * @return the events
     */
    public Map<String, PageEventConfig> getEvents() {
        return events;
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
     * Setter for the events field
     *
     * @param events the events to set
     */
    public void setEvents(Map<String, PageEventConfig> events) {
        this.events = events;
    }
    
    /**
     * Call to add an event to this page.
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
     * Setter for the controllers field
     *
     * @param controllers the controllers to set
     */
    public void setControllers(Map<String, Controller> controllers) {
        this.controllers = controllers;
    }
    
    /**
     * Call to add a controller to this page.
     * 
     * @param action The action this controller should fire on
     * @param controller The controller to use
     */
    public void addController(String action, Controller controller) {
        if( this.controllers == null ) this.controllers = new HashMap<String, Controller>();
        this.controllers.put(action, controller);
    }
    
    /////////////////////////////////////////////
    // Standard

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PageConfig [name=" + name + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((windows == null) ? 0 : windows.hashCode());
        result = prime * result + ((events == null) ? 0 : events.hashCode());
        result = prime * result + ((controllers == null) ? 0 : controllers.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    PageConfig other = BeanComparison.basic(this, obj);
        boolean ret = other != null;
        if( ret && other != this ) {
            ret = BeanComparison.equals(ret, this.name, other.name);
            ret = BeanComparison.equals(ret, this.template, other.template);
            ret = BeanComparison.equals(ret, this.model, other.model);
            ret = BeanComparison.equals(ret, this.windows, other.windows);
            ret = BeanComparison.equals(ret, this.events, other.events);
            ret = BeanComparison.equals(ret, this.controllers, other.controllers);
        }
           
        return ret;
	}
}
