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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.controller.annotations.ErrorInput;
import org.tpspencer.tal.mvc.controller.annotations.Input;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This class represents an action to perform as part
 * of a controller. A controller action can be configured
 * or it can be determined from annotations.
 * 
 * @author Tom Spencer
 */
final class ControllerAction {

	/** The result to result to return if the controller doesn't return anything */
	private final String result;
	/** The error result to return if there are validation errors */
	private final String errorResult;
	/** The method to invoke */
	private final Method method;
	/** The validation method to invoke (if there is one) */
	private final Method validationMethod;
	/** The parameters - which are also parameters to the validate method */
	private final ParameterBinding[] parameters;
	
	/**
	 * Constructor for use when there is no GenericController. This
	 * removes the ability to bind attributes and to validate
	 * 
	 * @param m The method action should be based on
	 */
	public ControllerAction(Method m) {
		this.method = m;
		
		Action action = this.method.getAnnotation(Action.class);
		this.result = action != null ? getAnnotationString(action.result()) : "ok";
		this.errorResult = action != null ? getAnnotationString(action.errorResult()) : "fail";
		 
		this.parameters = determineParameters(null);
		this.validationMethod = null;
	}
	
	/**
	 * Constructor used when setting up the action 
	 * from an annotated controller
	 * 
	 * @param controller The generic controller this action is attached to
	 * @param m The method on that controller representing this action (the method as an Action annotation)
	 */
	public ControllerAction(GenericController controller, Method m) {
		this.method = m;
		
		Action action = this.method.getAnnotation(Action.class);
		this.result = getAnnotationString(action.result());
		this.errorResult = getAnnotationString(action.errorResult());

		this.parameters = determineParameters(controller);
		this.validationMethod = determineValidationMethod(controller.getController(), action.validationMethod(), this.method.getParameterTypes(), false);
	}
	
	/**
	 * Constructor used when manually setting up the
	 * action.
	 * 
	 * @param controller The controller we are bound to 
	 * @param result The result
	 * @param errorResult The error result (returned if errors on bind or validation)
	 * @param method The name of the method for this action
	 * @param validationMethod The name of the validation method (if there is one)
	 * @param binder The binder to use for any unknown parameters on the input
	 */
	public ControllerAction(GenericController controller, String result, String errorResult, String method, String validationMethod) {
		this.result = result;
		this.errorResult = errorResult;
		
		Method m = null;
		Method[] methods = controller.getController().getClass().getMethods();
		if( methods != null ) {
			for( int i = 0 ; i < methods.length ; i++ ) {
				if( methods[i].getName().equals(method) ) {
					m = methods[i];
					break;
				}
			}
		}
		if( m == null ) throw new IllegalArgumentException("The method [" + method + "] cannot be found on the controller: " + controller);
		this.method = m;
		
		this.parameters = determineParameters(controller);
		this.validationMethod = determineValidationMethod(controller.getController(), validationMethod, this.method.getParameterTypes(), true);
	}
	
	/**
	 * Internal helper to get an array of parameter bindings for
	 * the methods parameters.
	 * 
	 * @param controller The controller
	 * @return The parameter bindings
	 */
	private ParameterBinding[] determineParameters(GenericController controller) {
		Class<?>[] paramTypes = this.method.getParameterTypes();
		if( paramTypes != null && paramTypes.length > 0 ) {
			ParameterBinding[] params = new ParameterBinding[paramTypes.length];
			Annotation[][] paramAnnotations = this.method.getParameterAnnotations();
			
			for( int i = 0 ; i < paramTypes.length ; i++ ) {
				params[i] = findParameterType(paramTypes[i], paramAnnotations[i], controller != null ? controller.getBinder() : null);
			}
			
			return params;
		}
		
		return null;
	}
	
