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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.mvc.Window;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.window.WindowCompiler;

/**
 * This configuration class represents a page inside an 
 * application. An application may contain one or more 
 * pages. The page contains, in addition to its name, 
 * the windows that make up the page (may be one or more).
 * The page may also have it's own PageConfiguration.
 * 
 * TODO: Add in initialised so it does not change!!
 * 
 * @author Tom Spencer
 */
public final class PageConfig {

	/** Determines if config is initialised */
	private boolean initialised = false;
	/** The name of the page */
	private String name;
	/** The template for the page */
	private String template = null;
	/** The model for the page (if any) */
	private ModelConfiguration model = null;
	/** The windows on the page */
	private Map<String, WindowConfig> windows = null;
	/** The page events th*/
	private Map<String, PageEventConfig> events = null;
	
	/**
	 * Default constructor - the page config will require
	 * initialising through an app.
	 */
	public PageConfig() {
	}
	
	/**
	 * Constructs a new page without any model configuration
	 * 
	 * @param app The app the page belongs to (required)
	 * @param name The name of the page (required)
	 */
	/*public PageConfig(String name, String template) {
		this.name = name;
		this.template = template;
		this.model = null;
	}*/
	
	/**
	 * Constructs a page given its name, model and window(s)
	 * 
	 * @param app The app the page belongs to (required)
	 * @param name The name of the page (required)
	 * @param model The model for the page (optional)
	 */
	/*public PageConfig(String name, String template, ModelConfiguration model) {
		this.name = name;
		this.template = template;
		this.model = model;
	}*/
	
	/**
	 * Private constructor used to create an adjusted clone of
	 * a PageConfig during initialisation. 
	 * 
	 * @param name
	 * @param model
	 * @param windows
	 * @param pageEvents
	 */
	private PageConfig(String name, String template, ModelConfiguration model, Map<String, WindowConfig> windows, Map<String, PageEventConfig> pageEvents) {
		this.initialised = true;
		this.name = name;
		this.template = template;
		this.model = model;
		this.windows = windows;
		this.events = pageEvents;
	}
	
