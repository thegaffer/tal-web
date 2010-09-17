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

package org.talframework.talui.template.compiler;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;

/**
 * This interface represents something that can generate
 * render elements given a model template element. This
 * will either be a property or a group of properties.
 * 
 * @author Tom Spencer
 */
public interface TemplateElementRenderMold {

	/**
	 * Called to generate a model element (a group or
	 * property) into a render element.
	 *  
	 * @param compiler The compiler (in case access is need to other templates)
	 * @param templateMold The render template we are in (any children are compiled through this)
	 * @param template The template the element is within
	 * @param element The element to compile
	 * @return The render element for that element
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element);
}
