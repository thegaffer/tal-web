package org.tpspencer.tal.mvc.controller.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.mvc.controller.ControllerAction;
import org.tpspencer.tal.mvc.controller.GenericController;
import org.tpspencer.tal.mvc.controller.ParameterBinding;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ErrorInput;
import org.tpspencer.tal.mvc.controller.annotations.ModelBindInput;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;

/**
 * This class compiles a GenericController given an annotated
 * class. The relevant annotations are: 
 * 
 * <ul>
 * <li>{@link Controller} - Indicates the class is a controller</li>
 * <li>{@link Action} - Indicates the method handles a particular 
 * subAction. A controller has at least one of these.</li>
 * <li>{@link BindInput} - Indicates a parameter to an action should
 * be bound from the input.</li>
 * <li>{@link ModelBindInput} - Indicates a parameter to an action should
 * be bound into a model attribute</li>
 * <li>{@link ModelInput} - Indicates a parameter to an action should
 * be taken from the model - or if no attribute should be the model.</li>
 * <li>{@link ErrorInput} - Indicates a parameter to an action should
 * be the current errors model object (shorthand for ModelInput(errors))</li>
 * </ul>
 * 
 * @author Tom Spencer
 */
public final class ControllerCompiler {
	/** The single instance of the compiler */
	private static final ControllerCompiler INSTANCE = new ControllerCompiler();
	
	/**
	 * @return The single controller compiler
	 */
	public static ControllerCompiler getCompiler() {
		return INSTANCE;
	}
	
	/** Holds the factories for the parameter bindings */
	private List<ParameterBindingFactory> bindingFactories;
	
	/**
	 * Hidden constructor
	 */
	private ControllerCompiler() {
		this.bindingFactories = new ArrayList<ParameterBindingFactory>();
		this.bindingFactories.add(new BindingParameterFactory());
		this.bindingFactories.add(new ModelParameterFactory());
		this.bindingFactories.add(new InputParameterFactory());
	}
	
	/**
	 * Call to set the binding factories (overwriting any default ones)
	 * 
	 * @param bindingFactories The factories
	 */
	public void setBindingFactories(List<ParameterBindingFactory> bindingFactories) {
		this.bindingFactories = bindingFactories;
	}
	
	/**
	 * Call to add a set of binding factories (preserving any default ones)
	 * 
	 * @param bindingFactories The factories
	 */
	public void setExtraBindingFactories(List<ParameterBindingFactory> bindingFactories) {
		this.bindingFactories.addAll(bindingFactories);
	}
	
	/**
	 * Call to compiler and create the generic controller
	 * 
	 * @param possibleController The possible controller
	 * @return The generic controller
	 */
	public GenericController compile(Object possibleController) {
		GenericController ret = null;
		
		Class<?> ctrlClass = determineController(possibleController);
		Controller ctrl = ctrlClass.getAnnotation(Controller.class);
		if( ctrl == null ) throw new IllegalArgumentException("Cannot create generic controller because controller is not annotated as such: " + possibleController);
		
		// Extract params from the annotation
		String subActionParameter = getString(ctrl.subActionParameter());
		String errorsAttribute = getString(ctrl.errorAttribute());
		
		// Create the new controller
		ret = new GenericController();
		ret.setController(possibleController);
		ret.setSubActionParameter(subActionParameter);
		ret.setErrorsModelAttribute(errorsAttribute);
		
		// Get the actions
		Map<String, ControllerAction> actions = getActions(possibleController, ctrlClass, errorsAttribute);
		if( actions != null ) ret.setActions(actions);
		
		ControllerAction defaultAction = getDefaultAction(possibleController, ctrlClass, errorsAttribute);
		if( defaultAction != null ) ret.setDefaultAction(defaultAction);
		
		// Initialise and return
		ret.init();
		return ret;
	}
	
