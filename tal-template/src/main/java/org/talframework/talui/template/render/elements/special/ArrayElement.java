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

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;

public final class ArrayElement extends BaseMemberElement {

	private boolean showIfNull = false;
	
	public ArrayElement(String name, RenderElement template) {
		super(name, template);
	}
	
	/**
	 * @return the showIfNull
	 */
	public boolean isShowIfNull() {
		return showIfNull;
	}

	/**
	 * @param showIfNull the showIfNull to set
	 */
	public void setShowIfNull(boolean showIfNull) {
		this.showIfNull = showIfNull;
	}

	/**
	 * Iterates around the array and invokes render on the
	 * embedded template.
	 */
	public void render(RenderModel model) throws IOException {
		Object[] arr = getArray(model);
		String name = getName();
		
		if( arr != null && arr.length > 0 ) {
			if( name != null ) model.pushNode(name, -1);
			for( int i = 0 ; i < arr.length ; i++ ) {
				model.pushNode(Integer.toString(i), i); 
				renderTemplate(model);
				model.popNode();
			}
			if( name != null ) model.popNode();
		}
		else if( showIfNull ){
			if( name != null ) model.pushNode(name, -1);
			model.pushNode(Integer.toString(0), 0); 
			renderTemplate(model);
			model.popNode();
			if( name != null ) model.popNode();
		}
	}
	
	/**
	 * Gets the array given settings and state of the current
	 * model.
	 * 
	 * @param model
	 * @return
	 */
	protected Object[] getArray(RenderModel model) {
		Object[] ret = null;
		String name = getName();
		if( model.getCurrentNode() == null ) {
			if( name != null ) ret = (Object[])model.getObject(name);
		}
		else {
			if( name == null ) ret = (Object[])model.getCurrentNode().getObject();
			else ret = (Object[])model.getCurrentNode().getProperty(name);
		}
		return ret;
	}

	/**
	 * Simply throws an exception
	 */
	public void addElement(RenderElement element) {
		throw new IllegalArgumentException("!!! Cannot add a child element manually to an ArrayElement must use constructor");
	}
}
