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

import java.util.Iterator;
import java.util.List;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.supporting.ContainerElement;
import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.compiler.html.HtmlElementConstants;
import org.talframework.talui.template.render.elements.special.EmptyElement;

/**
 * This class is a fragment that invokes compilation of
 * each child of this element.
 * 
 * @author Tom Spencer
 */
public class ChildrenFragment implements FragmentMold {

	/**
	 * Returns true if the element is a container element
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.getBehaviour(ContainerElement.class) != null;
	}
	
	/**
	 * Invokes the templateMold to render each child we have. 
	 * The children are wrapped in an empty element that 
	 * produces no output. If there are no children no render
	 * elements are returned.
	 */
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
		
		ContainerElement containerElement = element.getBehaviour(ContainerElement.class);
		List<TemplateElement> elements = containerElement.getElements();
		if( elements == null || elements.size() == 0 ) return null;
		
		EmptyElement ret = new EmptyElement();
		Iterator<TemplateElement> it = elements.iterator();
		compiler.addTemplateStyle(HtmlElementConstants.STYLE_IN_ELEMENT);
		while( it.hasNext() ) {
			TemplateElement elem = it.next();
			
			RenderElement child = templateMold.compileChild(compiler, template, elem);
			
			ret.addElement(child);
		}
		compiler.removeTemplateStyle(HtmlElementConstants.STYLE_IN_ELEMENT);
		
		return ret;
	}
}
