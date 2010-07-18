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

package org.tpspencer.tal.template.render.elements.html;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.behaviour.DynamicProperty;
import org.tpspencer.tal.template.render.elements.html.Label;
import org.tpspencer.tal.util.htmlhelper.AttributeAdaptor;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

public class TestLabel {

	/** The JMock context to get mocks from */
	private Mockery context = new JUnit4Mockery();
	/** The new writer for the test */
	private StringWriter writer = null;
	/** The mock template element (name is test) */
	private DynamicProperty elem = null;
	/** The mock render model, returns a generic element and a writer */
	private RenderModel model = null;
	
	/**
	 * Sets up the writer, element and model
	 */
	@Before
	public void setup() {
		elem = context.mock(DynamicProperty.class);
		writer = new StringWriter();
		model = context.mock(RenderModel.class);
		
		context.checking(new Expectations() {{
			allowing(elem).getName(); will(returnValue("test"));
			allowing(model).getGenericElement(); will(returnValue(new GenericElement((AttributeAdaptor)null)));
			allowing(model).getWriter(); will(returnValue(writer));
		}});
	}
	
	@Test
	public void basic() throws IOException {
		context.checking(new Expectations() {{
			oneOf(model).getMessage("label.test", "label.test"); will(returnValue("testLabel"));
		}});
		
		Label lbl = new Label("label.test");
		lbl.setForField("test");
		lbl.render(model);
		assertEquals("<label for=\"test\">testLabel</label>", writer.toString());
		context.assertIsSatisfied();
	}
	
	// TODO: Test label with a prefix
}
