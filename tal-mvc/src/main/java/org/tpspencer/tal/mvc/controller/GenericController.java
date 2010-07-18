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

package org.tpspencer.tal.mvc.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This generic controller is used by in-built MVC window
 * implementations when you try to use a class that does
 * not derive from Controller as a controller. It can also
 * be setup manually if desired.
 * 
 * <p>The {@link GenericController} uses the MVC controller
 * annotations to determine which method to call on the real
 * controller and how to bind the parameters to it.</p>
 * 
 * @author Tom Spencer
 */
public final class GenericController implements Controller {

	/** Holds the actual controller */
	private Object controller = null;
	/** Holds the name of the parameter holding the subAction */
	private String subActionParameter = null;
	/** Holds the binder that will bind any bind input parameter */
	private InputBinder binder = null;
	/** The name of the attribute in model to store errors within */
	private String errorsModelAttribute = "errors";
	/** Holds controller method to use as the default */
	private ControllerAction defaultAction = null;
	/** Holds any other controller methods */
	private Map<String, ControllerAction> actions = null;
	
	/**
	 * Constructs a generic controller pointing to given controller and
	 * initialises is in one step. The controller must have the 
	 * {@link org.tpspencer.tal.mvc.controller.annotations.Controller}
	 * annotation and have at least one {@link Action} annotated method.
	 * 
	 * @param controller The controller
	 */
	public GenericController(Object controller) {
		this.controller = controller;
	}

	/**
	 * Separate init method (separated out to allow other ways to
	 * create and initialise the class later).
	 */
	public void init() {
		if( controller == null ) throw new IllegalArgumentException("A controller must be present on the GenericController");
		
		Class<?> ctrlClass = determineController();
		org.tpspencer.tal.mvc.controller.annotations.Controller ctrl = ctrlClass != null ? ctrlClass.getAnnotation(org.tpspencer.tal.mvc.controller.annotations.Controller.class) : null;
		
		// Setup from annotation
		if( ctrl != null ) {
			// Basic parameters
			subActionParameter = getAnnotationString(ctrl.subActionParameter());
			errorsModelAttribute = getAnnotationString(ctrl.errorAttribute());
			
			// Setup binder
			if( ctrl.binderType() != null && !InputBinder.class.equals(ctrl.binderType()) ) {
				try {
					binder = (InputBinder)ctrl.binderType().newInstance();
				}
				catch( Exception e ) {
					throw new IllegalArgumentException("Cannot create the input binder specified in controller annotation: " + ctrl.binderType(), e);
				}
			}
			
			// Find all actions
			Method[] methods = ctrlClass.getMethods();
			if( methods == null || methods.length == 0 ) throw new IllegalArgumentException("The controller has no methods: " + ctrlClass);
			for( int i = 0 ; i < methods.length ; i++ ) {
				Action action = methods[i].getAnnotation(Action.class);
				if( action != null ) {
					if( "".equals(action.action()) ) {
						if( defaultAction != null ) throw new IllegalArgumentException("There are multiple methods on the controller marked as action with no action - only 1 can be the default");
						defaultAction = new ControllerAction(this, methods[i]);
					}
					else {
						if( actions == null ) actions = new HashMap<String, ControllerAction>();
						ControllerAction controllerAction = new ControllerAction(this, methods[i]);
						actions.put(action.action(), controllerAction);
					}
				}
			}
		}
		
		// Find any methods beginning "submit" and use this
		else {
			Method[] methods = ctrlClass.getMethods();
			if( methods == null || methods.length == 0 ) throw new IllegalArgumentException("The controller has no methods: " + ctrlClass);
			for( int i = 0 ; i < methods.length ; i++ ) {
				String result = methods[i].getName() + "Ok";
				String errorResult = methods[i].getName() + "Fail";
				
				// Default action
				if( methods[i].getName().equals("submit") ) {
					defaultAction = new ControllerAction(this, result, errorResult, methods[i].getName(), "validate");
				}
				
				// Sub actions
				else if( methods[i].getName().startsWith("submit") ) {
					String name = methods[i].getName().substring(6);
					String key = (name.length() > 1) ? name.substring(0, 1).toLowerCase() + name.substring(1) : name.toLowerCase();
					
					if( actions == null ) actions = new HashMap<String, ControllerAction>();
					actions.put(key, new ControllerAction(this, result, errorResult, methods[i].getName(), "validate" + name));
				}
			}
			
			// If there is only 1 method, make it the default
			if( defaultAction == null && actions != null && actions.size() == 1 ) {
				defaultAction = actions.get(actions.keySet().iterator().next());
				actions = null;
			}
		}
		
		// Check everything
		if( errorsModelAttribute == null ) throw new IllegalArgumentException("You must set the name of the errors attribute on the generic controller, either manually or through the controller annotation");
		if( defaultAction == null && (actions == null || actions.size() == 0) ) {
			throw new IllegalArgumentException("The controller does not appeat to have any methods marked as Action: " + controller);
		}
	}
	
