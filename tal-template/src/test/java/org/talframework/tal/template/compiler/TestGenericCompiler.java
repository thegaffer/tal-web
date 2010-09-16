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

package org.tpspencer.tal.template.compiler;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Renderer;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.compiler.SimpleGenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;

/**
 * Tests the SimpleGenericCompuiler
 * 
 * TODO: Need a test that styles/templateStyles set during template compilation
 *  
 * @author Tom Spencer
 */
public class TestGenericCompiler {

	private Mockery context = new JUnit4Mockery();
	
	private SimpleGenericCompiler compiler = null;
	private TemplateRenderMold defaultMold = null;
	private TemplateConfiguration config = null;
	private Template root = null;
	
	@SuppressWarnings("serial")
	@Before
	public void setup() {
		compiler = new SimpleGenericCompiler();
		
		defaultMold = context.mock(TemplateRenderMold.class, "default");
		
		compiler.setMold(defaultMold);
		
		config = context.mock(TemplateConfiguration.class);
		root = context.mock(Template.class);
		final Map<String, Template> templates = new HashMap<String, Template>() {{ put("root", root); }};
		
		context.checking(new Expectations() {{
			allowing(config).getName(); will(returnValue("/testTemplate"));
			allowing(config).getMainTemplate(); will(returnValue(root));
			allowing(config).getTemplates(); will(returnValue(templates));
		}});
	}
	
	@Test
	public void simple() {
		context.checking(new Expectations() {{
			oneOf(root).getName(); will(returnValue("root"));
			oneOf(defaultMold).compile(compiler, root);
		}});
		
		Renderer res = compiler.compile(config);
		context.assertIsSatisfied();
		assertNotNull(res);
	}
	
	@Test
	public void compileTemplate() {
		context.checking(new Expectations() {{
			oneOf(root).getName(); will(returnValue("root"));
			oneOf(defaultMold).compile(compiler, root);
			oneOf(root).getName(); will(returnValue("root"));
		}});
		
		compiler.compile(config);
		RenderElement tst = compiler.compileTemplate("root", null, null);
		context.assertIsSatisfied();
		assertNotNull(tst);
	}
	
	/**
	 * Tests that we do compile the same template twice if styles
	 * are different.
	 */
	@Test
	public void compileTemplateTwiceOnStyleChange() {
		context.checking(new Expectations() {{
			oneOf(root).getName(); will(returnValue("root"));
			oneOf(defaultMold).compile(compiler, root);
			oneOf(root).getName(); will(returnValue("root"));
			oneOf(defaultMold).compile(compiler, root);
			oneOf(root).getName(); will(returnValue("root"));
		}});
		
		compiler.compile(config);
		compiler.compileTemplate("root", null, null);
		compiler.addStyle("form");
		compiler.compileTemplate("root", null, null);
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests that we do not compile the same template twice if 
	 * styles are no different.
	 */
	@Test
	public void compileTemplateTwiceNoStyleChange() {
		context.checking(new Expectations() {{
			oneOf(root).getName(); will(returnValue("root"));
			oneOf(defaultMold).compile(compiler, root);
			oneOf(root).getName(); will(returnValue("root"));
			oneOf(root).getName(); will(returnValue("root"));
		}});
		
		compiler.compile(config);
		compiler.compileTemplate("root", null, null);
		compiler.compileTemplate("root", null, null);
		context.assertIsSatisfied();
	}
}
