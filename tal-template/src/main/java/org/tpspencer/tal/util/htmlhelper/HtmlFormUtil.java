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
public class HtmlFormUtil {
	public static final String METHOD_POST = "POST";
	
	private static final String TYPE_TEXT = "text";
	private static final String TYPE_BUTTON = "button";
	private static final String TYPE_BUTTON_SUBMIT = "submit";
	private static final String TYPE_BUTTON_RESET = "reset";
	private static final String TYPE_HIDDEN = "hidden";
	private static final String TYPE_CHECKBOX = "checkbox";
	private static final String TYPE_RADIO = "radio";
	// private static final String TYPE_SELECT = "select";
	
	//private static final String TYPE_SUBMIT = "submit";
	
	private static final String ATTR_CHECKED = "checked";
	
	public static GenericElement startForm(GenericElement elem, String id, String name, String method, String action) {
		elem.reset(HtmlConstants.ELEM_FORM);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addNameAttribute(HtmlConstants.ATTR_NAME, name);
		elem.addAttribute(HtmlConstants.ATTR_METHOD, method == null ? METHOD_POST : method, false);
		if( action == null ) throw new IllegalArgumentException("Cannot start a form without an action");
		elem.addActionAttribute(HtmlConstants.ATTR_ACTION, action, null);
		return elem;
	}
	
	public static void endForm(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_FORM);
	}
	
	public static GenericElement startLabel(GenericElement elem, String id, String forField) {
		elem.reset(HtmlConstants.ELEM_LABEL);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addAttribute(HtmlConstants.ATTR_FOR, forField, false);
		return elem;
	}
	
	public static void endLabel(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_LABEL);
	}
	
	/**
	 * Generic helper for any input element. There are
	 * more specific variations below
	 */
	public static GenericElement startInput(GenericElement elem, String type, String id, String name, String value) {
		elem.reset(HtmlConstants.ELEM_INPUT);
		elem.addAttribute(HtmlConstants.ATTR_TYPE, type, false);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addNameAttribute(HtmlConstants.ATTR_NAME, name);
		elem.addAttribute(HtmlConstants.ATTR_VALUE, value, true, true);
		return elem;
	}
	
	public static GenericElement startTextInput(GenericElement elem, String id, String name, String value, String size, String maxlength) {
		startInput(elem, TYPE_TEXT, id, name, value);
		elem.addAttribute(HtmlConstants.ATTR_SIZE, size, false);
		elem.addAttribute(HtmlConstants.ATTR_MAXLENGTH, maxlength, false);
		return elem;
	}
	
	public static GenericElement startHiddenInput(GenericElement elem, String id, String name, String value) {
		return startInput(elem, TYPE_HIDDEN, id, name, value);
	}
	
	public static GenericElement startCheckboxInput(GenericElement elem, String id, String name, String value, boolean checked) {
		startInput(elem, TYPE_CHECKBOX, id, name, value);
		if( checked ) elem.addAttribute(ATTR_CHECKED);
		return elem;
	}
	
	public static GenericElement startRadioInput(GenericElement elem, String id, String name, String value, boolean checked) {
		startInput(elem, TYPE_RADIO, id, name, value);
		if( checked ) elem.addAttribute(ATTR_CHECKED);
		return elem;
	}
	
	public static GenericElement startButtonInput(GenericElement elem, String id, String name, String value) {
		startInput(elem, TYPE_BUTTON, id, name, value);
		return elem;
	}
	
	public static GenericElement startSubmitInput(GenericElement elem, String id, String name, String value) {
		startInput(elem, TYPE_BUTTON_SUBMIT, id, name, value);
		return elem;
	}
	
	public static GenericElement startResetInput(GenericElement elem, String id, String name, String value) {
		startInput(elem, TYPE_BUTTON_RESET, id, name, value);
		return elem;
	}
	
	public static void endInput(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_INPUT);
	}
	
	/*public static GenericElement startSelectInput(GenericElement elem, String id, String name, String value) {
		
	} + textarea */
	
	public static GenericElement startTextArea(GenericElement elem, String id, String name, String value) {
		elem.reset(HtmlConstants.ELEM_TEXTAREA);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addNameAttribute(HtmlConstants.ATTR_NAME, name);
		elem.addAttribute(HtmlConstants.ATTR_VALUE, value, true, true);
		return elem;
	}
	public static GenericElement startSelectInput(GenericElement elem, String id, String name, String size) {
		elem.reset(HtmlConstants.ELEM_SELECT);
		elem.addIdAttribute(HtmlConstants.ATTR_ID, id);
		elem.addNameAttribute(HtmlConstants.ATTR_NAME, name);
		elem.addAttribute(HtmlConstants.ATTR_SIZE, size, false);
		return elem;
	}
	
	public static void endSelect(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_SELECT);
	}
	
	public static void endTextArea(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_TEXTAREA);
	}
	
	public static GenericElement startOption(GenericElement elem, String value, boolean selected) {
		elem.reset(HtmlConstants.ELEM_OPTION);
		elem.addAttribute(HtmlConstants.ATTR_VALUE, value, true);
		if( selected ) elem.addAttribute(HtmlConstants.ATTR_SELECTED);
		return elem;
	}
	
	public static void endOption(Writer writer) throws IOException {
		GenericElement.writeTerminate(writer, HtmlConstants.ELEM_OPTION);
	}
	
	
	//////////////////////////////////////////////
	// Decorations
	
	public static GenericElement addAccess(GenericElement elem, String access, String tab) {
		elem.addAttribute(HtmlConstants.ATTR_ACCESS, access, false);
		elem.addAttribute(HtmlConstants.ATTR_TAB, tab, false);
		return elem;
	}
}
