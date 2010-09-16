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
import org.tpspencer.tal.mvc.document.ElementWriter;
import org.tpspencer.tal.mvc.document.writers.ChapterWriter;

public class TestChapterWriter {
	
	private Mockery context = new JUnit4Mockery();

	@Test
	public void basic() {
		final DocumentWriter doc = context.mock(DocumentWriter.class);
		final ElementWriter inner = context.mock(ElementWriter.class);
		final AppElement app = context.mock(AppElement.class, "app");
		final AppElement child1 = context.mock(AppElement.class, "child1");
		final AppElement child2 = context.mock(AppElement.class, "child2");
		
		final List<AppElement> children = new ArrayList<AppElement>();
		children.add(child1);
		children.add(child2);
		
		ChapterWriter writer = new ChapterWriter(
				"bundle", "title", "test", 
				new String[]{"desc1", "desc2"}, 
				inner);
		
		context.checking(new Expectations() {{
			oneOf(doc).startChapter("title", "bundle", null);
			oneOf(app).getChildren("test"); will(returnValue(children));
			oneOf(doc).writeParagraph("desc1", app);
			oneOf(doc).writeParagraph("desc2", app);
			oneOf(inner).write(doc, app, child1);
			oneOf(inner).write(doc, app, child2);
			oneOf(doc).endChapter();
		}});
		
		writer.write(doc, app, app);
	}
}
