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

package org.tpspencer.tal.mvc.servlet.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This interface represents something that can handle
 * exceptions that are caught when processing MVC requests.
 * There is a default implementation of this interface that
 * redirects to an error page, but custom solutions could
 * be created to support more elaborate requirements.
 * 
 * @author Tom Spencer
 */
public interface ServletExceptionResolver {

	/**
	 * Called when an exception occurs.
	 * 
	 * @param request The request object
	 * @param response The response object
	 * @param e The exception that has occured
	 * @throws ServletException Optionally the resolver can throw a servlet exception to get std container handling
	 * @throws IOException Optionally the resolver can throw an IOException to get std container handling 
	 */
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException;
}