	/**
	 * Helper to determine the class to use when processing as a controller.
	 * Annotations may be on an interface rather than the class itself.
	 */
	private Class<?> determineController(Object possibleController) {
		Class<?> ctrlClass = possibleController.getClass();
		if( ctrlClass.getAnnotation(org.tpspencer.tal.mvc.controller.annotations.Controller.class) != null ) return ctrlClass;
		
		// Otherwise check interfaces, return first one with the controller annotation
		Class<?>[] interfaces = possibleController.getClass().getInterfaces();
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
	 * Helper to get all named actions in the controller. i.e.
	 * all methods marked with the {@link Action} annotation and
	 * which have an (sub) action name with them.
	 */
	private Map<String, ControllerAction> getActions(Object possibleController, Class<?> controllerClass, String errorsAttribute) {
		Map<String, ControllerAction> ret = null;
		
		Method[] methods = controllerClass.getMethods();
		if( methods == null || methods.length == 0 ) throw new IllegalArgumentException("The controller has no methods: " + controllerClass);
		for( int i = 0 ; i < methods.length ; i++ ) {
			Action action = methods[i].getAnnotation(Action.class);
			if( action != null ) {
				String actionName = getString(action.action());
				
				if( actionName != null ) {
					ControllerAction ca = createAction(possibleController, controllerClass, errorsAttribute, action, methods[i]);
					if( ret == null ) ret = new HashMap<String, ControllerAction>();
					ret.put(actionName, ca);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Gets the default action for the controller. This is a method
	 * marked with the {@link Action} annotation and with no action
	 * name. There can only be 1 of these in a controller.
	 */
	private ControllerAction getDefaultAction(Object possibleController, Class<?> controllerClass, String errorsAttribute) {
		ControllerAction ret = null;
		
		Method[] methods = controllerClass.getMethods();
		if( methods == null || methods.length == 0 ) throw new IllegalArgumentException("The controller has no methods: " + controllerClass);
		for( int i = 0 ; i < methods.length ; i++ ) {
			Action action = methods[i].getAnnotation(Action.class);
			if( action != null ) {
				String actionName = getString(action.action());
				
				if( actionName == null ) {
					if( ret != null ) throw new IllegalArgumentException("There are two or more default actions in the controller (actions with no action name set): " + possibleController);
					ret = createAction(possibleController, controllerClass, errorsAttribute, action, methods[i]);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Internal helper that actually creates the ControllerAction instance.
	 */
	private ControllerAction createAction(Object controller, Class<?> controllerClass, String errorsAttribute, Action action, Method method) {
		// Map the parameters (if any)
		ParameterBinding[] bindings = null;
		Class<?>[] params = method.getParameterTypes();
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		if( params != null && params.length > 0 ) {
			bindings = new ParameterBinding[params.length];
			for( int i = 0 ; i < params.length ; i++ ) {
				Iterator<ParameterBindingFactory> it = bindingFactories.iterator();
				while( bindings[i] == null && it.hasNext() ) {
					bindings[i] = it.next().getBinding(controller, controllerClass, method, params[i], paramAnnotations[i]);
				}
			}
		}
		
		Method validationMethod = determineValidationMethod(controller, action, method);
		
		ControllerAction ret = new ControllerAction(
				controller, 
				errorsAttribute,
				method, 
				validationMethod, 
				getString(action.result()), 
				getString(action.errorResult()), 
				bindings);
		
		return ret;
	}
	
	/**
	 * Helper to determine the validation method to use
	 */
	private Method determineValidationMethod(Object controller, Action action, Method method) {
		String validationMethod = getString(action.validationMethod());
		if( validationMethod == null ) return null;
		
		try {
			Method m = controller.getClass().getMethod(validationMethod, method.getParameterTypes());
			if( !List.class.isAssignableFrom(m.getReturnType()) ) throw new IllegalArgumentException("The controller action [" + method.getName() + "] validation method [" + validationMethod + "] cannot be found with a return type if java.util.List");
			return m;
		}
		catch( NoSuchMethodException e ) {
			throw new IllegalArgumentException("The controller action [" + method.getName() + "] validation method [" + validationMethod + "] cannot be found with matching parameters", e);
		}
	}
	
	/**
	 * Helper to ensure a string is null if empty
	 */
	private String getString(String val) {
		String ret = val;
		if( val != null && val.length() == 0 ) ret = null;
		return ret;
	}
}
