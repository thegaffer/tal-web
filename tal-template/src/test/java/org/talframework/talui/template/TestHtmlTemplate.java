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

package org.talframework.talui.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.talframework.talui.template.TemplateConfiguration;
import org.talframework.talui.template.render.SimpleRenderModel;
import org.talframework.talui.template.render.SpringRenderNodeFactory;
import org.talframework.talui.template.render.TestUrlGenerator;
import org.talframework.talui.template.render.UrlGenerator;
import org.talframework.talui.template.test.SimpleBeanA;
import org.talframework.talui.template.test.SimpleBeanB;

/**
 * This class tests the basic html rendering given a
 * basic template that includes most features.
 * 
 * @author Tom Spencer
 */
public class TestHtmlTemplate {

	private Mockery context = new JUnit4Mockery();
	/** Holds the template configuration */
	private TemplateConfiguration formConfig = null;
	/** Holds the simple template configuration */
	private TemplateConfiguration simpleFormConfig = null;
	/** Holds the table configuration */
	private TemplateConfiguration tableConfig = null;
	/** Holds the model for test */
	private Map<String, Object> model = null;
	
	@SuppressWarnings("serial")
	@Before
	public void setup() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/test-context.xml");
		formConfig = (TemplateConfiguration)ctx.getBean("testForm");
		simpleFormConfig = (TemplateConfiguration)ctx.getBean("simpleForm");
		tableConfig = (TemplateConfiguration)ctx.getBean("testResults");
		
		// The test model
		SimpleBeanA bean = new SimpleBeanA();
		bean.setAmount(100.23);
		bean.setCurrency("GBP");
		bean.setDate(new GregorianCalendar() {{ set(2008, 10, 30); }}.getTime());
		bean.setActive(true);
		
		SimpleBeanB beanb = new SimpleBeanB();
		beanb.setAddress1("1 High Street");
		beanb.setAddress2("Some Town");
		beanb.setCountry("UK");
		beanb.setPostCode("GB34 1AA");
		bean.setAddress(beanb);
		
		model = new HashMap<String, Object>();
		model.put("form", bean);
		model.put("results", new SimpleBeanA[]{bean, bean});
	}
	
	/**
	 * Basic test that we can generate html
	 */
	@Test
	public void basicHtml() throws Exception {
		StringWriter writer = new StringWriter();
		UrlGenerator urlGenerator = new TestUrlGenerator();
		SimpleRenderModel model = new SimpleRenderModel(writer, urlGenerator);
		model.setNodeFactory(SpringRenderNodeFactory.getInstance());
		model.setBundle(StubResourceBundle.class.getName());
		model.setModel(this.model);
		
		formConfig.getRenderer("html").render(model);
		assertEqualsTestData("basicHtml", "html", writer.toString());
	
		// Simple output to visually confirm its fast to render!
		for( int i = 0 ; i < 10 ; i++ ) {
			writer = new StringWriter();
			long st = System.currentTimeMillis();
			formConfig.getRenderer("html").render(model);
			long en = System.currentTimeMillis();
			
			System.out.println("Render " + i + " = " + (en-st) );
		}
	}
	
	/**
	 * Basic test that we can generate a form
	 */
	@Test
	public void formHtml() throws IOException {
		StringWriter writer = new StringWriter();
		final UrlGenerator urlGenerator = context.mock(UrlGenerator.class);
		
		context.checking(new Expectations() {{
			oneOf(urlGenerator).generateResourceUrl("resource/TestResources.js"); will(returnValue("/resource/TestResources.js"));
			oneOf(urlGenerator).generateActionUrl("submitBeanA", null); will(returnValue("/submitBeanA"));
		}});
		
		SimpleRenderModel model = new SimpleRenderModel(writer, urlGenerator);
		model.setNodeFactory(SpringRenderNodeFactory.getInstance());
		model.setBundle(StubResourceBundle.class.getName());
		model.setModel(this.model);
		
		formConfig.getRenderer("form-html").render(model);
		assertEqualsTestData("formHtml", "html", writer.toString());
	}
	
	/**
	 * Basic test that we can generate a table of results
	 */
	@Test
	public void tableHtml() throws IOException {
		StringWriter writer = new StringWriter();
		UrlGenerator urlGenerator = new TestUrlGenerator();
		SimpleRenderModel model = new SimpleRenderModel(writer, urlGenerator);
		model.setNodeFactory(SpringRenderNodeFactory.getInstance());
		model.setBundle(StubResourceBundle.class.getName());
		model.setModel(this.model);
		
		tableConfig.getRenderer("html").render(model);
		assertEqualsTestData("tableHtml", "html", writer.toString());
	}
	
	/**
	 * Basic test that we can generate a form from a 
	 * very simple template
	 */
	@Test
	public void simpleFormHtml() throws IOException {
		StringWriter writer = new StringWriter();
		final UrlGenerator urlGenerator = context.mock(UrlGenerator.class);
		
		context.checking(new Expectations() {{
			oneOf(urlGenerator).generateResourceUrl("resource/TestResources.js"); will(returnValue("/resource/TestResources.js"));
			oneOf(urlGenerator).generateActionUrl("submitBeanA", null); will(returnValue("/submitBeanA"));
		}});
		
		SimpleRenderModel model = new SimpleRenderModel(writer, urlGenerator);
		model.setNodeFactory(SpringRenderNodeFactory.getInstance());
		model.setBundle(StubResourceBundle.class.getName());
		model.setModel(this.model);
		
		simpleFormConfig.getRenderer("form-html").render(model);
		assertEqualsTestData("simpleFormHtml", "html", writer.toString());
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
