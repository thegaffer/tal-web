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

package org.tpspencer.tal.mvc.commons.views;

import java.util.ArrayList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.commons.views.form.FormView;
import org.tpspencer.tal.mvc.render.RenderModel;

/**
 * This class tests the form view
 * 
 * @author Tom Spencer
 */
public class TestFormView {
	
	private Mockery context = new JUnit4Mockery();
	
	private FormView view = null;
	private RenderModel renderModel = null;
	private Model model = null;
	
	@SuppressWarnings("serial")
	@Before
	public void setup() {
		view = new FormView();
		// view.setFormName("MenuItem");
		view.setPrimaryBean(BasicBean.class);
		//view.setPrimaryAction("/submitMenu");
		view.setCommands(new ArrayList<String>(){{ add("submit"); }});
		
		renderModel = context.mock(RenderModel.class);
		model = context.mock(Model.class);
	}

	@Test
	public void basic() {
		view.init();
		
		context.checking(new Expectations() {{
			oneOf(model).containsValueFor("form"); will(returnValue(false));
			oneOf(renderModel).setAttribute(with("form"), with(anything()));
			oneOf(renderModel).setTemplate("org/tpspencer/tal/mvc/commons/views/BasicBeanForm");
		}});
		
		view.prepareRender(renderModel, model);
		context.assertIsSatisfied();
	}
	
	/**
	 * Ensures we get an exception with no form bean
	 */
	@Test(expected=IllegalArgumentException.class)
	public void noFormBean() {
		view.setPrimaryBean(null);
		view.init();
	}
	
	/**
	 * Ensures we get an exception with no bean class
	 */
	@Test(expected=IllegalArgumentException.class)
	public void noBeanClass() {
		view.setPrimaryBean(null);
		view.init();
	}
}
