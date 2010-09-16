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

import com.lowagie.text.DocumentException;

/**
 * This class contains the basic building blocks of writing
 * a document. A document is split into the following.
 * <ul>
 * <li>Chapters - Which all start a new page and have a heading</li>
 * <li>Numbered Sections - Which are numbered or lettered and have a heading</li>
 * <li>Paragraphs - Which read from a properties file with substitutions</li>
 * <li>Quotes - Which are similar to paragraphs, but are in italics</li>
 * <li>Bullet Lists</li>
 * <li>Numbered Lists</li>
 * </ul>
 * 
 * This class is built on the iText library.
 * 
 * @author Tom Spencer
 */
public interface DocumentWriter {
	
	/**
	 * Call to start writing the document.
	 * 
	 * @param title The title
	 * @param appTitle The applications title
	 * @param author The author
	 * @param creator The creator
	 */
	public void start(String title, String appTitle, String author, String creator);
	
	/**
	 * Call to end writing the current document
	 */
	public void end();
	
	/**
	 * Call to start a new chapter
	 * 
	 * @param key The key for the chapter title
	 * @param substitutions The substitutions
	 */
	public void startChapter(String key, String resources, AppElement element);
	
	/**
	 * Called when we have finished adding to the chapter.
	 * 
	 * @throws DocumentException
	 */
	public void endChapter();
	
	/**
	 * Call to start a new section in the document
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void startSection(String key, AppElement element, boolean newPage);
	
	/**
	 * Call to end a section when finished adding to it.
	 */
	public void endSection();
	
	/**
	 * Call to write a paragraph into the current section. If there
	 * is no current section an unnamed section is created and ended.
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void writeParagraph(String key, AppElement element);
	
	/**
	 * Call to write a quote paragraph into the current section. If there
	 * is no current section an unnamed section is created and ended.
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void writeQuote(String key, AppElement element);
	
	/**
	 * Writes out an image with given legend
	 * 
	 * @param image The image
	 * @param legend The resource to use for the legend
	 */
	public void writeImage(String image, String legend, AppElement element);
	
	/**
	 * Start a list of items inside the current section (must be present)
	 */
	public void startList();
	
	/**
	 * Add a new list item to the current list.
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void addListItem(String key, AppElement element);
	
	/**
	 * Call when the list is completed
	 */
	public void endList();
	
	/**
	 * Start a new table with given heading resources
	 * 
	 * @param headings
	 */
	public void startTable(String[] headings);
	
	/**
	 * Add a row based on element
	 * 
	 * @param cols The cols to output
	 * @param element The element to use
	 */
	public void addTableRow(String[] cols, AppElement element);
	
	/**
	 * Add a row with specific text (no substitutions)
	 * 
	 * @param cols The cols to output
	 */
	public void addTableRow(String[] cols);
	
	/**
	 * Ends the current table
	 */
	public void endTable();
}
