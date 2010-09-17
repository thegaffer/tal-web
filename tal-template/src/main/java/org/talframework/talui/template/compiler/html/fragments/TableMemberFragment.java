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
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.render.elements.html.Row;

public class TableMemberFragment extends MemberFragment {
	
	/**
	 * Default constructor, sets the style of any inner 
	 * templates to "table".
	 */
	public TableMemberFragment() {
		super(null, new String[]{"table-row"});
	}
	
	/**
	 * Wraps the templates in a row
	 */
	@Override
	protected RenderElement wrapTemplate(GenericCompiler compiler, Template template, TemplateElement element, String templateName, RenderElement renderedTemplate) {
		Row rw = new Row(templateName);
		rw.setStyleClass(templateName);
		rw.setNewLineAfterStart(true);
		rw.setNewLineAfterTerminate(true);
		rw.addElement(renderedTemplate);
		return rw;
	}
}