	/**
	 * Helper to create the appropriate parameter binding for the
	 * parameter.
	 * 
	 * @param expected The type of the parameter
	 * @param annotations The annotations of the parameter
	 * @return The parameter binding
	 */
	private ParameterBinding findParameterType(Class<?> expected, Annotation[] annotations, InputBinder binder) {
		if( annotations == null ) return new NullBinding();
		
		ParameterBinding ret = null;
		for( int i = 0 ; i < annotations.length ; i++ ) {
			if( annotations[i].annotationType().equals(ModelInput.class) ) {
				ret = new ModelBinding();
				break;
			}
			else if( annotations[i].annotationType().equals(Input.class) ) {
				ret = new InputBinding(expected.isAssignableFrom(Map.class));
				break;
			}
			else if( annotations[i].annotationType().equals(ErrorInput.class) ) {
				if( !List.class.isAssignableFrom(expected) ) throw new IllegalArgumentException("Cannot bind to parameter as errors if not assignable from a List");
				ret = new ErrorsBinding();
				break;
			}
			else if( annotations[i].annotationType().equals(BindInput.class) ) {
				if( binder != null && !binder.supports(expected) ) throw new IllegalArgumentException("The binder [" + binder + "] does not support the parameter: " + expected);
				
				BindInput input = (BindInput)annotations[i];
				String prefix = getAnnotationString(input.prefix());
				String modelAttribute = getAnnotationString(input.modelAttribute());
				Class<?> type = input.type();
				if( Object.class.equals(type) ) type = expected;
				
				ret = new ArbitraryBinding(prefix, modelAttribute, type);
				break;
			}
		}
		
		// If there is no attribute, then determine based on type alone
		if( ret == null ) {
			if( expected.isAssignableFrom(InputModel.class) ) {
				ret = new InputBinding(false);
			}
			else if( expected.isAssignableFrom(List.class) ) {
				ret = new ErrorsBinding();
			}
			else if( expected.isAssignableFrom(Model.class) ) {
				ret = new ModelBinding();
			}
			else {
				if( binder == null || !binder.supports(expected) ) throw new IllegalArgumentException("The binder [" + binder + "] does not support the parameter: " + expected);
				ret = new ArbitraryBinding(null, null, expected);
			}
		}
		
		return ret;
	}
	
