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
 * This group is a specialised group that represents a table or
 * grid. It can be used when you want to wrap a member property
 * (typically) inside a table.
 * 
 * <p>A GridGroup is reasonably particular about it's contents.
 * Essentially a grid group is built around one or more member
 * properties - for each one of these, or each element inside
 * these in the case a member is a array, collection or map, it
 * will generate a row. However, any members before the member
 * property or after are also added to each and every row.</p>
 * 
 * @author Tom Spencer
 */
public class GridGroup extends SimpleGroup {
	
	private String[] headings = null;

	@Override
	public String getType() {
		return "grid-group";
	}
	
	@Override
	public String toString() {
		return "GridGroup: name=" + getName();
	}

	/**
	 * @return the headings
	 */
	public String[] getHeadings() {
		return headings;
	}

	/**
	 * @param headings the headings to set
	 */
	public void setHeadings(String[] headings) {
		this.headings = headings;
	}
}
