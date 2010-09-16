/*
 * Copyright 2009 Thomas Spencer
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

package org.tpspencer.tal.mvc.commons.views.menu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tpspencer.tal.mvc.commons.views.menu.MenuView;
import org.tpspencer.tal.mvc.render.BasicRenderModel;
import org.tpspencer.tal.mvc.render.RenderModel;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.render.SpringRenderNodeFactory;
import org.tpspencer.tal.template.render.TestUrlGenerator;
import org.tpspencer.tal.template.render.apacheel.ApacheELExpressionEvaluator;

/**
 * Tests the menu view renders correctly
 * 
 * @author Tom Spencer
 */
public class TestMenuView {

	private MenuView view = null;
	
	@Before
	public void setup() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/test-context.xml");
		view = (MenuView)ctx.getBean("menuView");
	}
	
	@Test
	public void basic() {
		RenderModel renderModel = new BasicRenderModel();
		view.prepareRender(renderModel, null);
		
		assertEquals("menuView", renderModel.getTemplate());
		assertNotNull(renderModel.getAttribute("menuItems"));
	}
	
	@Test
	public void render() throws IOException {
		BasicRenderModel model = new BasicRenderModel();
		view.prepareRender(model, null);
		
		TemplateConfiguration config = (TemplateConfiguration)model.getAttribute("templateConfig");
		
		StringWriter writer = new StringWriter();
		org.tpspencer.tal.template.render.SimpleRenderModel renderModel = new org.tpspencer.tal.template.render.SimpleRenderModel(writer, new TestUrlGenerator());
		renderModel.setEvaluator(new ApacheELExpressionEvaluator());
		renderModel.setNodeFactory(SpringRenderNodeFactory.getInstance());
		// if( config.getResourceBundleBaseName() != null ) renderModel.setBundle(ResourceBundle.getBundle(config.getResourceBundleBaseName()));
		renderModel.setModel(model.getAttributes());
		
		config.getRenderer("html").render(renderModel);
		assertEqualsTestData("menuRender", "html", writer.toString());
		
		long st = System.currentTimeMillis();
		config.getRenderer("html").render(renderModel);
		System.out.println("2nd Render Time: " + (System.currentTimeMillis() - st));
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