	/**
	 * Helper to determine the validation method. Currently the validation
	 * method must match the method it is validating in terms of parameters.
	 * And its return type must a list (of errors).
	 * 
	 * TODO: Does a validation method have to return - could it not return a string also??
	 * 
	 * @param controller The controller
	 * @param validationMethod The validation methods name
	 * @param paramTypes The parameters expected on the method
	 * @param failOk If true then failing to find the method is not considered an error
	 * @return The validation method (if applicable, null otherwise)
	 */
	private Method determineValidationMethod(Object controller, String validationMethod, Class<?>[] paramTypes, boolean failOk) {
		if( validationMethod == null || "".equals(validationMethod) ) return null;
		
		try {
			Method m = controller.getClass().getMethod(validationMethod, paramTypes);
			if( !List.class.isAssignableFrom(m.getReturnType()) ) throw new IllegalArgumentException("The controller action [" + this.method.getName() + "] validation method [" + validationMethod + "] cannot be found with a return type if java.util.List");
			return m;
		}
		catch( NoSuchMethodException e ) {
			if( failOk ) return null;
			else throw new IllegalArgumentException("The controller action [" + this.method.getName() + "] validation method [" + validationMethod + "] cannot be found with matching parameters", e);
		}
	}
	
	
	/**
	 * Call to invoke the action. The events occur as follows:
	 * 
	 * <ul>
	 * <li>Bind all parameters, if errors return errorResult
	 * <li>Call the validate method if there is one, if errors return errorResult
	 * <li>Invoke the action, if it doesn't return a result return the set result
	 * </ul>
	 * 
	 * @param model
	 * @param input
	 * @param errors
	 * @param binder
	 * @param target
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String invokeAction(Model model, InputModel input, String errorsAttribute, InputBinder binder, Object target) {
		Object ret = null;
		
		// a. Bind parameters
		Object[] args = null;
		if( parameters != null ) {
			args = new Object[parameters.length];
			for( int i = 0 ; i < parameters.length ; i++ ) {
				args[i] = parameters[i].bind(model, input, errorsAttribute, binder);
			}
		}
		
		// b. Validate if applicable (and check errors)
		if( validationMethod != null ) {
			List<Object> errors = null;
			try {
				errors = (List<Object>)validationMethod.invoke(target, args);
			}
			catch( RuntimeException e ) {
				throw e;
			}
			catch( Exception e ) {
				throw new ActionPerformException("Cannot invoke the validation method: " + method.getName(), e);
			}
		
			// Test for errors and add them if applicable
			if( errors != null && errors.size() > 0 ) model.setAttribute(errorsAttribute, errors);
		}
		
		// c. Check any errors during bind and validate
		if( model.containsKey(errorsAttribute) && 
				model.getAttribute(errorsAttribute) != null ) ret = errorResult != null ? errorResult : "failure";
		
		// d. Invoke (only if no errors)
		if( ret == null ) {
			try {
				ret = method.invoke(target, args);
			}
			catch( RuntimeException e ) {
				throw e;
			}
			catch( Exception e ) {
				throw new ActionPerformException("Cannot invoke the action method: " + method.getName(), e);
			}
			if( ret == null ) ret = result;
		}
		
		return ret != null ? ret.toString() : null;
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
		buf.append("ControllerAction: ");
		buf.append("method=").append(method.getName());
		if( result != null ) buf.append(", result=").append(result);
		if( errorResult != null ) buf.append(", errorResult=").append(errorResult);
		if( validationMethod != null ) buf.append(", val=").append(validationMethod.getName());
		if( parameters != null ) buf.append(", params={").append(parameters).append("}");
		return buf.toString();
	}
	
	///////////////////////////////////////////////////////////
	// Parameter Binding Classes
	
	/**
	 * This interface represents something that can bind a
	 * parameter from the model, the input and errors. The
	 * controller-wide binder is also passed in. 
	 * 
	 * @author Tom Spencer
	 */
	public interface ParameterBinding {

		/**
		 * Called to perform the binding for this parameter
		 * 
		 * @param model The current model
		 * @param input The input element
		 * @param errors The errors object
		 * @param binder The binder
		 * @return The bound object
		 */
		public Object bind(Model model, InputModel input, String errorAttribute, InputBinder binder);
	}
	
	/**
	 * Binds the parameter to the input model, either as the input
	 * model or a Map<String, String[]>
	 * 
	 * @author Tom Spencer
	 */
	public final class InputBinding implements ParameterBinding {

		/** Determines if we expect the parameter to be a map */
		private boolean map = false;
		
		public InputBinding(boolean map) {
			this.map = map;
		}
		
		/**
		 * Returns either the map or the input object
		 */
		public Object bind(Model model, InputModel input, String errorAttribute, InputBinder binder) {
			if( map ) return input.getParameters();
			else return input;
		}
		
		@Override
		public String toString() {
			return "InputBindingParameter: map=" + map;
		}
	}
	
	/**
	 * Binds the argument to the model
	 * 
	 * @author Tom Spencer
	 */
	public final class ModelBinding implements ParameterBinding {

		/**
		 * Binds the attribute to the model
		 */
		public Object bind(Model model, InputModel input, String errorAttribute, InputBinder binder) {
			return model;
		}
		
		@Override
		public String toString() {
			return "ModelBindingParameter";
		}
	}
	
	/**
	 * Represents a parameter with no binding. This will always
	 * be null when called from the controller.
	 * 
	 * @author Tom Spencer
	 */
	public final class NullBinding implements ParameterBinding {

		/**
		 * Always returns null
		 */
		public Object bind(Model model, InputModel input, String errorAttribute, InputBinder binder) {
			return null;
		}
		
		@Override
		public String toString() {
			return "NullBindingParameter";
		}
	}
	
	/**
	 * Binds the parameter to an expected type
	 * 
	 * @author Tom Spencer
	 */
	public final class ArbitraryBinding implements ParameterBinding {

