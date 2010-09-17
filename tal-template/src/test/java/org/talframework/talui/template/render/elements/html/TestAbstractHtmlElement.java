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

package org.talframework.talui.template.render.elements.html;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.render.elements.html.AbstractHtmlElement;
import org.talframework.talui.template.render.elements.html.Div;
import org.talframework.talui.util.htmlhelper.AttributeAdaptor;
import org.talframework.talui.util.htmlhelper.GenericElement;

/**
 * This class tests the abstract html element. It does this
 * via the use of the Div element, but it does not test the
 * div element itself.
 * 
 * @author Tom Spencer
 */
public class TestAbstractHtmlElement {

	/** The JMock context to get mocks from */
	private Mockery context = new JUnit4Mockery();
	/** The new writer for the test */
	private StringWriter writer = null;
	/** The mock template element (name is test) */
	private TemplateElement elem = null;
	/** The mock render model, returns a generic element and a writer */
	private RenderModel model = null;
	
	/**
	 * Sets up the writer, element and model
	 */
	@Before
	public void setup() {
		elem = context.mock(TemplateElement.class);
		writer = new StringWriter();
		model = context.mock(RenderModel.class);
		
		context.checking(new Expectations() {{
			allowing(elem).getName(); will(returnValue("test"));
			allowing(model).getGenericElement(); will(returnValue(new GenericElement((AttributeAdaptor)null)));
			allowing(model).getWriter(); will(returnValue(writer));
		}});
	}
	
	/**
	 * Tests output of the elements with an id
	 */
	@Test
	public void basic() throws Exception {
		AbstractHtmlElement tst = new Div(elem.getName());
		tst.render(model);
		assertEquals("<div id=\"test\"></div>", writer.toString());
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests the id suffix mechanism works
	 */
	@Test
	public void idSuffix() throws Exception {
		AbstractHtmlElement tst = new Div(elem.getName());
		tst.setIdSuffix("-fld");
		tst.render(model);
		assertEquals("<div id=\"test-fld\"></div>", writer.toString());
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests the id prefix mechanism works
	 */
	@Test
	public void idPrefix() throws Exception {
		AbstractHtmlElement tst = new Div(elem.getName());
		tst.setIdPrefix("fld-");
		tst.render(model);
		assertEquals("<div id=\"fld-test\"></div>", writer.toString());
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests the other common HTML attributes
	 */
	@Test
	public void commonAttributes() throws Exception {
		Map<String, String> props = new HashMap<String, String>();
		props.put("style", "style");
		props.put("class", "class");
		props.put("title", "title");
		props.put("tabIndex", "tab");
		props.put("accessKey", "access");
		
		AbstractHtmlElement tst = new Div(elem.getName());
		tst.setAttributes(props);
		tst.render(model);
		
		String elem = "<div id=\"test\" style=\"style\" class=\"class\" title=\"title\" tabindex=\"tab\" accesskey=\"access\"></div>";
		assertEquals(elem, writer.toString());
		context.assertIsSatisfied();
	}
	
	@Test
	public void addingStyles() {
		AbstractHtmlElement tst = new Div(elem.getName());
		
		// Basic
		assertNull(tst.getStyleClass());
		tst.setStyleClass("test");
		assertEquals("test", tst.getStyleClass());
		tst.setStyleClass(null);
		assertNull(tst.getStyleClass());
		
		// Adding
		tst.addStyleClass("test");
		assertEquals("test", tst.getStyleClass());
		tst.addStyleClass("another");
		assertEquals("test another", tst.getStyleClass());
		tst.addStyleClass("test");
		assertEquals("test another", tst.getStyleClass());
		tst.addStyleClass("please");
		assertEquals("test another please", tst.getStyleClass());
		
	}
	
	@Test
	public void addingMultipleStyles() {
		AbstractHtmlElement tst = new Div(elem.getName());
		
		tst.addStyleClasses("test1 test2");
		assertEquals("test1 test2", tst.getStyleClass());
		tst.addStyleClasses("test3 test4 test3");
		assertEquals("test1 test2 test3 test4", tst.getStyleClass());
		tst.addStyleClasses("test5");
		assertEquals("test1 test2 test3 test4 test5", tst.getStyleClass());
	}
}
