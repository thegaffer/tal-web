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

package org.talframework.talui.mvc.servlet.error;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.tal.aspects.annotations.HttpTrace;

/**
 * This class is the simple exception resolver that turns an 
 * exception into a {@link ServletException}. It also uses the
 * http trace annotation to log out its usage and the request.
 *
 * @author Tom Spencer
 */
public class StandardExceptionResolver implements ServletExceptionResolver {
	
    @HttpTrace
	public void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) throws ServletException, IOException {
		if( e instanceof IOException ) {
			throw (IOException)e;
		}
		else if( e instanceof ServletException ) {
			throw (ServletException)e;
		}
		else {
			throw new ServletException(e);
		}
	}
}