		/** The prefix for the binding */
		private final String prefix;
		/** The name of the model attribute to retreive/save */
		private final String modelAttribute;
		/** The expected type that the binding will return */
		private final Class<?> expected;
		
		public ArbitraryBinding(String prefix, String modelAttribute, Class<?> expected) {
			this.prefix = prefix;
			this.modelAttribute = modelAttribute;
			this.expected = expected;
		}
		
		public Object bind(Model model, InputModel input, String errorAttribute, InputBinder binder) {
			Object obj = null;
			boolean bind = true;
			
			// a. Obtain from model if set
			if( modelAttribute != null ) {
				obj = model.getAttribute(modelAttribute);
				if( obj != null && !expected.isInstance(obj) ) throw new IllegalArgumentException("An attempt has been made to pass a model attribute [" + modelAttribute + "] of a different type into the actions parameter [" + expected + "]: " + obj);
			}
			
			// b. Determine if already the object (may be on events)
			if( prefix != null ) {
				Object in = input.getParameterObject(prefix);
				if( expected.isInstance(in) ) {
					bind = false;
					if( modelAttribute != null ) model.setAttribute(modelAttribute, in);
					obj = in;
				}
			}

			// c. Create if neccessary
			if( obj == null ) {
				if( expected.isInterface() ) {
					obj = Proxy.newProxyInstance(
							Thread.currentThread().getContextClassLoader(), 
							new Class[]{expected}, 
							new InterfaceAdaptor());
				}
				else {
					try {
						obj = expected.newInstance();
					}
					catch( Exception e ) {
						throw new BindingException("Cannot create bind object: " + expected, e);
					}
				}
			
				// Save the model attribute again
				if( modelAttribute != null ) model.setAttribute(modelAttribute, obj);
			}
			
			// d. Pass into bind
			if( bind ) {
				List<Object> errors = binder.bind(model, input, prefix, obj);
				if( errors != null && errors.size() > 0 ) {
					model.setAttribute(errorAttribute, errors);
				}
			}
			
			return obj;
		}
		
		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			buf.append("ArbitraryBindingParameter: expected=").append(expected);
			if( prefix != null ) buf.append(", prefix=").append(prefix);
			return buf.toString();
		}
	}
	
	/**
	 * Binds the parameter to the errors object
	 * 
	 * @author Tom Spencer
	 */
	public final class ErrorsBinding implements ParameterBinding {

		/**
		 * Simply returns the errors object
		 */
		public Object bind(Model model, InputModel input, String errorAttribute, InputBinder binder) {
			return model.getAttribute(errorAttribute);
		}
		
		@Override
		public String toString() {
			return "ErrorsBindingParameter";
		}
	}
	
	public final class ValidationMethod {
		private final Method method;
		private final boolean boolReturn;

		public ValidationMethod(Method m) {
			this.method = m;
			if( m != null ) {
				if( Boolean.class.equals(m.getReturnType()) ||
						boolean.class.equals(m.getReturnType() ) )
					boolReturn = true;
				else 
					boolReturn = false;
			}
			else {
				boolReturn = false;
			}
		}
		
		/**
		 * Call to invoke the validation method
		 * 
		 * @param controller The controller to act upon
		 * @param args The arguments formed for the main method
		 * @param errorResult The default error result
		 * @return The result or null if all is ok
		 * @throws InvocationTargetException
		 * @throws IllegalAccessException
		 */
		public String invokeValidator(Object controller, Object[] args, String errorResult) throws InvocationTargetException, IllegalAccessException {
			Object ret = method.invoke(controller, args);
			if( boolReturn && ret != null ) {
				if( !((Boolean)ret).booleanValue() ) return errorResult != null ? errorResult : ret.toString();
				else ret = null;
			}
			
			return ret != null ? ret.toString() : null;
		}
		
		/**
		 * @return The name of the validation method
		 */
		public String getName() {
			return method.getName();
		}
	}
}
