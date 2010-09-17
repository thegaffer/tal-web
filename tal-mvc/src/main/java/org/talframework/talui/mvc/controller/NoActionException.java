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

package org.talframework.talui.mvc.controller;

/**
 * This exception indicates that we could not find a matching
 * action to perform. Typically this is caused by there being
 * no default action and no valid sub action can be found.
 * It is a configuration error.
 * 
 * @author Tom Spencer
 */
public class NoActionException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NoActionException(String message) {
		super(message);
	}
}
