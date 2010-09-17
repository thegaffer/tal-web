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

package org.talframework.talui.mvc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.Window;
import org.talframework.talui.mvc.config.EventConfig;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.SimpleModelAttribute;

/**
 * This class tests a window configuration
 * 
 * @author Tom Spencer
 */
public class TestWindowConfig {

	/** JMock Mockery context */
	private Mockery context = new JUnit4Mockery();
	/** Holds the (mock) window to use */
	private Window window = null;
	
	/**
	 * Sets up the model and window (mock) for tests
	 */
	@Before
	public void setup() {
		window = context.mock(Window.class);
	}
	
	/**
	 * Tests the basic operation
	 */
	@Test
	public void basic() {
		WindowConfig config = new WindowConfig("window", window);
		
		assertEquals("window", config.getName());
		assertEquals("window", config.getNamespace());
		assertEquals(window, config.getWindow());
	}
	
	/**
	 * Tests exception thrown if no name
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noName() {
		new WindowConfig(null, window);
	}
	
	/**
	 * Tests exception if no window
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noWindow() {
		new WindowConfig(null, null);
	}
	
	/**
	 * Tests we can initialise a window ok and get back a clone
	 */
	@Test
	public void initWindow() {
		WindowConfig config = new WindowConfig("window", window);
		
		context.checking(new Expectations() {{
			oneOf(window).getModel(); will(returnValue(null));
			oneOf(window).getEvents(); will(returnValue(null));
		}});
		
		WindowConfig config2 = config.init(null, null);
		assertEquals(config2, config);
		assertFalse(config2 == config);
	}
	
	/**
	 * Tests that attributes are merged
	 */
	@Test
	public void mergeAttributesOnInit() {
		WindowConfig config = new WindowConfig("window", window);
		
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		attrs.add(new SimpleModelAttribute("test"));
		final ModelConfiguration windowModel = new ModelConfiguration("window", attrs);
		
		attrs.clear();
		attrs.add(new SimpleModelAttribute("test1"));
		((SimpleModelAttribute)attrs.get(0)).setAliases(new String[]{"test"});
		ModelConfiguration pageModel = new ModelConfiguration("page", attrs);
		
		context.checking(new Expectations() {{
			oneOf(window).getModel(); will(returnValue(windowModel));
			oneOf(window).getEvents(); will(returnValue(null));
		}});
		
		WindowConfig config2 = config.init(null, pageModel);
		assertNull(config2.getModel());
		assertEquals(pageModel.getAttribute("test1"), pageModel.getAttribute("test"));
	}
	
	/**
	 * Tests that attributes that expect to be merged, but which
	 * are not causes an exception
	 */
	@Test(expected=IllegalArgumentException.class)
	public void stopIfAliasExpected() {
		WindowConfig config = new WindowConfig("window", window);
		
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		attrs.add(new SimpleModelAttribute("test"));
		((SimpleModelAttribute)attrs.get(0)).setAliasExpected(true);
		final ModelConfiguration windowModel = new ModelConfiguration("window", attrs);
		
		context.checking(new Expectations() {{
			oneOf(window).getModel(); will(returnValue(windowModel));
			oneOf(window).getEvents(); will(returnValue(null));
		}});
		
		config.init(null, null);
	}
	
	/**
	 * Tests that events are updated with real attrbite name when
	 * aliased
	 */
	@Test
	public void eventsKeyedOnAlias() {
		WindowConfig config = new WindowConfig("window", window);
		
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		attrs.add(new SimpleModelAttribute("test"));
		final ModelConfiguration windowModel = new ModelConfiguration("window", attrs);
		
		attrs.clear();
		attrs.add(new SimpleModelAttribute("test1"));
		((SimpleModelAttribute)attrs.get(0)).setAliases(new String[]{"test"});
		ModelConfiguration pageModel = new ModelConfiguration("page", attrs);
		
		EventConfig event = new EventConfig("test", "some", "attr"); 
		final List<EventConfig> events = new ArrayList<EventConfig>();
		events.add(event);
		
		context.checking(new Expectations() {{
			oneOf(window).getModel(); will(returnValue(windowModel));
			oneOf(window).getEvents(); will(returnValue(events));
			oneOf(window).getEvents(); will(returnValue(events));
		}});
		
		WindowConfig config2 = config.init(null, pageModel);
		assertEquals(event, config2.getEventConfig("page.test1"));
	}
	
	/**
	 * Tests that failure if event does not match any app/page
	 * attribute
	 */
	@Test(expected=IllegalArgumentException.class)
	public void eventsNoMatch() {
		WindowConfig config = new WindowConfig("window", window);
		
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		attrs.add(new SimpleModelAttribute("test1"));
		ModelConfiguration pageModel = new ModelConfiguration("page", attrs);
		
		EventConfig event = new EventConfig("test", "some", "attr"); 
		final List<EventConfig> events = new ArrayList<EventConfig>();
		events.add(event);
		
		context.checking(new Expectations() {{
			oneOf(window).getModel(); will(returnValue(null));
			oneOf(window).getEvents(); will(returnValue(events));
			oneOf(window).getEvents(); will(returnValue(events));
		}});
		
		config.init(null, pageModel);
	}
	
	/**
	 * Tests instance stringifies ok
	 */
	@Test
	public void stringify() {
		context.checking(new Expectations() {{
			oneOf(window).getModel(); 
			will(returnValue(null));
			oneOf(window).getEvents();
			will(returnValue(null));
		}});
		
		WindowConfig windowConfig = new WindowConfig("window1", window);
		assertEquals("WindowConfig [name=window1]", windowConfig.toString());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		WindowConfig test1 = new WindowConfig("window1", window);
		WindowConfig test2 = new WindowConfig("window2", window);
		WindowConfig test3 = new WindowConfig("window1", window);
		
		assertTrue(test1.hashCode() != 0);
		assertTrue(test1.hashCode() != test2.hashCode());
		assertEquals(test1.hashCode(), test3.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		WindowConfig test1 = new WindowConfig("window1", window);
		WindowConfig test2 = new WindowConfig("window2", window);
		WindowConfig test3 = new WindowConfig("window1", window);
		
		assertFalse(test1.equals(test2));
		assertTrue(test1.equals(test3));
	}
}
