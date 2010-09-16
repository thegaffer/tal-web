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

public abstract class BaseChildCompiler implements ChildCompiler {

	/** The compilers to use for the children */
	private final EntityCompiler[] childCompilers;
	
	/**
	 * Constructs a new child compiler
	 * 
	 * @param compilers The compiler(s) to use
	 */
	public BaseChildCompiler(EntityCompiler[] compilers) {
		this.childCompilers = compilers;
	}
	
	/**
	 * Constructs a new child compiler
	 * 
	 * @param compiler The compiler to use
	 */
	public BaseChildCompiler(EntityCompiler compiler) {
		this.childCompilers = new EntityCompiler[]{compiler};
	}
	
	/**
	 * Helper to compile the given object against the correct
	 * child compiler.
	 * 
	 * @param compiler The compiler
	 * @param app The contents
	 * @param parent The parent element
	 * @param obj The object in question
	 * @param name The name of the object
	 */
	protected void compileObject(AppCompiler compiler, AppElement app, AppElement parent, Object obj, String name) {
		if( obj == null ) return;
		if( childCompilers == null ) throw new IllegalArgumentException("Attempt to compile children with no child compilers!");
		
		for( int i = 0 ; i < childCompilers.length ; i++ ) {
			if( childCompilers[i].getExpected() == null ||
					childCompilers[i].getExpected().isAssignableFrom(obj.getClass()) ) {
				childCompilers[i].compile(compiler, app, parent, obj, name);
				break;
			}
		}
	}
}
