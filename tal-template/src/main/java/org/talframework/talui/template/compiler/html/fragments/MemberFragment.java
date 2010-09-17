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
import org.talframework.talui.template.behaviour.MemberProperty;
import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.render.elements.html.Div;
import org.talframework.talui.template.render.elements.special.ArrayElement;
import org.talframework.talui.template.render.elements.special.CollectionElement;
import org.talframework.talui.template.render.elements.special.DynamicMemberElement;
import org.talframework.talui.template.render.elements.special.MapElement;
import org.talframework.talui.template.render.elements.special.MemberElement;

/**
 * This fragment will create render elements for the member 
 * property.
 * 
 * @author Tom Spencer
 */
public class MemberFragment implements FragmentMold {
	
	/** Holds the styles to set when compiling template */
	private String[] innerStyles = null;
	/** Any styles to set as template styles when compiling inner template */
	private String[] innerTemplateStyles = null;
	
	
	public MemberFragment() {
	}
	
	public MemberFragment(String[] styles, String[] templateStyles) {
		this.innerStyles = styles;
		this.innerTemplateStyles = templateStyles;
	}

	/**
	 * Returns true if the property is a member property
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.getBehaviour(MemberProperty.class) != null;
	}
	
	/**
	 * Creates a special member property for the child.
	 */
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
		
		MemberProperty member = element.getBehaviour(MemberProperty.class);
		
		// Compiler and wrap inner template
		String templateName = member.getTemplate();
		RenderElement renderedTemplate = compiler.compileTemplate(templateName, innerStyles, innerTemplateStyles);
		renderedTemplate = wrapTemplate(compiler, template, element, templateName, renderedTemplate);
		
		RenderElement ret = null;
		if( !member.isTypeKnown() ) {
			ret = new DynamicMemberElement(member.getName(), renderedTemplate);
		}
		else if( member.isMap() ) {
			ret = new MapElement(member.getName(), renderedTemplate);
		}
		else if( member.isCollection() ) {
			ret = new CollectionElement(member.getName(), renderedTemplate);
		}
		else if( member.isArray() ) {
			ret = new ArrayElement(member.getName(), renderedTemplate);
		}
		else {
			ret = new MemberElement(member.getName(), renderedTemplate);
		}
		
		return ret;
	}
	
	/**
	 * This internal helper, which can be overridden, is used to wrap
	 * the compiled template in some form of shell. By default this is
	 * a div, but it could be a row in a table or other construct. 
	 * 
	 * @param compiler The compiler
	 * @param template The template we are in right now (not of the compiled inner template)
	 * @param element The element 
	 * @param templateName The inner template name
	 * @param renderedTemplate The inner template compiled
	 * @return The wrapping element
	 */
	protected RenderElement wrapTemplate(GenericCompiler compiler, Template template, TemplateElement element, String templateName, RenderElement renderedTemplate) {
		Div dv = new Div(templateName);
		dv.setAsDiv(true);
		dv.setStyleClass(templateName);
		dv.setNewLineAfterStart(true);
		dv.setNewLineAfterTerminate(true);
		dv.addElement(renderedTemplate);
		return dv;
	}

	/**
	 * @return the innerStyles
	 */
	public String[] getInnerStyles() {
		return innerStyles;
	}

	/**
	 * @param innerStyles the innerStyles to set
	 */
	public void setInnerStyles(String[] innerStyles) {
		this.innerStyles = innerStyles;
	}

	/**
	 * @return the innerTemplateStyles
	 */
	public String[] getInnerTemplateStyles() {
		return innerTemplateStyles;
	}

	/**
	 * @param innerTemplateStyles the innerTemplateStyles to set
	 */
	public void setInnerTemplateStyles(String[] innerTemplateStyles) {
		this.innerTemplateStyles = innerTemplateStyles;
	}
}
