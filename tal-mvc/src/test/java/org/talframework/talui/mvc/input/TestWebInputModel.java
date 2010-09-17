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

package org.talframework.talui.mvc.input;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.input.WebInputModel;

/**
 * This class tests an Input Model
 * 
 * @author Tom Spencer
 */
public class TestWebInputModel {
	
	private WebInputModel input = null;
	
	/**
	 * Sets up an InputModel for us
	 */
	@Before
	public void setup() {
		Map<String, String[]> params = new HashMap<String, String[]>();
		
		params.put("simple", new String[]{"simpleval"});
		params.put("multi", new String[]{"val1", "val2"});
		params.put("empty", new String[]{});
		
		input = new WebInputModel(params);
	}

	/**
	 * Ensures we can get a single parameter
	 */
	@Test
	public void single() {
		assertEquals("simpleval", input.getParameter("simple"));
	}
	
	/**
	 * Ensures we can get a multi-value parameter
	 */
	@Test
	public void multiValue() {
		String[] val = input.getParameterValues("multi"); 
		assertNotNull(val);
		assertEquals(2, val.length);
		assertEquals("val1", val[0]);
		assertEquals("val2", val[1]);
	}
	
	/**
	 * Ensures we get the map of parameters
	 */
	@Test
	public void getParameters() {
		Map<String, String[]> params = input.getParameters();
		assertNotNull(params);
		assertEquals(3, params.size());
		assertEquals("simpleval", params.get("simple")[0]);
		assertNotNull("multi");
		assertEquals(0, params.get("empty").length);
	}
	
	/**
	 * Tests we get an empty parameter as null
	 */
	@Test
	public void empty() {
		assertNull(input.getParameter("empty"));
	}
	
	/**
	 * Ensures we get null if parameter not found
	 */
	@Test
	public void invalid() {
		assertNull(input.getParameter("invalid"));
	}
	
	/**
	 * Checks the property exists
	 */
	@Test
	public void hasParameter() {
		assertTrue(input.hasParameter("simple"));
		assertTrue(input.hasParameter("multi"));
		assertTrue(input.hasParameter("empty"));
		assertFalse(input.hasParameter("invalid"));
	}
	
	/**
	 * Checks that multivalue returns ok for different props
	 */
	@Test
	public void hasMultiValue() {
		assertFalse(input.hasMultiValue("simple"));
		assertTrue(input.hasMultiValue("multi"));
		assertFalse(input.hasMultiValue("empty"));
		assertFalse(input.hasMultiValue("invalid"));
	}
	
	/**
	 * Checks we stringify ok
	 */
	@Test
	public void stringify() {
		assertEquals("WebInputModel [params={multi={val1, val2}, simple=simpleval, empty}]", input.toString());
	}
}
