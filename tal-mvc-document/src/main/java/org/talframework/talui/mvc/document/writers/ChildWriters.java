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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.ElementWriter;

/**
 * This is a wrapper class for all the different child
 * writers that each take the children of the current
 * element and do something with each one.
 * 
 * @author Tom Spencer
 */
public class ChildWriters {

	/**
	 * This interface represents something that can get children
	 * of an element.
	 * 
	 * @author Tom Spencer
	 */
	public interface ChildAccessor {
		
		/**
		 * Gets the children from the current element
		 * 
		 * @param app The app element
		 * @param element The current element
		 * @return The children
		 */
		public Collection<AppElement> getChildren(AppElement app, AppElement element);
	}
	
	/**
	 * Simple gets children of give type from {@link AppElement}
	 * 
	 * @author Tom Spencer
	 */
	public static class SimpleChildAccessor implements ChildAccessor {
		private final String childType;
		
		public SimpleChildAccessor(String childType) {
			this.childType = childType;
		}
		
		/**
		 * Simply gets the children of given type
		 */
		public Collection<AppElement> getChildren(AppElement app, AppElement element) {
			return element.getChildren(childType);
		}
	}
	
	/**
	 * Gets children of children in one fail swoop
	 * 
	 * @author Tom Spencer
	 */
	public static class SubChildChildAccessor extends SimpleChildAccessor {
		private final String subChild;
		
		public SubChildChildAccessor(String childType, String subChildType) {
			super(childType);
			this.subChild = subChildType;
		}
		
		/**
		 * Gets the children of each child and adds to a super list
		 */
		@Override
		public Collection<AppElement> getChildren(AppElement app, AppElement element) {
			Collection<AppElement> ret = new ArrayList<AppElement>();
			
			Collection<AppElement> children = super.getChildren(app, element);
			for( AppElement child : children ) {
				ret.addAll(child.getChildren(subChild));
			}
			
			return ret;
		}
	}
	
	/**
	 * Matches the children of current element to root elements
	 * of given rootType with same ID and gets the children from
	 * there.
	 * 
	 * @author Tom Spencer
	 */
	public static class RootMatchChildAccessor extends SimpleChildAccessor {
		private final String rootType;
		private final String rootChildType;
		
		public RootMatchChildAccessor(String childType, String rootType, String rootChildType) {
			super(childType);
			this.rootType = rootType;
			this.rootChildType = rootChildType;
		}
		
		/**
		 * Matches each child to a root type and gets it children
		 */
		@Override
		public Collection<AppElement> getChildren(AppElement app, AppElement element) {
			Collection<AppElement> ret = new ArrayList<AppElement>();
			Collection<AppElement> children = super.getChildren(app, element);
			for( AppElement child : children ) {
				AppElement root = app.getChild(rootType, child.getId());
				if( root != null ) {
					if( rootChildType == null ) ret.add(root);
					else ret.addAll(root.getChildren(rootChildType));
				}
			}
			
			return ret;
		}
	}
	
	/**
	 * This class writes out a list of application elements
	 * given the {@link AppElement} passed in.
	 * 
	 * @author Tom Spencer
	 */
	public static class ListWriter implements ElementWriter {

		/** Accessor to get children */ 
		private final ChildAccessor childAccessor;
		/** The string to include against each element in the list */
		private final String description;
		
		/**
		 * Constructs a list writer that creates a single list entry
		 * for each element passed in
		 * 
		 * @param type The type of child to extract from current element
		 * @param desc The list/bullet item for each child
		 */
		public ListWriter(String type, String desc) {
			this.childAccessor = new SimpleChildAccessor(type);
			this.description = desc;
		}
		
		/**
		 * Constructs a list writer that creates a single list entry
		 * for each element passed in
		 * 
		 * @param accessor The children accessor instance to use
		 * @param desc The list/bullet item for each child
		 */
		public ListWriter(ChildAccessor accessor, String desc) {
			this.childAccessor = accessor;
			this.description = desc;
		}
		
		/**
		 * Writes out each element. This writes
		 * 
		 * @param writer The writer
		 * @param elements The elements to output
		 * @param params Optional parameters to pass when writing prelim paragraphs
		 */
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			Collection<AppElement> children = childAccessor.getChildren(app, element);
			
			writer.startList();
			for( AppElement child : children ) {
				writer.addListItem(description, child);
			}
			writer.endList();
		}
	}
	
	/**
	 * This class will write out a table in the document iterating
	 * around a given type of child from element. 
	 * 
	 * @author Tom Spencer
	 */
	public static class ChildTableWriter implements ElementWriter {
		
		/** Accessor to get children */ 
		private final ChildAccessor childAccessor;
		/** The resource to use for columns, title is suffixed .title, and for desc .desc */
		private final String[] columns;
		
		public ChildTableWriter(String childType, String... columns) {
			this.childAccessor = new SimpleChildAccessor(childType);
			this.columns = columns;
		}
		
		public ChildTableWriter(ChildAccessor accessor, String... columns) {
			this.childAccessor = accessor;
			this.columns = columns;
		}
		
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			Collection<AppElement> children = childAccessor.getChildren(app, element);
			if( children == null || children.size() == 0 ) return;

			String[] headings = new String[columns.length];
			String[] cols = new String[columns.length];
			for( int i = 0 ; i < columns.length ; i++ ) {
				headings[i] = columns[i] + ".title";
				cols[i] = columns[i] + ".desc"; 
			}
			
			writer.startTable(headings);
			for( AppElement child : children ) {
				writer.addTableRow(cols, child);
			}
			writer.endTable();
		}
	}
	
	/**
	 * This writer writes out a single summary bullet list point
	 * about the children of the current element.
	 * 
	 * @author Tom Spencer
	 */
	public static class SummaryBulletWriter implements ElementWriter {

		/** Accessor to get children */ 
		private final ChildAccessor childAccessor;
		/** The resource to output if there are no children of childType */
		private final String emptyResource;
		/** The resource to output if there are children of childType */
		private final String resource;
		
		/**
		 * Constructs an instance with childtype, empty resource 
		 * and resource.
		 * 
		 * @param childType The child type to get from the element
		 * @param empty The empty resource to output if list is empty
		 * @param resource The resource to output if list is not empty
		 */
		public SummaryBulletWriter(String childType, String empty, String resource) {
			this.childAccessor = new SimpleChildAccessor(childType);
			this.emptyResource = empty;
			this.resource = resource;
		}
		
		/**
		 * Constructs an instance with childtype, empty resource 
		 * and resource.
		 * 
		 * @param accessor The {@link ChildAccessor} to use to get children
		 * @param empty The empty resource to output if list is empty
		 * @param resource The resource to output if list is not empty
		 */
		public SummaryBulletWriter(ChildAccessor accessor, String empty, String resource) {
			this.childAccessor = accessor;
			this.emptyResource = empty;
			this.resource = resource;
		}
		
		/**
		 * Outputs the single bullet list point as appropriate
		 */
		public void write(DocumentWriter writer, AppElement app, AppElement element) {
			Collection<AppElement> children = childAccessor.getChildren(app, element);
			if( children == null || children.size() == 0 ) {
				writer.writeParagraph(emptyResource, null);
			}
			else {
				Map<String, String> params = new HashMap<String, String>(); 
				params.put("Nos", Integer.toString(children.size()));
				
				writer.startList();
				writer.addListItem(resource, element);
				writer.endList();
			}
		}
	}
}
