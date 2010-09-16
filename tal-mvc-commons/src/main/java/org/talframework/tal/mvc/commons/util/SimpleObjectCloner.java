package org.tpspencer.tal.mvc.commons.util;

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
