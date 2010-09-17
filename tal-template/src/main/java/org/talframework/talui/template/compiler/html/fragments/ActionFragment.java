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
import org.talframework.talui.template.behaviour.CommandElement;
import org.talframework.talui.template.behaviour.supporting.ContainerElement;
import org.talframework.talui.template.behaviour.supporting.ReferenceElement;
import org.talframework.talui.template.behaviour.supporting.ResourceProperty;
import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.compiler.html.HtmlElementConstants;
import org.talframework.talui.template.render.elements.DynamicParameter;
import org.talframework.talui.template.render.elements.ResourceParameter;
import org.talframework.talui.template.render.elements.html.Cell;
import org.talframework.talui.template.render.elements.html.Div;
import org.talframework.talui.template.render.elements.html.Link;
import org.talframework.talui.template.render.elements.html.attributes.RenderParameterAttribute;
import org.talframework.talui.template.render.elements.special.WrappingRenderElement;

/**
 * This fragment acts as the wrapper for a group or property
 * that is associated as a command which broadly means it is
 * a link. 
 * 
 * <p>Because this fragment acts as a wrapper there is logic 
 * to output a cell if we are in a table at the top level, 
 * just like the standard wrapper.</p>
 * 
 * TODO: Extend from WrapperFragment - make wrapper fragment work out top and delegate!
 * 
 * @author Tom Spencer
 */
public class ActionFragment implements FragmentMold {
	
	private String propertySet = "htmlLink";
	private String cellPropertySet = "htmlWrapper";

	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.getBehaviour(CommandElement.class) != null;
	}
	
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
		
		RenderElement wrapper = null;
		if( !compiler.isStyle(HtmlElementConstants.STYLE_IN_ELEMENT) && 
				compiler.isStyle(HtmlElementConstants.STYLE_IN_TABLE_ROW) ) {
			Cell cl = new Cell(element.getName() + "-grp");
			cl.addStyleClass(element.getType());
			cl.addStyleClass("role-wrapper");
			cl.addStyleClass(template.getName() + "-" + element.getName());
			
			if( element.getBehaviour(ReferenceElement.class) != null ) {
				cl.addAttribute(new RenderParameterAttribute("rel", new DynamicParameter(element, "getReferenceUrl")));
			}
			
			cl.setAttributes(element.getPropertySet(cellPropertySet));
			cl.setNewLineAfterTerminate(true);
			wrapper = cl;
		}
		
		CommandElement cmd = element.getBehaviour(CommandElement.class);
		
		// Get the message
		String message = null;
		if( element instanceof ResourceProperty ) message = ((ResourceProperty)element).getResource();
		else if( !(element instanceof ContainerElement) ) message = "label." + template.getName() + "." + element.getName();
		
		Link ret = new Link(element.getName(), cmd);
		if( message != null ) {
			Div msgSpan = new Div(element.getName() + "-content", new ResourceParameter(message));
			msgSpan.setAsDiv(false);
			ret.addElement(msgSpan);
		}
		if( wrapper == null ) {
			ret.addStyleClass("role-wrapper");
			ret.addStyleClass(template.getName() + "-" + element.getName());
		}
		ret.addStyleClass(element.getType());
		ret.setAttributes(element.getPropertySet(propertySet));
		
		// Add to wrapper or return directly
		if( wrapper != null ) {
			return new WrappingRenderElement(wrapper, ret);
		}
		else {
			return ret;
		}
	}

	/**
	 * @return the propertySet
	 */
	public String getPropertySet() {
		return propertySet;
	}

	/**
	 * @param propertySet the propertySet to set
	 */
	public void setPropertySet(String propertySet) {
		this.propertySet = propertySet;
	}

	/**
	 * @return the cellPropertySet
	 */
	public String getCellPropertySet() {
		return cellPropertySet;
	}

	/**
	 * @param cellPropertySet the cellPropertySet to set
	 */
	public void setCellPropertySet(String cellPropertySet) {
		this.cellPropertySet = cellPropertySet;
	}
}