	/**
	 * Initialises this page config and returns a new private 
	 * instance of itself with an adjusted model and private
	 * windows.
	 * 
	 * @param appModel The model for the app (which can be adjusted)
	 * @return The cloned and initialised page config
	 */
	protected PageConfig init(ModelConfiguration appModel) {
		if( name == null ) throw new IllegalArgumentException("You must supply a name to an page config");
		if( template == null ) throw new IllegalArgumentException("You must supply a template to an page config");
		
		ModelConfiguration newModel = null;
		
		// Adjust to model to merge with app layer
		if( model != null && model.getAttributes() != null ) {
			List<ModelAttribute> adjustedModel = new ArrayList<ModelAttribute>();
			Iterator<ModelAttribute> it = model.getAttributes().iterator();
			while( it.hasNext() ) {
				ModelAttribute attr = it.next();
				if( appModel == null || appModel.mergeAttribute(attr) == null ) {
					if( attr.isAliasExpected() ) throw new IllegalArgumentException("The aittrubte [" + attr.getName() + "] in the [" + model.getName() + "] model expects to be aliased and is not!");
					adjustedModel.add(attr);
				}
			}
			
			if( adjustedModel.size() > 0 ) newModel = new ModelConfiguration(model.getName(), adjustedModel);
		}
		
		// Now initialise each window
		Map<String, WindowConfig> newWindows = new HashMap<String, WindowConfig>();
		Iterator<String> it = windows != null ? windows.keySet().iterator() : null;
		while( it != null && it.hasNext() ) {
			String k = it.next();
			WindowConfig w = windows.get(k);
			newWindows.put(k, w.init(appModel, newModel));
		}
		
		return new PageConfig(this.name, this.template, newModel, newWindows, events);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The pages name
	 */
	public void setName(String name) {
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
		this.name = name;
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	
	/**
	 * @param template The new template name
	 */
	public void setTemplate(String template) {
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
		this.template = template;
	}

	/**
	 * @return the model
	 */
	public ModelConfiguration getModel() {
		return model;
	}
	
	/**
	 * @param model The model to act upon
	 */
	public void setModel(ModelConfiguration model) {
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
		this.model = model;
	}
	
	/**
	 * Helper to get a window configuration from the app
	 * given its name. 
	 * 
	 * @param name The window to get
	 * @return The window
	 * @throws IllegalArgumentException if page is not known
	 */
	public WindowConfig getWindow(String name) {
		WindowConfig ret = windows != null ? windows.get(name) : null;
		if( ret == null ) throw new IllegalArgumentException("The window requested is not valid: " + name);
		return ret;
	}
	
	/**
	 * @return the windows
	 */
	public Collection<WindowConfig> getWindows() {
		return windows != null ? windows.values() : null;
	}
	
	/**
	 * Call to set the windows on the page config.
	 * 
	 * @param windows The windows to set
	 */
	public void setWindows(Collection<WindowConfig> windows) {
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
		if( windows == null || windows.size() == 0 ) throw new IllegalArgumentException("You have not created any windows in the " + name + " page");
		
		this.windows = new HashMap<String, WindowConfig>();
		Iterator<WindowConfig> it = windows.iterator();
		while( it.hasNext() ) {
			WindowConfig config = it.next();
			if( this.windows.containsKey(config.getName()) ) throw new IllegalArgumentException("You are inserting a window with the same name [" + config.getName() + "] twice into the page: " + name);
			this.windows.put(config.getName(), config);
		}
	}
	
	/**
	 * Call to set named windows on this page. The windows
	 * may be window configs or directly windows.
	 * 
	 * @param windows The windows to add
	 */
	public void setWindowMap(Map<String, Object> windows) {
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
		if( windows == null || windows.size() == 0 ) throw new IllegalArgumentException("You have not created any windows in the " + name + " page");
		
		this.windows = new HashMap<String, WindowConfig>();
		processWindows(windows);
	}
	
	/**
	 * Setter to add additional windows to the page.
	 * 
	 * @param windows The windows to add (either windows or window configs)
	 */
	public void setAdditionalWindows(Map<String, Object> windows) {
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
		if( this.windows == null ) this.windows = new HashMap<String, WindowConfig>();
		processWindows(windows);
	}
	
	/**
	 * Helper to process a map of possible windows and create
	 * {@link WindowConfig} instances out of them in this page.
	 */
	private void processWindows(Map<String, Object> windows) {
		Iterator<String> it = windows.keySet().iterator();
		while( it.hasNext() ) {
			String key = it.next();
			String name = key;
			String namespace = null;
			
			// Check for name=namespace notation
			int index = name.indexOf('=');
			if( index > 0 && index < (name.length() - 1) ) {
				namespace = name.substring(index + 1);
				name = name.substring(0, index);
			}
			
			if( this.windows.containsKey(name) ) throw new IllegalArgumentException("You are inserting a window with the same name [" + name + "] twice into the page: " + this.name);
			
			WindowConfig config = null;
			Object possibleWindow = windows.get(key);
			if( possibleWindow instanceof WindowConfig ) {
				config = (WindowConfig)possibleWindow;
			}
			else if( possibleWindow instanceof Window ) {
				config = new WindowConfig(name, (Window)possibleWindow);
			}
			else {
				Window window = WindowCompiler.getInstance().compileWindow(possibleWindow);
				config = new WindowConfig(name, window);
			}
			
			if( namespace != null ) config.setNamespace(namespace);
			this.windows.put(name, config);
		}
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
		if( initialised ) throw new IllegalArgumentException("You cannot change a page config once initialised");
		
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
		return "PageConfig [name=" + name + "]";
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
		result = prime * result
				+ ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((windows == null) ? 0 : windows.hashCode());
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
		PageConfig other = (PageConfig) obj;
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
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		// FUTURE: Compare windows, but need to see if same windows not on window
		return true;
	}
}
