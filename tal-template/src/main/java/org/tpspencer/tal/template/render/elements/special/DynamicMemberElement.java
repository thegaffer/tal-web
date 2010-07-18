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

package org.tpspencer.tal.template.render.elements.special;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;

/**
 * This special render element determines what type of
 * object the target is at runtime and delegates to the
 * appropriate member render element (map, collection, array
 * or simple object). If it's null nothing is output.
 * 
 * @author Tom Spencer
 */
public final class DynamicMemberElement extends MemberElement {

	private final ArrayElement arrayElement;
	private final CollectionElement collElement;
	private final MapElement mapElement;
	
	public DynamicMemberElement(String prop, RenderElement template) {
		super(prop, template);
		this.arrayElement = new ArrayElement(prop, template);
		this.collElement = new CollectionElement(prop, template);
		this.mapElement = new MapElement(prop, template);
	}
	
	/**
	 * Determines the type of object and delegates as
	 * appropriate to base or member render elements.
	 */
	@SuppressWarnings("unchecked")
	public void render(RenderModel model) throws IOException {
		Object obj = getObject(model);
		
		// Surpress show if null because it is unsafe!
		if( obj == null ) return;
		
		if( obj instanceof Map ) mapElement.render(model);
		else if( obj instanceof Collection ) collElement.render(model);
		else if( obj.getClass().isArray() ) arrayElement.render(model);
		else super.render(model);
	}
}
