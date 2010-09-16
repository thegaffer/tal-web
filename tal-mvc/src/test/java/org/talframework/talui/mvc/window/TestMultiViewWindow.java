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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;
import org.tpspencer.tal.mvc.window.MultiViewWindow;

/**
 * This class tests a multi-view window operates
 * correctly.
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestMultiViewWindow {
	
	/** Holds the mocking context */
	private Mockery context = new JUnit4Mockery();
	
	/** Holds a mocked view we can use under test */
	private Map<String, View> views = null;
	/** Holds a model we can use under test */
	private ModelConfiguration model = null;
	/** Holds a couple of controllers we can use under test */
	private Map<String, Object> controllers = null;
	/** Holds some sameple action mappings */
	private Map<String, String> actionMappings = null;
	
	@Before
	public void setup() {
		views = new HashMap<String, View>() {{
			put("view1", context.mock(View.class, "view1"));
			put("view2", context.mock(View.class, "view2"));
		}};
		
		model = new ModelConfiguration("test", (List<ModelAttribute>)new ArrayList<ModelAttribute>() {{
			add(new SimpleModelAttribute("state"));
			add(new SimpleModelAttribute("test1"));
			add(new SimpleModelAttribute("test2"));
		}});
		
		controllers = new HashMap<String, Object>() {{
			put("action1", context.mock(Controller.class, "ctrl1"));
			put("action2", context.mock(Controller.class, "ctrl2"));
		}};
		
		actionMappings = new HashMap<String, String>() {{
			put("ok", "view2");
			put("fail", "view1");
		}};
	}
	
	/**
	 * Tests the basic operation of the window
	 */
	@Test
	public void basic() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
		
		final Model model = context.mock(Model.class);
		context.checking(new Expectations() {{
			oneOf(model).getAttribute("state");
			  will(returnValue(null));
		}});
		
		assertEquals(views.get("view1"), window.getDefaultView());
		assertEquals(views.get("view1"), window.getCurrentState(model));
		assertNotSame(this.model, window.getModel());
		assertEquals(controllers, window.getControllers());
	}
	
	/**
	 * Ensures the controller processes the actions ok
	 */
	@Test
	public void actionProcess() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
		
		final Model model = context.mock(Model.class);
		context.checking(new Expectations() {{
			oneOf((Controller)controllers.get("action1")).performAction(model, null);
			  will(returnValue("ok"));
			oneOf(model).setAttribute("state", "view2");
			oneOf(model).getAttribute("state");
			  will(returnValue("view2"));
			  
			oneOf((Controller)controllers.get("action2")).performAction(model, null);
			  will(returnValue("fail"));
			oneOf(model).setAttribute("state", "view1");
			oneOf(model).getAttribute("state");
			  will(returnValue("view1"));
		}});
		
		assertEquals("ok", window.processAction(model, null, "action1"));
		assertEquals(views.get("view2"), window.getCurrentState(model));
		assertEquals("fail", window.processAction(model, null, "action2"));
		assertEquals(views.get("view1"), window.getCurrentState(model));
	}

	/**
	 * Ensures we get a failure if we have an invalid action
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidAction() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
		window.processAction(null, null, "invalid");
	}
	
	/**
	 * Test to ensure failure if no views
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noViews() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		// window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if empty views
	 */
	@Test(expected = IllegalArgumentException.class)
	public void emptyViews() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(new HashMap<String, View>());
		window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if invalid default view
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noDefaultView() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		// window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if no controllers
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noControllers() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		// window.setControllers(controllers);
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if empty controllers
	 */
	@Test(expected = IllegalArgumentException.class)
	public void emptyControllers() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(new HashMap<String, Object>());
		window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if no action mappings
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noActionMappings() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		// window.setActionMappings(actionMappings);
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if empty action mappings
	 */
	@Test(expected = IllegalArgumentException.class)
	public void emptyActionMappings() {
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(new HashMap<String, String>());
		window.setModel(model);
		window.init();
	}
	
	/**
	 * Test to ensure failure if mappings point to
	 * invalid view
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidActionMappings() {
		Map<String, String> mappings = new HashMap<String, String>() {{
			put("action1", "invalid");
		}};
		
		MultiViewWindow window = new MultiViewWindow();
		window.setName("view1");
		window.setDefaultView(views.get("view1"));
		window.setViews(views);
		window.setControllers(controllers);
		window.setActionMappings(mappings);
		window.setModel(model);
		window.init();
	}
}
