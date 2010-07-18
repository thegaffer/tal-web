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

package org.tpspencer.tal.template.compiler.html.fragments;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.compiler.FragmentMold;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;
import org.tpspencer.tal.template.render.elements.html.Form;

/**
 * This fragment is matched to explicitly to a group or element
 * that directly represents a form. This fragment produces the
 * form.
 * 
 * @author Tom Spencer
 */
public class FormGroupFragment implements FragmentMold {
	
	private String propertySet = "htmlForm";
	
	/**
	 * Returns true if element has an action setting
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.hasSetting("action");
	}
	
	/**
	 * Adds in the table and headings
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		String action = element.getSetting("action", String.class);
		
		Form frm = new Form(element.getName(), action);
		frm.setAttributes(element.getPropertySet(propertySet));
		frm.setNewLineAfterStart(true);
		frm.setNewLineAfterTerminate(true);
		
		return frm;
	}
}
