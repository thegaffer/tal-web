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

package org.tpspencer.tal.template.render.codes;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;

/**
 * Implements {@link CodeTypeFactory} to hold a single map of
 * code types. This type of code should probably not be used
 * beyond prototyping.
 * 
 * @author Tom Spencer
 */
public final class SimpleCodeTypeFactory implements CodeTypeFactory {
	
	private final SimpleCodeType type;
	
	public SimpleCodeTypeFactory(SimpleCodeType type) {
		if( type == null ) throw new IllegalArgumentException("You must provide a pre-configured SimpleCodeType instance to the SimpleCodeType factory");
		this.type = type;
		
		CodeTypeFactoryLocator.getInstance().addCodeFactory(type.getType(), this);
	}

	/**
	 * Just returns the code type
	 */
	public CodeType getCodeType(Locale locale, Principal user, Map<String, String> params) {
		return type;
	}
	
	@Override
	public String toString() {
		return "SimpleCodeTypeFactory: type=" + type.getType();
	}
}
