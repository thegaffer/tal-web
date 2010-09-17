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

import java.util.HashMap;
import java.util.Map;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.DynamicProperty;
import org.talframework.talui.template.behaviour.property.CodedProperty;
import org.talframework.talui.template.behaviour.property.DateProperty;
import org.talframework.talui.template.behaviour.property.NumberProperty;
import org.talframework.talui.template.behaviour.property.TextProperty;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.render.elements.DynamicParameter;
import org.talframework.talui.template.render.elements.ExpressionParameter;
import org.talframework.talui.template.render.elements.RenderParameter;
import org.talframework.talui.template.render.elements.js.DynamicFieldAttachment;
import org.talframework.talui.template.render.elements.special.EmptyElement;

/**
 * This mold is the default for any dynamic property. It will
 * as approriate create render elements that at render time
 * create Javascript to attach dynamic effects to the field
 * based on its type. This mold is attached to elements inside
 * a form. See {@link JsElementMold} for the standard elements
 * added to a field outside of a form.
 * 
 * @author Tom Spencer
 */
public class DynamicFieldElementMold extends BaseJsElementMold {
	
	private static final String[] stdEventAttrs = { "onBlur", "onFocus", "onClick", "onDblClick", "onMouseUp", "onMouseDown", "onMouseMove", "onMouseOut", "onMouseOver", "onKeyDown", "onKeyUp", "onKeyPress" };
	private static final String[] defAttrs = { "intermediateChanges", "onChange" };
	private static final String[] textAttrs = { "trim", "uppercase", "lowercase", "propercase", "maxLength" };
	private static final String[] valAttrs = { "trim", "uppercase", "lowercase", "propercase", "promptMessage", "constraints", "regExpGen" };
	private static final String[] dateAttrs = { "strict" };
	private static final String[] timeAttrs = { "strict", "clickableIncrement", "visibleIncrement", "visibleRange" };
	private static final String[] numberAttrs = { "pattern" };
	private static final String[] comboAttrs = { "pageSize", "autoComplete", "searchDelay", "minChars", "showId" };
	private static final String[] filterAttrs = { "pageSize", "autoComplete", "searchDelay", "minChars", "showId" };
	private static final String[] memoAttrs = { "cols", "rows" };
	private static final String[] checkboxAttrs = new String[0];
	private static final String[] buttonAttrs = { "label", "showLabel", "iconClass" };

