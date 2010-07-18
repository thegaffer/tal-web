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

import java.util.Map;

public interface UrlGenerator extends org.tpspencer.tal.template.render.UrlGenerator {

	/**
	 * Call to generate any custom url referring to an app, page
	 * window and action (the latter two are optional).
	 * 
	 * <p>Note: <p>The signature of this method is geared towards
	 * the use of the Web MVC framework. It probably would be better
	 * to re-think this interface somewhat.</p>
	 *  
	 * @param type The type of request to generate
	 * @param app The app (or context root) to generate the url on (if null uses current if known)
	 * @param page The page to generate the url on (if null uses current if known)
	 * @param window The optional window or portion of the screen to generate url on (if any)
	 * @param action The action to generate (if any)
	 * @param params The parameters to pass
	 * @return
	 */
	public String generateCustomUrl(String type, String app, String page, String window, String action, Map<String, String> params);
}
