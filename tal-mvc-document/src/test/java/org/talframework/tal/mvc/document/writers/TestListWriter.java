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

package org.tpspencer.tal.mvc.document.writers;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.writers.ChildWriters;

public class TestListWriter {

	private Mockery context = new JUnit4Mockery();
	
	@Test
	public void basic() {
		final DocumentWriter doc = context.mock(DocumentWriter.class);
		final AppElement element = context.mock(AppElement.class);
		final AppElement childElement = context.mock(AppElement.class, "childElement");
		final AppElement childElement2 = context.mock(AppElement.class, "childElement2");
		final List<AppElement> elems = new ArrayList<AppElement>();
		elems.add(childElement);
		elems.add(childElement2);
		
		ChildWriters.ListWriter writer = new ChildWriters.ListWriter("test", "desc");
		
		context.checking(new Expectations() {{
			oneOf(element).getChildren("test"); will(returnValue(elems));
			// oneOf(doc).writeParagraph("test", element);
			oneOf(doc).startList();
			oneOf(doc).addListItem("desc", childElement);
			oneOf(doc).addListItem("desc", childElement2);
			oneOf(doc).endList();
		}});
		
		writer.write(doc, null, element);
		
		context.assertIsSatisfied();
	}
}
