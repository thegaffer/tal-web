package org.tpspencer.tal.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Holds any additional mappings of results to views
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mappings {
	public After[] value(); 
}
