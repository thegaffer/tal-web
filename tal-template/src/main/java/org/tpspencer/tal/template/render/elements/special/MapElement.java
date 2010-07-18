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
import java.util.Iterator;
import java.util.Map;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;

public final class MapElement extends BaseMemberElement {

	private String keyIfNull = null;
	
	public MapElement(String name, RenderElement template) {
		super(name, template);
	}
	
	/**
	 * @return the keyIfNull
	 */
	public String getKeyIfNull() {
		return keyIfNull;
	}

	/**
	 * @param keyIfNull the keyIfNull to set
	 */
	public void setKeyIfNull(String keyIfNull) {
		this.keyIfNull = keyIfNull;
	}

	/**
	 * iterates the map and calls the internal render on it.
	 */
	public void render(RenderModel model) throws IOException {
		Map<?, ?> map = getMap(model);
		String name = getName();
		
		if( map != null && map.size() > 0 ) {
			if( name != null ) model.pushNode(name, -1);
			Iterator<? extends Object> it = map.keySet().iterator();
			int index = 0;
			while( it.hasNext() ) {
				Object key = it.next();
				model.pushNode(key.toString(), index); 
				renderTemplate(model);
				model.popNode();
				index++;
			}
			if( name != null ) model.popNode();
		}
		else if( keyIfNull != null ){
			if( name != null ) model.pushNode(name, -1);
			model.pushNode(keyIfNull, 0); 
			renderTemplate(model);
			model.popNode();
			if( name != null ) model.popNode();
		}
	}
	
	/**
	 * Returns the map given the current model.
	 * 
	 * @param model The model
	 * @return The map to feed into template
	 */
	protected Map<?, ?> getMap(RenderModel model) {
		Map<?, ?> ret = null;
		String name = getName();
		
		if( model.getCurrentNode() == null ) {
			if( name != null ) ret = (Map<?, ?>)model.getObject(name);
		}
		else {
			if( name == null ) ret = (Map<?, ?>)model.getCurrentNode().getObject();
			else ret = (Map<?, ?>)model.getCurrentNode().getProperty(name);
		}
		return ret;
	}
	
	/**
	 * Simply throws an exception
	 */
	public void addElement(RenderElement element) {
		throw new IllegalArgumentException("!!! Cannot add a child element manually to an MapElement must use constructor");
	}
}
