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

package org.talframework.talui.template.compiler.js;

import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.supporting.ReferenceElement;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.render.elements.js.ReferencePageAttachment;
import org.talframework.talui.template.render.elements.special.EmptyElement;

/**
 * The default mold is used for all elements by default and
 * includes the basic Javascript added to fields. This 
 * includes ...
 * <ul>
 * <li>Wrapper event handler attachment
 * <li>Label event handler attachment
 * <li>Value event handler attachment
 * <li>Hover over frames for reference pages
 * </ul>
 * 
 * @author Tom Spencer
 */
public class JsElementMold extends BaseJsElementMold {
	
	/**
	 * Default constructor
	 */
	public JsElementMold() {
		
	}
	
	/**
	 * Constructs a normal element mold with styles and templateStyles
	 * set for inner members and template.
	 * 
	 * @param styles The styles
	 * @param templateStyles The template styles
	 */
	public JsElementMold(String[] styles, String[] templateStyles) {
		super(styles, templateStyles);
	}

	/**
	 * The default only checks for events assigned to the wrapper
	 */
	@Trace
    public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		RenderElement ret = super.compile(compiler, templateMold, template, element);
		
		// Attach Handlers to wrappers, label and value elements
		ret = checkHandlers(compiler, template, element, "wrapper", ret);
		ret = checkHandlers(compiler, template, element, "value", ret);
		ret = checkHandlers(compiler, template, element, "label", ret);
		ret = checkReferencePage(template, element, ret);
		
		return ret;
	}
	
	/**
	 * Adds a reference page attachment element if the element supports
	 * the reference page behaviour. 
	 * 
	 * @param template
	 * @param element
	 * @param base
	 * @return
	 */
	private RenderElement checkReferencePage(Template template, TemplateElement element, RenderElement base) {
		ReferenceElement ref = element.getBehaviour(ReferenceElement.class);
		if( ref == null ) return base;
		
		if( base == null ) base = new EmptyElement();
		base.addElement(new ReferencePageAttachment(template.getName() + "-" + element.getName()));
		
		return base;
	}
}
