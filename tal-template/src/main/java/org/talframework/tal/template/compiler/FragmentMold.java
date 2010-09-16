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
 * This interface represents something that generates a part
 * of the render elements for any given model element. The
 * SimpleTemplateElementMold is configured with a set of these
 * fragments that together turn a template element into a set
 * of Render Elements. This is useful when the render 
 * instructions for different types of render elements are 
 * largely the same except for small differences. This is
 * certainly the case with HTML, but it can be used elsewhere.
 * 
 * @author Tom Spencer
 */
public interface FragmentMold {

	/**
	 * Called to determine if this mold fragment is interested in
	 * the template element we are compiling. Note that if this
	 * frament represents the 'wrapping' fragment inside a 
	 * HtmlTemplateElementMold then returning false effectively
	 * means this template element is not rendered.
	 * 
	 * @param compiler The current compiler
	 * @param template The template the element belongs to
	 * @param element The template element
	 * @return True if we are interested, false otherwise.
	 */
	public boolean isInterested(GenericCompiler compiler, Template template, TemplateElement element);
	
	/**
	 * Called if the fragment answers yes to the interested question.
	 * 
	 * @param compiler The compiler
	 * @param templateMold The template mold
	 * @param template The template the element is on
	 * @param element The element
	 * @return Any new render elements
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element);
}
