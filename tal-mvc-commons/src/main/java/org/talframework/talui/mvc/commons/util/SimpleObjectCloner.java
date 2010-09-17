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

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * This object cloner uses reflection to clone two nearly 
 * identical objects. It is very useful to copy from an
 * interface to an object that is known to implement that
 * interface.
 * 
 * @author Tom Spencer
 */
public class SimpleObjectCloner implements ObjectCloner {
	private static final SimpleObjectCloner INSTANCE = new SimpleObjectCloner();
	
	public static ObjectCloner getInstance() {
		return INSTANCE;
	}
	
	public void clone(Object src, Object dest, String... exclusions) {
		BeanWrapper destWrapper = new BeanWrapperImpl(dest);
		BeanWrapper srcWrapper = new BeanWrapperImpl(src);
		
		PropertyDescriptor[] descs = srcWrapper.getPropertyDescriptors();
		for(PropertyDescriptor desc : descs) {
			String name = desc.getName();
			if( exclusions != null ) {
				for( int i = 0 ; i < exclusions.length ; i++ ) {
					if( name.equals(exclusions[i]) ) {
						name = null;
						break;
					}
				}
			}
			
			if( name != null && srcWrapper.isWritableProperty(name) &&
					destWrapper.isWritableProperty(name) ) {
				destWrapper.setPropertyValue(name, srcWrapper.getPropertyValue(name));
			}
		}
	}
	
}