	/**
	 * Internal helper to find the class, class or interface, that is
	 * marked as a controller. This is the one we should use.
	 * 
	 * @return The controller class 
	 */
	private Class<?> determineController() {
		Class<?> ctrlClass = controller.getClass();
		if( ctrlClass.getAnnotation(org.tpspencer.tal.mvc.controller.annotations.Controller.class) != null ) return ctrlClass;
		
		// Otherwise check interfaces, return first one with the controller annotation
		Class<?>[] interfaces = controller.getClass().getInterfaces();
		if( interfaces != null ) {
			for( int i = 0 ; i < interfaces.length ; i++ ) {
				if( interfaces[i].getAnnotation(org.tpspencer.tal.mvc.controller.annotations.Controller.class) != null ) {
					return interfaces[i];
				}
			}
		}
		
		// Just return the controller itself
		return ctrlClass;
	}
	
	/**
	 * Finds the action method and calls it
	 */
	public String performAction(Model model, InputModel input) {
		String subAction = determineSubAction(input);
		ControllerAction action = subAction != null ? actions.get(subAction) : defaultAction;
		
		if( action == null ) {
			throw new NoActionException("No subaction found in input: " + input);
		}
		
		String ret = action.invokeAction(model, input, errorsModelAttribute, binder, controller);
		
		return ret;
	}

	/**
	 * Internal method to get the subAction. The subAction will
	 * be null unless the subAction is determined and matches
	 * to a controller.
	 * 
	 * @param input The input model
	 * @return The subAction.
	 */
	private String determineSubAction(InputModel input) {
		String ret = null;
		if( actions == null ) return ret;
		
		if( subActionParameter != null ) {
			ret = input.getParameter(subActionParameter);
			if( ret != null && ret.toString().equals("") ) ret = null;
			if( ret != null && !actions.containsKey(ret) ) ret = null;
		}
		else {
			Iterator<String> it = actions.keySet().iterator();
			while( it.hasNext() ) {
				String subAction = it.next();
				if( input.hasParameter(subAction) ) {
					ret = subAction;
					break;
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * @return Null if val is empty string, otherwise val
	 */
	private String getAnnotationString(String val) {
		if( val != null && val.length() == 0 ) return null;
		return val;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("GenericController: ");
		buf.append("ctrl=").append(controller);
		if( subActionParameter != null ) buf.append(", subActionParam=").append(subActionParameter);
		if( binder != null ) buf.append(", binder=").append(binder);
		if( errorsModelAttribute != null ) buf.append(", errorsModel=").append(errorsModelAttribute);
		if( defaultAction != null ) buf.append(", action=").append(defaultAction);
		if( actions != null ) buf.append(", actions={").append(actions).append("}");
		return buf.toString();
	}

	/**
	 * @return the controller
	 */
	public Object getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(Object controller) {
		this.controller = controller;
	}

	/**
	 * @return the subActionParameter
	 */
	public String getSubActionParameter() {
		return subActionParameter;
	}

	/**
	 * @param subActionParameter the subActionParameter to set
	 */
	public void setSubActionParameter(String subActionParameter) {
		this.subActionParameter = subActionParameter;
	}

	/**
	 * @return the binder
	 */
	public InputBinder getBinder() {
		return binder;
	}

	/**
	 * @param binder the binder to set
	 */
	public void setBinder(InputBinder binder) {
		this.binder = binder;
	}

	/**
	 * @return the errorsModelAttribute
	 */
	public String getErrorsModelAttribute() {
		return errorsModelAttribute;
	}

	/**
	 * @param errorsModelAttribute the errorsModelAttribute to set
	 */
	public void setErrorsModelAttribute(String errorsModelAttribute) {
		this.errorsModelAttribute = errorsModelAttribute;
	}

	/**
	 * @return the defaultAction
	 */
	public ControllerAction getDefaultAction() {
		return defaultAction;
	}

	/**
	 * @param defaultAction the defaultAction to set
	 */
	public void setDefaultAction(ControllerAction defaultAction) {
		this.defaultAction = defaultAction;
	}

	/**
	 * @return the actions
	 */
	public Map<String, ControllerAction> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(Map<String, ControllerAction> actions) {
		this.actions = actions;
	}
}
