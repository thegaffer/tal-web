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

import java.io.IOException;
import java.io.StringWriter;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.MockStringBufferAction;
import org.tpspencer.tal.template.render.UrlGenerator;
import org.tpspencer.tal.template.render.elements.html.Script;
import org.tpspencer.tal.template.render.elements.html.TemplateScript;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

public class TestScript {

	private Mockery context = new JUnit4Mockery();
	private StringWriter writer = null;
	private RenderModel model = null;
	private UrlGenerator generator = null; 
	
	@Before
	public void setup() {
		model = context.mock(RenderModel.class);
		writer = new StringWriter();
		final GenericElement elem = new GenericElement();
		generator = context.mock(UrlGenerator.class);
		
		context.checking(new Expectations() {{
			allowing(model).getWriter(); will(returnValue(writer));
			allowing(model).getTempBuffer(); will(new MockStringBufferAction());
			allowing(model).getGenericElement(); will(returnValue(elem));
			allowing(model).getUrlGenerator(); will(returnValue(generator));
		}});
	}
	
	@Test
	public void basic() throws IOException {
		Script script = new Script("test.js");
		
		context.checking(new Expectations(){{
			oneOf(generator).generateResourceUrl("test.js"); will(returnValue("/resource/test.js"));
		}});
		
		script.render(model);
		context.assertIsSatisfied();
		
		String exp = "<script src=\"/resource/test.js\" type=\"text/javascript\"></script>\n";
		Assert.assertEquals(exp, writer.toString());
	}
	
	@Test
	public void templateScript() throws IOException {
		Script script = new TemplateScript("test");
		
		context.checking(new Expectations(){{
			oneOf(generator).generateTemplateUrl("test", "js"); will(returnValue("/template/test.js"));
		}});
		
		script.render(model);
		context.assertIsSatisfied();
		
		String exp = "<script src=\"/template/test.js\" type=\"text/javascript\"></script>\n";
		Assert.assertEquals(exp, writer.toString());
	}
}
