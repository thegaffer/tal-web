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

import java.awt.Color;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Stack;

import com.lowagie.text.Anchor;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

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
public class DocumentWriterImpl implements DocumentWriter {
	
	private static Font titleFont = new Font(Font.TIMES_ROMAN, 24, Font.BOLD & Font.ITALIC);
	private static Font subHeadingFont = new Font(Font.TIMES_ROMAN, 18, Font.BOLD & Font.ITALIC, Color.DARK_GRAY);
	private static Font h1Font = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
	private static Font h2Font = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
	private static Font h3Font = new Font(Font.TIMES_ROMAN, 14, Font.BOLD);
	private static Font paraFont = new Font(Font.TIMES_ROMAN, 12);
	private static Font headingFont = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.WHITE);
	private static Font quoteFont = new Font(Font.TIMES_ROMAN, 12, Font.ITALIC);

	/** The PDF document */
	private Document document = null;
	/** The resource bundle that holds all the basic text for this doc */
	private ResourceBundle resources = null;
	/** A temp buffer for internal usage */
	private StringBuilder tempBuffer;
	/** The current chapter */
	private Chapter chapter = null;
	/** The resource bundle that holds all the basic text for this doc */
	private ResourceBundle chapterResources = null;
	/** The current chapter number */
	private int chapterNumber = 0;
	/** The sections, arranged into a Stack */
	private Stack<Section> sections = new Stack<Section>();
	/** The current list (if there is one) */
	private List list = null;
	/** The current table (if there is one) */
	private Table table = null;
	
	/**
	 * Call to start a new document.
	 * 
	 * @param fileName The filename to write into
	 * @param resources The resources to output
	 * @throws Exception Any IO or underlying exceptions
	 */
	public DocumentWriterImpl(String fileName, String resources) throws Exception {
		document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
		writer.setStrictImageSequence(true);
		document.open();
		
		
		this.resources = ResourceBundle.getBundle(resources);
	}
	
	public void start(String title, String appTitle, String author, String creator) {
		document.addTitle(title);
		document.addSubject(appTitle);
		document.addCreator(creator);
		document.addAuthor(author);
		document.addCreationDate();
		
		Paragraph page = new Paragraph();
		page.add(new Paragraph()); // Empty line
		
		page.add(new Paragraph(title, titleFont));
		page.add(new Paragraph(appTitle, subHeadingFont));
		
		try {
			document.add(page);
		}
		catch( DocumentException e ) {
			throw new IllegalArgumentException("Cannot add title page to document, likely an incorrect argument when setting chapter up", e);
		}
	}
	
	/**
	 * Call to end writing the current document
	 */
	public void end() {
		document.close();
	}
	
	/**
	 * Call to start a new chapter
	 * 
	 * @param key The key for the chapter title
	 * @param substitutions The substitutions
	 */
	public void startChapter(String key, String resources, AppElement element) {
		if( resources != null ) chapterResources = ResourceBundle.getBundle(resources);
		
		String text = getText(key, element);
		
		Anchor anchor = new Anchor(text, h1Font);
		anchor.setName(text);

		chapter = new Chapter(new Paragraph(anchor), ++chapterNumber);
	}
	
	/**
	 * Called when we have finished adding to the chapter.
	 * 
	 * @throws DocumentException
	 */
	public void endChapter() {
		if( chapter == null ) throw new IllegalArgumentException("Cannot start a section with no chapter");
		
		chapterResources = null;
		
		try {
			document.add(chapter);
			chapter = null;
		}
		catch( DocumentException e ) {
			throw new IllegalArgumentException("Cannot add chapter to document, likely an incorrect argument when setting chapter up", e);
		}
	}
	
	/**
	 * Call to start a new section in the document
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void startSection(String key, AppElement element, boolean newPage) {
		if( chapter == null ) throw new IllegalArgumentException("Cannot start a section with no chapter");
		
		String text = getText(key, element);
		Paragraph p = new Paragraph(text, sections.size() > 0 ? h3Font : h2Font);
		
		if( sections.size() > 0 ) {
			sections.push(sections.peek().addSection(p));
		}
		else {
			if( newPage ) chapter.newPage();
			sections.push(chapter.addSection(p));
		}
	}
	
	/**
	 * Call to end a section when finished adding to it.
	 */
	public void endSection() {
		sections.pop();
	}
	
	/**
	 * Call to write a paragraph into the current section. If there
	 * is no current section an unnamed section is created and ended.
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void writeParagraph(String key, AppElement element) {
		String text = getText(key, element);
		
		if( sections.size() > 0 ) {
			sections.peek().add(new Paragraph(formatText(text)));
		}
		else {
			chapter.add(new Paragraph(formatText(text)));
		}
	}
	
	/**
	 * Call to write a quote paragraph into the current section. If there
	 * is no current section an unnamed section is created and ended.
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void writeQuote(String key, AppElement element) {
		String text = getText(key, element);
		
		if( sections.size() > 0 ) {
			sections.peek().add(new Paragraph(text, quoteFont));
		}
		else {
			chapter.add(new Paragraph(text, quoteFont));
		}
	}
	
	public void writeImage(String image, String legend, AppElement element) {
		Section section = sections.size() > 0 ? sections.peek() : chapter;
		
		URL url = this.getClass().getResource(image);
		if( url == null ) return;
		
		try {
			Image img = Image.getInstance(url);
			if( img != null ) {
				img.scaleToFit(document.getPageSize().getWidth() - 100, document.getPageSize().getHeight() - 100);
				section.add(img);
				if( legend != null ) writeParagraph(legend, element);
			}
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Cannot add given image to document", e);
		}
	}
	
	/**
	 * Start a list of items inside the current section (must be present)
	 */
	public void startList() {
		if( sections.size() == 0 ) throw new IllegalArgumentException("Cannot start a list if there is no section");
		
		list = new List();
		list.setNumbered(false);
		list.setLettered(true);
		list.setAlignindent(true);
		// list.setAutoindent(true);
		list.setIndentationLeft(20);
	}
	
	/**
	 * Add a new list item to the current list.
	 * 
	 * @param key The key in resources for the section title
	 * @param substitutions The substitutions to use
	 */
	public void addListItem(String key, AppElement element) {
		if( list == null ) throw new IllegalArgumentException("Cannot add a list item if there is no list");
		
		String text = getText(key, element);
		
		list.add(new ListItem(formatText(text)));
	}
	
	/**
	 * Call when the list is completed
	 */
	public void endList() {
		if( list == null ) throw new IllegalArgumentException("Cannot end a list item if there is no list");
		
		sections.peek().add(list);
		list = null;
	}
	
	public void startTable(String[] headings) {
		if( sections.size() == 0 ) throw new IllegalArgumentException("Cannot start a list if there is no section");
		if( table != null ) throw new IllegalArgumentException("Cannot nest a table in another table");
		
		try {
			table = new Table(headings.length);
			table.setPadding(2);
			table.setBorderColor(Color.GRAY);
			table.setBorderWidth(1);
			
			for( int i = 0 ; i < headings.length ; i++ ) {
				Cell c = new Cell(new Phrase(getText(headings[i]), headingFont));
				c.setUseAscender(true);
				c.setBackgroundColor(Color.DARK_GRAY);
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c.setHeader(true);
				table.addCell(c);
			}
			
			table.endHeaders();
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Error creating table", e);
		}
	}
	
	public void addTableRow(String[] cols, AppElement element) {
		if( table == null ) throw new IllegalArgumentException("Cannot add a row if there is no table!");
		
		try {
			for( int i = 0 ; i < cols.length ; i++ ) {
				Cell c = new Cell(new Phrase(getText(cols[i], element), paraFont));
				c.setUseAscender(true);
				c.setVerticalAlignment("middle");
				table.addCell(c);
			}
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Error adding row to a table", e);
		}
	}
	
	public void addTableRow(String[] cols) {
		if( table == null ) throw new IllegalArgumentException("Cannot add a row if there is no table!");
		
		try {
			for( int i = 0 ; i < cols.length ; i++ ) {
				Cell c = new Cell(new Phrase(cols[i], paraFont));
				c.setUseAscender(true);
				c.setVerticalAlignment("middle");
				table.addCell(c);
			}
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Error adding row to a table", e);
		}
	}
	
	public void endTable() {
		if( table == null ) throw new IllegalArgumentException("Cannot end a table if there is no table!");
		
		sections.peek().add(table);
		table = null;
	}
	
	/////////////////////////////////////////////////
	// Internal Helpers
	
	/**
	 * Helper to format the text replacing _something_ with
	 * italics (the underscores are omitted).
	 */
	private Phrase formatText(String text) {
		Phrase p = new Phrase();
		StringBuilder buf = new StringBuilder();
		char[] chars = text.toCharArray();
		boolean inItals = false;
		for( int i = 0 ; i < chars.length ; i++ ) {
			if( chars[i] == '_' ) {
				// TODO: Could check that there is another _
				
				Font f = inItals ? quoteFont : paraFont;
				p.add(new Chunk(buf.toString(), f));
				buf.setLength(0);
				inItals = !inItals;
			}
			/*else if( chars[i] == '*' ) {
				// TODO: Do same for bold
			}*/
			else {
				buf.append(chars[i]);
			}
		}
		if( buf.length() > 0 ) {
			Font f = inItals ? quoteFont : paraFont;
			p.add(new Chunk(buf.toString(), f));
		}
		
		return p;
	}
	
	/**
	 * @return The temp buffer for caller to use (it will be empty)
	 */
	public StringBuilder getTempBuffer() {
		if( tempBuffer == null ) tempBuffer = new StringBuilder();
		else tempBuffer.setLength(0);
		return tempBuffer;
	}
	
	/**
	 * Helepr to get the text for the output with substitutions.
	 * This method just uses the other internal methods to first
	 * get the text then get the subsitutions.
	 * 
	 * @param key The key in the resource bundle
	 * @param substitutions The substitutions
	 * @return The formatted text
	 */
	private String getText(String key, AppElement element) {
		String ret = getText(key);
		if( ret != null ) ret = substituteText(ret, element);
		else throw new IllegalArgumentException("There is no text for the given key: " + key);
		return ret;
	}
	
	/**
	 * Private helper to get the text for the current output.
	 * The key may represent an actual item (in which case use it)
	 * or it may represent a base that is appened key.0, key.1 to
	 * split the paragraph up in the resource file.
	 * 
	 * @param key
	 * @return
	 */
	private String getText(String key) {
		ResourceBundle resources = chapterResources != null ? chapterResources : this.resources;
		
		// Get as a single string
		try {
			return resources.getString(key);
		}
		
		// Otherwise try and get several parts
		catch( MissingResourceException ex ) {
			String[] parts = new String[0];
			Enumeration<String> e = resources.getKeys();
			while( e.hasMoreElements() ) {
				String res = e.nextElement();
				if( res.startsWith(key + ".") ) {
					try {
						int i = Integer.parseInt(res.substring(key.length() + 1));
						if( i >= parts.length ) {
							String[] newParts = new String[i+1];
							if( parts.length > 0 ) System.arraycopy(parts, 0, newParts, 0, parts.length);
							parts = newParts;
						}
						parts[i] = resources.getString(res).trim(); 
					}
					catch( Exception ex2 ) {
						// TODO: Log out a warning or return with invalid file or ignore!!
					}
				}
			}
			
			if( parts.length > 0 ) {
				StringBuilder buf = getTempBuffer();
				for( int i = 0 ; i < parts.length ; i++ ) {
					if( i > 0 ) buf.append(" ");
					buf.append(parts[i]);
				}
				return buf.toString();
			}
			
			return null;
		}
	}
	
	/**
	 * Private helper to substitute the given text with any
	 * substitutions in the form of <key>.
	 * 
	 * @param text The raw text
	 * @param substitutions Any substitutions
	 * @return The text to output
	 */
	private String substituteText(String text, AppElement element) {
		if( text == null ) return text;
		
		// Go around to see if there are any substitutions to perform
		Collection<String> subs = new ArrayList<String>();
		char[] ch = text.toCharArray();
		StringBuilder buf = null;
		for( int i = 0 ; i < ch.length ; i++ ) {
			if( buf == null && ch[i] == '<' ) {
				buf = new StringBuilder();
			}
			else if( buf != null && ch[i] == '>' ) {
				subs.add(buf.toString());
				buf = null;
			}
			else if( buf != null ) {
				buf.append(ch[i]);
			}
		}
		
		// Replace all Subs
		if( subs.size() == 0 ) return text;
		
		Iterator<String> it = subs.iterator();
		while(it.hasNext()) {
			String k = it.next();
			String v = element.getInfo(k, "<" + k + ">");
			if( v != null ) text = text.replace("<" + k + ">", v);
		}
		
		return text;
	}
}
