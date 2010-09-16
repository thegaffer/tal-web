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
import org.tpspencer.tal.template.behaviour.InnerTemplateElement;
import org.tpspencer.tal.template.compiler.FragmentMold;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;

/**
 * This specialised fragment is for elements that have an inner 
 * template. Typically these elements have absolutely no inner
 * settings at all and copy the template styles over.
 * 
 * @author Tom Spencer
 */
public class TemplateFragment implements FragmentMold {

	/**
	 * Ensures it is an inner template element
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.getBehaviour(InnerTemplateElement.class) != null;
	}
	
	/**
	 * Simply gets the template preserving all template styles
	 * at this point - it as if the inner template we copied
	 * into the body of this element.
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		InnerTemplateElement innerTemplate = element.getBehaviour(InnerTemplateElement.class);
		return compiler.compileTemplate(innerTemplate.getTemplate(), null, compiler.getTemplateStyles());
	}
}
