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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.ModelLayer;
import org.talframework.talui.mvc.model.SimpleModelAttribute;

/**
 * This class tests the model layer value object
 * 
 * @author Tom Spencer
 */
public class TestModelLayer {
	
	private ModelConfiguration configuration = null;

	/**
	 * Creates the test model configuration
	 */
	@Before
	public void createConfiguration() {
		List<ModelAttribute> attributes = new ArrayList<ModelAttribute>();
		attributes.add(new SimpleModelAttribute("test"));
		configuration = new ModelConfiguration("testModel", attributes);
	}

	/**
	 * Tests the layer responds correctly
	 */
	@Test
	public void basic() {
		ModelLayer layer = new ModelLayer(configuration, null);
		
		assertNull(layer.getAttributes());
		assertEquals(configuration, layer.getConfiguration());
	}
	
	/**
	 * Tests the layer is ok with current attributes
	 */
	@Test
	public void withAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("test", "testValue");
		ModelLayer layer = new ModelLayer(configuration, attributes);
		
		assertEquals(attributes, layer.getAttributes());
		assertEquals(configuration, layer.getConfiguration());
	}
	
	/**
	 * Tests the layer fails if no configuration supplied
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noConfiguration() {
		new ModelLayer(null, null);
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		ModelConfiguration config = new ModelConfiguration("test", new ArrayList<ModelAttribute>());
		
		ModelLayer layer1 = new ModelLayer(configuration, null);
		ModelLayer layer2 = new ModelLayer(config, null);
		ModelLayer layer3 = new ModelLayer(configuration, null);
		
		assertTrue(layer1.hashCode() != 0);
		assertEquals(layer1.hashCode(), layer3.hashCode());
		assertTrue(layer1.hashCode() != layer2.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		ModelConfiguration config = new ModelConfiguration("test", new ArrayList<ModelAttribute>());
		
		ModelLayer layer1 = new ModelLayer(configuration, null);
		ModelLayer layer2 = new ModelLayer(config, null);
		ModelLayer layer3 = new ModelLayer(configuration, null);
		
		assertTrue(layer1.equals(layer3));
		assertFalse(layer1.equals(layer2));
	}
}
