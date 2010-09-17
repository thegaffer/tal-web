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

package org.talframework.talui.mvc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.talframework.talui.mvc.process.ModelLayerAttributesResolver;

/**
 * This interface represents an object that can handle a
 * particular type of request. The requests are registered
 * with the main DispatchingServlet with a given request
 * name. When an incoming request is received its 
 * coordinates are worked out and then dispatched to the
 * appropriate MVCRequestHandler. All requests are expected
 * to support GET requests.
 * 
 * @author Tom Spencer
 */
public interface MVCRequestHandler {

	/**
	 * @return True if the request handler supports post operations, false otherwise
	 */
	public boolean canHandlePost();
	
	/**
	 * This method is called before the call to handle request as a sanity
	 * check that should be applied to make sure the request can be processed.
	 * Although problems could occur during the request this allows the 
	 * handler to get the basic validation, such as missing coordinates, out
	 * of the way before processing. This method should raise a runtime 
	 * exception as appropriate if it fails.
	 * 
	 * @param request The current request
	 * @param coords The coordinates
	 */
	public void validate(HttpServletRequest request, RequestCoordinates coords);
	
	/**
	 * This method is called after the validate method to actually perform
	 * the request. All aspects of the request are not the responsibility of
	 * the request handler including the response.
	 * 
	 * @param req The request object
	 * @param resp The response object
	 * @param resolver The model attribute resolver
	 * @param coords The coordinates
	 */
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp, ModelLayerAttributesResolver resolver, RequestCoordinates coords) throws ServletException, IOException;
}
