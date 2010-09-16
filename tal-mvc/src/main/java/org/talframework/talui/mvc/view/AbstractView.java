/*
 * Copyright 2009 Thomas Spencer
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

package org.tpspencer.tal.mvc.view;

import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelConfiguration;

/**
 * This class is a base for a custom view. The AbstractView
 * holds and serves up the model. It also provides an 
 * implementation of the enter/exit view, although they
 * do nothing. By deriving from this class you simply have
 * to implement the prepareRender method - either to generate
 * out the view there and then or to determine the correct
 * template to use.
 * 
 * @author Tom Spencer
 */
public abstract class AbstractView implements View {
	
	/** The model for the view (if any) */
	private ModelConfiguration model = null;
	/** An optional list of objects to clear when exiting */
	private List<String> tempObjects = null;
	
	/**
	 * Default constructor
	 */
	public AbstractView() {
		this.model = null;
	}
	
	/**
	 * Construct a view with a model.
	 * 
	 * @param model The model to construct
	 */
	public AbstractView(ModelConfiguration model) {
		this.model = model;
	}

	/**
	 * Simply returns the model if there is one
	 */
	public ModelConfiguration getModel() {
		return model;
	}
	
	/**
	 * @param model The model to use in this view
	 */
	public void setModel(ModelConfiguration model) {
		this.model = model;
	}
	
	/**
	 * Base version does nothing, but can be overidden
	 */
	public void enterView(Model model) {
	}
	
	/**
	 * Base version does nothing, but can be overidden
	 */
	public void exitView(Model model, Model viewModel, String result) {
		if( tempObjects != null ) {
			Iterator<String> it = tempObjects.iterator();
			while(it.hasNext()) {
				model.removeAttribute(it.next());
			}
		}
	}

	/**
	 * @return the tempObjects
	 */
	public List<String> getTempObjects() {
		return tempObjects;
	}

	/**
	 * @param tempObjects the tempObjects to set
	 */
	public void setTempObjects(List<String> tempObjects) {
		this.tempObjects = tempObjects;
	}
}
