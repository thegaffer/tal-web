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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.SimpleModelAttribute;

/**
 * This class tests the basic ModelAttribute value object
 * class.
 * 
 * @author Tom Spencer
 */
public class TestSimpleModelAttribute {

	/**
	 * Tests the basic constructor of the ModelAttribute class
	 */
	@Test
	public void basicConstructor() {
		SimpleModelAttribute attr = new SimpleModelAttribute("test");
		
		assertEquals("test", attr.getName());
		assertNull(attr.getDefaultValue());
		assertNull(attr.getValue(null));
		assertTrue(attr.isSimple());
		
		assertFalse(attr.isFlash());
		assertFalse(attr.isEventable());
		assertTrue(attr.isAliasable());
		assertFalse(attr.isAliasExpected());
		assertFalse(attr.isClearOnAction());
		
		attr.setFlash(true);
		attr.setEventable(true);
		attr.setAliasable(false);
		attr.setAliasExpected(true);
		attr.setClearOnAction(true);
		assertFalse(!attr.isFlash());
		assertFalse(!attr.isEventable());
		assertTrue(!attr.isAliasable());
		assertFalse(!attr.isAliasExpected());
		assertFalse(!attr.isClearOnAction());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		ModelAttribute attr1 = new SimpleModelAttribute("test");
		ModelAttribute attr2 = new SimpleModelAttribute("test2");
		ModelAttribute attr3 = new SimpleModelAttribute("test");
		
		assertTrue(attr1.hashCode() > 0);
		assertEquals(attr1.hashCode(), attr3.hashCode());
		assertTrue(attr1.hashCode() != attr2.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		ModelAttribute attr1 = new SimpleModelAttribute("test");
		ModelAttribute attr2 = new SimpleModelAttribute("test2");
		ModelAttribute attr3 = new SimpleModelAttribute("test");
		
		assertTrue(attr1.equals(attr3));
		assertFalse(attr1.equals(attr2));
	}
	
	/**
	 * Tests that the attribute does not equals a string
	 * of same name
	 */
	@Test
	public void testNotEquals() {
		ModelAttribute attr1 = new SimpleModelAttribute("test");
		assertFalse(attr1.equals("test"));
	}
	
	/**
	 * Ensures we stringify ok
	 */
	@Test
	public void stringify() {
		SimpleModelAttribute attr1 = new SimpleModelAttribute("test");
		attr1.setDefaultValue("def");
		attr1.setEventable(true);
		attr1.setFlash(true);
		
		assertEquals("SimpleModelAttribute [name=test, default=def, flash, eventable]", attr1.toString());
	}
}
