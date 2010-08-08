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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.Model.ModelCleanupTask;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.model.ModelEvent;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.model.NestedResolvedAttributeException;
import org.tpspencer.tal.mvc.model.ResolvedModelAttribute;
import org.tpspencer.tal.mvc.model.SimpleModelAttribute;
import org.tpspencer.tal.mvc.model.StandardModel;
import org.tpspencer.tal.mvc.model.UnsupportedModelAttributeException;
import org.tpspencer.tal.mvc.process.SimpleModelAttributeResolver;

/**
 * Tests the StandardModel class and its behaviour
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestStandardModel {

	/** JMock Mockery context */
	private Mockery context = new JUnit4Mockery();
	/** Holds the main test layer */
	private ModelConfiguration layer1 = null;
	/** Holds a second test layer */
	private ModelConfiguration layer2 = null;
	/** Holds the resolved model attribute that has a mocked resolver */
	private ResolvedModelAttribute resolved = null;
	
	/**
	 * Sets up the standard model for each test
	 */
	@Before
	public void setupModel() {
		final SimpleModelAttribute simple = new SimpleModelAttribute("simple");
		simple.setDefaultValue("default");
		simple.setEventable(true);
			
		final SimpleModelAttribute layered = new SimpleModelAttribute("layered");
		layered.setDefaultValue("layer1");
			
		final SimpleModelAttribute layered2 = new SimpleModelAttribute("layered");
		layered2.setDefaultValue("layer2");
		
		final SimpleModelAttribute parent = new SimpleModelAttribute("parent");
		
		final SimpleModelAttribute nonDefault = new SimpleModelAttribute("nonDefault");
		
		layer1 = new ModelConfiguration("parentLayer", new ArrayList<ModelAttribute>() {{
				add(simple);
				add(layered);
				add(parent); }});
		
		final ModelResolver resolver = context.mock(ModelResolver.class);
		context.checking(new Expectations() {{
            allowing(resolver).canNestResolver(); will(returnValue(false));
        }});
		
		resolved = new ResolvedModelAttribute("resolved", resolver, null, null);
		
		final ModelAttribute nestedResolved = new ResolvedModelAttribute("nestedResolved", new NestedResolver(), null, null);
		
		layer2 = new ModelConfiguration("childLayer", new ArrayList<ModelAttribute>() {{
				add(nonDefault);
				add(layered2);
				add(resolved);
				add(nestedResolved); }});
	}
	
	/**
	 * Simply tests we get a simple attributes
	 */
	@Test
	public void basicAttributes() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		
		Object attr = model.getAttribute("simple");
		assertNotNull(attr);
		assertEquals("default", attr);
		
		attr = model.getAttribute("nonDefault");
		assertNull(attr);
	}
	
	/**
	 * Ensures an exception if we pop the wrong layer
	 */
	@Test(expected=IllegalArgumentException.class)
	public void invalidLayer() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.popLayer(layer2);
	}
	
	/**
	 * Tests that if the attribute is not of the expected 
	 * type a ClassCastException is thrown.
	 */
	@Test(expected = ClassCastException.class)
	public void unexpectedType() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		model.getAttribute("simple", Double.class);
	}
	
	/**
	 * Tests we get an exception if attribute is not 
	 * present
	 */
	@Test(expected = UnsupportedModelAttributeException.class)
	public void invalidAttribute() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		
		model.getAttribute("invalid");
	}
	
	/**
	 * Tests we get the right layered attribute
	 */
	@Test
	public void layeredAttribute() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		
		Object attr = model.getAttribute("layered");
		assertNotNull(attr);
		assertEquals("layer2", attr);
	}
	
	/**
	 * Tests we get a resolved attribute
	 */
	@Test
	public void resolvedAttribute() {
		final StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		
		final String value = "resolvedok";
		final ModelResolver resolver = resolved.getResolver();
		context.checking(new Expectations() {{
			oneOf(resolver).getModelAttribute(model, "resolved", null); 
				will(returnValue(value));
		}});
		
		Object attr = model.getAttribute("resolved");
		context.assertIsSatisfied();
		assertNotNull(attr);
		assertEquals("resolvedok", attr);
	}
	
	/**
	 * Ensures a cleanup task is run
	 */
	@Test
	public void cleanupTask() {
	    final StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
        model.pushLayer(layer1);
        model.pushLayer(layer2);
        
        final ModelCleanupTask task = context.mock(ModelCleanupTask.class);
        model.registerCleanupTask(task, "simple");
        
        // This will not cause cleanup task to run
        model.popLayer(layer2);
        
        context.checking(new Expectations() {{
            oneOf(task).cleanup(model);
        }});
        
        // This will cause cleanup task to run
        model.popLayer(layer1);
        
        context.assertIsSatisfied();
	}
	
	/**
	 * Tests we get an exception on nested resolved attributes
	 */
	@Test(expected = NestedResolvedAttributeException.class)
	public void stopNestedResolvers() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		
		model.getAttribute("nestedResolved");
	}
	
	/**
	 * Tests we keep hold of any saved attributes
	 */
	@Test
	public void savedAttribute() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), false);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		
		model.setAttribute("simple", "changed");
		
		Object attr = model.getAttribute("simple");
		assertNotNull(attr);
		assertEquals("changed", attr);
	}
	
	/**
	 * Tests we record events
	 */
	@Test
	public void testEvents() {
		StandardModel model = new StandardModel(new SimpleModelAttributeResolver(), true);
		model.pushLayer(layer1);
		model.pushLayer(layer2);
		model.setSource("source");
		
		model.setAttribute("simple", "changed");
		
		List<ModelEvent> events = model.getEvents();
		assertNotNull(events);
		assertEquals(1, events.size());
		assertEquals("source", events.get(0).getSource());
		assertEquals("parentLayer", events.get(0).getConfiguration().getName());
		assertEquals("simple", events.get(0).getAttribute().getName());
		assertNull(events.get(0).getOldValue());
		assertEquals("changed", events.get(0).getNewValue());
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
			return model.getAttribute("resolved");
		}
		public boolean canNestResolver() {
			return false;
		}
	}
}
