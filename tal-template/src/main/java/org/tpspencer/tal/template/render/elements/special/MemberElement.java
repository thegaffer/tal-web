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

/**
 * This special element will delegate processing to a
 * new template. It will optionally only do this is a
 * particular object exists
 * 
 * @author Tom Spencer
 */
public class MemberElement extends BaseMemberElement {

	/** Determines if we should show if null */
	private boolean showIfNull = false;
	
	public MemberElement(String name, RenderElement template) {
		super(name, template);
		if( name != null ) showIfNull = true; /* By default show a member even if null */
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
	 * Implements by simply call render on the embedded template.
	 */
	public void render(RenderModel model) throws IOException {
		Object obj = getObject(model);
		String name = getName();
		
		if( obj != null || showIfNull ) {
			if( name != null ) model.pushNode(name, -1);
			renderTemplate(model);
			if( name != null ) model.popNode();
		}
	}
	
	/**
	 * Returns the object to operate on based on name
	 * setting and state of model.
	 * 
	 * @param model
	 * @return
	 */
	protected Object getObject(RenderModel model) {
		Object ret = null;
		String name = getName();
		
		if( model.getCurrentNode() == null ) {
			if( name != null ) ret = model.getObject(name);
		}
		else {
			if( name == null ) ret = model.getCurrentNode().getObject();
			else ret = model.getCurrentNode().getProperty(name);
		}
		return ret;
	}

	/**
	 * Throws an exception as not clean!
	 */
	public void addElement(RenderElement element) {
		throw new IllegalArgumentException("!!! Cannot add an element to a member element, must be done in constructor");
	}
}
