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

package org.tpspencer.tal.mvc.document;

/**
 * This interface represents something that can write
 * out all or part of the element it is passed. There
 * are many different types of writers defined.
 * 
 * @author Tom Spencer
 */
public interface ElementWriter {

	/**
	 * Call to write out the relevant portions of the element.
	 * 
	 * @param writer The writer to write into
	 * @param app The overall app element
	 * @param element The element to write out
	 */
	public void write(DocumentWriter writer, AppElement app, AppElement element);
}
