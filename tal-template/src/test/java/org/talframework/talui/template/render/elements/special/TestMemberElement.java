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

package org.tpspencer.tal.template.render.elements.special;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.RenderNode;
import org.tpspencer.tal.template.behaviour.MemberProperty;
import org.tpspencer.tal.template.render.elements.special.MemberElement;

/**
 * Tests the special Array render element.
 * 
 * @author Tom Spencer
 */
public class TestMemberElement {

	private Mockery context = new JUnit4Mockery();
	
	/** The member element to test */
	private MemberElement element = null;
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
		final Object tst = "testing";
		
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(tst));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
		}});
		
		element = new MemberElement(property.getName(), template);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void nullObject() throws IOException {
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(null));
			oneOf(model).pushNode("test", -1); will(returnValue(node));
			oneOf(template).render(model);
			oneOf(model).popNode();
		}});
		
		element = new MemberElement(property.getName(), template);
		element.setShowIfNull(true);
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void nullObjectNoShow() throws IOException {
		context.checking(new Expectations() {{
			allowing(property).getName(); will(returnValue("test"));
			allowing(model).getCurrentNode(); will(returnValue(node));
			oneOf(node).getProperty("test"); will(returnValue(null));
		}});
		
		element = new MemberElement(property.getName(), template);
		element.setShowIfNull(false);
		element.render(model);
		context.assertIsSatisfied();
	}
}