	/**
	 * This will attach a Dynamic JS RenderElement to dynamic property 
	 * at the field level. If there are no 'fields' then we end.
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		RenderElement ret = null; // We don't do children as only attached to props
		
		// Attach fields
		if( element instanceof DynamicProperty ) {
			if( ret == null ) ret = new EmptyElement();
			
			if( element.getType().endsWith("date-prop") ) {
				ret.addElement(addDateField(compiler, template, element));
			}
			else if( element.getType().endsWith("number-prop") ) {
				ret.addElement(addNumberField(compiler, template, element));
			}
			else if( element.getType().endsWith("choice-prop") ) {
				ret.addElement(addChoiceField(compiler, template, element));
			}
			else if( element.getType().endsWith("bool-prop") ) {
				ret.addElement(addBoolField(compiler, template, element));
			}
			else if( element.getType().endsWith("command-prop") ) {
				ret.addElement(addCommandField(compiler, template, element));
			}
			else if( element.getType().endsWith("memo-prop") ) {
				ret.addElement(addMemoField(compiler, template, element));
			}
			else if( element.getType().endsWith("text-prop") ) {
				ret.addElement(addValidationField(compiler, template, element));
			}
			else if( element.getType().equals("prop") ) {
				ret.addElement(addTextField(compiler, template, element));
			}
		}
		
		return ret;
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addTextField(GenericCompiler compiler, Template template, TemplateElement element) {
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = new DynamicFieldAttachment(wrapperType, "role-field", "input");
		
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		getExtraAttributes(attrs, element, textAttrs, "field");
		ret.setAttributes(attrs);
		
		return ret;
	}
	
	/**
	 * Helper to add a validation widget set of render elements
	 */
	private RenderElement addValidationField(GenericCompiler compiler, Template template, TemplateElement element) {
		TextProperty prop = element instanceof TextProperty ? (TextProperty)element : null;
		
		// If not a text prop, std text box
		if( prop == null ) return addTextField(compiler, template, element);
		
		// If memo, treat as such
		if( prop.isMemo() ) {
			return addMemoField(compiler, template, element);
		}
		
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = new DynamicFieldAttachment(wrapperType, "role-field", "text");
		
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		addAttribute(attrs, "maxLength", new DynamicParameter(prop, "getMaxLength"));
		addAttribute(attrs, "required", new DynamicParameter(prop, "isMandatory"));
		addAttribute(attrs, "regExp", new DynamicParameter(prop, "getValidationExpression"));
		getExtraAttributes(attrs, element, valAttrs, "field");
		ret.setAttributes(attrs);
		
		return ret;
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addMemoField(GenericCompiler compiler, Template template, TemplateElement element) {
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = new DynamicFieldAttachment(wrapperType, "role-field", "memo");
		
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		if( element instanceof TextProperty ) {
			addAttribute(attrs, "maxLength", new DynamicParameter(element, "getMaxLength"));
		}
		getExtraAttributes(attrs, element, memoAttrs, "field");
		ret.setAttributes(attrs);
		
		return ret;
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addDateField(GenericCompiler compiler, Template template, TemplateElement element) {
		DateProperty prop = element instanceof DateProperty ? (DateProperty)element : null;
		
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment dt = null;
		DynamicFieldAttachment tm = null;
		
		if( prop == null || prop.isDate() ) {
			dt = new DynamicFieldAttachment(wrapperType, "role-field", "date");
			Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
			if( prop != null ) addAttribute(attrs, "formatLength", new DynamicParameter(element, "getDateFormat"));
			if( prop != null ) addAttribute(attrs, "datePattern", new DynamicParameter(element, "getDatePattern"));
			
			getExtraAttributes(attrs, element, dateAttrs, "field");
			dt.setAttributes(attrs);
		}
		
		if( prop != null && prop.isTime() ) {
			tm = new DynamicFieldAttachment(wrapperType, "role-timeField", "time");
			Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
			addAttribute(attrs, "formatLength", new DynamicParameter(element, "getTimeFormat"));
			addAttribute(attrs, "timePattern", new DynamicParameter(element, "getTimePattern"));
 
			getExtraAttributes(attrs, element, timeAttrs, "timeField");
			tm.setAttributes(attrs);
		}
		
		// Return one or both in a empty element
		if( tm == null ) return dt;
		else if( dt == null ) return tm;
		else {
			EmptyElement ret = new EmptyElement();
			ret.addElement(dt);
			ret.addElement(tm);
			return ret;
		}
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addNumberField(GenericCompiler compiler, Template template, TemplateElement element) {
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = new DynamicFieldAttachment(wrapperType, "role-field", "number");
		
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		if( element instanceof NumberProperty ) {
			addAttribute(attrs, "min", new DynamicParameter(element, "getMinimum"));
			addAttribute(attrs, "max", new DynamicParameter(element, "getMinimum"));
			addAttribute(attrs, "places", new DynamicParameter(element, "getDecimalPlaces"));
			
			// TODO: If percentage, then set type
			// TODO: If currency, then set currency
			// TODO: Spinner!?!
		}
		getExtraAttributes(attrs, element, numberAttrs, "field");
		ret.setAttributes(attrs);
		
		return ret;
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addChoiceField(GenericCompiler compiler, Template template, TemplateElement element) {
		CodedProperty prop = element instanceof CodedProperty ? (CodedProperty)element : null;
		
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = null;
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		
		if( prop.isUnbounded() ) {
			ret = new DynamicFieldAttachment(wrapperType, "role-field", "combo");
			addAttribute(attrs, "searchUrl", new DynamicParameter(prop, "getSearchUrl"));
			getExtraAttributes(attrs, element, comboAttrs, "field");
			ret.setAttributes(attrs);
		}
		else if( prop.isDynamic() ) {
			ret = new DynamicFieldAttachment(wrapperType, "role-field", "select");
			addAttribute(attrs, "searchUrl", new DynamicParameter(prop, "getSearchUrl"));
			getExtraAttributes(attrs, element, filterAttrs, "field");
			ret.setAttributes(attrs);
		}
		else {
			ret = new DynamicFieldAttachment(wrapperType, "role-field", "select");
			getExtraAttributes(attrs, element, filterAttrs, "field");
			ret.setAttributes(attrs);
		}
		
		return ret;
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addBoolField(GenericCompiler compiler, Template template, TemplateElement element) {
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = new DynamicFieldAttachment(wrapperType, "role-field", "checkbox");
		
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		// Add in standard attrs if they exist
		getExtraAttributes(attrs, element, checkboxAttrs, "field");
		ret.setAttributes(attrs);
		
		return ret;
	}
	
	/**
	 * Helper to add a text widget set of render elements
	 */
	private RenderElement addCommandField(GenericCompiler compiler, Template template, TemplateElement element) {
		String wrapperType = template.getName() + "-" + element.getName();
		DynamicFieldAttachment ret = new DynamicFieldAttachment(wrapperType, "role-field", "button");
		
		Map<String, RenderParameter> attrs = new HashMap<String, RenderParameter>();
		
		// Add in standard attrs if they exist
		getExtraAttributes(attrs, element, buttonAttrs, "field");
		ret.setAttributes(attrs);
		
		return ret;
	}
	
	/**
	 * Simple helper to only add an attribute if it is non-null
	 */
	private void addAttribute(Map<String, RenderParameter> attrs, String attr, RenderParameter param) {
		if( param == null ) return;
		attrs.put(attr, param);
	}
	
	/**
	 * Helper to add on all extra non-core attributes against
	 * the widgets.
	 */
	private void getExtraAttributes(Map<String, RenderParameter> attrs, TemplateElement element, String[] allowedAttributes, String propertySet) {
		Map<String, String> props = element.getPropertySet(propertySet);
		
		for( int j = 0 ; j < 3 ; j++ ) {
			String[] attrsToTest = null;
			if( j == 0 ) attrsToTest = stdEventAttrs;
			if( j == 1 ) attrsToTest = defAttrs;
			if( j == 2 ) attrsToTest = allowedAttributes;
			
			for( int i = 0 ; i < attrsToTest.length ; i++ ) {
				String name = attrsToTest[i];
				Object v = null;
				
				// See if setting or a property
				if( element.hasSetting(name) ) {
					v = element.getSetting(name, Object.class);
				}
				else if( props != null && props.containsKey(name) ) {
					v = props.get(name);
				}
				
				// Handlers need parameter
				if( v != null ) {
					if( j == 0 ) {
						name = name.toLowerCase();
						v = v.toString() + "(this);";
					}
					attrs.put(name, ExpressionParameter.createSimpleOrExpression(v.toString()));
				}
			}
		}
	}
}
