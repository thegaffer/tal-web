/*
 * Copyright 2010 Thomas Spencer
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

package org.tpspencer.tal.mvc.document.compiler;

import org.tpspencer.tal.mvc.document.AppElement;

/**
 * This interface represents a class that can compile children
 * of the current element. Various strategies are used to
 * determine the children.
 * 
 * @author Tom Spencer
 */
public interface ChildCompiler {
	
	/**
	 * Compiles the given children using the child compilers.
	 * 
	 * @param compiler The app compiler
	 * @param app The contents
	 * @param parent The parent element
	 */
	public void compile(AppCompiler compiler, AppElement app, AppElement parent);
}
