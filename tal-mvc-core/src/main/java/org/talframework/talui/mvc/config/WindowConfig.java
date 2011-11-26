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
import org.talframework.talui.mvc.View;
import org.talframework.util.beans.BeanComparison;

/**
 * This configuration class represents a window inside 
 * a page. An page may contain one or more windows. 
 * The window contains the {@link Controller} and
 * {@link View} instances that make up the window. There
 * must be at least one view in the window, but that
 * is it. A window can also have its own
 * {@link ModelLayerConfig}.
 * 
 * @author Tom Spencer
 */
public final class WindowConfig implements MVCConfig {

    /** The page this window belongs to */
    private final PageConfig page;
    /** The name of the window */
	private final String name;
	/** The namespace for the window (defaults to name) */
	private String namespace;
	/** The window-wide model */
    private ModelLayerConfig model;
    /** The windows events converted held in a map against the app/page attribute name */
	private Map<String, EventConfig> events;
	/** The controllers that the window contains */
    private Map<String, Controller> controllers = null;
    /** The default view of the window */
    private View defaultView;
    /** The views of the window, keyed by result */
    private Map<String, View> views = null;
	
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
	public WindowConfig(PageConfig page, String name) {
	    if( page == null ) throw new IllegalArgumentException("You must supply a page to a window config");
		if( name == null ) throw new IllegalArgumentException("You must supply a name to a window config");
		
		this.page = page;
		this.name = name;
		this.namespace = name;
	}
	
	/**
	 * @return The page the window belongs to
	 */
	public PageConfig getPage() {
        return page;
    }
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Always returns the page for a window
	 */
	public MVCConfig getParent() {
	    return getPage();
	}
	
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}
	
	/**
	 * @param namespace The short name or namespace for the window
	 */
	public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

	/**
	 * @return the Model configuration for the window
	 */
	public ModelLayerConfig getModel() {
        return model;
    }
	
	/**
	 * @param model The model for the window (over and above page and app)
	 */
	public void setModel(ModelLayerConfig model) {
        this.model = model;
    }
	
	/**
     * @return the events
     */
    public Map<String, EventConfig> getEvents() {
        return events;
    }

    /**
     * Called to get the event configuration for the given
     * changed attribute.
     * 
     * FUTURE: Can have more than 1 event against an attribute!?!
     * 
     * @param changedAttribute The attribute that has changed
     * @return The event if there is one
     */
    public EventConfig findEvent(String changedAttribute) {
        if( this.events == null ) return null;
        else return this.events.get(changedAttribute);
    }
    
    /**
     * Setter for the events field
     *
     * @param events the events to set
     */
    public void setEvents(Map<String, EventConfig> events) {
        this.events = events;
    }
    
    /**
     * Call to add an event to this window
     * 
     * @param config The event
     */
    public void addEvent(EventConfig config) {
        if( this.events == null ) this.events = new HashMap<String, EventConfig>();
        this.events.put(config.getAttr(), config);
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
     * Call to add a controller to this window.
     * 
     * @param action The action this controller should fire on
     * @param controller The controller to use
     */
    public void addController(String action, Controller controller) {
        if( this.controllers == null ) this.controllers = new HashMap<String, Controller>();
        this.controllers.put(action, controller);
    }
    
    /**
     * @return the defaultView
     */
    public View getDefaultView() {
        return defaultView;
    }
    
    /**
     * @param defaultView The default view for the window
     */
    public void setDefaultView(View defaultView) {
        this.defaultView = defaultView;
    }

    /**
     * @return the views
     */
    public Map<String, View> getViews() {
        return views;
    }
    
    /**
     * Finds a view given the result.
     * 
     * @param result The result of the last action performed on window
     * @return The view against that result (or null)
     */
    public View findView(String result) {
        View ret = defaultView;
        if( views != null && views.containsKey(result) ) ret = views.get(result);
        return ret;
    }

    /**
     * Setter for the views field
     *
     * @param views the views to set
     */
    public void setViews(Map<String, View> views) {
        this.views = views;
    }
    
    /**
     * Adds the given view under the given result.
     * 
     * @param result The result
     * @param view The view
     */
    public void addView(String result, View view) {
        if( views == null ) views = new HashMap<String, View>();
        views.put(result, view);
    }
    
    ///////////////////////////////////////////////////////
    // Standard

    /*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "WindowConfig [name=" + name + "]";
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
		result = prime * result + ((controllers == null) ? 0 : controllers.hashCode());
		result = prime * result + ((defaultView == null) ? 0 : defaultView.hashCode());
		result = prime * result + ((views == null) ? 0 : views.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
	    WindowConfig other = BeanComparison.basic(this, obj);
        boolean ret = other != null;
        if( ret && other != this ) {
            ret = BeanComparison.equals(ret, this.name, other.name);
            ret = BeanComparison.equals(ret, this.namespace, other.namespace);
            ret = BeanComparison.equals(ret, this.model, other.model);
            ret = BeanComparison.equals(ret, this.events, other.events);
            ret = BeanComparison.equals(ret, this.controllers, other.controllers);
            ret = BeanComparison.equals(ret, this.defaultView, other.defaultView);
            ret = BeanComparison.equals(ret, this.views, other.views);
        }
           
        return ret;
	}
}
