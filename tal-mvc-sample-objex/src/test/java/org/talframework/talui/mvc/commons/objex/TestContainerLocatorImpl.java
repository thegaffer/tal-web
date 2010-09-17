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

package org.talframework.talui.mvc.commons.objex;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.objexj.Container;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.talui.mvc.Model;

public class TestContainerLocatorImpl {
	
	private Mockery context = new JUnit4Mockery();
	private ContainerFactory factory = null;

	@Before
	public void setup() {
		factory = context.mock(ContainerFactory.class);
	}
	
	@Test
	public void fixedContainer() {
		ContainerLocator underTest = new ContainerLocatorImpl("test", true, factory);

		// Setup
		final Container container = context.mock(Container.class);
		context.checking(new Expectations() {{
			oneOf(factory).get("test"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(null);
		Assert.assertNotNull(c);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void fixedContainerOpen() {
		ContainerLocator underTest = new ContainerLocatorImpl("test", true, factory);

		// Setup
		final Container container = context.mock(Container.class);
		context.checking(new Expectations() {{
			oneOf(factory).open("test"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getOpenContainer(null);
		Assert.assertNotNull(c);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void withBean() {
		ContainerLocator underTest = new ContainerLocatorImpl("test", factory);
		
		// Setup
		final Container container = context.mock(Container.class);
		final SimpleBean bean = new SimpleBean();
		
		context.checking(new Expectations() {{
			oneOf(factory).get(bean.getTest()); will(returnValue(container));
			oneOf(factory).open(bean.getTest()); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(bean);
		Assert.assertNotNull(c);
		
		Container ec = underTest.getOpenContainer(bean);
		Assert.assertNotNull(ec);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void withModel() {
		ContainerLocatorImpl underTest = new ContainerLocatorImpl();
		underTest.setContainerAttribute("test");
		underTest.setContainerIsAbsolute(false);
		underTest.setFactory(factory);
		
		// Setup
		final Container container = context.mock(Container.class);
		final Model model = context.mock(Model.class);
		
		context.checking(new Expectations() {{
			oneOf(model).getAttribute("test"); will(returnValue("testContainer"));
			oneOf(factory).get("testContainer"); will(returnValue(container));
			oneOf(model).getAttribute("test"); will(returnValue("testContainer"));
			oneOf(factory).open("testContainer"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(model);
		Assert.assertNotNull(c);
		
		Container ec = underTest.getOpenContainer(model);
		Assert.assertNotNull(ec);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void withMap() {
		ContainerLocator underTest = new ContainerLocatorImpl("test", factory);
		
		// Setup
		final Container container = context.mock(Container.class);
		final Map<String, String> model = new HashMap<String, String>();
		model.put("test", "testContainer");
		
		context.checking(new Expectations() {{
			oneOf(factory).get("testContainer"); will(returnValue(container));
			oneOf(factory).open("testContainer"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(model);
		Assert.assertNotNull(c);
		
		Container ec = underTest.getOpenContainer(model);
		Assert.assertNotNull(ec);
		
		context.assertIsSatisfied();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidContainerAttr() {
		ContainerLocator underTest = new ContainerLocatorImpl("testC", factory);
		
		SimpleBean bean = new SimpleBean();
		
		// Test
		underTest.getContainer(bean);
	}
	
	
	/**
	 * Test bean
	 * 
	 * @author Tom Spencer
	 */
	private static class SimpleBean {
		private String test = "1";
		public String getTest() {
			return test;
		}
	}
}
