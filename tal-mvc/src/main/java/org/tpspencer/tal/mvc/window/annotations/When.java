package org.tpspencer.tal.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation describes what will happen when a certain action
 * is fired. This annotation is applied to the type in an 
 * {@link Actions} annotation (it must then contain either the
 * qualifying name of the handler or the controller class - the
 * appropriate DI mechanism can fill in the rest.
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface When {
	/** The action to operate on */
	public String action();
	/** The name of a handler/controller */
	public String handler() default "";
	/** Class of the controller */
	public Class<?> controller() default Object.class;
}
