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

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateElement;


/**
 * This interface represents something that can "mold"
 * an incoming template into a set of render elements
 * and likewise for the elements found within the 
 * template.
 * 
 * @author Tom Spencer
 */
public interface TemplateRenderMold {

	/**
	 * Called for the template to compile the given element
	 * from a template into zero or more render elements and 
	 * add them to the base element. 
	 *  
	 * @param compiler The compiler
	 * @param template The template to compile
	 * @return The root render element for the whole template
	 */
	public RenderElement compile(GenericCompiler compiler, Template template);
	
	/**
	 * Called typically by a element render template that was
	 * originally asked to compile from this render template.
	 * The render template should select and appropriate
	 * element render template to use based upon itself, the
	 * type of element (the child), and any group the child
	 * is in.
	 * 
	 * @param compiler The compiler
	 * @param template The template the child element belongs to
	 * @param child The child element
	 * @return The render element
	 */
	public RenderElement compileChild(GenericCompiler compiler, Template template, TemplateElement child);
}
