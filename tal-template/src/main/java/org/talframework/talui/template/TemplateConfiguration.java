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

package org.talframework.talui.template;

import java.util.Map;

/**
 * This interface represents template and its associated
 * compilers. The idea is you set up a template configuration
 * for each unique template and load it with the compilers
 * you need. This template is then held (by the app for all
 * users) and accessed to get a particular renderer. In 
 * addition to the compilers the configuration also holds
 * the resource bundle and the name of the default model
 * object (if any). The bundle and the default model object
 * mean nothing unless you get them and set them in the
 * render model yourself.
 * 
 * @author Tom Spencer
 */
public interface TemplateConfiguration {
	
	/**
	 * This represents the name of the template configuration.
	 * This should be very much like a Java Package and Class
	 * name, i.e. /org/tpspencer/tal/template/MyView. This 
	 * name is important for a number of reasons:
	 * 
	 * <ul>
	 * <li>It is used as the base name for the resource
	 * properties when rendering out.
	 * <li>It is used as the base name for any manual Javascript
	 * or CSS resources -i.e. /org/tpspencer/tal/template/MyVew.js
	 * is searched on the classpath.
	 * <li>It may be used to actually find the template config
	 * instance given its name.
	 * </ul>
	 * 
	 * <p>Main takeaway point is ensure it is unique!</p>
	 * 
	 * @return The full path and name of the template
	 */
	public String getName();
	
	/**
	 * The name of the resources bundle to use.
	 * 
	 * @return The resource bundle base name
	 */
	public String getResourceName();
	
	/**
	 * The main template is required by the compilers that produce
	 * model based renderers which start from a root template and
	 * in conjunction with a model determine what to display. The
	 * other type of renderer is one which just iterates all the
	 * templates. This type does not need the main template.
	 * 
	 * @return The root template for the config
	 */
	public Template getMainTemplate();
	
	/**
	 * @return All the templates, keyed by name, in the config
	 */
	public Map<String, Template> getTemplates();
	
	/**
	 * Call to get the renderer for the root template.
	 * 
	 * @param renderType The render method
	 * @return The appropriate renderer for the template
	 */
	public Renderer getRenderer(String renderType);
	
	/**
	 * Determines if the configuration has a particular renderer.
	 * Useful to match up clients expectations.
	 * 
	 * @param renderType Render type
	 * @return True if it is supported
	 */
	public boolean hasRenderer(String renderType);
}
