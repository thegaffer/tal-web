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

/**
 * This class tries to simplify the writing of form elements
 * by providing convienience methods to output common form
 * elements and append common sets of attributes to a generic
 * element object.
 * 
 * @author Tom Spencer
 */
public class HtmlTableUtil {
	private static final String ELEM_TABLE = "table";
	private static final String ELEM_ROW = "tr";
	private static final String ELEM_CELL = "td";
	private static final String ELEM_HEADING = "th";
	
	private static final String ATTR_CELLSPACING = "cellspacing";
	private static final String ATTR_CELLPADDING = "cellpadding";

	public static GenericElement startTable(GenericElement html, String id, String styleClass, String cellSpacing, String cellPadding) {
		html.reset(ELEM_TABLE);
		html.addIdAttribute(HtmlConstants.ATTR_ID, id);
		html.addAttribute(HtmlConstants.ATTR_CLASS, styleClass, false);
		html.addAttribute(ATTR_CELLSPACING, cellPadding, false);
		html.addAttribute(ATTR_CELLPADDING, cellSpacing, false);
		return html;
	}
	
	public static void endTable(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, ELEM_TABLE);
	}
	
	public static GenericElement startRow(GenericElement html, String id, String styleClass) {
		html.reset(ELEM_ROW);
		html.addIdAttribute(HtmlConstants.ATTR_ID, id);
		html.addAttribute(HtmlConstants.ATTR_CLASS, styleClass, false);
		return html;
	}
	
	public static void endRow(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, ELEM_ROW);
	}
	
	public static GenericElement startCell(GenericElement html, String id, String styleClass, String style) {
		html.reset(ELEM_CELL);
		html.addIdAttribute(HtmlConstants.ATTR_ID, id);
		HtmlElementUtil.addStyle(html, styleClass, style);
		return html;	
	}
	
	public static void endCell(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, ELEM_CELL);
	}
	
	public static GenericElement startHeadingCell(GenericElement html, String id, String styleClass) {
		html.reset(ELEM_HEADING);
		html.addIdAttribute(HtmlConstants.ATTR_ID, id);
		html.addAttribute(HtmlConstants.ATTR_CLASS, styleClass, false);
		return html;
	}
	
	public static void endHeadingCell(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, ELEM_HEADING);
	}
}
