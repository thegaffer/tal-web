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
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.compiler.html.HtmlElementConstants;
import org.talframework.talui.template.render.elements.html.Cell;
import org.talframework.talui.template.render.elements.html.Div;

/**
 * This class acts a fragment that produces a wrapping render
 * element around a group or property. It, by default, treats
 * top-level children as a div and subsequent as a span. This
 * can be changed.
 * 
 * @author Tom Spencer
 */
public class WrapperFragment implements FragmentMold {
	public static final short AS_CELL = 1;
	public static final short AS_DIV = 2;
	public static final short AS_SPAN = 3;
	
	private String templateRole = "wrapper";
	private String propertySet = "htmlWrapper";
	private short topLevel = AS_DIV;
	private short secondLevel = AS_SPAN;
	
	public WrapperFragment() {
	}
	
	/**
	 * This constructor allows the caller to set the type
	 * of render element for a top level element or a 
	 * second level element manually.
	 * 
	 * @param topLevel The style if the element is not in another element
	 * @param secondLevel The style if the element is within another element
	 */
	public WrapperFragment(short topLevel, short secondLevel) {
		this.topLevel = topLevel;
		this.secondLevel = secondLevel;
	}

	/**
	 * Simply returns true. You could override this method if
	 * you want to be more conditional
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return true;
	}
	
	/**
	 * Outputs either a cell, a div or span based on the settings.
	 * If this is set as a cell then it will always
	 * 
	 * Outputs a div or span based on whether the element is within
	 * a group or not and its internal configuration. Default is
	 * div if not in a group and a span otherwise. Passes on the 
	 * property set called "wrapper" by default. Finally sets the
	 * class of the element to match the type on the template element. 
	 */
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
		
		boolean top = true;
		if( compiler.isStyle(HtmlElementConstants.STYLE_IN_ELEMENT) ) top = false;
		
		short type = top ? topLevel : secondLevel;
		if( top && compiler.isStyle(HtmlElementConstants.STYLE_IN_TABLE_ROW) ) type = AS_CELL;
		
		String suffix = "-grp";
		if( element.getBehaviour(DynamicProperty.class) != null ) suffix = "-fld";
		
		RenderElement ret = null;
		switch(type) {
		case AS_DIV:
			Div dv = new Div(element.getName() + suffix);
			dv.setAsDiv(true);
			dv.addStyleClass(element.getType());
			dv.addStyleClass("role-" + templateRole);
			dv.addStyleClass(template.getName() + "-" + element.getName());
			dv.setAttributes(element.getPropertySet(propertySet));
			if( element.getBehaviour(DynamicProperty.class) != null ) dv.setErrorField(element.getName());
			dv.setNewLineAfterTerminate(true);
			ret = dv;
			break;
			
		case AS_SPAN:
			dv = new Div(element.getName() + suffix);
			dv.setAsDiv(false);
			dv.addStyleClass(element.getType());
			dv.addStyleClass("role-" + templateRole);
			dv.addStyleClass(template.getName() + "-" + element.getName());
			dv.setAttributes(element.getPropertySet(propertySet));
			if( element.getBehaviour(DynamicProperty.class) != null ) dv.setErrorField(element.getName());
			dv.setNewLineAfterTerminate(true);
			ret = dv;
			break;
			
		case AS_CELL:
			Cell cl = new Cell(element.getName() + suffix);
			cl.addStyleClass(element.getType());
			// cl.setTemplateRole(templateRole);
			cl.addStyleClass("role-" + templateRole);
			// cl.setTemplateType(template.getName() + "." + element.getName());
			cl.addStyleClass(template.getName() + "-" + element.getName());
			cl.setAttributes(element.getPropertySet(propertySet));
			cl.setNewLineAfterTerminate(true);
			ret = cl;
			break;
		}
		
		return ret;
	}
	
	////////////////////////////////////
	// Getters / Setters

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
	 * @return the topLevel
	 */
	public short getTopLevel() {
		return topLevel;
	}

	/**
	 * @param topLevel the topLevel to set
	 */
	public void setTopLevel(short topLevel) {
		this.topLevel = topLevel;
	}

	/**
	 * @return the secondLevel
	 */
	public short getSecondLevel() {
		return secondLevel;
	}

	/**
	 * @param secondLevel the secondLevel to set
	 */
	public void setSecondLevel(short secondLevel) {
		this.secondLevel = secondLevel;
	}

	/**
	 * @return the templateRole
	 */
	public String getTemplateRole() {
		return templateRole;
	}

	/**
	 * @param templateRole the templateRole to set
	 */
	public void setTemplateRole(String templateRole) {
		this.templateRole = templateRole;
	}
}
