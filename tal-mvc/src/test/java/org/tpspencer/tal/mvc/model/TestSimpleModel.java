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

package org.tpspencer.tal.mvc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.model.NestedResolvedAttributeException;
import org.tpspencer.tal.mvc.model.ResolvedModelAttribute;
import org.tpspencer.tal.mvc.model.SimpleModel;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;

/**
 * Tests that {@link SimpleModel} works correctly.
 * 
 * @author Tom Spencer
 */
public class TestSimpleModel {

	private Mockery context = new JUnit4Mockery();
	private ModelConfiguration config = null;
	private Map<String, Object> attrs = null;
	
	@Before
	public void setup() {
		final ModelResolver resolver = context.mock(ModelResolver.class);
		
		SimpleModelAttribute simple1 = new SimpleModelAttribute("simple1");
		SimpleModelAttribute simple2 = new SimpleModelAttribute("simple2");
		simple2.setDefaultValue("test");
		ResolvedModelAttribute resolve1 = new ResolvedModelAttribute("resolve1", resolver, null, null);
		ResolvedModelAttribute resolve2 = new ResolvedModelAttribute("resolve2", new NestedResolver(), null, null);
		
		List<ModelAttribute> modelAttributes = new ArrayList<ModelAttribute>();
		modelAttributes.add(simple1);
		modelAttributes.add(simple2);
		modelAttributes.add(resolve1);
		modelAttributes.add(resolve2);
		
		config = new ModelConfiguration("test", modelAttributes);
		attrs = new HashMap<String, Object>();
		
		context.checking(new Expectations() {{
			allowing(resolver).getModelAttribute(with(any(Model.class)), with("resolve1"), with((Object)null)); will(returnValue("resolved"));
		}});
	}
	
	@Test
	public void basic() {
		SimpleModel model = new SimpleModel(config, attrs);
		
		Assert.assertNull(model.getAttribute("simple1"));
		Assert.assertEquals("test", model.getAttribute("simple2"));
		
		model.setAttribute("simple1", "testing");
		Assert.assertEquals("testing", model.getAttribute("simple1"));
		
		model.removeAttribute("simple1");
		Assert.assertNull(model.getAttribute("simple1"));
	}
	
	@Test 
	public void resolve() {
		SimpleModel model = new SimpleModel(config, attrs);
		Assert.assertEquals("resolved", model.getAttribute("resolve1"));
	}
	
	@Test(expected = NestedResolvedAttributeException.class)
	public void stopNestedResolve() {
		SimpleModel model = new SimpleModel(config, attrs);
		model.getAttribute("resolve2");
	}
	
	/**
	 * Simple resolver to test that we cannot get a resolved
	 * attribute when resolving another model attribute. 
	 * Cannot do this with mocks as requires dynamic invocation
	 * of the resolver, not a setup.
	 * 
	 * @author Tom Spencer
	 */
	private class NestedResolver implements ModelResolver {
		public Object getModelAttribute(Model model, String name, Object param) {
			return model.getAttribute("resolve1");
		}
	}
}
