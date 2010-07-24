package org.tpspencer.tal.mvc.controller.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.tpspencer.tal.mvc.controller.annotations.Controller;

/**
 * This base class provides common functionality for all factories
 * 
 * @author Tom Spencer
 */
public class BaseParameterFactory {
	
	/**
	 * Helper to get the name of the errors attribute. This is
	 * stored in the Controller annotation.
	 */
	protected String getErrorAttribute(Class<?> controller) {
		Controller annotation = controller.getAnnotation(Controller.class);
		return annotation.errorAttribute();
	}
	
	/**
	 * Helper to test a parameter can be assigned from at least one
	 * of the given classes.
	 * 
	 * @param method The method (used to report error)
	 * @param parameter Type of the parameter binding to
	 * @param assignableFrom The base type we expect it to be assignable to
	 */
	public void testExpected(Method method, Class<?> parameter, Class<?>... assignableFrom) {
		boolean ret = false;
		for( int i = 0 ; i < assignableFrom.length ; i++ ) {
			ret = testAssignable(method, parameter, assignableFrom[i]);
			if( ret ) break;
		}
		
		if( !ret ) {
			throw new IllegalArgumentException("Cannot bind to parameter on method. Expected [" + parameter + "] to be bindable to [" + assignableFrom[0] + "]: " + method);
		}
	}
	
	/**
	 * Helper to determine if the parameter (of a method) is 
	 * compatible with another class.
	 * 
	 * @param method The method (used to report error)
	 * @param parameter Type of the parameter binding to
	 * @param assignableFrom The base type we expect it to be assignable to
	 */
	public boolean testAssignable(Method method, Class<?> parameter, Class<?> assignableFrom) {
		return assignableFrom.isAssignableFrom(parameter);
	}
	
	/**
	 * Helper to get a string value from an annotation making
	 * it null if empty
	 * 
	 * @param value The raw value
	 * @return The value
	 */
	public String getValue(String value) {
		String ret = value;
		if( ret != null && ret.length() == 0 ) ret = null;
		return ret;
	}

	/**
	 * Call to find an annotation of the given type from a list
	 * of annotations.
	 * 
	 * @param annotations The list of annotations
	 * @param expected The expected type
	 * @return The annotation if found
	 */
	protected <T> T getAnnotation(Annotation[] annotations, Class<T> expected) {
		if( annotations == null ) return null;
		
		T ret = null;
		for( int i = 0 ; i < annotations.length ; i++ ) {
			if( annotations[i].annotationType().equals(expected) ) {
				ret = expected.cast(annotations[i]);
				break;
			}
		}
		return ret;
	}
}
