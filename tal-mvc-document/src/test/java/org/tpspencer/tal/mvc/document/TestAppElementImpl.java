/*
 * Copyright 2010 Thomas Spencer
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

package org.tpspencer.tal.mvc.document;

import java.util.HashMap;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.AppElementImpl;

import junit.framework.Assert;

public class TestAppElementImpl {
	
	private Mockery context = new JUnit4Mockery();

	@Test
	public void basic() {
		Map<String, String> info = new HashMap<String, String>();
		info.put("info1", "info1val");
		info.put("info2", "info2val");
		
		AppElementImpl element = new AppElementImpl("1", "element", info);
		
		Assert.assertEquals(element.getId(), "1");
		Assert.assertEquals(element.getElement(), "element");
		Assert.assertEquals(element.getElement(String.class), "element");
		Assert.assertEquals(element.getInfo("info1", null), "info1val");
		Assert.assertEquals(element.getInfo("info2", null), "info2val");
		Assert.assertNotNull(element.getExtraInfo());
		Assert.assertEquals(2, element.getExtraInfo().size());
		Assert.assertEquals(element.getExtraInfo().get("info1"), "info1val");
		Assert.assertEquals(element.getExtraInfo().get("info2"), "info2val");
	}
	
	@Test
	public void withChildren() {
		AppElementImpl element = new AppElementImpl("1", "element", null);
		
		final AppElement child1 = context.mock(AppElement.class, "child1");
		final AppElement child2 = context.mock(AppElement.class, "child2");
		final AppElement child3 = context.mock(AppElement.class, "child3");
		
		element.add("test", child1);
		element.add("test", child2);
		element.add("another", child3);
		
		Assert.assertNotNull(element.getChildren("test"));
		Assert.assertEquals(2, element.getChildren("test").size());
		Assert.assertEquals(child1, element.getChildren("test").get(0));
		Assert.assertEquals(child2, element.getChildren("test").get(1));
		Assert.assertNotNull(element.getChildren("another"));
		Assert.assertEquals(child3, element.getChildren("another").get(0));
		Assert.assertEquals(1, element.getChildren("another").size());
		Assert.assertNull(element.getChildren("magic"));
		
		context.checking(new Expectations() {{
			allowing(child1).getId(); will(returnValue("child1"));
			allowing(child2).getId(); will(returnValue("child2"));
			allowing(child3).getId(); will(returnValue("child3"));
		}});
		
		Assert.assertEquals(child1, element.getChild("test", "child1"));
		Assert.assertEquals(child2, element.getChild("test", "child2"));
		Assert.assertEquals(child3, element.getChild("another", "child3"));
	}
}
