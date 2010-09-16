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

package org.tpspencer.tal.template.compiler.html.fragments;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.behaviour.DynamicProperty;
import org.tpspencer.tal.template.behaviour.supporting.ImageElement;
import org.tpspencer.tal.template.behaviour.supporting.ResourceProperty;
import org.tpspencer.tal.template.compiler.FragmentMold;
import org.tpspencer.tal.template.compiler.GenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateRenderMold;
import org.tpspencer.tal.template.render.elements.DynamicParameter;
import org.tpspencer.tal.template.render.elements.ResourceParameter;
import org.tpspencer.tal.template.render.elements.html.Div;
import org.tpspencer.tal.template.render.elements.html.Img;

public class ValueFragment implements FragmentMold {
	
	private String templateRole = "role-value";
	private String propertySet = "htmlValue";

	/**
	 * Returns true if its a dynamic element
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element) {
		return element.getBehaviour(DynamicProperty.class) != null ||
			element.getBehaviour(ResourceProperty.class) != null ||
			element.getBehaviour(ImageElement.class) != null;
	}

	/**
	 * Creates a span for the content using the "field" property set.
	 */
	public RenderElement compile(GenericCompiler compiler,
			TemplateRenderMold templateMold, Template template,
			TemplateElement element) {
	
		if( element.getBehaviour(DynamicProperty.class) != null ) {
			Div ret = new Div(element.getName(), new DynamicParameter(element, "getValue"));
			ret.setAsDiv(false);
			ret.addStyleClass(templateRole);
			ret.setAttributes(element.getPropertySet(propertySet));
			return ret;
		}
		else if( element.getBehaviour(ResourceProperty.class) != null ) {
			ResourceProperty prop = element.getBehaviour(ResourceProperty.class);
			Div ret = new Div(element.getName(), new ResourceParameter(prop.getResource()));
			ret.setAsDiv(false);
			ret.addStyleClass(templateRole);
			ret.setAttributes(element.getPropertySet(propertySet));
			return ret;
		}
		else if( element.getBehaviour(ImageElement.class) != null ) {
			Img ret = new Img(element.getName());
			ret.addStyleClass(templateRole);
			ret.setResource(new DynamicParameter(element, "getImageResource"));
			ret.setAttributes(element.getPropertySet(propertySet));
			return ret;
		}
		
		return null;
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
}
