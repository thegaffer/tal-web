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

package org.talframework.talui.template.render.codes;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;


/**
 * This interface represents a something that can create/serve up
 * code type instances. A code type just provides access to all 
 * the possible codes a field can take on together with access to 
 * convert a code to a description.
 * 
 * @sa {@link CodeType}
 * 
 * @author Tom Spencer
 */
public interface CodeTypeFactory {
	
	/**
	 * Call to get the relevant code type. If parameters are
	 * supplied then the code type will likely be a new instance
	 * otherwise it may be a new one or a different instance
	 * each time.
	 * 
	 * @param locale The current locale to use for descriptions
	 * @param user The current user/principal (sometimes codes possible depend on security)
	 * @param params Any additional parameters code is dependent on.
	 * @return The code type
	 */
	public CodeType getCodeType(Locale locale, Principal user, Map<String, String> params);

}
