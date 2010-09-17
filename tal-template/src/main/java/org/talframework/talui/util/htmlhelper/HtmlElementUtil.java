/*
 * Copyright 2005 Thomas Spencer
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

package org.talframework.talui.util.htmlhelper;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * This class tries to simplify writing html elements
 * by providing a seris of methods for setting up 
 * common HTML elements as an 'Element' class. It also
 * contains helper methods for many of the ancilliary
 * attributes often added to different types of 
 * attributes. This methods in this class understand
 * the type of attribute and so call the appropriate
 * method on the Element class. It also uses the Html
 * constants to set the attribute names successfully.
 * 
 * @author Tom Spencer
 */
public class HtmlElementUtil {

	public static GenericElement startHeading(GenericElement elem, int level, String id) {
		if( level < 1 || level > 6 ) throw new IllegalArgumentException("The level on a heading can only be 1-6");
		elem.reset(HtmlConstants.ELEM_HEADINGS[level - 1]);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		return elem;
	}
	
	public static void endHeading(Writer writer, int level) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_HEADINGS[level]);
	}
	
	public static GenericElement startParagraph(GenericElement elem, String id) {
		elem.reset(HtmlConstants.ELEM_PARA);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		return elem;
	}
	
	public static void endParagraph(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_PARA);
	}
	
	public static GenericElement startDiv(GenericElement elem, String id, String cls, String style) {
		elem.reset(HtmlConstants.ELEM_DIV);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		return addStyle(elem, cls, style);
	}
	
	public static void endDiv(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_DIV);
	}
	
	public static GenericElement startSpan(GenericElement elem, String id, String cls, String style) {
		elem.reset(HtmlConstants.ELEM_SPAN);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		return addStyle(elem, cls, style);
	}
	
	public static void endSpan(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_SPAN);
	}
	
	public static GenericElement startLink(GenericElement elem, String id, String href) {
		elem.reset(HtmlConstants.ELEM_LINK);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addAttribute(HtmlConstants.ATTR_HREF, href != null ? href : "#", false);
		return elem;
	}
	
	public static GenericElement startActionLink(GenericElement elem, String id, String action, Map<String, String> params) {
		elem.reset(HtmlConstants.ELEM_LINK);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addActionAttribute(HtmlConstants.ATTR_HREF, action, params);
		return elem;
	}
	
	public static void endLink(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_LINK);
	}
	
	public static GenericElement startImage(GenericElement elem, String id, String src, String alt) {
		elem.reset(HtmlConstants.ELEM_IMG);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		if( src == null ) throw new IllegalArgumentException("Must have a source on an image");
		elem.addResourceAttribute(HtmlConstants.ATTR_SRC, src);
		elem.addAttribute(HtmlConstants.ATTR_ALT, alt, true);
		return elem;
	}
	
	public static void endImage(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_IMG);
	}
	
	public static void writeScript(GenericElement elem, Writer writer, String src) throws IOException {
		elem.reset(HtmlConstants.ELEM_SCRIPT);
		elem.addResourceAttribute(HtmlConstants.ATTR_SRC, src);
		elem.write(writer, false); elem.writeTerminate(writer);
		writer.write('\n');
	}
	
	public static void writeCssResource(GenericElement elem, Writer writer, String[] src) throws IOException {
		elem.reset(HtmlConstants.ELEM_STYLE);
		elem.addAttribute(HtmlConstants.ATTR_TYPE, "text/css", false);
		elem.write(writer, false);
		for( int i = 0 ; i < src.length ; i++ ) {
			writer.write('\n');
			writer.write("\t@import \"");
			writer.write(elem.getAttributeAdaptor().adaptResource(src[i]));
			writer.write("\";");
		}
		writer.write('\n');
		elem.writeTerminate(writer);
		writer.write('\n');
	}
	
	///////////////////////////////////////////
	// Decoration
	
	/**
	 * Adds a title to an element
	 */
	public static GenericElement addTitle(GenericElement elem, String title) {
		elem.addAttribute(HtmlConstants.ATTR_TITLE, title, true);
		return elem;
	}
	
	/**
	 * Decorates an element by adding style and/or class attributes to it.
	 * 
	 * @return The passed in element for convienience
	 */
	public static GenericElement addStyle(GenericElement elem, String styleClass, String style) {
		elem.addAttribute(HtmlConstants.ATTR_CLASS, styleClass, false);
		elem.addAttribute(HtmlConstants.ATTR_STYLE, style, false);
		return elem;
	}
}
