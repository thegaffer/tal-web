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

import org.junit.Test;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.ModelEvent;
import org.talframework.talui.mvc.model.SimpleModelAttribute;

/**
 * This class tests the ModelEvent value object
 * 
 * @author Tom Spencer
 */
public class TestModelEvent {

	@Test
	public void basic() {
		ModelConfiguration config1 = new ModelConfiguration("test", new ArrayList<ModelAttribute>());
		ModelAttribute attr1 = new SimpleModelAttribute("test");
		
		ModelEvent event = new ModelEvent("source", config1, attr1, "old", "new");
		
		assertEquals("source", event.getSource());
		assertEquals(config1, event.getConfiguration());
		assertEquals(attr1, event.getAttribute());
		assertEquals("old", event.getOldValue());
		assertEquals("new", event.getNewValue());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		ModelConfiguration config1 = new ModelConfiguration("test", new ArrayList<ModelAttribute>());
		ModelAttribute attr1 = new SimpleModelAttribute("test");
		
		ModelEvent event1 = new ModelEvent("source", config1, attr1, "old", "new");
		ModelEvent event2 = new ModelEvent("source2", config1, attr1, "old2", "new2");
		ModelEvent event3 = new ModelEvent("source", config1, attr1, "old", "new");
		
		assertTrue(event1.hashCode() != 0);
		assertEquals(event1.hashCode(), event3.hashCode());
		assertTrue(event1.hashCode() != event2.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		ModelConfiguration config1 = new ModelConfiguration("test", new ArrayList<ModelAttribute>());
		ModelAttribute attr1 = new SimpleModelAttribute("test");
		
		ModelEvent event1 = new ModelEvent("source", config1, attr1, "old", "new");
		ModelEvent event2 = new ModelEvent("source2", config1, attr1, "old2", "new2");
		ModelEvent event3 = new ModelEvent("source", config1, attr1, "old", "new");
		
		assertTrue(event1.equals(event3));
		assertFalse(event1.equals(event2));
	}
}
