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

package org.tpspencer.tal.mvc.document.spec.render;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateElementRenderMold;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;
import org.tpspencer.tal.template.render.elements.special.EmptyElement;

public class GroupRenderMold extends BaseRenderMold implements TemplateElementRenderMold {

	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		RenderElement entry = null;
		
		// Special groups we do want to explicitly mention
		if( "composite-group".equals(element.getType()) ) {
			entry = new SpecEntryRenderElement(element.getName(), "grouping", "The members of this group share the groups label");
		}
		else if( "form-group".equals(element.getType()) ) {
			entry = new SpecEntryRenderElement(element.getName(), "grouping", "The group defines a form which invokes the " + element.getSetting("action", String.class) + " action");
		}
		else if( "grid-group".equals(element.getType()) ) {
			entry = new SpecEntryRenderElement(element.getName(), "grouping", "The group defines a table, the top-level members are treated as columns in the table");
		}
		else if( "action-group".equals(element.getType()) ) {
			entry = new SpecEntryRenderElement(element.getName(), "action", "This action group invokes the " + element.getSetting("action", String.class) + " action");
		}
		
		RenderElement ret = null;
		if( element.getName() != null && !"".equals(element.getName().trim()) ) {
			ret = new SpecGroupRenderElement(element.getName());
		}
		else {
			ret = new EmptyElement();
		}
		
		if( entry != null ) ret.addElement(entry);
		
		super.compile(compiler, templateMold, template, element, entry != null ? entry : ret);
		
		return ret;
	}
}
