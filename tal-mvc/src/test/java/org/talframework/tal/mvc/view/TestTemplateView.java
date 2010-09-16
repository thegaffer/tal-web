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

package org.tpspencer.tal.mvc.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;
import org.tpspencer.tal.mvc.render.RenderModel;
import org.tpspencer.tal.mvc.view.TemplateView;

/**
 * Tests the template window
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestTemplateView {
	
	private Mockery context = new JUnit4Mockery();
	private Model model = null;
	private ModelConfiguration config = null;
	
	/**
	 * Sets up mock model
	 */
	@Before
	public void setup() {
		model = context.mock(Model.class);
		config = new ModelConfiguration("model", (List<ModelAttribute>)new ArrayList<ModelAttribute>() {{
			add(new SimpleModelAttribute("testAttr"));
		}});
	}

	/**
	 * Basic operation
	 */
	@Test
	public void basic() {
		TemplateView view = new TemplateView("test");
		assertEquals("test", view.getTemplate());
		assertNull(view.getModel());
		
		final RenderModel renderModel = context.mock(RenderModel.class);
		context.checking(new Expectations() {{
			oneOf(renderModel).setTemplate("test");
		}});
		
		view.prepareRender(renderModel, this.model);
		context.assertIsSatisfied();
	}
	
	/**
	 * Ensures the 2nd constructor works
	 */
	@Test
	public void withModel() {
		TemplateView view = new TemplateView("test", config);
		assertEquals("test", view.getTemplate());
		assertEquals(config, view.getModel());
	}
	
	/**
	 * Ensures we get an error if there is no template
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noTemplate() {
		new TemplateView(null);
	}
	
	/**
	 * Ensures we stringify ok
	 */
	@Test
	public void stringify() {
		TemplateView view = new TemplateView("test", config);
		assertEquals("TemplateView [template=test, model=model]", view.toString());
	}
}
