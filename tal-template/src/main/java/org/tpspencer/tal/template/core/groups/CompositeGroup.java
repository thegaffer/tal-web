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

/**
 * This class represents a group that combines a 
 * number of fields into a more coherent single
 * composite property. Visually this is not 
 * different from another group other than the
 * containing fields tend not to have a label
 * themselves.
 * 
 * @author Tom Spencer
 */
public class CompositeGroup extends SimpleGroup {

	/** Holds the label for the composite group */
	private String label = null;

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String getType() {
		return "composite-group";
	}
	
	@Override
	public String toString() {
		return "CompositeGroup: name=" + getName() + ", label=" + getLabel();
	}
}
