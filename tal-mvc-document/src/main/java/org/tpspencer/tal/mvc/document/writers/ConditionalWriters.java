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

import java.util.List;

import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.ElementWriter;

/**
 * This is a wrapper class that holds all the common conditional
 * writers. These writers check on a certain condition before
 * delegating to other writers that actually output anything.
 * 
 * @author Tom Spencer
 */
public class ConditionalWriters {

	/**
	 * Base class for conditional writers that determine a set
	 * of writers to use for output based on a condition
	 * 
	 * @author Tom Spencer
	 */
	public static abstract class ConditionalWriter implements ElementWriter {
		/** The writers to use if condition is true */
		private final ElementWriter trueWriter;
		/** The writers to use if condition is false */
		private final ElementWriter falseWriter;
		
		/**
		 * Convienience constructor that sets only the true
		 * writers given a variable arg list.
		 * 
		 * @param writers The writers (0 or more)
		 */
		public ConditionalWriter(ElementWriter trueWriter, ElementWriter falseWriter) {
			this.trueWriter = trueWriter;
			this.falseWriter = falseWriter;
		}
		
		/**
		 * Chooses the writers based on whether condition is true
		 */
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			boolean check = checkCondition(app, element);
			if( check && trueWriter != null ) {
				trueWriter.write(writer, app, element);
			}
			else if( !check && falseWriter != null ) {
				falseWriter.write(writer, app, element);
			}
		}
		
		/**
		 * Abstract method that derived class must implement to make
		 * the check. Based on the check this class will delegate to 
		 * appropriate writers. 
		 * 
		 * @param app The element representing entire app 
		 * @param current The current application element
		 * @return The result of the condition check
		 */
		protected abstract boolean checkCondition(AppElement app, AppElement current);
	}
	
	/**
	 * Tests the element if it contains a piece of info
	 * against a known value (or simply that it is present).
	 * 
	 * @author Tom Spencer
	 */
	public static class InfoConditionalWriter extends ConditionalWriter {
		/** Holds the name of the info to check against */
		private final String info;
		/** Holds the value we should compare against (or null if it should just be present) */
		private final String value;
		
		public InfoConditionalWriter(String info, ElementWriter writer) {
			super(writer, null);
			this.info = info;
			this.value = null;
		}
		
		public InfoConditionalWriter(String info, String value, ElementWriter writer) {
			super(writer, null);
			this.info = info;
			this.value = value;
		}
		
		public InfoConditionalWriter(String info, ElementWriter trueWriter, ElementWriter falseWriter) {
			super(trueWriter, falseWriter);
			this.info = info;
			this.value = null;
		}
		
		public InfoConditionalWriter(String info, String value, ElementWriter trueWriter, ElementWriter falseWriter) {
			super(trueWriter, falseWriter);
			this.info = info;
			this.value = value;
		}
		
		/**
		 * Determines if given piece of info on the element is 
		 * present or equals the expected value.
		 */
		@Override
		protected boolean checkCondition(AppElement app, AppElement current) {
			String val = current.getInfo(info, null);
			if( val != null && value == null ) return true;
			else if( val != null ) return value.equals(val);
			else return false;
		}
	}
	
	/**
	 * This conditional writer looks at the type of the object
	 * held by the current element to decide what to output.
	 * 
	 * @author Tom Spencer
	 */
	public static class TypeConditionalWriter extends ConditionalWriter {
		/** Holds the expected type of object held by element */
		private final Class<?> expected;
		
		public TypeConditionalWriter(Class<?> expected, ElementWriter writer) {
			super(writer, null);
			this.expected = expected;
		}
		
		public TypeConditionalWriter(Class<?> expected, ElementWriter trueWriter, ElementWriter falseWriter) {
			super(trueWriter, falseWriter);
			this.expected = expected;
		}
		
		/**
		 * Simply checks the current element holds an object of set type
		 */
		@Override
		protected boolean checkCondition(AppElement app, AppElement current) {
			return current.getElement().getClass().equals(expected);
		}
	}
	
	/**
	 * ConditionalWriter that checks if there are children of the
	 * given type.
	 * 
	 * @author Tom Spencer
	 */
	public static class ChildConditionalWriter extends ConditionalWriter {
		/** The type of children to extract from element */
		private final String type;
		
		public ChildConditionalWriter(String type, ElementWriter writer) {
			super(writer, null);
			this.type = type;
		}
		
		public ChildConditionalWriter(String type, ElementWriter trueWriter, ElementWriter falseWriter) {
			super(trueWriter, falseWriter);
			this.type = type;
		}
		
		/**
		 * Checks current element for children of type. Returns true
		 * if there are any children of this type.
		 */
		@Override
		protected boolean checkCondition(AppElement app, AppElement current) {
			List<AppElement> children = current.getChildren(type);
			if( children == null || children.size() == 0 ) return false;
			else return true;
		}
	}
}
