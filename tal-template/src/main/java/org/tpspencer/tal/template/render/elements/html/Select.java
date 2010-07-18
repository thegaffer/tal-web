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

package org.tpspencer.tal.template.render.elements.html;

import java.io.IOException;
import java.io.Writer;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.behaviour.property.CodedProperty;
import org.tpspencer.tal.template.render.codes.CodeType;
import org.tpspencer.tal.template.render.codes.CodeTypeFactoryLocator;
import org.tpspencer.tal.template.render.elements.html.attributes.NameAttribute;
import org.tpspencer.tal.util.htmlhelper.GenericElement;
import org.tpspencer.tal.util.htmlhelper.HtmlConstants;

/**
 * This class generates out a HTML select field
 * 
 * @author Tom Spencer
 */
public class Select extends AbstractHtmlElement {

	private final CodedProperty prop;

	public Select(CodedProperty prop) {
		super(HtmlConstants.ELEM_SELECT, prop.getName());
		this.prop = prop;
		
		addAttribute(new NameAttribute(HtmlConstants.ATTR_NAME, prop.getName()));
	}
	
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		boolean ret = super.preRender(model);
		
		if( ret ) {
			// Add on the options
			addOptions(model, prop.getCodeValue(model));
		}
		
		return ret;
	}
	
	@Override
	protected void postRender(RenderModel model) throws IOException {
		super.postRender(model);
	}
	
	/**
	 * Adds on the value as a custom DOM attribute
	 */
	@Override
	protected void addAttributes(RenderModel model, GenericElement elem) {
		Object val = prop.getCodeValue(model);
		if( val != null ) elem.addAttribute(HtmlConstants.ATTR_VALUE, val.toString(), false);
		
		super.addAttributes(model, elem);
	}
	
	/**
	 * Helper to add the options. First this method gets the drop-down
	 * value and then renders out the options using other helpers 
	 * depending what the drop-down is.
	 * 
	 * @param model The render model
	 * @param val The (code) value of the property
	 * @throws IOException
	 */
	private void addOptions(RenderModel model, Object val) throws IOException {
		Writer writer = model.getWriter();
		GenericElement elem = model.getGenericElement();
		
		// Blank / Select
		// All
		
		// Add on options
		CodeType lookup = findCodeType(model);
		String[] codes = lookup.getCodes();
		if( codes != null && codes.length > 0 ) {
			for( int i = 0 ; i < codes.length ; i++ ) {
				addOption(writer, elem, codes[i], lookup.getCodeDescription(codes[i]), val);
			}
		}
	}
	
	/**
	 * Helper to add an individual option statement
	 */
	private void addOption(Writer writer, GenericElement elem, String key, String desc, Object currentVal) throws IOException {
		elem.reset(HtmlConstants.ELEM_OPTION);
		elem.addAttribute(HtmlConstants.ATTR_VALUE, key, false);
		if( key.equals(currentVal) ) elem.addAttribute(HtmlConstants.ATTR_SELECTED);
		elem.write(writer, false);
		if( desc != null ) writer.append(desc);
		else writer.append(key);
		elem.writeTerminate(writer);
	}
	
	/**
	 * Helper to get the code type for this property. First
	 * the model is checked for a resource bundle, map or
	 * CodeType instance in the properties type name. If that
	 * fails we turn to the singleton code type factory lookup
	 * and get the code from there.
	 * 
	 * @param model The model
	 * @return The code type
	 */
	private CodeType findCodeType(RenderModel model) {
		return CodeTypeFactoryLocator.getCodeType(prop.getCodeType(model), model, null);
	}
}
