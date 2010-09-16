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

package org.tpspencer.tal.util.htmlhelper;

/**
 * This class contains a whole bunch of standard html 
 * element names and attribute names. Whilst usage of
 * this class directly is possible usage is discouraged
 * rather you should use the element building util
 * classes and their methods so as to not be tied to 
 * html per se. Not that it really matters - if your
 * here you know you are using html!
 * 
 * @author Tom Spencer
 */
public class HtmlConstants {
	// Base Elements
	public static final String[] ELEM_HEADINGS = new String[]{"h1", "h2", "h3", "h4", "h5", "h6"};
	public static final String ELEM_PARA = "p";
	public static final String ELEM_DIV = "div";
	public static final String ELEM_SPAN = "span";
	public static final String ELEM_LINK = "a";
	public static final String ELEM_IMG = "img";
	
	public static final String ELEM_SCRIPT = "script";
	public static final String ELEM_STYLE = "style";
	
	// Form Elements
	public static final String ELEM_FORM = "form";
	public static final String ELEM_LABEL = "label";
	public static final String ELEM_INPUT = "input";
	public static final String ELEM_TEXTAREA = "textarea";
	public static final String ELEM_SELECT = "select";
	public static final String ELEM_OPTION = "option";
	public static final String ELEM_BUTTON = "button";
	
	// Table Elements
	public static final String ELEM_TABLE = "table";
	public static final String ELEM_HEADING = "th";
	public static final String ELEM_CELL = "td";
	public static final String ELEM_ROW = "tr";

	// Base Attributes
	public static final String ATTR_ID = "id";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_CLASS = "class";
	public static final String ATTR_STYLE = "style";
	public static final String ATTR_TITLE = "title";
	public static final String ATTR_ACCESS = "accesskey";
	public static final String ATTR_TAB = "tabindex";
	
	// Links
	public static final String ATTR_HREF = "href";
	
	// Images
	public static final String ATTR_SRC = "src";
	public static final String ATTR_ALT = "alt";
	
	// Events
	public static final String ATTR_ONFOCUS = "onfocus";
	public static final String ATTR_ONBLUR = "onblur";
	public static final String ATTR_ONDBLCLICK = "ondblclick";
	public static final String ATTR_ONCLICK = "onclick";
	public static final String ATTR_ONMOUSEDOWN = "onmousedown";
	public static final String ATTR_ONMOUSEMOVE = "onmousemove";
	public static final String ATTR_ONMOUSEOUT = "onmouseout";
	public static final String ATTR_ONMOUSEOVER = "onmouseover";
	public static final String ATTR_ONMOUSEUP = "onmouseup";
	public static final String ATTR_ONKEYDOWN = "onkeydown";
	public static final String ATTR_ONKEYPRESS = "onkeypress";
	public static final String ATTR_ONKEYUP = "onkeyup";
	public static final String ATTR_ONSELECT = "onselect";
	public static final String ATTR_ONCHANGE = "onchange";
	public static final String ATTR_ONSUBMIT = "onsubmit";
	public static final String ATTR_ONRESET = "onreset";
	
	// Form Specific
	public static final String ATTR_ACTION = "action";
	public static final String ATTR_METHOD = "method";
	public static final String ATTR_FOR = "for";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_SIZE = "size";
	public static final String ATTR_MAXLENGTH = "maxlength";
	public static final String ATTR_MULTIPLE = "multiple";
	public static final String ATTR_SELECTED = "selected";
	public static final String ATTR_CHECKED = "checked";

	// Table Specific
	
	// Function
	public static final String[] FUNC_THIS_PARAM = new String[]{"this"};
}
