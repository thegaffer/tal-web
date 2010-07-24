package org.tpspencer.tal.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation when used on a ModelAttr field declares an
 * event that should fire when this attribute changes.
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OnChange {
	/** The name of the attribute this event should fire on */
	public String attribute();
	/** The name of the parameter passed to action holding new value */
	public String newValueParam();
	/** The name of the parameter passed to action holding old value */
	public String oldValueParam() default "";
}
