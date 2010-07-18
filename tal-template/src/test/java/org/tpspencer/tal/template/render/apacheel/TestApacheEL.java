/*
 * Copyright 2008 Thomas Spencer
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

package org.tpspencer.tal.template.render.apacheel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.tpspencer.tal.template.RenderNode;
import org.tpspencer.tal.template.render.apacheel.ApacheELExpressionEvaluator;
import org.tpspencer.tal.template.test.SimpleBeanB;

/**
 * This class tests the operation of the Apache based
 * Expression Evaluator. 
 * 
 * Note: This is not a full test of Apache EL resolution!
 * 
 * @author Tom Spencer
 */
public class TestApacheEL {

	private Mockery context = new JUnit4Mockery();
	private RenderNode node = null;
	private Map<String, Object> model = null;
	private ApacheELExpressionEvaluator evaluator = null;
	
	@Before
	public void setup() {
		evaluator = new ApacheELExpressionEvaluator();
		model = new HashMap<String, Object>();
		model.put("test", "testing");
		model.put("simpleTrue", Boolean.TRUE);
		node = context.mock(RenderNode.class);
	}
	
	@Test
	public void basic() {
		Object ret = evaluator.evaluateExpression(model, null, "${test}", String.class);
		assertNotNull(ret);
		assertEquals("testing", ret);
	}
	
	@Test
	public void notFound() {
		Object ret = evaluator.evaluateExpression(model, null, "${test2}", Object.class);
		assertNull(ret);
	}
	
	@Test
	public void testCheck() {
		Object ret = evaluator.evaluateExpression(model, null, "${simpleTrue}", Boolean.class);
		assertNotNull(ret);
		assertEquals(true, ret);
	}
	
	@Test
	public void testFalseCheck() {
		Object ret = evaluator.evaluateExpression(model, null, "${!simpleTrue}", Boolean.class);
		assertNotNull(ret);
		assertEquals(false, ret);
	}
	
	@Test
	public void testStringCheck() {
		Object ret = evaluator.evaluateExpression(model, null, "${test == 'testing'}", Boolean.class);
		assertNotNull(ret);
		assertEquals(true, ret);
	}
	
	@Test
	public void testBeanAccess() {
		SimpleBeanB bean = new SimpleBeanB();
		model.put("bean", bean);
		
		Object ret = evaluator.evaluateExpression(model, null, "${bean}", SimpleBeanB.class);
		assertNotNull(ret);
		assertEquals(bean, ret);
	}
	
	@Test
	public void testBeanCheck() {
		SimpleBeanB bean = new SimpleBeanB();
		bean.setAddress1("15 High Street");
		model.put("bean", bean);
		
		Object ret = evaluator.evaluateExpression(model, null, "${bean.address1}", String.class);
		assertNotNull(ret);
		assertEquals("15 High Street", ret);
	}
	
	@Test
	public void testNodeCheck() {
		final SimpleBeanB bean = new SimpleBeanB();
		bean.setAddress1("15 High Street");
		
		context.checking(new Expectations() {{
			oneOf(node).getObject(); will(returnValue(bean));
		}});
		
		Object ret = evaluator.evaluateExpression(model, node, "${this}", SimpleBeanB.class);
		context.assertIsSatisfied();
		assertNotNull(ret);
		assertEquals(bean, ret);
	}
	
	@Test
	public void testNodeAccess() {
		final SimpleBeanB bean = new SimpleBeanB();
		bean.setAddress1("15 High Street");
		
		context.checking(new Expectations() {{
			oneOf(node).getObject(); will(returnValue(bean));
		}});
		
		Object ret = evaluator.evaluateExpression(model, node, "${this.address1}", String.class);
		context.assertIsSatisfied();
		assertNotNull(ret);
		assertEquals("15 High Street", ret);
	}
}
