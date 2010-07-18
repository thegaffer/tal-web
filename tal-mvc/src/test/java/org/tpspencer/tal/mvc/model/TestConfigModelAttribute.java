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

package org.tpspencer.tal.mvc.model;

import static org.junit.Assert.*;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.tpspencer.tal.mvc.model.ConfigModelAttribute;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.SimpleModelResolver;

/**
 * This class tests the basic ModelAttribute value object
 * class.
 * 
 * @author Tom Spencer
 */
public class TestConfigModelAttribute {
	private Mockery context = new JUnit4Mockery();

	/**
	 * Tests the resolver constructor of the ModelAttribute class
	 */
	@Test
	public void resolverConstructor() {
		final SimpleModelResolver resolver = context.mock(SimpleModelResolver.class);
		
		ConfigModelAttribute attr = new ConfigModelAttribute("test", resolver, "test.param", "default");
		
		assertEquals("test", attr.getName());
		assertEquals("default", attr.getDefaultValue());
		assertEquals(resolver, attr.getResolver());
		assertEquals("test.param", attr.getParameter());
		assertTrue(attr.isFlash());
		assertFalse(attr.isEventable());
		assertTrue(attr.isAliasable());
		assertFalse(attr.isAliasExpected());
		assertFalse(attr.isClearOnAction());
		assertTrue(attr.isSimple());
		
		attr.setEventable(true);
		attr.setAliasable(false);
		attr.setAliasExpected(true);
		attr.setClearOnAction(true);
		assertFalse(!attr.isEventable());
		assertTrue(!attr.isAliasable());
		assertFalse(!attr.isAliasExpected());
		assertFalse(!attr.isClearOnAction());
	}
	
	/**
	 * Ensure we cannot set this to a non-flash attribute
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFailIfFlash() {
		final SimpleModelResolver resolver = context.mock(SimpleModelResolver.class);
		ConfigModelAttribute attr = new ConfigModelAttribute("test", resolver, "test.param", "default");
		attr.setFlash(false);
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		final SimpleModelResolver resolver = context.mock(SimpleModelResolver.class);
		ModelAttribute attr1 = new ConfigModelAttribute("test", resolver, null, null);
		ModelAttribute attr2 = new ConfigModelAttribute("test2", resolver, null, null);
		ModelAttribute attr3 = new ConfigModelAttribute("test", resolver, null, null);
		
		assertTrue(attr1.hashCode() != 0);
		assertEquals(attr1.hashCode(), attr3.hashCode());
		assertTrue(attr1.hashCode() != attr2.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		final SimpleModelResolver resolver = context.mock(SimpleModelResolver.class);
		ModelAttribute attr1 = new ConfigModelAttribute("test", resolver, null, null);
		ModelAttribute attr2 = new ConfigModelAttribute("test2", resolver, null, null);
		ModelAttribute attr3 = new ConfigModelAttribute("test", resolver, null, null);
		
		assertTrue(attr1.equals(attr3));
		assertFalse(attr1.equals(attr2));
	}
	
	/**
	 * Ensures we stringify ok
	 */
	@Test
	public void stringify() {
		final SimpleModelResolver resolver = context.mock(SimpleModelResolver.class);
		ConfigModelAttribute attr1 = new ConfigModelAttribute("test", resolver, "param", "def");
		attr1.setEventable(true);
		
		assertEquals("ConfigModelAttribute [name=test, resolver=simpleModelResolver, param=param, default=def, eventable]", attr1.toString());
	}
}
