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

package org.talframework.talui.mvc.window;

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
import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.config.EventConfig;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.SimpleModelAttribute;
import org.talframework.talui.mvc.window.SimpleWindow;

/**
 * This class tests that a simple window operates
 * correctly.
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestSimpleWindow {
	
	/** Holds the mocking context */
	private Mockery context = new JUnit4Mockery();
	
	/** Holds a mocked view we can use under test */
	private View view = null;
	/** Holds a model we can use under test */
	private ModelConfiguration model = null;
	/** Holds a couple of controllers we can use under test */
	private Map<String, Object> controllers = null;
	
	@Before
	public void setup() {
		view = context.mock(View.class);
		
		model = new ModelConfiguration("test", (List<ModelAttribute>)new ArrayList<ModelAttribute>() {{
			add(new SimpleModelAttribute("test1"));
			add(new SimpleModelAttribute("test2"));
		}});
		
		controllers = new HashMap<String, Object>() {{
			put("action1", context.mock(Controller.class, "ctrl1"));
			put("action2", context.mock(Controller.class, "ctrl2"));
		}};
	}

	/**
	 * Tests the simplest of windows
	 */
	@Test
	public void basic() {
		SimpleWindow window = new SimpleWindow();
		window.setName("test1");
		window.setDefaultView(view);
		window.init();
		
		assertEquals(view, window.getCurrentState(null));
		assertNull(window.getControllers());
	}
	
	/**
	 * Tests we can setup a simple window with
	 * a model
	 */
	@Test
	public void basicModel() {
		SimpleWindow window = new SimpleWindow();
		window.setDefaultView(view);
		window.setModel(model);
		window.init();
		
		assertEquals(view, window.getCurrentState(null));
		assertNotSame(model, window.getModel());
		assertNull(window.getControllers());
	}
	
	/**
	 * Tests we can setup a simple window with 
	 * controllers
	 */
	@Test
	public void basicControllers() {
		SimpleWindow window = new SimpleWindow();
		window.setName("test1");
		window.setDefaultView(view);
		window.setControllers(controllers);
		window.init();
		
		assertEquals(view, window.getCurrentState(null));
		assertEquals(controllers, window.getControllers());
	}
	
	/**
	 * Tests we can setup a simple window with
	 * controllers and a model.
	 */
	@Test
	public void basicControllersAndModel() {
		SimpleWindow window = new SimpleWindow();
		window.setDefaultView(view);
		window.setControllers(controllers);
		window.setModel(model);
		window.init();
		
		assertEquals(view, window.getCurrentState(null));
		assertNotSame(model, window.getModel());
		assertEquals(controllers, window.getControllers());
	}
	
	/**
	 * Ensures the window stores events correctly
	 */
	@Test
	public void withEvents() {
		SimpleWindow window = new SimpleWindow();
		window.setEvents(new ArrayList<EventConfig>() {{
			add(new EventConfig("test1", "onEvent", "newValue", "oldValue"));
		}});
		window.setDefaultView(view);
		window.setModel(model);
		window.init();
		
		assertNotNull(window.getEvents());
		assertEquals("test1", window.getEvents().get(0).getAttr());
		assertEquals("onEvent", window.getEvents().get(0).getAction());
		assertEquals("newValue", window.getEvents().get(0).getNewValueName());
		assertEquals("oldValue", window.getEvents().get(0).getOldValueName());
	}
	
	/**
	 * Tests that if we have an action of a simple
	 * window with no actions it fails
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void noControllers() {
		SimpleWindow window = new SimpleWindow();
		window.setName("test1");
		window.setDefaultView(view);
		window.init();
		
		window.processAction(null, null, "test");
	}
	
	/**
	 * Tests that if we have an action of a simple
	 * window with no actions it fails
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidControllers() {
		SimpleWindow window = new SimpleWindow();
		window.setDefaultView(view);
		window.setControllers(controllers);
		window.setModel(model);
		window.init();
		
		window.processAction(null, null, "action3");
	}
	
	/**
	 * Tests we process the actions accordingly
	 */
	@Test
	public void processAction() {
		SimpleWindow window = new SimpleWindow();
		window.setDefaultView(view);
		window.setControllers(controllers);
		window.setModel(model);
		window.init();
		
		context.checking(new Expectations() {{
			oneOf((Controller)controllers.get("action1")).performAction(null, null);
			  will(returnValue("ok1"));
			oneOf((Controller)controllers.get("action2")).performAction(null, null);
			  will(returnValue("ok2"));
		}});
		
		assertEquals("ok1", window.processAction(null, null, "action1"));
		assertEquals("ok2", window.processAction(null, null, "action2"));
	}
}
