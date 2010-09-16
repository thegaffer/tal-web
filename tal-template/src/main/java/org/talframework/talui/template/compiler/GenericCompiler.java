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

package org.tpspencer.tal.template.compiler;

import org.tpspencer.tal.template.Compiler;
import org.tpspencer.tal.template.RenderElement;

/**
 * This class is a generic render compiler. An instance of
 * this class is configured with a number of render templates.
 * These guys are registered against the model template, group
 * and property types and know how to wrap the model element.
 * The generic render compiler then steps through the input 
 * template and uses the render templates to produce the 
 * compiled renderer.
 * 
 * @author Tom Spencer
 */
public interface GenericCompiler extends Compiler {
	
	/**
	 * @return The array of set styles that cross into other templates
	 */
	public String[] getStyles();
	
	/**
	 * @return The template specific styles (these do not cross to other templates)
	 */
	public String[] getTemplateStyles();
	
	/**
	 * @return True if the given style is enabled
	 */
	public boolean isStyle(String style);
	
	/**
	 * Call to add a style to the compiler
	 * 
	 * @param style The style to add
	 * @return True if already existed, false otherwise
	 */
	public boolean addStyle(String style);
	
	/**
	 * Call to remove a style from the compiler
	 * 
	 * @param style The style to remove
	 */
	public void removeStyle(String style);
	
	/**
	 * Call to add a style to current template. If
	 * we move to another template these styles are
	 * reset inside that 2nd template.
	 * 
	 * @param style The style to add
	 * @return True if already existed, false otherwise
	 */
	public boolean addTemplateStyle(String style);
	
	/**
	 * Call to remove a style from current template.
	 * 
	 * @param style The style to remove
	 */
	public void removeTemplateStyle(String style);
	
	/**
	 * First this method finds the template (throwing if it does not
	 * exist). Then it sees if we have already rendered this template.
	 * If so returns it's name, otherwise starts compiling it.
	 */
	public RenderElement compileTemplate(String templateName, String[] styles, String[] templateStyles);
}
