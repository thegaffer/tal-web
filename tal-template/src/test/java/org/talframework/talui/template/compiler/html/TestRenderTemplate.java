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

package org.talframework.talui.template.compiler.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.util.ArrayList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.compiler.BasicTemplateRenderMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.SimpleTemplateElementMold;
import org.talframework.talui.template.compiler.TemplateElementRenderMold;
import org.talframework.talui.template.compiler.html.fragments.ChildrenFragment;
import org.talframework.talui.template.compiler.html.fragments.LabelFragment;
import org.talframework.talui.template.compiler.html.fragments.MemberFragment;
import org.talframework.talui.template.compiler.html.fragments.ValueFragment;
import org.talframework.talui.template.compiler.html.fragments.WrapperFragment;
import org.talframework.talui.util.htmlhelper.AttributeAdaptor;
import org.talframework.talui.util.htmlhelper.GenericElement;

/**
 * Tests the main HTML render template.
 * 
 * TODO: Add in tests for named elements and behaviour
 * 
 * @author Tom Spencer
 */
public class TestRenderTemplate {

	private Mockery context = new JUnit4Mockery();
	
	private GenericCompiler compiler = null;
	private BasicTemplateRenderMold renderTemplate = null;
	
	@Before
	public void setup() {
		compiler = context.mock(GenericCompiler.class);
		renderTemplate = new BasicTemplateRenderMold();
		renderTemplate.setDefaultMold(new SimpleTemplateElementMold() {{
			setWrapper(new WrapperFragment());
			addFragment(new LabelFragment());
			addFragment(new ValueFragment());
			addFragment(new MemberFragment());
			addFragment(new ChildrenFragment());
		}});
	}
	
	@Test
	public void basic() {
		final Template template = context.mock(Template.class);
		
		context.checking(new Expectations() {{
			allowing(compiler).isStyle("table-row"); will(returnValue(false));
			oneOf(template).getElements(); will(returnValue(null));
		}});
		
		RenderElement re = renderTemplate.compile(compiler, template);
		context.assertIsSatisfied();
		assertNotNull(re);
		testOutput(re, "");
	}
	
	@Test
	@SuppressWarnings("serial")
	public void withChildren() {
		final Template template = context.mock(Template.class);
		final TemplateElement child1 = context.mock(TemplateElement.class, "child1");
		final TemplateElement child2 = context.mock(TemplateElement.class, "child2");
		
		context.checking(new Expectations() {{
			allowing(compiler).isStyle("table-row"); will(returnValue(false));
			allowing(compiler).isStyle("no-label"); will(returnValue(false));
			
			allowing(template).getName(); will(returnValue("template"));
			oneOf(template).getElements(); will(returnValue(new ArrayList<TemplateElement>() {{ add(child1); add(child2); }}));
			
			atLeast(1).of(child1).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			allowing(child1).getName(); will(returnValue("child1"));
			oneOf(compiler).isStyle("in-element"); will(returnValue(false));
			allowing(child1).getType(); will(returnValue("text-prop"));
			oneOf(child1).getPropertySet("htmlWrapper"); will(returnValue(null));
			oneOf(child1).hasSetting("label"); will(returnValue(false));
			
			atLeast(1).of(child2).getBehaviour((Class<?>)with(anything())); will(returnValue(null));
			allowing(child2).getName(); will(returnValue("child2"));
			oneOf(compiler).isStyle("in-element"); will(returnValue(false));
			allowing(child2).getType(); will(returnValue("text-prop"));
			oneOf(child2).getPropertySet("htmlWrapper"); will(returnValue(null));
			oneOf(child2).hasSetting("label"); will(returnValue(false));
		}});
		
		RenderElement re = renderTemplate.compile(compiler, template);
		context.assertIsSatisfied();
		assertNotNull(re);
		testOutput(re, "<div id=\"child1-grp\" class=\"text-prop role-wrapper template-child1\"></div>\n<div id=\"child2-grp\" class=\"text-prop role-wrapper template-child2\"></div>\n");
	}
	
	@Test
	public void testStrongStyleMatch() {
		final TemplateElementRenderMold defaultMold = context.mock(TemplateElementRenderMold.class, "defaultTemplate");
		final TemplateElementRenderMold weakStyleMold = context.mock(TemplateElementRenderMold.class, "lesserStyleMold");
		final TemplateElementRenderMold strongStyleMold = context.mock(TemplateElementRenderMold.class, "styleMold");
		final TemplateElement testChild = context.mock(TemplateElement.class, "testChild");
		
		context.checking(new Expectations() {{
			allowing(testChild).getName(); will(returnValue("test"));
			allowing(testChild).getType(); will(returnValue("specific"));
			allowing(compiler).isStyle("form"); will(returnValue(true));
			allowing(compiler).isStyle("table"); will(returnValue(true));
			
			oneOf(strongStyleMold).compile(compiler, renderTemplate, null, testChild); will(returnValue(null));
		}});
		
		renderTemplate.setDefaultMold(defaultMold);
		renderTemplate.addTypedMold("specific", "form", weakStyleMold);
		renderTemplate.addTypedMold("specific", "form,table", strongStyleMold);
		renderTemplate.compileChild(compiler, null, testChild);
		context.assertIsSatisfied();
	}
	
	@Test
	public void testWeakStyleMatch() {
		final TemplateElementRenderMold defaultMold = context.mock(TemplateElementRenderMold.class, "defaultTemplate");
		final TemplateElementRenderMold weakStyleMold = context.mock(TemplateElementRenderMold.class, "lesserStyleMold");
		final TemplateElementRenderMold strongStyleMold = context.mock(TemplateElementRenderMold.class, "styleMold");
		final TemplateElement testChild = context.mock(TemplateElement.class, "testChild");
		
		context.checking(new Expectations() {{
			allowing(testChild).getName(); will(returnValue("test"));
			allowing(testChild).getType(); will(returnValue("specific"));
			allowing(compiler).isStyle("form"); will(returnValue(true));
			allowing(compiler).isStyle("table"); will(returnValue(false));
			
			oneOf(weakStyleMold).compile(compiler, renderTemplate, null, testChild); will(returnValue(null));
		}});
		
		renderTemplate.setDefaultMold(defaultMold);
		renderTemplate.addTypedMold("specific", "form", weakStyleMold);
		renderTemplate.addTypedMold("specific", "form,table", strongStyleMold);
		renderTemplate.compileChild(compiler, null, testChild);
		context.assertIsSatisfied();
	}
		
	/**
	 * This internal helper can be used to test the actual output
	 * of the rendered element. This should be done with <b>care</b>
	 * because were using another element here to test the output
	 * of one. There could be an error in the renderer, which is not
	 * under test ... so ...
	 * 
	 * <p><b>IF A TEST FAILS HERE IT MIGHT NOT BE BROKEN!</b></p>
	 * 
	 * @param re
	 * @param expected
	 */
	private void testOutput(RenderElement re, String expected) {
		final StringWriter writer = new StringWriter();
		final RenderModel model = context.mock(RenderModel.class);
		context.checking(new Expectations() {{
			allowing(model).getGenericElement(); will(returnValue(new GenericElement((AttributeAdaptor)null)));
			allowing(model).getWriter(); will(returnValue(writer));
		}});
		
		try {
			re.render(model);
			if( expected == null ) System.out.println(writer.toString());
			else assertEquals(expected, writer.toString());
		}
		catch( Exception e ) {
			throw new RuntimeException("Failed to test output - the class might not be broken, it might be the renderer!");
		}
	}
}
