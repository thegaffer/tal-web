/*
 * Copyright 2010 Thomas Spencer
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

package org.tpspencer.tal.mvc.document.compiler;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * This specialised compiler does not process beans of
 * particular root classes. 
 * 
 * @author Tom Spencer
 */
public class BeanEntityCompiler extends EntityCompiler {
	
	public BeanEntityCompiler(String type, boolean root, Class<?> expected) {
		super(type, root, expected);
	}

	@Override
	protected boolean checkCompile(Object obj) {
		Class<?> type = obj instanceof Class<?> ? (Class<?>)obj : obj.getClass();
		
		if( type == null ) return false;
		if( type.isPrimitive() ) return false;
		if( Number.class.isAssignableFrom(type) ) return false;
		if( String.class.isAssignableFrom(type) ) return false;
		if( Date.class.isAssignableFrom(type) ) return false;
		if( Collection.class.isAssignableFrom(type) ) return false;
		if( Map.class.isAssignableFrom(type) ) return false;
		if( Class.class.isAssignableFrom(type) ) return false;
		if( type.equals(Object.class) ) return false;
		
		return super.checkCompile(obj);
	}
}
