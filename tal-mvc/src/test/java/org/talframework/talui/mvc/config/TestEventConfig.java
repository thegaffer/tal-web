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

import static org.junit.Assert.*;

import org.junit.Test;
import org.tpspencer.tal.mvc.config.EventConfig;

/**
 * Tests the basic operation of the EventConfig 
 * value object.
 * 
 * @author Tom Spencer
 */
public class TestEventConfig {

	/**
	 * Basic operation
	 */
	@Test
	public void basic() {
		EventConfig event = new EventConfig("test", "onTest", "newValue");
		
		assertEquals("test", event.getAttr());
		assertEquals("onTest", event.getAction());
		assertEquals("newValue", event.getNewValueName());
	}
	
	/**
	 * With an old value attribute name
	 */
	@Test
	public void withOldValue() {
		EventConfig event = new EventConfig("test", "onTest", "newValue", "oldValue");
		
		assertEquals("oldValue", event.getOldValueName());
	}
	
	/**
	 * Tests instance stringifies ok
	 */
	@Test
	public void stringify() {
		EventConfig event = new EventConfig("test", "onTest", "newValue", "oldValue");
		assertEquals("EventConfig [name=test, action=onTest, new=newValue, old=oldValue]", event.toString());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		EventConfig test1 = new EventConfig("test", "onTest", "newValue", "oldValue");
		EventConfig test2 = new EventConfig("test2", "onTest", "newValue", "oldValue");
		EventConfig test3 = new EventConfig("test", "onTest", "newValue", "oldValue");
		
		assertTrue(test1.hashCode() != 0);
		assertTrue(test1.hashCode() != test2.hashCode());
		assertEquals(test1.hashCode(), test3.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		EventConfig test1 = new EventConfig("test", "onTest", "newValue", "oldValue");
		EventConfig test2 = new EventConfig("test2", "onTest", "newValue", "oldValue");
		EventConfig test3 = new EventConfig("test", "onTest", "newValue", "oldValue");
		
		assertFalse(test1.equals(test2));
		assertTrue(test1.equals(test3));
	}
}
