package org.tpspencer.tal.mvc.sample.objex;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class tests the configuration
 * 
 * @author Tom Spencer
 */
public class TestConfig {

	@Test
	public void basic() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("org/tpspencer/tal/mvc/sample/objex/system-test.xml");
	}
}
