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

package org.tpspencer.tal.template.render;

import java.util.Map;

/**
 * This interface represents a class that can generate
 * urls (or platform equivilent) to resources and to
 * actions. As the format of any url is very much specific
 * to the environment web-template is running in we have this
 * interface to abstract us from the details.
 * 
 * @author Tom Spencer
 */
public interface UrlGenerator {
	
	/**
	 * Call to generate a resource url. This method is
	 * shorthand for the full generateResourceUrl where
	 * there are no parameters to pass (which is common).
	 * 
	 * @param resource The base resource 
	 * @return The generated url
	 */
	public String generateResourceUrl(String resource);
	
	/**
	 * Call to generate a resource url with associated
	 * parameters.
	 * 
	 * @param resource The base resource
	 * @param params The parameters to pass 
	 * @return The generated url
	 */
	public String generateResourceUrl(String resource, Map<String, String> params);

	/**
	 * Call to generate an action url pointing back to current
	 * source with given action and parameters.
	 * 
	 * @param action The action to perform
	 * @param params The parameters
	 * @return The url
	 */
	public String generateActionUrl(String action, Map<String, String> params);
	
	/**
	 * Call to generate a template based URL. This is typically used to
	 * generate call backs. For instance when outputting HTML there is
	 * often a JS renderer and this is used to generate the URL back on
	 * the template servlet for that. This is required because often the
	 * URL Generator (which will be needed when generated the JS for 
	 * instance) will likely require additional parameters.
	 * 
	 * @param templateName
	 * @return
	 */
	public String generateTemplateUrl(String templateName, String renderType);
	
	/**
	 * Used to generate a url to a different page in the app. This method
	 * can generate a plan render url or it can optionally generate an
	 * action on that page to the given target - what this target is 
	 * depends on the outer container.
	 * 
	 * @param page The page to generate a url on
	 * @param target The target to generate action on (optional)
	 * @param action The action on the target
	 * @param params The parameters to pass in the request
	 * @return The url
	 */
	public String generatePageUrl(String page, String target, String action, Map<String, String> params);
}
