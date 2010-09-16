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

package org.tpspencer.tal.mvc.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.Window;
import org.tpspencer.tal.mvc.config.EventConfig;
import org.tpspencer.tal.mvc.controller.GenericController;
import org.tpspencer.tal.mvc.controller.compiler.ControllerCompiler;
import org.tpspencer.tal.mvc.input.InputModel;
import org.tpspencer.tal.mvc.model.ConfigModelAttribute;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.model.ResolvedModelAttribute;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;
import org.tpspencer.tal.mvc.model.SimpleModelResolver;
import org.tpspencer.tal.mvc.model.message.MessagesModelAttribute;
import org.tpspencer.tal.util.aspects.annotations.Trace;

/**
 * This abstract class is the base for the internal
 * window implementations. It supports the concept
 * of a default view that is returned. The points
 * where another class should override are:
 * 
 * <ul>
 * <li>getCurrentState - If this is dynamic, otherwise
 * only the default view is returned</li>
 * <li>processAction - If the window supports registering
 * actions against views then it should override to
 * get hold of those.</li>
 * </ul>
 * 
 * <p>There is no validation on the settings for a 
 * BaseWindow to allow derived classes maximum 
 * flexibility in determining the behaviour</p>
 * 
 * @author Tom Spencer
 */
@Trace
public abstract class BaseWindow implements Window {
	/** The name of the window */
	private String name = null;
	/** Holds the default view */
	private View defaultView = null;
	/** Holds the list of base controllers */
	private Map<String, Controller> controllers = null;
	/** Holds the model for the window */
	private ModelConfiguration model = null;
	/** Holds the optional list of events this window receives */
	private List<EventConfig> events = null;
	
	/** The name of the errors model attribute, default is errors */
	private String errorsAttribute = "errors";
	/** The name of the messages attribute, default is messages */
	private String messagesAttribute = "messages";
	/** The name of the warnings attribute, default is warnings */
	private String warningsAttribute = "warnings";
	
	public void init() {
		if( defaultView == null ) throw new IllegalArgumentException("A window must have at least a default view");
		if( model == null && name == null ) throw new IllegalArgumentException("A window must have a name if it does not have a model");
		
		// Setup model if not set and mandatory attrs for view are required
		List<ModelAttribute> extraAttrs = initModelAttributes();
		if( extraAttrs != null ) {
			if( model != null ) extraAttrs.addAll(model.getAttributes());
			String name = model != null ? model.getName() : this.name + "Model";
			
			model = new ModelConfiguration(name, extraAttrs);
		}
	}
	
	/**
	 * This method gives this class (and its derived class a 
	 * chance to add on any required attributes that do not
	 * exist in the model as configured.
	 * 
	 * @return A list containing any attributes to add (or null)
	 */
	protected List<ModelAttribute> initModelAttributes() {
		boolean errorsExist = model != null ? model.hasAttribute(errorsAttribute) : false;
		boolean warningsExist = model != null ? model.hasAttribute(warningsAttribute) : false;
		boolean messagesExist = model != null ? model.hasAttribute(messagesAttribute) : false;
	
		List<ModelAttribute> ret = null;
		if( !errorsExist || !warningsExist || !messagesExist ) {
			ret = new ArrayList<ModelAttribute>();
			
			if( !errorsExist ) ret.add(new MessagesModelAttribute(errorsAttribute));
			if( !warningsExist ) ret.add(new MessagesModelAttribute(warningsAttribute));
			if( !messagesExist ) ret.add(new MessagesModelAttribute(messagesAttribute));
		}
		return ret;
	}
	
	/**
	 * Adds a model attribute prior to initialisation
	 * 
	 * @param name The name of the attribute
	 * @param expected Its expected type
	 * @param defValue The default value
	 */
	protected void addModelAttr(String name, Class<?> expected, Object defValue) {
		SimpleModelAttribute attr = new SimpleModelAttribute(name, expected);
		if( defValue != null ) attr.setDefaultValue(defValue);
		if( model == null ) model = new ModelConfiguration(this.name, new ArrayList<ModelAttribute>());
		model.addAttribute(attr);
	}
	
	/**
	 * Adds a model attribute prior to initialisation.
	 * This method works out if it is a normal or Config
	 * resolver passed in.
	 * 
	 * @param name The name of the attribute
	 * @param resolverParamDefault The resolver (mandatory), parameter and defValue for attribute
	 */
	protected void addModelAttr(String name, Object... resolverParamDefault) {
		Object resolver = resolverParamDefault[0];
		Object param = resolverParamDefault.length > 1 ? resolverParamDefault[1] : null;
		Object defValue = resolverParamDefault.length > 2 ? resolverParamDefault[2] : null;
		
		ModelAttribute attr = null;
		if( resolver instanceof ModelResolver ) {
			attr = new ResolvedModelAttribute(name, (ModelResolver)resolver, param, defValue);
		}
		else if( resolver instanceof SimpleModelResolver ) {
			attr = new ConfigModelAttribute(name, (SimpleModelResolver)resolver, param, defValue);
		}
		
		if( attr == null ) throw new IllegalArgumentException("Cannot determine the resolver for model attribute: " + resolverParamDefault);
		if( model == null ) model = new ModelConfiguration(this.name, new ArrayList<ModelAttribute>());
		model.addAttribute(attr);
	}
	
