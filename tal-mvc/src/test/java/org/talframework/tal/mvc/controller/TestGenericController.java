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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.GenericController;
import org.tpspencer.tal.mvc.controller.compiler.ControllerCompiler;
import org.tpspencer.tal.mvc.controller.test.BasicController;
import org.tpspencer.tal.mvc.controller.test.BindClass;
import org.tpspencer.tal.mvc.controller.test.BindInterface;
import org.tpspencer.tal.mvc.controller.test.BindingController;
import org.tpspencer.tal.mvc.controller.test.ComplexController;
import org.tpspencer.tal.mvc.controller.test.InputBinderStub;
import org.tpspencer.tal.mvc.controller.test.InvalidController;
import org.tpspencer.tal.mvc.controller.test.InvalidControllerNoActions;
import org.tpspencer.tal.mvc.controller.test.InvalidControllerNoValidation;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This class tests the generic controller
 * 
 * @author Tom Spencer
 */
public class TestGenericController {

	private Mockery context = new JUnit4Mockery();
	
	private Model model = null;
	private InputModel input = null;
	private BasicController basic = null;
	private ComplexController complex = null;
	private BindingController binding = null;
	
	@Before
	public void setup() {
		model = context.mock(Model.class);
		input = context.mock(InputModel.class);
		basic = context.mock(BasicController.class);
		complex = context.mock(ComplexController.class);
		binding = context.mock(BindingController.class);
		
		context.checking(new Expectations() {{
			allowing(binding).getBinder(); will(returnValue(new InputBinderStub()));
		}});
	}
	
	/**
	 * Ensures the compiler works
	 */
	@Test
	public void compilation() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(basic);
		Assert.assertNotNull(ctrl);
		
		ctrl = ControllerCompiler.getCompiler().compile(binding);
		Assert.assertNotNull(ctrl);
		
