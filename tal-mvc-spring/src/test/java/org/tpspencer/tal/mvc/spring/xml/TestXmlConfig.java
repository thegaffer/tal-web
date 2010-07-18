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

package org.tpspencer.tal.mvc.spring.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.Window;
import org.tpspencer.tal.mvc.config.AppConfig;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.config.WindowConfig;
import org.tpspencer.tal.mvc.input.InputModel;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.render.RenderModel;

public class TestXmlConfig {

	private Mockery context = new JUnit4Mockery();
	public ApplicationContext ctx = null;

	@Before
	public void setup() {
		ctx = new ClassPathXmlApplicationContext("classpath:/mvcContext.xml");
	}
	
	/**
	 * Ensures we can get a simple app with no pages
	 */
	@Test
	public void getApp() {
		AppConfig app = (AppConfig)ctx.getBean("app");
		assertEquals("app", app.getName());
		assertTrue(app.getModel().hasAttribute("test"));
	}
	
	/**
	 * Ensures we can get a simple page
	 */
	@Test
	public void getPage1() {
		AppConfig app = (AppConfig)ctx.getBean("pagedApp");
		PageConfig page = (PageConfig)ctx.getBean("page");
		assertEquals("page", page.getName());
		assertEquals("pageTemplate.jsp", page.getTemplate());
		assertNotNull(app.getPages());
		assertEquals(1, app.getPages().size());
	}

	/**
	 * Ensures the basic configuration of a window config
	 */
	@Test
	public void getWindowConfig() {
		WindowConfig window = (WindowConfig)ctx.getBean("window1");
		assertEquals("window1", window.getName());
		assertEquals("window1", window.getNamespace());
		assertEquals(ctx.getBean("someWindow"), window.getWindow());
		assertNull(window.getModel()); // Because we have aliases
	}
	
	/**
	 * Ensures our configured view is ok
	 */
	@Test
	public void getView() {
		final Model model = context.mock(Model.class);
		final RenderModel renderModel = context.mock(RenderModel.class);
		
		context.checking(new Expectations() {{
			oneOf(renderModel).setTemplate("someView");
		}});
		
		View view = (View)ctx.getBean("someView");
		view.prepareRender(renderModel, model);
		context.assertIsSatisfied();
	}
	
	/**
	 * Ensures our configured simple window is ok
	 */
	@Test
	public void getSimpleWindow() {
		View view = (View)ctx.getBean("someView");
		Window window = (Window)ctx.getBean("someWindow");
		
		assertNotNull(window.getModel());
		assertEquals(view, window.getCurrentState(null));
	}
	
	/**
	 * Ensures the complex window is fully setup
	 */
	@Test
	public void getComplexWindow() {
		Window window = (Window)ctx.getBean("complexWindow");
		
		assertNotNull(window.getModel());
		assertTrue(window.getModel().hasAttribute("state"));
		assertTrue(window.getModel().hasAttribute("window1"));
		assertTrue(window.getModel().hasAttribute("window2"));
		
		// Default View
		final Model model = context.mock(Model.class);
		context.checking(new Expectations() {{
			oneOf(model).getAttribute("state"); will(returnValue(null));
		}});
		
		assertEquals(ctx.getBean("someView"), window.getCurrentState(model));
		context.assertIsSatisfied();
		
		// Views
		context.checking(new Expectations() {{
			oneOf(model).getAttribute("state"); will(returnValue("view2"));
		}});
		
		assertEquals(ctx.getBean("anotherView"), window.getCurrentState(model));
		context.assertIsSatisfied();
		
		// Controllers and Action Mappings
		context.checking(new Expectations() {{
			oneOf(model).setAttribute("state", "view2");
		}});
		
		assertEquals("ok", window.processAction(model, null, "submit"));
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests a simple controller is created correctly
	 */
	@Test
	public void testSimpleController() {
		Controller ctrl = (Controller)ctx.getBean("simpleCtrl");
		
		final Model model = context.mock(Model.class);
		final InputModel input = context.mock(InputModel.class);
		
		context.checking(new Expectations() {{
			oneOf(input).hasParameter("action"); will(returnValue(true));
			oneOf(input).hasParameter("anotherAction"); will(returnValue(false));
			oneOf(model).containsKey("errors"); will(returnValue(true));
			oneOf(model).getAttribute("errors"); will(returnValue(null));
		}});
		
		String ret = ctrl.performAction(model, input);
		context.assertIsSatisfied();
		assertEquals(ret, "ok");
	}
	
	/**
	 * Tests we create a model successfully
	 */
	@Test
	public void testModel() {
		ModelConfiguration model = (ModelConfiguration)ctx.getBean("testModel");
		assertNotNull(model);
		assertTrue(model.hasAttribute("attr1"));
		
		AppConfig app = (AppConfig)ctx.getBean("modelApp");
		assertNotNull(app);
		assertEquals(model, app.getModel());
		
		PageConfig page = (PageConfig)ctx.getBean("modelPage");
		assertNotNull(page);
		assertEquals(model, page.getModel());
		
		View view = (View)ctx.getBean("modelView");
		assertNotNull(view);
		assertEquals(model, view.getModel());
	}
}
