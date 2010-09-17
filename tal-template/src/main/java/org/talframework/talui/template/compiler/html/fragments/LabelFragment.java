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
import org.talframework.talui.template.render.elements.html.Label;

/**
 * This class produces a label against any dynamic property or
 * if the element has a "label" setting.
 * 
 * @author Tom Spencer
 */
public class LabelFragment implements FragmentMold {
	
	private String propertySet = "htmlLabel";

	/**
	 * Is interested if its a DynamicProperty
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		// TODO: This is a bit draconian. What about sub-rows with a label. Are we better just hiding labels by CSS?
		if( compiler.isStyle(HtmlElementConstants.STYLE_NO_LABEL) ) return false;
		else return element.getBehaviour(DynamicProperty.class) != null || element.hasSetting("label");
	}
	
	/**
	 * Generates out a label
	 */
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
		
		String forField = null;
		String label = null;
		if( element.getBehaviour(DynamicProperty.class) != null ) {
			label = "label." + template.getName() + "." + element.getName();
			forField = element.getName();
		}
		else {
			String lbl = element.getSetting("label", String.class);
			if( lbl == null ) label = "label." + template.getName() + "." + lbl;
		}
		
		if( label != null ) {
			Label ret = new Label(label);
			if( forField != null ) ret.setForField(forField);
			ret.setAttributes(element.getPropertySet(propertySet));
			return ret;
		}
		
		return null;
	}
}
