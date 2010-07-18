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

package org.tpspencer.tal.mvc.servlet;


/**
 * This exception is raised when the dispatching servlet is
 * unable to determine the coordinates for the current request.
 * Typically this will be because of a bad url
 * 
 * @author Tom Spencer
 */
public class InvalidCoordinatesException extends RuntimeException {
	private final static long serialVersionUID = 1L;

	private final String reason;
	private final String context;
	private final String url;
	
	public InvalidCoordinatesException(String context, String url, String reason) {
		this.context = context;
		this.url = url;
		this.reason = reason;
	}
	
	public InvalidCoordinatesException(String context, String url, Exception cause) {
		super(cause);
		this.context = context;
		this.url = url;
		this.reason = cause.getMessage();
	}
	
	@Override
	public String getMessage() {
		return "Invalid Coordinates in servlet [" + context + "] from url [" + url + "], reason: " + reason;
	}
}
