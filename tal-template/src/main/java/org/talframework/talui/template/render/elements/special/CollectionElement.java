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
import java.util.Iterator;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;

public final class CollectionElement extends BaseMemberElement {

	private boolean showIfNull = false;
	
	public CollectionElement(String name, RenderElement template) {
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
	 * Iterates around the collection and invokes render on
	 * the template.
	 */
	public void render(RenderModel model) throws IOException {
		Collection<? extends Object> coll = getCollection(model);
		String name = getName();
		
		if( coll != null && coll.size() > 0 ) {
			if( name != null ) model.pushNode(name, -1);
			int index = 0;
			Iterator<? extends Object> it = coll.iterator();
			while( it.hasNext() ) {
				it.next();
				model.pushNode(Integer.toString(index), index);
				renderTemplate(model);
				model.popNode();
				index++;
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
	 * Gets the collection as appropriate given settings and
	 * state of the model.
	 */
	@SuppressWarnings("unchecked")
	protected Collection<? extends Object> getCollection(RenderModel model) {
		Collection<? extends Object> ret = null;
		String name = getName();
		
		if( model.getCurrentNode() == null ) {
			if( name != null ) ret = (Collection<? extends Object>)model.getObject(name);
		}
		else {
			if( name == null ) ret = (Collection<? extends Object>)model.getCurrentNode().getObject();
			else ret = (Collection<? extends Object>)model.getCurrentNode().getProperty(name);
		}
		return ret;
	}
	
	/**
	 * Simply throws an exception
	 */
	public void addElement(RenderElement element) {
		throw new IllegalArgumentException("!!! Cannot add a child element manually to an CollectionElement must use constructor");
	}
}
