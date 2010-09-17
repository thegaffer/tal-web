/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.compiler.html.fragments;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.render.elements.html.Cell;
import org.talframework.talui.template.render.elements.html.Row;
import org.talframework.talui.template.render.elements.html.Table;

/**
 * This fragment is matched to an element, typically a grid-group,
 * that represents a table. The fragment starts the table and sets
 * the table style. Each inner member is then treated as rows of
 * the table and direct to templates. Non-member element children
 * will likely cause incorrect output.
 * 
 * @author Tom Spencer
 */
public class TableGroupFragment implements FragmentMold {
	
	private String tablePropertySet = "htmlTable";
	private String headingPropertySet = "htmlHeadings";

	/**
	 * Returns true of the element has a headings setting
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.hasSetting("headings");
	}
	
	/**
	 * Creates the table and headings
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		Table tbl = new Table(element.getName());
		tbl.setStyleClass(element.getName());
		tbl.setAttributes(element.getPropertySet(tablePropertySet));
		tbl.setNewLineAfterStart(true);
		tbl.setNewLineAfterTerminate(true);
		
		String[] headings = element.getSetting("headings", String[].class);
		
		if( headings != null && headings.length > 0 ) {
			Row rw = new Row(element.getName() + "-headings");
			rw.setAttributes(element.getPropertySet(headingPropertySet));
			rw.addStyleClass("headings");
			rw.setNewLineAfterTerminate(true);
			tbl.addElement(rw);
			
			for( int i = 0 ; i < headings.length ; i++ ) {
				Cell heading = new Cell(element.getName() + "-" + headings[i], true);
				heading.setMessage("label.heading." + headings[i]);
				heading.setStyleClass(headings[i]);
				rw.addElement(heading);
			}
		}
		
		return tbl;
	}
}
