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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelResolver;
import org.talframework.talui.mvc.model.ResolvedModelAttribute;

/**
 * This class tests the basic ModelAttribute value object
 * class.
 * 
 * @author Tom Spencer
 */
public class TestResolvedModelAttribute {
	private Mockery context = new JUnit4Mockery();

	/**
	 * Tests the resolver constructor of the ModelAttribute class
	 */
	@Test
	public void resolverConstructor() {
		final ModelResolver resolver = context.mock(ModelResolver.class);
		
		context.checking(new Expectations() {{
		    allowing(resolver).canNestResolver(); will(returnValue(false));
		}});
		
		ResolvedModelAttribute attr = new ResolvedModelAttribute("test", resolver, "test.param", "default");
		
		assertEquals("test", attr.getName());
		assertEquals("default", attr.getDefaultValue().toString());
		assertEquals(resolver, attr.getResolver());
		assertEquals("test.param", attr.getParameter());
		assertTrue(attr.isSimple());
		
		assertTrue(attr.isFlash());
		assertFalse(attr.isEventable());
		assertTrue(attr.isAliasable());
		assertFalse(attr.isAliasExpected());
		assertFalse(attr.isClearOnAction());
		
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
		final ModelResolver resolver = context.mock(ModelResolver.class);
		context.checking(new Expectations() {{
            allowing(resolver).canNestResolver(); will(returnValue(false));
        }});
		
		ResolvedModelAttribute attr = new ResolvedModelAttribute("test", resolver, "test.param", "default");
		attr.setFlash(false);
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		final ModelResolver resolver = context.mock(ModelResolver.class);
		context.checking(new Expectations() {{
            allowing(resolver).canNestResolver(); will(returnValue(false));
        }});
		
		ModelAttribute attr1 = new ResolvedModelAttribute("test", resolver, null, null);
		ModelAttribute attr2 = new ResolvedModelAttribute("test2", resolver, null, null);
		ModelAttribute attr3 = new ResolvedModelAttribute("test", resolver, null, null);
		
		assertTrue(attr1.hashCode() != 0);
		assertEquals(attr1.hashCode(), attr3.hashCode());
		assertTrue(attr1.hashCode() != attr2.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		final ModelResolver resolver = context.mock(ModelResolver.class);
		context.checking(new Expectations() {{
            allowing(resolver).canNestResolver(); will(returnValue(false));
        }});
		
		ModelAttribute attr1 = new ResolvedModelAttribute("test", resolver, null, null);
		ModelAttribute attr2 = new ResolvedModelAttribute("test2", resolver, null, null);
		ModelAttribute attr3 = new ResolvedModelAttribute("test", resolver, null, null);
		
		assertTrue(attr1.equals(attr3));
		assertFalse(attr1.equals(attr2));
	}
	
	/**
	 * Ensures we stringify ok
	 */
	@Test
	public void stringify() {
		final ModelResolver resolver = context.mock(ModelResolver.class);
		context.checking(new Expectations() {{
            allowing(resolver).canNestResolver(); will(returnValue(false));
        }});
		
		ResolvedModelAttribute attr1 = new ResolvedModelAttribute("test", resolver, "param", "def");
		attr1.setEventable(true);
		
		assertEquals("ResolvedModelAttribute [name=test, resolver=modelResolver, param=param, default=def, eventable]", attr1.toString());
	}
}
