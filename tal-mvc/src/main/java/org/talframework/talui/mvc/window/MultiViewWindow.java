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

package org.talframework.talui.mvc.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.input.InputModel;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.SimpleModelAttribute;
import org.tpspencer.tal.util.aspects.annotations.Trace;

/**
 * This window supports the use of a multiple views.
 * To determine which view to use a model attribute
 * is required, which defaults to "state". This attribute
 * is updated whenever an action is performed by 
 * matching the result to a view name. When using
 * this kind of window the following is required:
 * 
 * <ul>
 * <li>A window model is required with the state attr</li>
 * <li>A set of views and their internal names is required</li>
 * <li>A set of action results to view mappings is required</li>
 * <li>These mappings must point to valid views (by name)</li>
 * <li>A default view is required initially</li>
 * <li>A set of controllers is mandatory</li>
 * </ul>
 * 
 * @author Tom Spencer
 */
@Trace
public class MultiViewWindow extends BaseWindow {

	/** Holds the additional views in this window */
	private Map<String, View> views;
	/** Holds the action mappings for the view */
	private Map<String, String> actionMappings;
	
	/** Holds the name of state attribute for this view, default is state */
	private String stateAttribute = "state"; 
	
	@Override
	public void init() {
		if( views == null || views.size() == 0 ) throw new IllegalArgumentException("No views supplied to multi view window");
		if( getControllers() == null || getControllers().size() == 0 ) throw new IllegalArgumentException("No controllers supplied to multi view window");
		if( actionMappings == null || actionMappings.size() == 0 ) throw new IllegalArgumentException("No action mappings supplied to multi view window");
		super.init();
		
		// Check action mappings map to a valid view
		Iterator<String> it = actionMappings.values().iterator();
		while( it.hasNext() ) {
			String view = it.next();
			if( views.get(view) == null )
				throw new IllegalArgumentException("Action mapping does not map to a valid view: " + view);
		}
	}
	
	/**
	 * Adds on the state attribute if it does not exist
	 */
	@Override
	protected List<ModelAttribute> initModelAttributes() {
		List<ModelAttribute> ret = super.initModelAttributes();
		
		if( getModel() == null || !getModel().hasAttribute(stateAttribute) ) {
			if( ret == null ) ret = new ArrayList<ModelAttribute>();
			ret.add(new SimpleModelAttribute(stateAttribute));
		}
		
		return ret;
	}
	
	/**
	 * Adds a view to the window prior to initialisation
	 * 
	 * @param name
	 * @param view
	 */
	public void addView(String name, View view) {
		if( getDefaultView() == null ) setDefaultView(view);
		if( views == null ) views = new HashMap<String, View>();
		views.put(name, view);
	}
	
	/**
	 * Adds a mapping between an action result and a view
	 * 
	 * @param name The result
	 * @param view The name of the view to go to
	 */
	public void addMapping(String name, String view) {
		if( actionMappings == null ) actionMappings = new HashMap<String, String>();
		actionMappings.put(name, view);
	}
	
	/**
	 * Adds a mapping between an action result and a view
	 * 
	 * @param name The result
	 * @param view The view to go to
	 */
	public void addMapping(String name, View view) {
		String viewName = null;
		
		if( views != null ) {
			Iterator<String> it = views.keySet().iterator();
			while( it.hasNext() && viewName == null ) {
				String n = it.next();
				View v = views.get(n);
				if( v.equals(view) ) viewName = n;
			}
		}
		
		if( viewName == null ) throw new IllegalArgumentException("Cannot add a mapping to a view when view is not held by window");
		addMapping(name, viewName);
	}
	
	/**
	 * Overridden to get view based on state model attr
	 */
	@Override
	public View getCurrentState(Model model) {
		String viewName = (String)model.getAttribute(stateAttribute);
		if( viewName == null ) return super.getCurrentState(model);
		else return views.get(viewName);
	}
	
	/**
	 * Overridden to update state based on result and to enter/exit
	 * state
	 */
	@Override
	public String processAction(Model model, InputModel input, String action) {
		String result = super.processAction(model, input, action);
		if( actionMappings.containsKey(result) ) {
			model.setAttribute(stateAttribute, actionMappings.get(result));
		}
		
		return result;
	}
	
	///////////////////////////////////////
	// Getters/Setters

	/**
	 * @return the views
	 */
	public Map<String, View> getViews() {
		return views;
	}

	/**
	 * @param views the views to set
	 */
	public void setViews(Map<String, View> views) {
		this.views = views;
	}

	/**
	 * @return the actionMappings
	 */
	public Map<String, String> getActionMappings() {
		return actionMappings;
	}

	/**
	 * @param actionMappings the actionMappings to set
	 */
	public void setActionMappings(Map<String, String> actionMappings) {
		this.actionMappings = actionMappings;
	}

	/**
	 * @return the stateAttribute
	 */
	public String getStateAttribute() {
		return stateAttribute;
	}

	/**
	 * @param stateAttribute the stateAttribute to set
	 */
	public void setStateAttribute(String stateAttribute) {
		this.stateAttribute = stateAttribute;
	}
}
