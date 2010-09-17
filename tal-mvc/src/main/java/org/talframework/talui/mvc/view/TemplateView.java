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

package org.talframework.talui.mvc.view;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.render.RenderModel;

/**
 * This class implements a view that simply redirects
 * to a given template - the template can be anything.
 * 
 * @author Tom Spencer
 */
public class TemplateView implements View {
	
	/** The model for the view (if any) */
	private final ModelConfiguration model;
	/** The template for the view to use */
	private final String template;
	
	public TemplateView(String template) {
		if( template == null ) throw new IllegalArgumentException("No template provided to template view");
		this.template = template;
		this.model = null;
	}
	
	public TemplateView(String template, ModelConfiguration model) {
		if( template == null ) throw new IllegalArgumentException("No template provided to template view");
		this.template = template;
		this.model = model;
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Returns the model
	 */
	public ModelConfiguration getModel() {
		return model;
	}
	
	/**
	 * A no-op in this view
	 */
	public void enterView(Model model) {
	}
	
	/**
	 * A no-op in this view
	 */
	public void exitView(Model model, Model viewModel, String result) {
	}
	
	/**
	 * Returns the template.
	 * 
	 * @param renderModel The render model
	 * @param model The model
	 * @return The view templates
	 */
	public void prepareRender(RenderModel renderModel, Model model) {
		renderModel.setTemplate(template);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TemplateView [template=" + template + ", model=" + model.getName() + "]";
	}
}
