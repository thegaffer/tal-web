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

package org.talframework.talui.testapp.integrationtest;

import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.selenium.Selenium;

/**
 * This helper class contains methods that make writing Selenium
 * integration tests easier for web-template and web-mvc based
 * applications.
 * 
 * @author Tom Spencer
 */
public class SeleniumHelper {

	/**
	 * This method fills in the form with the parameters in the map.
	 * Each field entered is prefixed with the namespace and form name.
	 * 
	 * @param selenium The selenium object to power
	 * @param namespace The namespace for the form (or null)
	 * @param formName The formName
	 * @param values The values to set
	 */
	public static void fillInForm(Selenium selenium, String namespace, String formName, Map<String, String> values) {
		String prefix = formName + "-";
		if( namespace != null ) prefix = namespace + "-" + prefix;
		
		Iterator<String> it = values.keySet().iterator();
		while( it.hasNext() ) {
			String name = it.next();
			String val = values.get(name);
			
			if( val.startsWith("label=") ) {
				selenium.select(prefix + name, val);
			}
			else {
				selenium.type(prefix + name, val);
			}
		}
	}
}
