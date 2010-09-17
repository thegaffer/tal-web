/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.render.elements.special;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.RenderNode;
import org.talframework.talui.template.behaviour.MemberProperty;
import org.talframework.talui.template.render.elements.special.MapElement;

/**
 * Tests the special Array render element.
 * 
 * @author Tom Spencer
 */
public class TestMapElement {

	private Mockery context = new JUnit4Mockery();
	
	/** The array element to test */
	private MapElement element = null;
	/** The property we want the array element to act upon, name is test */
	private MemberProperty property = null;
	/** The render element for inner template */
	private RenderElement template = null;
	/** The render model to use */
	private RenderModel model = null;
	/** The node returned by the model */
	private RenderNode node = null;
	
	@Before
	public void setup() {
		model = context.mock(RenderModel.class);
		node = context.mock(RenderNode.class);
		property = context.mock(MemberProperty.class);
		template = context.mock(RenderElement.class);
	}
	
	@Test
	public void basic() throws IOException {
		final Map<String, String> tst = new HashMap<String, String>();
		tst.put("it1", "test1");
		tst.put("it2", "test2");
		
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(tst));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(model).pushNode("it1", 0); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).pushNode("it2", 1); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).popNode();
		}});
		
		element = new MapElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void defaultKey() throws IOException {
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(null));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(model).pushNode("defaultKey", 0); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).popNode();
		}});
		
		element = new MapElement(property.getName(), template);
		element.setKeyIfNull("defaultKey");
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void emptyMap() throws IOException {
		final Map<String, String> tst = new HashMap<String, String>();
		
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(tst));
		}});
		
		element = new MapElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test(expected=ClassCastException.class)
	public void invalidMap() throws IOException {
		final String tst = "not a map";
		
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(tst));
		}});
		
		element = new MapElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void nullMap() throws IOException {
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(null));
		}});
		
		element = new MapElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
}
