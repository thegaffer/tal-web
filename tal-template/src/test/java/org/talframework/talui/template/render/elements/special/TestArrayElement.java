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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.RenderNode;
import org.talframework.talui.template.behaviour.MemberProperty;
import org.talframework.talui.template.render.elements.special.ArrayElement;

/**
 * Tests the special Array render element.
 * 
 * @author Tom Spencer
 */
public class TestArrayElement {

	private Mockery context = new JUnit4Mockery();
	
	/** The array element to test */
	private ArrayElement element = null;
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
		
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
		}});
	}
	
	@Test
	public void basic() throws IOException {
		final Object[] tst = new Object[]{"test1", "test2"};
		
		context.checking(new Expectations() {{
			oneOf(node).getProperty("test"); will(returnValue(tst));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(model).pushNode("0", 0); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).pushNode("1", 1); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).popNode();
		}});
	
		element = new ArrayElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void emptyArray() throws IOException {
		final Object[] tst = new Object[0];
		
		context.checking(new Expectations() {{
			oneOf(node).getProperty("test"); will(returnValue(tst));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(model).pushNode("0", 0); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).popNode();
		}});
		
		element = new ArrayElement(property.getName(), template);
		element.setShowIfNull(true);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void nullArray() throws IOException {
		context.checking(new Expectations() {{
			oneOf(node).getProperty("test"); will(returnValue(null));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(model).pushNode("0", 0); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
			oneOf(model).popNode();
		}});
		
		element = new ArrayElement(property.getName(), template);
		element.setShowIfNull(true);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void nullArrayNoShow() throws IOException {
		context.checking(new Expectations() {{
			oneOf(node).getProperty("test"); will(returnValue(null));
		}});
		
		element = new ArrayElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test(expected=ClassCastException.class)
	public void invalidArray() throws IOException {
		final String tst = "not an array";
		
		context.checking(new Expectations() {{
			oneOf(node).getProperty("test"); will(returnValue(tst));
		}});
		
		element = new ArrayElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
}
