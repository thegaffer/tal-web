package org.tpspencer.tal.mvc.controller;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This interface represents a class that knows how to bind 
 * an parameter, typically on an action method, from the
 * inputs it has available (i.e. what has been submitted by
 * the user and what is in the model).
 * 
 * @author Tom Spencer
 */
public interface ParameterBinding {

	/**
	 * Called at runtime to create a parameter based on the
	 * input.
	 * 
	 * @param model The current model
	 * @param input The input to the controller
	 * @return The parameter
	 */
	public Object bind(Model model, InputModel input);
}
