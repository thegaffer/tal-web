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
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.behaviour.property.CheckedProperty;
import org.talframework.talui.template.behaviour.property.CodedProperty;
import org.talframework.talui.template.behaviour.property.DateProperty;
import org.talframework.talui.template.behaviour.property.NumberProperty;
import org.talframework.talui.template.behaviour.property.TextProperty;
import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.render.elements.DynamicParameter;
import org.talframework.talui.template.render.elements.html.AbstractHtmlElement;
import org.talframework.talui.template.render.elements.html.ButtonInput;
import org.talframework.talui.template.render.elements.html.Input;
import org.talframework.talui.template.render.elements.html.Select;
import org.talframework.talui.template.render.elements.html.TextArea;
import org.talframework.talui.template.render.elements.special.EmptyElement;

/**
 * This fragment outputs a HTML input for the template
 * element. It handles most types of input by configuring
 * its type property as a constructor arg. If the default
 * constructor is used then a simple input element is
 * produced. 
 * 
 * <p>In the default class the handling of date, number,
 * & string inputs is as simple text properties. Derived
 * classes can override this class to provide extra 
 * support for these fields (such as Dojo attributes).</p>
 * 
 * @author Tom Spencer
 */
public class InputFragment implements FragmentMold {
	/** Holds the name of the property set to apply to field - TPS, not sure, would like splitting of date/time to be here */
	private String propertySet = "htmlField";
	/** Holds the name of the template role for the field - TPS, not sure, would like splitting of date/time to be here */
	private String templateRole = "field";
	
	/**
	 * Default constructor
	 */
	public InputFragment() {
	}
	
	/**
	 * Is interested if the element is a dynamic property
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.getBehaviour(DynamicProperty.class) != null || element.getBehaviour(CommandElement.class) != null;
	}
	
	/**
	 * Outputs an input control specific to the actual element
	 */
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
		
		DynamicProperty prop = element.getBehaviour(DynamicProperty.class);
		
		RenderElement ret = null;
		
		// If hidden, then its always hidden
		if( prop != null && prop.isHidden() ) {
			ret = asHiddenInput(compiler, template, prop);
		}
		
		// If text prop, draw as string input or memo
		if( prop != null && element.getBehaviour(TextProperty.class) != null ) {
			TextProperty text = element.getBehaviour(TextProperty.class);
			if( text.isMemo() ) ret = asMemoInput(compiler, template, text);
			else ret = asStringInput(compiler, template, text);
		}
		
		// If number prop, output as number
		else if( prop != null && element.getBehaviour(NumberProperty.class) != null ) {
			NumberProperty num = element.getBehaviour(NumberProperty.class);
			ret = asNumberInput(compiler, template, num);
		}
		
		// If date prop, output as date
		else if( prop != null && element.getBehaviour(DateProperty.class) != null ) {
			DateProperty dt = element.getBehaviour(DateProperty.class);
			ret = asDateInput(compiler, template, dt);
		}
		
		// If coded prop, output as behaviour
		else if( prop != null && element.getBehaviour(CodedProperty.class) != null ) {
			CodedProperty code = element.getBehaviour(CodedProperty.class);
			ret = asChoiceInput(compiler, template, code);
		}
		
		// If checked prop, output as checked
		else if( prop != null && element.getBehaviour(CheckedProperty.class) != null ) {
			CheckedProperty check = element.getBehaviour(CheckedProperty.class);
			ret = asBoolInput(compiler, template, check);
		}
		
		// If command element (doesn't have to be a prop), output as button
		else if( element.getBehaviour(CommandElement.class) != null ) {
			CommandElement cmd = element.getBehaviour(CommandElement.class);
			ret = asButtonInput(compiler, template, cmd);
		}
		
		// If a dynamic prop, output as input
		else if( prop != null ){
			ret = asInput(compiler, template, prop);
		}
		
