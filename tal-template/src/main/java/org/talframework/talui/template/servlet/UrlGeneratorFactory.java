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

package org.tpspencer.tal.template.servlet;

import javax.servlet.http.HttpServletRequest;

import org.tpspencer.tal.template.render.UrlGenerator;

/**
 * The WebTemplateServlet needs a mechanism to get hold of
 * the URL generator as it has no idea what environment it
 * is operating within. This is typically set as a config
 * parameter.
 * 
 * @author Tom Spencer
 */
public interface UrlGeneratorFactory {

	/**
	 * Called to get the URLGenerator for request
	 * 
	 * @param req The current request
	 * @return The URL generator
	 */
	public UrlGenerator getUrlGenerator(HttpServletRequest req);
}
