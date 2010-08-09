package org.tpspencer.tal.mvc.commons.objex;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.locator.ContainerFactory;

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
		final EditableContainer container = context.mock(EditableContainer.class);
		context.checking(new Expectations() {{
			oneOf(factory).open("test"); will(returnValue(container));
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
		final EditableContainer container = context.mock(EditableContainer.class);
		context.checking(new Expectations() {{
			oneOf(factory).open("test"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getEditableContainer(null);
		Assert.assertNotNull(c);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void withBean() {
		ContainerLocator underTest = new ContainerLocatorImpl("test", factory);
		
		// Setup
		final EditableContainer container = context.mock(EditableContainer.class);
		final SimpleBean bean = new SimpleBean();
		
		context.checking(new Expectations() {{
			oneOf(factory).open(bean.getTest()); will(returnValue(container));
			oneOf(factory).open(bean.getTest()); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(bean);
		Assert.assertNotNull(c);
		
		EditableContainer ec = underTest.getEditableContainer(bean);
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
		final EditableContainer container = context.mock(EditableContainer.class);
		final Model model = context.mock(Model.class);
		
		context.checking(new Expectations() {{
			oneOf(model).getAttribute("test"); will(returnValue("testContainer"));
			oneOf(factory).open("testContainer"); will(returnValue(container));
			oneOf(model).getAttribute("test"); will(returnValue("testContainer"));
			oneOf(factory).open("testContainer"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(model);
		Assert.assertNotNull(c);
		
		EditableContainer ec = underTest.getEditableContainer(model);
		Assert.assertNotNull(ec);
		
		context.assertIsSatisfied();
	}
	
	@Test
	public void withMap() {
		ContainerLocator underTest = new ContainerLocatorImpl("test", factory);
		
		// Setup
		final EditableContainer container = context.mock(EditableContainer.class);
		final Map<String, String> model = new HashMap<String, String>();
		model.put("test", "testContainer");
		
		context.checking(new Expectations() {{
			oneOf(factory).open("testContainer"); will(returnValue(container));
			oneOf(factory).open("testContainer"); will(returnValue(container));
		}});
		
		// Test
		Container c = underTest.getContainer(model);
		Assert.assertNotNull(c);
		
		EditableContainer ec = underTest.getEditableContainer(model);
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
