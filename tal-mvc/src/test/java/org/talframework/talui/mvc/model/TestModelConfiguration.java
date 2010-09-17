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

package org.talframework.talui.mvc.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.SimpleModelAttribute;

/**
 * Tests the ModelConfiguration value object
 * 
 * @author Tom Spencer
 */
public class TestModelConfiguration {
	
	/** Holds the attributes we will test with */
	private List<ModelAttribute> attributes = null;
	
	/**
	 * Creates the test attributes
	 */
	@Before
	public void createTestAttributes() {
		attributes = new ArrayList<ModelAttribute>();
		attributes.add(new SimpleModelAttribute("test2"));
		attributes.add(new SimpleModelAttribute("test3"));
		attributes.add(new SimpleModelAttribute("test1"));
	}

	/**
	 * Basic test to ensure model configuration holds settings
	 */
	@Test
	public void basic() {
		ModelConfiguration config = new ModelConfiguration("test", attributes);
		
		assertEquals("test", config.getName());
		assertArrayEquals(attributes.toArray(), config.getAttributes().toArray());
	}
	
	/**
	 * Ensures we stringify ok
	 */
	@Test
	public void stringify() {
		ModelConfiguration config = new ModelConfiguration("test", attributes);
		assertEquals("ModelConfiguration [name=test, attrs={test2=SimpleModelAttribute [name=test2], test3=SimpleModelAttribute [name=test3], test1=SimpleModelAttribute [name=test1]}]", config.toString());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		ModelConfiguration config1 = new ModelConfiguration("test", attributes);
		ModelConfiguration config2 = new ModelConfiguration("test2", attributes);
		ModelConfiguration config3 = new ModelConfiguration("test", attributes);
		
		assertTrue(config1.hashCode() != 0);
		assertEquals(config1.hashCode(), config3.hashCode());
		assertTrue(config1.hashCode() != config2.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		ModelConfiguration config1 = new ModelConfiguration("test", attributes);
		ModelConfiguration config2 = new ModelConfiguration("test2", attributes);
		ModelConfiguration config3 = new ModelConfiguration("test", attributes);
		
		assertTrue(config1.equals(config3));
		assertFalse(config1.equals(config2));
	}
}
