package org.tpspencer.tal.mvc.sample;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tpspencer.tal.mvc.sample.order.controllers.OrderCreateController;

/**
 * This class tests the sample configuration loads correctly.
 * 
 * @author Tom Spencer
 */
public class TestSampleConfig {

	@Test
	public void basic() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("system-test-context.xml");
		
		Assert.assertNotNull(ctx.getBean("test"));
		
		// Quick check to make sure autowiring is working
		OrderCreateController ctrl = (OrderCreateController)ctx.getBean("orderCreateController");
		Assert.assertNotNull(ctrl.getService());
	}
}
