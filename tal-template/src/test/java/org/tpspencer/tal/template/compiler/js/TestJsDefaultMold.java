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

package org.tpspencer.tal.template.compiler.js;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.compiler.js.JsElementMold;
import org.tpspencer.tal.template.render.MockStringBufferAction;

/**
 * Tests the default mold finds event handlers for elements
 * at the wrapper, value and field level. Then creates one
 * variable for each of the wrapper, value, field that has 
 * at least 1 event handler. Finally creates a render element
 * to connect the element(s) to the handler.
 * 
 * <p><b>Note: </b>Some of the tests rely on the rendering of
 * the compiled elements. Separate tests cover these elements.</p>
 * 
 * @author Tom Spencer
 */
public class TestJsDefaultMold {

	private Mockery context = new JUnit4Mockery();
	private JsElementMold mold = null;
	private Template template = null;
	private TemplateElement element = null;
	
	/** The new writer for the test */
	private StringWriter writer = null;
	/** The mock render model, returns a generic element and a writer */
	private RenderModel model = null;
	
	@Before
	public void setup() {
		mold = new JsElementMold();
		
		template = context.mock(Template.class);
		element = context.mock(TemplateElement.class);
		
		writer = new StringWriter();
		model = context.mock(RenderModel.class);
		
		context.checking(new Expectations(){{
			allowing(template).getName(); will(returnValue("template"));
			allowing(element).getName(); will(returnValue("element"));
			allowing(element).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			allowing(model).getWriter(); will(returnValue(writer));
			allowing(model).getNamespace(); will(returnValue("tst"));
			allowing(model).getTempBuffer(); will(new MockStringBufferAction());
		}});
	}
	
	/**
	 * Tests nothing is produced if no props
	 */
	@Test
	public void noHandlers() {
		context.checking(new Expectations(){{
			atLeast(1).of(element).getPropertySet((String)with(anything())); will(returnValue(null));
		}});
		
		RenderElement ret = mold.compile(null, null, template, element);
		Assert.assertNull(ret);
		context.assertIsSatisfied();
	}
	
	@Test
	public void eventHandlers() throws IOException {
		final Map<String, String> props = new HashMap<String, String>();
		props.put("onBlur", "blur");
		props.put("onFocus", "focus");
		
		context.checking(new Expectations(){{
			oneOf(element).getPropertySet("wrapper"); will(returnValue(props));
			atLeast(1).of(element).getPropertySet((String)with(anything())); will(returnValue(null));
		}});
		
		RenderElement ret = mold.compile(null, null, template, element);
		Assert.assertNotNull(ret);
		context.assertIsSatisfied();
		
		ret.render(model);
		assertEqualsTestData("eventHandlers", "js", writer.toString());
	}
	
	@Test
	public void mouseHandlers() throws IOException {
		final Map<String, String> props = new HashMap<String, String>();
		props.put("onClick", "click");
		props.put("onDblClick", "dblClick");
		props.put("onMouseDown", "mouseDown");
		props.put("onMouseUp", "mouseUp");
		props.put("onMouseMove", "mouseMove");
		props.put("onMouseOver", "mouseOver");
		props.put("onMouseOut", "mouseOut");
		
		context.checking(new Expectations(){{
			oneOf(element).getPropertySet("label"); will(returnValue(props));
			atLeast(1).of(element).getPropertySet((String)with(anything())); will(returnValue(null));
		}});
		
		RenderElement ret = mold.compile(null, null, template, element);
		Assert.assertNotNull(ret);
		context.assertIsSatisfied();
		
		ret.render(model);
		assertEqualsTestData("mouseHandlers", "js", writer.toString());
	}
	
	@Test
	public void keyHandlers() throws IOException {
		final Map<String, String> props = new HashMap<String, String>();
		props.put("onKeyDown", "keyDown");
		props.put("onKeyUp", "keyUp");
		props.put("onKeyPress", "keyPress");
		
		context.checking(new Expectations(){{
			oneOf(element).getPropertySet("value"); will(returnValue(props));
			atLeast(1).of(element).getPropertySet((String)with(anything())); will(returnValue(null));
		}});
		
		RenderElement ret = mold.compile(null, null, template, element);
		Assert.assertNotNull(ret);
		context.assertIsSatisfied();
		
		ret.render(model);
		assertEqualsTestData("keyHandlers", "js", writer.toString());
	}
	
	/**
	 * Helper to get expected test results from a file into
	 * a string
	 * 
	 * @param name The name of the test
	 */
	private void assertEqualsTestData(String name, String type, String actual) {
		try {
			String tstRes = getClass().getPackage().getName().replace('.', '/') + "/" + name + "." + type;
			Reader reader = new InputStreamReader(
					this.getClass().getClassLoader().getResourceAsStream(tstRes));
			
			StringWriter writer = new StringWriter();
			char[] cbuf = new char[1000];
			int ln = -1;
			do {
				ln = reader.read(cbuf);
				if( ln > 0 ) {
					writer.write(cbuf, 0, ln);
				}
			} while( ln >= 0 );
			
			// Strip out line/carriage returns if they exist
			String ret = writer.toString();
			String test = ret.replaceAll("\r\n", "\n");
			
			assertEquals(test, actual);
		}
		catch( Exception e ) {
			System.out.println(actual);
			assertTrue("Actual tests not found or not loadable", false);
		}
	}
}
