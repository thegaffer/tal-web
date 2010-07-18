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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.config.AppConfig;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;

/**
 * This class tests an app config
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestAppConfig {
	
	/** Holds the model to test with */
	private ModelConfiguration model = null;
	
	/**
	 * Sets up the model and the page
	 */
	@Before
	public void setup() {
		model = new ModelConfiguration("app",
				new ArrayList<ModelAttribute>(){{
					add(new SimpleModelAttribute("simple"));
				}});
	}

	/**
	 * Tests basic operation
	 */
	@Test
	public void basic() {
		AppConfig app = new AppConfig("test", model);
		
		assertEquals("test", app.getName());
		assertEquals(model, app.getModel());
	}
	
	/**
	 * Tests we get a page ok
	 */
	@Test
	public void validPage() {
		PageConfig page = new PageConfig();
		page.setName("page");
		
		List<PageConfig> pages = new ArrayList<PageConfig>();
		pages.add(page);
		
		AppConfig app = new AppConfig("test", model);
		app.setPages(pages);
		
		assertEquals("test", app.getName());
		assertEquals(model, app.getModel());
		assertNotNull(app.getPages());
		assertEquals(page, app.getPage("page"));
	}
	
	/**
	 * Tests we get a page ok
	 */
	@Test
	public void init() {
		PageConfig page = new PageConfig();
		page.setName("page");
		page.setTemplate("page");
		
		List<PageConfig> pages = new ArrayList<PageConfig>();
		pages.add(page);
		
		AppConfig app = new AppConfig("test", model);
		app.setPages(pages);
		app.init();
		
		assertEquals("test", app.getName());
		assertEquals(model, app.getModel());
		assertNotNull(app.getPages());
		assertEquals(page, app.getPage("page"));
		assertFalse(page == app.getPage("page")); // Different instance!
	}
	
	/**
	 * Tests we get an exception if page invalid
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidPage() {
		AppConfig app = new AppConfig("test", model);
		app.getPage("invalid");
	}
	
	/**
	 * Tests an exception is thrown if no name
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noName() {
		new AppConfig(null,	model);
	}
	
	/**
	 * Tests no exception is thrown if no model
	 */
	@Test
	public void noModel() {
		new AppConfig("page", null);
	}
	
	/**
	 * Tests instance stringifies ok
	 */
	@Test
	public void stringify() {
		AppConfig app = new AppConfig("test");
		assertEquals("AppConfig [name=test]", app.toString());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		AppConfig test1 = new AppConfig("app1", null);
		AppConfig test2 = new AppConfig("app2", null);
		AppConfig test3 = new AppConfig("app1", null);
		
		assertTrue(test1.hashCode() != 0);
		assertTrue(test1.hashCode() != test2.hashCode());
		assertEquals(test1.hashCode(), test3.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		AppConfig test1 = new AppConfig("app1", null);
		AppConfig test2 = new AppConfig("app2", null);
		AppConfig test3 = new AppConfig("app1", null);
		
		assertFalse(test1.equals(test2));
		assertTrue(test1.equals(test3));
	}
}
