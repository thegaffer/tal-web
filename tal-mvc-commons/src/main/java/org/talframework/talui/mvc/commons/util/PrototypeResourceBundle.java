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

package org.talframework.talui.mvc.commons.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import org.talframework.tal.aspects.annotations.PrototypeObject;

/**
 * This class is resource bundle that provides default
 * values for requested resources. This is useful in
 * prototyping. The resource bundle returns back the
 * key, except when the key beings "label." in this
 * case it takes the last element (after the last .)
 * and turns it into "Title Case", i.e. label.titleCase
 * is returned as "Title Case".
 * 
 * @author Tom Spencer
 */
@PrototypeObject
public class PrototypeResourceBundle extends ResourceBundle {
	
	private Hashtable<String, String> requestedKeys = new Hashtable<String, String>();
	
	@Override
	public Enumeration<String> getKeys() {
		return requestedKeys.keys();
	}
	
	@Override
	protected Object handleGetObject(String key) {
		String ret = key;
		
		if( key.startsWith("label.") ) {
			String val = key;
			
			int index = key.lastIndexOf('.'); 
			if( index >= 0 ) val = key.substring(index + 1);
			
			StringBuilder buf = new StringBuilder();
			char[] c = val.toCharArray();
			for( int i = 0 ; i < c.length ; i++ ) {
				if( i == 0 ) buf.append(Character.toUpperCase(c[i]));
				else if( Character.isUpperCase(c[i]) ) buf.append(' ').append(c[i]);
				else buf.append(c[i]);
			}
			
			ret = buf.toString();
		}
		
		requestedKeys.put(key, ret);
		return ret;
	}
}
