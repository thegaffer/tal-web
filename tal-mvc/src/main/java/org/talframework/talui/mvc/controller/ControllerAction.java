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

package org.talframework.talui.mvc.controller;

import java.lang.reflect.Method;
import java.util.List;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This class represents an action to perform as part
 * of a controller. A controller action can be configured
 * or it can be determined from annotations.
 * 
 * @author Tom Spencer
 */
public final class ControllerAction {

	/** The controller we are invoking */
	private final Object controller;
	/** Holds the name of the errors on the model */
	private final String errorsAttribute;
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
	 * Constructors a controller action instance.
	 * 
	 * @param controller The controller to invoke
	 * @param errorsAttribute The name of the errors attribute in the model
	 * @param method The method to invoke if binding and validation goes ok
	 * @param validationMethod The validation method to use (if any)
	 * @param result The result to return if successful (if method does not return anything!)
	 * @param failResult The result to return if unsuccessful 
	 * @param parameters The parameter bindings for the method
	 */
	public ControllerAction(
			Object controller,
			String errorsAttribute,
			Method method,
			Method validationMethod,
			String result,
			String failResult,
			ParameterBinding[] parameters) {
		this.controller = controller;
		this.errorsAttribute = errorsAttribute;
		this.method = method;
		this.validationMethod = validationMethod;
		this.result = result;
		this.errorResult = failResult;
		this.parameters = parameters;
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
	 * @param model The current model
	 * @param input The input to this action
	 * @return The result
	 */
	@SuppressWarnings("unchecked")
	public String invokeAction(Model model, InputModel input) {
		Object ret = null;
		
		// a. Bind parameters
		Object[] args = null;
		if( parameters != null ) {
			args = new Object[parameters.length];
			for( int i = 0 ; i < parameters.length ; i++ ) {
				args[i] = parameters[i] != null ? parameters[i].bind(model, input) : null;
			}
		}
		
		// b. Validate if applicable (and check errors)
		if( validationMethod != null ) {
			List<Object> errors = null;
			try {
				errors = (List<Object>)validationMethod.invoke(controller, args);
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
				ret = method.invoke(controller, args);
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
}
