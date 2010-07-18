package org.tpspencer.tal.mvc.controller;

import java.lang.reflect.Method;

import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This controller will delegate to a single method on an
 * object that has the @When annotation.
 * 
 * @author Tom Spencer
 */
public class MethodController implements Controller {
	
	public final Object instance;
	public final ControllerAction action;
	
	public MethodController(Object instance, Method method) {
		this.instance = instance;
		this.action = new ControllerAction(method);
	}
	
	public String performAction(Model model, InputModel input) {
		return action.invokeAction(model, input, null, null, instance);
	}
}
