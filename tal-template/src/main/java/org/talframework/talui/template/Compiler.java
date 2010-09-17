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


/**
 * This interface represents an object that can compile
 * from a template into a renderable compiled form. The
 * idea is the multiple compilers can take the same
 * basic templates and produce different output. The
 * compiler should do as much as possible during 
 * compilation to ensure the actual rendering is as fast
 * as possible.
 * 
 * @author Tom Spencer
 */
public interface Compiler {
	
	/**
	 * Initial call made to actually compile a template. It returns back
	 * the root render element for the template (which in turn contains 
	 * all the other compiled elements).
	 * 
	 * @param config The configuration the compiler is ultimately compiling
	 * @param rootTemplate The initial template to produce renderer for (if model renderer)
	 * @param templates The other templates in the model
	 * @return The map of template to render elements found
	 */
	public Renderer compile(TemplateConfiguration config);
}