		return ret;
	}
	
	/**
	 * Treats the property as a simple text input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asInput(GenericCompiler compiler, Template template, DynamicProperty prop) {
		String type = "text";
		Input ret = new Input(type, prop);
		ret.setAttributes(prop.getPropertySet(propertySet));
		ret.addStyleClass("role-" + templateRole);
		return ret;
	}
	
	/**
	 * Treats the property as a string input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asStringInput(GenericCompiler compiler, Template template, DynamicProperty prop) {
		AbstractHtmlElement ret = asInput(compiler, template, prop);
		return ret;
	}
	
	/**
	 * Treats the property as a multiline text input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	
	protected AbstractHtmlElement asMemoInput(GenericCompiler compiler, Template template, DynamicProperty prop) {
		TextArea ret = new TextArea(prop);
		ret.setAttributes(prop.getPropertySet(propertySet));
		ret.addStyleClass("role-" + templateRole);
		return ret;
	}
	
	/**
	 * Treats the property as a date input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asDateInput(GenericCompiler compiler, Template template, DynamicProperty prop) {
		AbstractHtmlElement ret = asInput(compiler, template, prop);
		return ret;
	}
	
	/**
	 * Treats the property as a number input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asNumberInput(GenericCompiler compiler, Template template, DynamicProperty prop) {
		AbstractHtmlElement ret = asInput(compiler, template, prop);
		return ret;
	}
	
	/**
	 * Treats the property as a checkbox input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asBoolInput(GenericCompiler compiler, Template template, CheckedProperty prop) {
		String val = prop.getCheckedValue();
		if( val == null ) val = "true";
		
		ButtonInput ret = new ButtonInput(val, prop);
		ret.setAttributes(prop.getPropertySet(propertySet));
		ret.addStyleClass("role-" + templateRole);
		return ret;
	}
	
	/**
	 * Treats the property as a select input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The render html element.
	 */
	protected RenderElement asChoiceInput(GenericCompiler compiler, Template template, CodedProperty prop) {
		/* ID is the value, just put out the text prop */ 
		if( prop.isUnbounded() ) {
			return asInput(compiler, template, prop);
		}
		
		/* Put out ID in normal box, but also value in hidden elem */
		else if( prop.isDynamic() ) {
			EmptyElement ret = new EmptyElement();
			ret.addElement(new Input("hidden", prop.getName() + "_visible", new DynamicParameter(prop, "getValue")));
			
			// The code value (because dynamic must be entered as code)
			Input inp = new Input("text", prop.getName(), new DynamicParameter(prop, "getCodeValue"));
			inp.setAttributes(prop.getPropertySet(propertySet));
			inp.addStyleClass("role-" + templateRole);
			ret.addElement(inp);
			
			return ret;
		}
		
		/* Otherwise is just a standard value */
		else {
			Select ret = new Select(prop);
			ret.setAttributes(prop.getPropertySet(propertySet));
			ret.addStyleClass("role-" + templateRole);
			return ret;
		}
	}
	
	/**
	 * Treats the property as a button input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asButtonInput(GenericCompiler compiler, Template template, TemplateElement prop) {
		String buttonType = null;
		if( prop.hasSetting("buttonType") ) {
			buttonType = prop.getSetting("buttonType", String.class);
		}
		
		// Set default type to button
		if( buttonType == null ) {
			if( "reset".equals(prop.getName()) ) buttonType = "reset";
			else buttonType = "submit";
		}
		
		String label = "label." + template.getName() + "." + prop.getName();
		
		ButtonInput ret = buttonType != null ? new ButtonInput(buttonType, prop.getName()) : new ButtonInput(prop.getName());
		ret.setLabel(label);
		ret.setAttributes(prop.getPropertySet(propertySet));
		ret.addStyleClass("role-" + templateRole);
		return ret;
	}
	
	/**
	 * Treats the property as a hidden input control.
	 * Derived classes can override to add in extra attributes
	 * or replace altogether.
	 * 
	 * @param template The template the property is in
	 * @param prop The property
	 * @return The abstract html element.
	 */
	protected AbstractHtmlElement asHiddenInput(GenericCompiler compiler, Template template, DynamicProperty prop) {
		String type = "hidden";
		Input ret = new Input(type, prop);
		ret.setAttributes(prop.getPropertySet(propertySet));
		ret.addStyleClass("role-" + templateRole);
		return ret;
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
