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

import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.behaviour.InnerTemplateElement;
import org.tpspencer.tal.template.behaviour.MemberProperty;
import org.tpspencer.tal.template.behaviour.supporting.ContainerElement;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;

public class BaseRenderMold {

	/**
	 * Simple tells the template mold to compile any children
	 */
	public void compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element, RenderElement parent) {
		if( element.getBehaviour(ContainerElement.class) != null ) {
			ContainerElement container = element.getBehaviour(ContainerElement.class);
			List<TemplateElement> elems = container.getElements();
			if( elems != null && elems.size() > 0 ) {
				Iterator<TemplateElement> it = elems.iterator();
				while( it.hasNext() ) {
					RenderElement re = templateMold.compileChild(compiler, template, it.next());
					if( re != null ) {
						parent.addElement(re);
					}
				}
			}
		}
		
		if( element.getBehaviour(MemberProperty.class) != null ) {
			MemberProperty member = element.getBehaviour(MemberProperty.class);
			member.getTemplate();
			compiler.compileTemplate(member.getTemplate(), null, null);
		}
		if( element.getBehaviour(InnerTemplateElement.class) != null ) {
			InnerTemplateElement member = element.getBehaviour(InnerTemplateElement.class);
			member.getTemplate();
			compiler.compileTemplate(member.getTemplate(), null, null);
		}
	}
}