		ctrl = ControllerCompiler.getCompiler().compile(complex);
		Assert.assertNotNull(ctrl);
	}
	
	/**
	 * Simple setup and call of the controller
	 */
	@Test
	public void simple() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(basic);
		
		context.checking(new Expectations() {{
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			oneOf(basic).performMagic(); will(returnValue("magical"));
		}});
		
		String res = ctrl.performAction(model, input);
		Assert.assertEquals("magical", res);
		context.assertIsSatisfied();
	}
	
	///////////////////////////////////////////////
	// Complex Controller

	/**
	 * Tests the default action on the controller
	 */
	@Test
	public void complex() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(complex);
		
		final Map<String, String> params = new HashMap<String, String>();
		
		context.checking(new Expectations() {{
			allowing(input).getParameter("subAction"); will(returnValue(null));
			allowing(input).getParameters(); will(returnValue(params));
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			
			oneOf(complex).defaultAction(input); will(returnValue("default"));
		}});
		
		String res = ctrl.performAction(model, input);
		Assert.assertEquals("default", res);
		context.assertIsSatisfied();
	}
	
	/**
	 * Ensures we can call a sub action that has a validator, which
	 * will pass and then call action with params as model and input.
	 */
	@Test
	public void complexAlternative() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(complex);
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("subAction", "alt");
		
		context.checking(new Expectations() {{
			allowing(input).getParameter("subAction"); will(returnValue(params.get("subAction")));
			allowing(input).getParameters(); will(returnValue(params));
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
		
			oneOf(complex).alternativeAction(model, params);
		}});
		
		String res = ctrl.performAction(model, input);
		Assert.assertEquals("success", res);
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests we can call another action based on subAction,
	 * converting model and input to maps
	 */
	@Test
	public void complexAnother() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(complex);
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("subAction", "another");
		
		context.checking(new Expectations() {{
			allowing(input).getParameter("subAction"); will(returnValue(params.get("subAction")));
			allowing(input).getParameters(); will(returnValue(params));
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			
			oneOf(complex).validateMethod(model, input); will(returnValue(null));
			oneOf(complex).anotherAction(model, input); will(returnValue("another"));
		}});
		
		String res = ctrl.performAction(model, input);
		Assert.assertEquals("another", res);
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests we can call another action based on subAction,
	 * with a failing validation method, that failure is returned
	 */
	@Test
	public void complexAnotherFailValidation() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(complex);
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("subAction", "another");
		final List<Object> errors = new ArrayList<Object>(); errors.add(new Date());
		
		context.checking(new Expectations() {{
			allowing(input).getParameter("subAction"); will(returnValue(params.get("subAction")));
			allowing(input).getParameters(); will(returnValue(params));
			
			oneOf(complex).validateMethod(model, input); will(returnValue(errors));
			oneOf(model).setAttribute(with("errors"), with(errors));
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(errors));
		}});
		
		String res = ctrl.performAction(model, input);
		Assert.assertEquals("fail", res);
		context.assertIsSatisfied();
	}
	
	
	///////////////////////////////////////////////
	// Binding Controller
	
	@SuppressWarnings("unchecked")
	@Test
	public void binding() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(binding);
		
		final Date dt = new Date();
		
		context.checking(new Expectations() {{
			allowing(input).hasParameter(with(any(String.class))); will(returnValue(false));
			
			oneOf(model).getAttribute("date"); will(returnValue(dt));
			oneOf(model).setAttribute(with("date"), with(any(Date.class)));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			oneOf(binding).validate(with(any(Date.class)), with(any(List.class))); will(returnValue(null));
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			
			oneOf(binding).magic(with(any(Date.class)), with(any(List.class))); will(returnValue("success"));
		}});
		
		String res = ctrl.performAction(model, input);
		context.assertIsSatisfied();
		Assert.assertEquals("success", res);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void bindingValidationFailure() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(binding);
		
		final List<Object> errors = new ArrayList<Object>(); errors.add(new Date());
		
		context.checking(new Expectations() {{
			allowing(input).hasParameter(with(any(String.class))); will(returnValue(false));
			
			oneOf(model).getAttribute("date"); will(returnValue(null));
			oneOf(model).setAttribute(with("date"), with(any(Date.class)));
			oneOf(model).getAttribute(with("errors")); will(returnValue(null)); /* binding */
			oneOf(binding).validate(with(any(Date.class)), with(any(List.class))); will(returnValue(errors));
			
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute(with("errors")); will(returnValue(errors)); /* check */
			oneOf(model).setAttribute(with("errors"), with(errors));
		}});
		
		String res = ctrl.performAction(model, input);
		context.assertIsSatisfied();
		Assert.assertEquals("errors", res);
	}
	
	@Test
	public void bindingClass() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(binding);
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("bindClass", "bindClass");
		
		context.checking(new Expectations() {{
			allowing(input).hasParameter("bindClass"); will(returnValue(true));
			allowing(input).hasParameter("bindInterface"); will(returnValue(false));
			
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			
			oneOf(binding).classMagic(with(any(BindClass.class))); will(returnValue("success"));
		}});
		
		String res = ctrl.performAction(model, input);
		context.assertIsSatisfied();
		Assert.assertEquals("success", res);
	}
	
	@Test
	public void bindingInterface() {
		GenericController ctrl = ControllerCompiler.getCompiler().compile(binding);
		
		final Map<String, String> params = new HashMap<String, String>();
		params.put("bindInterface", "bindInterface");
		
		context.checking(new Expectations() {{
			allowing(input).hasParameter("bindClass"); will(returnValue(false));
			allowing(input).hasParameter("bindInterface"); will(returnValue(true));
			
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
			
			oneOf(binding).interfaceMagic(with(any(BindInterface.class))); will(returnValue("success"));
		}});
		
		String res = ctrl.performAction(model, input);
		context.assertIsSatisfied();
		Assert.assertEquals("success", res);
	}
	
	
	///////////////////////////////////////////////
	// Failure Tests
	
	/**
	 * Ensures we cannot create a controller when the controller 
	 * object is not marked as a controller.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void invalidController() {
		ControllerCompiler.getCompiler().compile(context.mock(InvalidController.class));
	}
	
	/**
	 * Ensures we cannot create a controller when the controller 
	 * object has no methods marked as action.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void controllerWithNoActions() {
		ControllerCompiler.getCompiler().compile(context.mock(InvalidControllerNoActions.class));
	}
	
	/**
	 * Ensures we cannot create a controller when the controller 
	 * object has an action marked with a validation method that
	 * does not exist
	 */
	@Test(expected=IllegalArgumentException.class)
	public void controllerWithInvalidValidation() {
		ControllerCompiler.getCompiler().compile(context.mock(InvalidControllerNoValidation.class));
	}
}
