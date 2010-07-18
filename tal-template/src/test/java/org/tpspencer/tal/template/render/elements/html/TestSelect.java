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
import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.behaviour.property.CodedProperty;
import org.tpspencer.tal.template.render.codes.CodeType;
import org.tpspencer.tal.template.render.codes.CodeTypeFactory;
import org.tpspencer.tal.template.render.codes.CodeTypeFactoryLocator;
import org.tpspencer.tal.template.render.elements.html.Select;
import org.tpspencer.tal.util.htmlhelper.AttributeAdaptor;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

/**
 * This class tests the select html elements
 * 
 * @author Tom Spencer
 */
public class TestSelect {

	/** The JMock context to get mocks from */
	private Mockery context = new JUnit4Mockery();
	/** The new writer for the test */
	private StringWriter writer = null;
	/** The mock template element (name is test) */
	private CodedProperty elem = null;
	/** The mock render model, returns a generic element and a writer */
	private RenderModel model = null;
	/** Holds our code type mock */
	private CodeType codeType = null;
	
	/**
	 * Sets up the writer, element and model
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		elem = context.mock(CodedProperty.class);
		writer = new StringWriter();
		model = context.mock(RenderModel.class);
		codeType = context.mock(CodeType.class); 
		final CodeTypeFactory typeFactory = context.mock(CodeTypeFactory.class);
		
		final String[] codes = new String[]{"code1", "code2"};
		
		context.checking(new Expectations() {{
			allowing(elem).getName(); will(returnValue("test"));
			allowing(model).getLocale(); will(returnValue(null));
			allowing(model).getUser(); will(returnValue(null));
			allowing(model).getGenericElement(); will(returnValue(new GenericElement((AttributeAdaptor)null)));
			allowing(model).getWriter(); will(returnValue(writer));
			allowing(typeFactory).getCodeType((Locale)with(anything()), (Principal)with(anything()), (Map<String, String>)with(anything())); will(returnValue(codeType));
			allowing(codeType).getType(); will(returnValue("testCodes"));
			allowing(codeType).getCodes(); will(returnValue(codes));
			allowing(codeType).getCodeDescription("code1"); will(returnValue("Code 1"));
			allowing(codeType).getCodeDescription("code2"); will(returnValue("Code 2"));
		}});
		
		CodeTypeFactoryLocator.getInstance().addCodeFactory("testCodes", typeFactory);
	}
	
	@Test
	public void basic() throws IOException {
		context.checking(new Expectations() {{
			oneOf(elem).getCodeValue(model); will(returnValue("code2")); // Attribute
			oneOf(elem).getCodeValue(model); will(returnValue("code2")); // For value
			
			allowing(elem).getCodeType(model); will(returnValue("testCodes"));
			oneOf(model).getObject("testCodes"); will(returnValue(null));
		}});
		
		Select sel = new Select(elem);
		sel.render(model);
		
		String exp = "<select id=\"test\" value=\"code2\" name=\"test\">";
		exp += "<option value=\"code1\">Code 1</option>";
		exp += "<option value=\"code2\" selected=\"selected\">Code 2</option>";
		exp += "</select>";
		
		assertEquals(exp, writer.toString());
		context.assertIsSatisfied();
	}
	
	/**
	 * As basic, but the code type is returned from the model directly
	 */
	@Test
	public void modelCodeType() throws IOException {
		context.checking(new Expectations() {{
			oneOf(elem).getCodeValue(model); will(returnValue("code2")); // Attr
			oneOf(elem).getCodeValue(model); will(returnValue("code2")); // Value
			
			allowing(elem).getCodeType(model); will(returnValue("testCodes"));
			oneOf(model).getObject("testCodes"); will(returnValue(codeType));
		}});
		
		Select sel = new Select(elem);
		sel.render(model);
		
		String exp = "<select id=\"test\" value=\"code2\" name=\"test\">";
		exp += "<option value=\"code1\">Code 1</option>";
		exp += "<option value=\"code2\" selected=\"selected\">Code 2</option>";
		exp += "</select>";
		
		assertEquals(exp, writer.toString());
		context.assertIsSatisfied();
	}

	/**
	 * Ensures we get a failure if there is an invalid code type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidCodeType() throws IOException {
		context.checking(new Expectations() {{
			oneOf(elem).getCodeValue(model); will(returnValue("code2")); // Attribute
			oneOf(elem).getCodeValue(model); will(returnValue("code2")); // For value
			
			allowing(elem).getCodeType(model); will(returnValue("invalidCodes"));
			oneOf(model).getObject("invalidCodes"); will(returnValue(null));
		}});
		
		Select sel = new Select(elem);
		sel.render(model);
	}
}
