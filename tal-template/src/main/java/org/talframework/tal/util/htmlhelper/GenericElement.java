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
import java.util.Map;

public class GenericElement {
	private static final int growSize = 10;
	
	/** Member holds an attribute adaptor */
	private AttributeAdaptor adaptor = null;
	/** Member holds a temp string buffer for this element */
	private StringBuilder temp = null;
	/** Member holds the name of this element */
	private String elementName = null;
	/** Member holds the properties of this element */
	private ElementProperty[] properties = new ElementProperty[growSize];
	/** Member holds the index of the next free property */
	private int freeIndex = 0;
	
	public GenericElement() {
	}
	
	public GenericElement(AttributeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	
	public GenericElement(AttributeAdaptor adaptor, String element) {
		this.adaptor = adaptor;
		this.elementName = element;
	}
	
	public GenericElement(String element) {
		this.elementName = element;
	}
	
	/**
	 * Call to reset this instance to a new element.
	 * All properties are cleared down.
	 * 
	 * @param element The new element to set to
	 */
	public void reset(String element) {
		this.elementName = element;
		
		// Clear down any properties
		for( int i = 0 ; i < properties.length ; i++ ) {
			if( properties[i] != null ) properties[i].reset();
		}
		
		freeIndex = 0;
	}
	
	/**
	 * Call to write out the element and its properties
	 * 
	 * @param writer The writer to write into
	 * @param terminate If true the element will self-terminate
	 */
	public void write(Writer writer, boolean terminate) throws IOException {
		writer.write('<');
		writer.write(elementName);
		
		// Write out each element
		for( int i = 0 ; i < properties.length ; i++ ) {
			if( properties[i] != null ) properties[i].writeAttribute(writer);
		}
		
		if( terminate ) {
			writer.write(' ');
			writer.write('/');
		}
		writer.write('>');
	}
	
	/**
	 * Call to write a separate termination element for the
	 * current settings
	 * 
	 * @param buf The buffer to write into
	 */
	public void writeTerminate(Writer writer) throws IOException {
		writeTerminate(writer, elementName);
	}
	
	/**
	 * Helper to write out the termination element of any element.
	 * Useful if the element has been released for writing children.
	 * 
	 * @param buf The buffer to write into
	 * @param elementName The name of the element
	 */
	public static void writeTerminate(Writer writer, String elementName) throws IOException {
		writer.write('<');
		writer.write('/');
		writer.write(elementName);
		writer.write('>');
	}
	
	/**
	 * @return A (very) temporary string buffer - others may share this!
	 */
	public StringBuilder getTempBuffer() {
		if( temp == null ) temp = new StringBuilder();
		else temp.setLength(0);
		return temp;
	}
	
	/**
	 * @return Protected helper to get the attribute adaptor
	 */
	protected AttributeAdaptor getAttributeAdaptor() {
		return adaptor;
	}
	
	/**
	 * Call to add a special attribute like readonly or 
	 * disabled. Following later specs of HTML we turn 
	 * this, for example, readonly="readonly". Not 
	 * escaped
	 * 
	 * @param name The name of the attribute
	 */
	public void addAttribute(String name) {
		addAttribute(name, name, false);
	}
	
	/**
	 * Call to add a general attribute to the element.
	 * Ignored if the attribute is null, so safe to call
	 * 
	 * @param name The name of the attribute
	 * @param value Its value
	 * @param escape If true the value will be checked for escape params
	 */
	public void addAttribute(String name, String value, boolean escape) {
		addAttribute(name, value, false, escape);
	}
	
	/**
	 * Call to add a boolean attribute to the element.
	 * If only outputs if the value is true, in which 
	 * case it becomes name="true", unless include false
	 * is indicated
	 * 
	 * @param name
	 * @param val
	 * @param includeFalse
	 */
	public void addAttribute(String name, boolean val, boolean incFalse) {
		if( val || incFalse ) addAttribute(name, val ? Boolean.TRUE.toString() : Boolean.FALSE.toString(), false);
	}
	
	/**
	 * Call to add a general attribute to the element and 
	 * control its output even if null.
	 * 
	 * @param name The name of the attribute
	 * @param value The value of the attribute
	 * @param allowNull If true attribute is added even if null
	 */
	public void addAttribute(String name, String value, boolean allowNull, boolean escape) {
		if( !allowNull && value == null ) return;
		else if( value == null ) value = "";
		
		if( freeIndex >= properties.length ) {
			// Grow the array
			ElementProperty[] newArray = new ElementProperty[properties.length + growSize];
			for( int i = 0 ; i < properties.length ; i++ ) {
				newArray[i] = properties[i];
			}
			properties = newArray;
		}
	
		if( properties[freeIndex] == null ) {
			properties[freeIndex] = new ElementProperty(name, value, escape);
		}
		else {
			properties[freeIndex].set(name, value, escape);
		}
		++freeIndex;
	}
	
	/**
	 * Call to pass in an id attribute whereby the adaptor is used
	 * to uniquely prefix the attribute. This is not escaped as
	 * ID's are in the programmers control
	 * 
	 * @param name
	 * @param value
	 */
	public void addIdAttribute(String name, String value) {
		if( value == null ) return;
		value = adaptor != null ? adaptor.adaptId(value) : value;
		addAttribute(name, value, false);
	}
	
	/**
	 * Call to add a 'name' attribute whereby the adpator may
	 * need to vary that name. This is not escaped as
	 * names are in the programmers control
	 * 
	 * @param name
	 * @param value
	 */
	public void addNameAttribute(String name, String value) {
		if( value == null ) return;
		value = adaptor != null ? adaptor.adaptName(value) : value;
		addAttribute(name, value, false);	
	}
	
	/**
	 * Call to add a Javascript function attribute. The function is 
	 * passed through the adaptor to possibly prefix its name with a
	 * namespace.
	 * 
	 * @param name Name of the attriubte
	 * @param value The function() as the value (this may be prefixed)
	 * @param ret If true the return statement is placed in front of the converted value
	 */
	public void addFunctionAttribute(String name, String value, String[] args, boolean ret) {
		if( value == null ) return;
		
		value = adaptor != null ? adaptor.adaptFunction(value) : value;
		
		StringBuilder buf = getTempBuffer();
		
		if( ret ) buf.append("return ");
		buf.append(value);
		
		buf.append('(');
		if( args != null && args.length > 0 ) {
			for( int i = 0 ; i < args.length ; i++ ) {
				if( i > 0 ) buf.append(", ");
				buf.append(args[i]);
			}
		}
		buf.append(')').append(';');
		
		addAttribute(name, buf != null ? buf.toString() : value, false);
	}
	
	/**
	 * Call to add a resource based attribute. Typicaly usage of 
	 * this would be the 'src' attribute of an img, whereby the
	 * source is relative to somewhere in the application.
	 * 
	 * @param name The name of the attribute
	 * @param value It value in base form (i.e. "/img/new.gif")
	 */
	public void addResourceAttribute(String name, String value) {
		value = adaptor != null ? adaptor.adaptResource(value) : value;
		addAttribute(name, value, false);
	}
	
	/**
	 * Call to add an action attribute. This method just adds
	 * the attribute as-is and expects the input to have been
	 * formed into an action parameter for the environment via
	 * the adaptor directly.
	 * 
	 * Call to add a resource based attribute. Typicaly usage of 
	 * this would be the 'src' attribute of an img, whereby the
	 * source is relative to somewhere in the application.
	 * 
	 * @param name The name of the attribute
	 * @param value It value in base form (i.e. "/img/new.gif")
	 */
	public void addActionAttribute(String name, String action, Map<String, String> params) {
		action = adaptor != null ? adaptor.adaptActionUrl(action, params) : action;
		addAttribute(name, action, false);
	}
	
	/**
	 * Call to add a generic url attribute.
	 * 
	 * @param name
	 * @param value
	 */
	public void addUrlAttribute(String name, String action, Map<String, String> params) {
		if( action == null ) return;
		
		action = adaptor != null ? adaptor.adaptActionUrl(action, params) : action;
		addAttribute(name, action, false);
	}
	
	
	/**
	 * Internal class that just holds the name/value pair
	 * of any compiled attribute
	 * 
	 * @author Tom Spencer
	 */
	private class ElementProperty {
		private String name = null;
		private String value = null;
		private boolean requireEscaping = false;
		
		/**
		 * Constructs a new element property given its name and value
		 * 
		 * @param name
		 * @param value
		 */
		public ElementProperty(String name, String value, boolean escape) {
			this.name = name;
			this.value = value;
			this.requireEscaping = escape;
		}
		
		/**
		 * Call to reset the property (set name/value to null)
		 */
		public void reset() {
			this.name = null;
			this.value = null;
			this.requireEscaping = false;
		}
		
		/**
		 * Call to set the property value
		 * 
		 * @param name
		 * @param value
		 */
		public void set(String name, String value, boolean escape) {
			this.name = name;
			this.value = value;
			this.requireEscaping = escape;
		}
		
		/**
		 * Call to write the attribute out in ' name="value"' form/
		 * Skips itself if the name is null
		 * 
		 * @param writer The writer to write into
		 */
		public void writeAttribute(Writer writer) throws IOException {
			if( name == null ) return;
			
			writer.write(' ');
			writer.write(name);
			writer.write('=');
			writer.write('"');
			
			if( value != null ) {
				if( requireEscaping ) HtmlCharStripper.strip(value, writer);
				else writer.write(value);
			}
			
			writer.write('"');
		}
	}
}
