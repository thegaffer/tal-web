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

package org.tpspencer.tal.mvc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Window;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.config.WindowConfig;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;

/**
 * This class tests a page configuration
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestPageConfig {

	/** JMock Mockery context */
	private Mockery context = new JUnit4Mockery();
	/** Holds the model to test with */
	private ModelConfiguration model = null;
	
	/**
	 * Sets up the model and the page
	 */
	@Before
	public void setup() {
		model = new ModelConfiguration("page",
				new ArrayList<ModelAttribute>(){{
					add(new SimpleModelAttribute("simple"));
				}});
	}

	/**
	 * Tests basic operation
	 */
	@Test
	public void basic() {
		PageConfig page = new PageConfig();
		page.setName("test");
		page.setTemplate("template");
		page.setModel(model);
		
		assertEquals("test", page.getName());
		assertEquals("template", page.getTemplate());
		assertEquals(model, page.getModel());
	}
	
	/**
	 * Tests valid window
	 */
	@Test
	public void validWindow() {
		PageConfig page = new PageConfig();
		page.setName("test");
		page.setTemplate("template");
		page.setModel(model);
		
		WindowConfig window = new WindowConfig("window", context.mock(Window.class));
		List<WindowConfig> windows = new ArrayList<WindowConfig>();
		windows.add(window);
		page.setWindows(windows);
		
		assertNotNull(page.getWindows());
		assertEquals(window, page.getWindow("window"));
	}
	
	/**
	 * Tests clone after init
	 */
	@Test
	public void initPage() {
		PageConfig page = new PageConfig();
		page.setName("test");
		page.setTemplate("template");
		page.setModel(model);
		
		final Window w = context.mock(Window.class);
		WindowConfig window = new WindowConfig("window", w);
		List<WindowConfig> windows = new ArrayList<WindowConfig>();
		windows.add(window);
		page.setWindows(windows);
		
		context.checking(new Expectations() {{
			oneOf(w).getEvents(); will(returnValue(null));
			oneOf(w).getModel(); will(returnValue(null));
		}});
		
		PageConfig initPage = page.init(null);
		assertEquals(page, initPage);
		assertFalse(page == initPage);
	}
	
	/**
	 * Tests that the page model attribute is removed from
	 * page because it is merged with one in app
	 */
	@Test
	public void mergeAttributesOnInit() {
		ModelConfiguration appModel = new ModelConfiguration("app",
				new ArrayList<ModelAttribute>(){{
					add(new SimpleModelAttribute("simple"));
				}});
		
		PageConfig page = new PageConfig();
		page.setName("test");
		page.setTemplate("template");
		page.setModel(model);
		
		final Window w = context.mock(Window.class);
		WindowConfig window = new WindowConfig("window", w);
		List<WindowConfig> windows = new ArrayList<WindowConfig>();
		windows.add(window);
		page.setWindows(windows);
		
		context.checking(new Expectations() {{
			oneOf(w).getEvents(); will(returnValue(null));
			oneOf(w).getModel(); will(returnValue(null));
		}});
		
		PageConfig initPage = page.init(appModel);
		assertNull(initPage.getModel());
	}
	
	/**
	 * Tests we get an exception if window invalid
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidWindow() {
		PageConfig page = new PageConfig();
		page.setName("test");
		page.setTemplate("template");
		page.setModel(model);
		
		page.getWindow("invalid");
	}
	
	/**
	 * Tests an exception is thrown if no name
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noName() {
		PageConfig page = new PageConfig();
		page.setTemplate("template");
		page.setModel(model);
		
		page.init(null);
	}
	
	/**
	 * Tests an exception is thrown if no name
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noTemplate() {
		PageConfig page = new PageConfig();
		page.setName("test");
		page.setModel(model);
		
		page.init(null);
	}
	
	/**
	 * Tests instance stringifies ok
	 */
	@Test
	public void stringify() {
		PageConfig page = new PageConfig();
		page.setName("page");
		page.setTemplate("template");
		page.setModel(model);
		
		assertEquals("PageConfig [name=page]", page.toString());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		PageConfig test1 = new PageConfig();
		test1.setName("page1");
		test1.setTemplate("template");
		test1.setModel(model);
		PageConfig test2 = new PageConfig();
		test2.setName("page2");
		test2.setTemplate("template");
		test2.setModel(model);
		PageConfig test3 = new PageConfig();
		test3.setName("page1");
		test3.setTemplate("template");
		test3.setModel(model);
		
		assertTrue(test1.hashCode() != 0);
		assertTrue(test1.hashCode() != test2.hashCode());
		assertEquals(test1.hashCode(), test3.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		PageConfig test1 = new PageConfig();
		test1.setName("page1");
		test1.setTemplate("template");
		test1.setModel(model);
		PageConfig test2 = new PageConfig();
		test2.setName("page2");
		test2.setTemplate("template");
		test2.setModel(model);
		PageConfig test3 = new PageConfig();
		test3.setName("page1");
		test3.setTemplate("template");
		test3.setModel(model);
		
		assertFalse(test1.equals(test2));
		assertTrue(test1.equals(test3));
	}
}
