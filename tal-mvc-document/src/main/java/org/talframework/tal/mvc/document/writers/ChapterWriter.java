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

import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.ElementWriter;

/**
 * This class writes out a chapter inside one of the documents.
 * 
 * @author Tom Spencer
 */
public final class ChapterWriter {

	/** Holds the resource bundle for chapter */
	private final String resource;
	/** Holds the title resource for chapter */
	private final String title;
	/** The type of elements to write out */
	private final String type;
	/** The initial paragraphs to write out */
	private final String[] descriptions;
	/** The section to write out each part */
	private final ElementWriter section;
	
	public ChapterWriter(
			String resource,
			String title,
			String type,
			String[] descriptions,
			ElementWriter section) {
		this.resource = resource;
		this.title = title;
		this.type = type;
		this.descriptions = descriptions;
		this.section = section;
	}
	
	/**
	 * Writes out the chapter for the specific element
	 * 
	 * @param writer The writer
	 * @param app The app contents
	 */
	public void write(DocumentWriter writer, AppElement app, AppElement element) {
		writer.startChapter(title, resource, null);
		
		List<AppElement> elements = element.getChildren(type);
		
		int ln = descriptions != null ? descriptions.length : 0;
		for( int i = 0 ; i < ln ; i++ ) {
			writer.writeParagraph(descriptions[i], element);
		}
		
		if( elements != null && elements.size() > 0 ) {
			Iterator<AppElement> it = elements.iterator();
			while( it.hasNext() ) {
				section.write(writer, app, it.next());
			}
		}
		
		writer.endChapter();
	}
}
