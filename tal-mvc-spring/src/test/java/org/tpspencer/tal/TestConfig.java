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

package org.tpspencer.tal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tpspencer.tal.mvc.config.AppConfig;

/**
 * This test loads a Web MVC model from a spring
 * configuration and ensures it loads ok.
 * 
 * @author Tom Spencer
 */
public class TestConfig {
	
	public AppConfig app = null;

	@Before
	public void setup() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/testContext.xml");
		
		app = (AppConfig)ctx.getBean("app");
	}
	
	@Test
	public void basic() {
		assertEquals("app", app.getName());
		assertTrue(app.getModel().hasAttribute("test"));
	}
}
