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

package org.tpspencer.tal.mvc.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;

import org.junit.Test;
import org.tpspencer.tal.mvc.render.BasicRenderModel;

public class TestBasicRenderModel {

	@Test
	public void basic() {
		BasicRenderModel model = new BasicRenderModel();
		model.setTemplate("testTemplate");
		Assert.assertEquals("testTemplate", model.getTemplate());
		
		Assert.assertNull(model.getAttributes());
		
		model.setAttribute("test", "testing");
		Assert.assertEquals("testing", model.getAttribute("test"));
		Assert.assertNotNull(model.getAttributes());
		
		model.setAttribute("another", "test2");
		Assert.assertEquals("test2", model.getAttribute("another"));
		Assert.assertNotNull(model.getAttributes());
	}
	
	/**
	 * Tests instance stringifies ok
	 */
	@Test
	public void stringify() {
		BasicRenderModel model = new BasicRenderModel();
		model.setTemplate("template");
		assertEquals("BasicRenderModel: template=template, attributes=null", model.toString());
	}
	
	/**
	 * Tests the hashcode behaves as predicted
	 */
	@Test
	public void testHashCode() {
		BasicRenderModel test1 = new BasicRenderModel(); test1.setTemplate("template1");
		BasicRenderModel test2 = new BasicRenderModel(); test2.setTemplate("template2");
		BasicRenderModel test3 = new BasicRenderModel(); test3.setTemplate("template1");
		
		assertTrue(test1.hashCode() != 0);
		assertTrue(test1.hashCode() != test2.hashCode());
		assertEquals(test1.hashCode(), test3.hashCode());
	}
	
	/**
	 * Test the equals method behaves as predicted
	 */
	@Test
	public void testEquals() {
		BasicRenderModel test1 = new BasicRenderModel(); test1.setTemplate("template1");
		BasicRenderModel test2 = new BasicRenderModel(); test2.setTemplate("template2");
		BasicRenderModel test3 = new BasicRenderModel(); test3.setTemplate("template1");
		
		assertFalse(test1.equals(test2));
		assertTrue(test1.equals(test3));
	}
}
