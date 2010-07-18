package org.tpspencer.tal.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base attribute for all model attributes
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ModelAttr {
	/** The name of the attribute */
	public String name() default "";
	/** The expected type (default is string if simple, otherwise plain object) */
	public Class<?> type() default Object.class;
	/** The resolver, without which the attributre is a simple attribute */
	public Class<?> resolver() default Object.class;
	/** Determines if attribute is only ever temporary within request */
	public boolean flash() default false;
	/** Determines if attributes can have events raised on it */
	public boolean evantable() default false;
	/** Determines if attribute should always be in render model for all views */
	public boolean autoRender() default false;
	/** Determines if attribute should be reset when the window gets an action */
	public boolean clearOnAction() default false;
	/** Determines if attribute should be cleared when we render */
	public boolean clearOnRender() default false;
	/** Determines if we expect the attribute to be alised to a page/app attr */
	public boolean aliasExpected() default false;
	/** Determines if the attribute can be aliased (default is true) */
	public boolean aliasable() default true;
}
