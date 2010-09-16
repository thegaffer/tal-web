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
import org.tpspencer.tal.template.render.elements.special.EmptyElement;

/**
 * Tests the special Array render element.
 * 
 * @author Tom Spencer
 */
public class TestEmptyElement {

	private Mockery context = new JUnit4Mockery();
	
	/** The member element to test */
	private EmptyElement element = null;
	/** The mock child render elements */
	private RenderElement childElement = null;
	/** The render model to use */
	private RenderModel model = null;
	
	@Before
	public void setup() {
		model = context.mock(RenderModel.class);
		childElement = context.mock(RenderElement.class);
		
		element = new EmptyElement();
	}
	
	@Test
	public void basic() throws IOException {
		element.addElement(childElement);
		
		context.checking(new Expectations() {{
			oneOf(childElement).render(model);
		}});
		
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void multiple() throws IOException {
		element.addElement(childElement);
		element.addElement(childElement);
		
		context.checking(new Expectations() {{
			oneOf(childElement).render(model);
			oneOf(childElement).render(model);
		}});
		
		element.render(model);
		context.assertIsSatisfied();
	}
	
	@Test
	public void noChildren() throws IOException {
		element.render(model);
		context.assertIsSatisfied();
	}
}
