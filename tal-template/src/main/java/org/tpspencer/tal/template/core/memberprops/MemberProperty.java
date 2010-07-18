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

package org.tpspencer.tal.template.core.memberprops;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.core.BaseElement;

public class MemberProperty extends BaseElement implements org.tpspencer.tal.template.behaviour.MemberProperty {
	
	/** Holds the name of the template to delegate rendering to */
	private String template = null;
	/** Holds the type of object this member prop refers to (if null treated as a plain object) */
	private Class<?> memberType = null;
	
	/**
	 * Passes children on to base init and sets up member
	 * type if its null and template has a class.
	 */
	public void init(Template template, List<TemplateElement> children) {
		super.init(children);
		
		// Auto set member type if we can
		if( memberType == null && template.getTemplateClass() != null ) {
			try {
				PropertyDescriptor prop = new PropertyDescriptor(getName(), template.getTemplateClass());
				memberType = prop.getPropertyType();
			}
			catch( Exception e ) {
				// Just ignore???
			}
		}
	}

	public String getType() {
		return "member-prop";
	}
	
	public boolean isTypeKnown() {
		return memberType != null;
	}
	
	public boolean isArray() {
		if( memberType == null ) return false;
		return memberType.isArray();
	}
	
	public boolean isCollection() {
		if( memberType == null ) return false;
		return Collection.class.isAssignableFrom(memberType);
	}
	
	public boolean isMap() {
		if( memberType == null ) return false;
		return Map.class.isAssignableFrom(memberType);
	}
	
	@Override
	public String toString() {
		return "MemberProp: name=" + getName() + ", template=" + template + ", memberType=" + memberType;
	}
	
	////////////////////////////////////
	// Getters / Setters
	
	/**
	 * @returns The template this property defers to
	 */
	public String getTemplate() {
		return template;
	}
	
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the memberType
	 */
	public Class<?> getMemberType() {
		return memberType;
	}

	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(Class<?> memberType) {
		this.memberType = memberType;
	}
}
