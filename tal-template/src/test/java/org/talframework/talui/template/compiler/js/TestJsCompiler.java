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

package org.talframework.talui.template.compiler.js;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.Renderer;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateConfiguration;
import org.talframework.talui.template.compiler.js.JsCompiler;
import org.talframework.talui.template.core.xml.XmlTemplateReader;
import org.talframework.talui.template.render.MockStringBufferAction;

/**
 * This class tests the JrCompiler against the templates in
 * the test resources for JsCompilation
 * 
 * @author Tom Spencer
 */
public class TestJsCompiler {

	private Mockery context = new JUnit4Mockery();
	private JsCompiler compiler = null;
	private TemplateConfiguration config = null;
	private Map<String, Template> templates = null;
	private StringWriter writer = null;
	private RenderModel model = null;
	
	@Before
	public void setup() {
		compiler = new JsCompiler(true);
		writer = new StringWriter();
		model = context.mock(RenderModel.class);
		
		// Load the templates
		this.templates = new HashMap<String, Template>();
		List<Template> temps = XmlTemplateReader.getStdReader().loadTemplates("/testTemplate.xml");
		Iterator<Template> it = temps.iterator();
		while( it.hasNext() ) {
			Template t = it.next();
			this.templates.put(t.getName(), t);
		}
		
		config = context.mock(TemplateConfiguration.class);
		
		context.checking(new Expectations(){{
			allowing(config).getMainTemplate(); will(returnValue(null));
			allowing(config).getTemplates(); will(returnValue(templates));
			
			allowing(model).getWriter(); will(returnValue(writer));
			allowing(model).getNamespace(); will(returnValue("tst"));
			allowing(model).getTempBuffer(); will(new MockStringBufferAction());
		}});
	}
	
	@Test
	@Ignore
	public void basic() throws IOException {
		Renderer r = compiler.compile(config);
		r.render(model);
		assertEqualsTestData("basic", "js", writer.toString());
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
