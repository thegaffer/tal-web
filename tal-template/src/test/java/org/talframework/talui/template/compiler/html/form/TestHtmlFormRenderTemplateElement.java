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

package org.talframework.talui.template.compiler.html.form;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.SimpleTemplateElementMold;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.compiler.html.fragments.ChildrenFragment;
import org.talframework.talui.template.compiler.html.fragments.InputFragment;
import org.talframework.talui.template.compiler.html.fragments.LabelFragment;
import org.talframework.talui.template.compiler.html.fragments.MemberFragment;
import org.talframework.talui.template.compiler.html.fragments.WrapperFragment;

/**
 * Tests that we generate a form element correctly.
 * 
 * @author Tom Spencer
 */
public class TestHtmlFormRenderTemplateElement {

	private Mockery context = new JUnit4Mockery();
	
	private Template template = null;
	private SimpleTemplateElementMold element = null;
	private GenericCompiler compiler = null;
	private TemplateRenderMold templateMold = null;
	
	@Before
	public void setup() {
		element = new SimpleTemplateElementMold();
		element.setWrapper(new WrapperFragment());
		element.addFragment(new LabelFragment());
		element.addFragment(new InputFragment());
		element.addFragment(new MemberFragment());
		element.addFragment(new ChildrenFragment());

		template = context.mock(Template.class);
		templateMold = context.mock(TemplateRenderMold.class);
		
		compiler = context.mock(GenericCompiler.class);
		
		context.checking(new Expectations() {{
			allowing(template).getName(); will(returnValue("template"));
			allowing(compiler).isStyle("dojo"); will(returnValue(false));
			allowing(compiler).isStyle("table-row"); will(returnValue(false));
			allowing(compiler).isStyle("no-label"); will(returnValue(false));
			allowing(compiler).isStyle("in-element"); will(returnValue(false));
		}});
	}
	
	@Test
	public void basic() {
		final DynamicProperty prop = context.mock(DynamicProperty.class);
		
		context.checking(new Expectations() {{
			atLeast(1).of(prop).getBehaviour(DynamicProperty.class); will(returnValue(prop));
			atLeast(1).of(prop).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			atLeast(1).of(prop).getName(); will(returnValue("prop1")); // Wrapper element
			oneOf(prop).getType(); will(returnValue("prop")); // Wrapper class
			oneOf(prop).getPropertySet("htmlWrapper"); will(returnValue(null));
			
			oneOf(prop).getPropertySet("htmlLabel"); will(returnValue(null));
			
			oneOf(prop).isHidden(); will(returnValue(false));
			oneOf(prop).getPropertySet("htmlField"); will(returnValue(null));
		}});
		
		element.compile(compiler, templateMold, template, prop);
		context.assertIsSatisfied();
	}
}
