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

import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.ElementWriter;

/**
 * A wrapper class that holds various simple writers
 * 
 * @author Tom Spencer
 */
public class SimpleWriters {

	/**
	 * Simple class that holds multiple writers in place of one
	 * 
	 * @author Tom Spencer
	 */
	public static class MultiWriter implements ElementWriter {
		private final ElementWriter[] writers;
		
		public MultiWriter(ElementWriter... writers) {
			this.writers = writers;
		}
		
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			for( ElementWriter w : writers ) {
				w.write(writer, app, element);
			}
		}
	}
	
	/**
	 * This abstract class is configured to write out an
	 * {@link AppElement} in a document.
	 * 
	 * @author Tom Spencer
	 */
	public static class SectionWriter implements ElementWriter {

		/** The sub-title resource - if not no explicit section is created */
		private final String subTitle;
		/** The inner sections to write out */
		private final ElementWriter[] parts;
		/** Determines if each of these sections should be on a new page */
		private final boolean newPage;
		
		public SectionWriter(
				String subTitle,
				boolean newPage, 
				ElementWriter... parts) {
			this.subTitle = subTitle;
			this.parts = parts;
			this.newPage = newPage;
		}
		
		/**
		 * Writes out the element as configured.
		 * 
		 * @param writer
		 * @param element
		 */
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			if( subTitle != null ) writer.startSection(subTitle, element, newPage);
			
			if( parts != null ) {
				for( int i = 0 ; i < parts.length ; i++ ) {
					parts[i].write(writer, app, element);
				}
			}
				
			if( subTitle != null ) writer.endSection();
		}
	}
	
	/**
	 * This writer just outputs one or more paragraphs for
	 * the element.
	 * 
	 * @author Tom Spencer
	 */
	public static class SimpleWriter implements ElementWriter {

		private final String[] descriptions;
		
		public SimpleWriter(String... descriptions) {
			this.descriptions = descriptions;
		}
		
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			int ln = descriptions != null ? descriptions.length : 0;
			for( int i = 0 ; i < ln ; i++ ) {
				writer.writeParagraph(descriptions[i], element);
			}
		}
	}
	
	/**
	 * This class writes out an image if a particular
	 * property is set.
	 * 
	 * @author Tom Spencer
	 */
	public static class ImageWriter implements ElementWriter {
		
		private final String imageParam;
		private final String legend;
		
		public ImageWriter(String imageParam, String legend) {
			this.imageParam = imageParam;
			this.legend = legend;
		}

		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			String image = element.getInfo(imageParam, null);
			
			// TODO: Remove this is for testing
			image = imageParam;
			
			if( image != null ) {
				writer.writeImage(image, legend, element);
			}
		}
	}
}
