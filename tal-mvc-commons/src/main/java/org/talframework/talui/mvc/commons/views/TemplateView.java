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

package org.talframework.talui.mvc.commons.views;

import java.util.Iterator;
import java.util.List;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.render.RenderModel;
import org.talframework.talui.mvc.view.AbstractView;
import org.talframework.talui.template.TemplateConfiguration;

/**
 * This class represents a template view - a view which delegates
 * to a Web Template.
 * 
 * @author Tom Spencer
 */
public class TemplateView extends AbstractView {

	/** The model objects to place inside the render model at render time */
	private List<String> modelObjects = null;
	/** The template configuration on which view is based */
	private TemplateConfiguration config = null;
	
	/**
	 * Simply adds any model objects to the view and
	 * sets template equals to the template name.
	 */
	public void prepareRender(RenderModel renderModel, Model model) {
		renderModel.setTemplate(config.getName());
		
		if( modelObjects != null ) {
			Iterator<String> it = modelObjects.iterator();
			while( it.hasNext() ) {
				String attr = it.next();
				renderModel.setAttribute(attr, model.getAttribute(attr));
			}
		}
	}

	/**
	 * @return the modelObjects
	 */
	public List<String> getModelObjects() {
		return modelObjects;
	}

	/**
	 * @param modelObjects the modelObjects to set
	 */
	public void setModelObjects(List<String> modelObjects) {
		this.modelObjects = modelObjects;
	}

	/**
	 * @return the config
	 */
	public TemplateConfiguration getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(TemplateConfiguration config) {
		this.config = config;
	}
}
