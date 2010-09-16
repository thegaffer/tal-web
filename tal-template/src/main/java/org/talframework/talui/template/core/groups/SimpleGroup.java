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

package org.tpspencer.tal.template.core.groups;

import java.util.List;

import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.behaviour.GroupElement;
import org.tpspencer.tal.template.behaviour.supporting.ContainerElement;
import org.tpspencer.tal.template.core.BaseElement;

/**
 * This class represents a single group of other elements
 * (be they properties or further groups). This is typically
 * the base class for any other more specilised groups.
 * 
 * @author Tom Spencer
 */
public class SimpleGroup extends BaseElement implements GroupElement, ContainerElement {

	/**
	 * Passes children on to base init
	 */
	public void init(Template template, List<TemplateElement> children) {
		super.init(children);
	}
	
	/**
	 * @return The type of the group
	 */
	public String getType() {
		return "group";
	}
	
	@Override
	public String toString() {
		return "SimpleGroup: name=" + getName();
	}
}
