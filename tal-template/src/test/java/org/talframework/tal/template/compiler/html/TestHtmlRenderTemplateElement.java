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

package org.tpspencer.tal.template.compiler.html;

import static org.junit.Assert.assertNotNull;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.behaviour.DynamicProperty;
import org.tpspencer.tal.template.behaviour.MemberProperty;
import org.tpspencer.tal.template.behaviour.supporting.ContainerElement;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.SimpleTemplateElementMold;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;
import org.tpspencer.tal.template.compiler.html.fragments.ChildrenFragment;
import org.tpspencer.tal.template.compiler.html.fragments.InputFragment;
import org.tpspencer.tal.template.compiler.html.fragments.LabelFragment;
import org.tpspencer.tal.template.compiler.html.fragments.MemberFragment;
import org.tpspencer.tal.template.compiler.html.fragments.WrapperFragment;

public class TestHtmlRenderTemplateElement {
	
	private Mockery context = new JUnit4Mockery();
	
	private Template template = null;
	private SimpleTemplateElementMold renderElement = null;
	private GenericCompiler compiler = null;
	private TemplateRenderMold templateMold = null;

	@Before
	public void setup() {
		renderElement = new SimpleTemplateElementMold();
		renderElement.setWrapper(new WrapperFragment());
		renderElement.addFragment(new LabelFragment());
		renderElement.addFragment(new InputFragment());
		renderElement.addFragment(new MemberFragment());
		renderElement.addFragment(new ChildrenFragment());
		
		compiler = context.mock(GenericCompiler.class);
		templateMold = context.mock(TemplateRenderMold.class);
		template = context.mock(Template.class);
		
		context.checking(new Expectations() {{
			allowing(template).getName(); will(returnValue("template"));
			allowing(compiler).isStyle("table-row"); will(returnValue(false));
			allowing(compiler).isStyle("no-label"); will(returnValue(false));
			allowing(compiler).isStyle("in-element"); will(returnValue(false));
		}});
	}
	
	@Test
	public void basic() {
		final TemplateElement element = context.mock(TemplateElement.class);
		
		context.checking(new Expectations() {{
			atLeast(1).of(element).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			atLeast(1).of(element).getName(); will(returnValue("test"));
			oneOf(element).getType(); will(returnValue("prop"));
			oneOf(element).getPropertySet("htmlWrapper"); will(returnValue(null));
			oneOf(element).hasSetting("label"); will(returnValue(false));
		}});
		
		RenderElement re = renderElement.compile(compiler, templateMold, template, element);
		context.assertIsSatisfied();
		assertNotNull(re);
	}
	
	@Test
	public void children() {
		final ContainerElement element = context.mock(ContainerElement.class);
		
		context.checking(new Expectations() {{
			atLeast(1).of(element).getBehaviour(ContainerElement.class); will(returnValue(element));
			atLeast(1).of(element).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			atLeast(1).of(element).getName(); will(returnValue("test"));
			oneOf(element).getType(); will(returnValue("group"));
			oneOf(element).getPropertySet("htmlWrapper"); will(returnValue(null));
			oneOf(element).hasSetting("label"); will(returnValue(false));
			
			oneOf(element).getElements(); 
		}});
		
		RenderElement re = renderElement.compile(compiler, templateMold, template, element);
		context.assertIsSatisfied();
		assertNotNull(re);
	}
	
	@Test
	public void property() {
		final DynamicProperty element = context.mock(DynamicProperty.class);
		
		context.checking(new Expectations() {{
			atLeast(1).of(element).getBehaviour(DynamicProperty.class); will(returnValue(element));
			atLeast(1).of(element).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			allowing(element).getName(); will(returnValue("test"));
			oneOf(element).getType(); will(returnValue("prop"));
			oneOf(element).getPropertySet("htmlWrapper"); will(returnValue(null));
			oneOf(element).getPropertySet("htmlLabel"); will(returnValue(null));
			oneOf(element).isHidden(); will(returnValue(false));
			oneOf(element).getPropertySet("htmlField"); will(returnValue(null));
			
			// Get Value for test render
			allowing(element).getValue((RenderModel)with(anything())); will(returnValue("tst"));
		}});
		
		RenderElement re = renderElement.compile(compiler, templateMold, template, element);
		context.assertIsSatisfied();
		assertNotNull(re);
	}
	
	@Test
	public void member() {
		final MemberProperty element = context.mock(MemberProperty.class);
		
		context.checking(new Expectations() {{
			atLeast(1).of(element).getBehaviour(MemberProperty.class); will(returnValue(element));
			atLeast(1).of(element).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			allowing(element).getName(); will(returnValue("test"));
			oneOf(element).getType(); will(returnValue("prop"));
			oneOf(element).getPropertySet("htmlWrapper"); will(returnValue(null));
			oneOf(element).hasSetting("label"); will(returnValue(false));
			oneOf(element).getTemplate(); will(returnValue("template"));
			oneOf(compiler).compileTemplate("template", null, null);
			oneOf(element).isTypeKnown(); will(returnValue(true));
			oneOf(element).isMap(); will(returnValue(false));
			oneOf(element).isCollection(); will(returnValue(false));
			oneOf(element).isArray(); will(returnValue(false));
		}});
		
		RenderElement re = renderElement.compile(compiler, templateMold, template, element);
		context.assertIsSatisfied();
		assertNotNull(re);
	}
}
