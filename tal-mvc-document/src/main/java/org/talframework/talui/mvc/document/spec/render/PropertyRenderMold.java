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

public class PropertyRenderMold extends BaseRenderMold implements TemplateElementRenderMold {

	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		String desc = "A property";
		// TODO: Pad desc out with more information
		
		SpecEntryRenderElement ret = new SpecEntryRenderElement(
				element.getName(), 
				element.getType(), 
				desc);
		
		super.compile(compiler, templateMold, template, element, ret);
		
		return ret;
	}
}