	/**
	 * Sets the default view prior to initialisation
	 * 
	 * TODO: Does not have to be a actual view, annotations!?!
	 * 
	 * @param view The view to add
	 */
	protected void setView(View view) {
		this.defaultView = view;
	}
	
	/**
	 * Adds a controller prior to initialisation
	 * 
	 * @param action The action to use controller for
	 * @param controller The controller
	 */
	protected void addController(String action, Object controller) {
		Controller ctrl = null;
		if( controller instanceof Controller ) ctrl = (Controller)controller;
		else {
			ctrl = ControllerCompiler.getCompiler().compile(controller);
		}
		
		if( this.controllers == null ) this.controllers = new HashMap<String, Controller>(); 
		this.controllers.put(action, ctrl);
	}
	
	/**
	 * Helper to add an event to the window.
	 * 
	 * @param attr The attribute we are listening on
	 * @param action The action to invoke
	 * @param newValueName The name of the parameter to action holding new value
	 * @param oldValueName The name of the parameter to action holding old value
	 */
	protected void addEvent(String attr, String action, String newValueName, String oldValueName) {
		if( "".equals(newValueName) ) newValueName = null;
		if( "".equals(oldValueName) ) oldValueName = null;
		
		EventConfig event = new EventConfig(attr, action, newValueName, oldValueName);
		if( this.events == null ) this.events = new ArrayList<EventConfig>();
		this.events.add(event);
	}
	
	/**
	 * Returns the model
	 */
	public ModelConfiguration getModel() {
		return model;
	}
	
	/**
	 * Always returns the single view
	 */
	public View getCurrentState(Model model) {
		return defaultView;
	}
	
	/**
	 * Looks up action in view and gets it to perform
	 * the action.
	 * 
	 * @throws IllegalArgumentException if action in unrecognised
	 */
	public String processAction(Model model, InputModel input, String action) {
		if( controllers == null ) throw new UnsupportedOperationException("Cannot fire actions into a simple window with no mappings"); 
		Controller ctrl = controllers.get(action);
		if( ctrl == null ) throw new IllegalArgumentException("No controller for action: " + action);
		
		return ctrl.performAction(model, input);
	}

	/**
	 * @return the defaultView
	 */
	public View getDefaultView() {
		return defaultView;
	}

	/**
	 * @param defaultView the defaultView to set
	 */
	public void setDefaultView(View defaultView) {
		this.defaultView = defaultView;
	}

	/**
	 * @return the controllers
	 */
	public Map<String, Controller> getControllers() {
		return controllers;
	}

	/**
	 * @param controllers the controllers to set
	 */
	public void setControllers(Map<String, Object> controllers) {
		this.controllers = null;
		if( controllers == null || controllers.size() == 0 ) return;
		
		this.controllers = new HashMap<String, Controller>(controllers.size());
		Iterator<String> it = controllers.keySet().iterator();
		while(it.hasNext()) {
			String k = it.next();
			Object v = controllers.get(k);
			
			if( v instanceof Controller ) this.controllers.put(k, (Controller)v);
			else {
				GenericController ctrl = ControllerCompiler.getCompiler().compile(v);
				ctrl.init();
				this.controllers.put(k, ctrl);
			}
		}
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(ModelConfiguration model) {
		this.model = model;
	}

	/**
	 * @return the events
	 */
	public List<EventConfig> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<EventConfig> events) {
		this.events = events;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the errorsAttribute
	 */
	public String getErrorsAttribute() {
		return errorsAttribute;
	}

	/**
	 * @param errorsAttribute the errorsAttribute to set
	 */
	public void setErrorsAttribute(String errorsAttribute) {
		this.errorsAttribute = errorsAttribute;
	}

	/**
	 * @return the messagesAttribute
	 */
	public String getMessagesAttribute() {
		return messagesAttribute;
	}

	/**
	 * @param messagesAttribute the messagesAttribute to set
	 */
	public void setMessagesAttribute(String messagesAttribute) {
		this.messagesAttribute = messagesAttribute;
	}

	/**
	 * @return the warningsAttribute
	 */
	public String getWarningsAttribute() {
		return warningsAttribute;
	}

	/**
	 * @param warningsAttribute the warningsAttribute to set
	 */
	public void setWarningsAttribute(String warningsAttribute) {
		this.warningsAttribute = warningsAttribute;
	}
}

