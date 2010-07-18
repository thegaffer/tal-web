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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.writers.SimpleWriters;

public class TestSimpleWriter {

	private Mockery context = new JUnit4Mockery();
	
	@Test
	public void basic() {
		final DocumentWriter doc = context.mock(DocumentWriter.class);
		final AppElement element = context.mock(AppElement.class);
		
		SimpleWriters.SimpleWriter writer = new SimpleWriters.SimpleWriter("desc1", "desc2");
		
		context.checking(new Expectations() {{
			oneOf(doc).writeParagraph("desc1", element);
			oneOf(doc).writeParagraph("desc2", element);
		}});
		
		writer.write(doc, null, element);
		
		context.assertIsSatisfied();
	}
}
