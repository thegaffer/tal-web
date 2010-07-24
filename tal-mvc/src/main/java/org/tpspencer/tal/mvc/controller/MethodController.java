package org.tpspencer.tal.mvc.controller;

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
	
	public final ControllerAction action;
	
	public MethodController(ControllerAction action) {
		this.action = action;
	}
	
	public String performAction(Model model, InputModel input) {
		return action.invokeAction(model, input);
	}
}
