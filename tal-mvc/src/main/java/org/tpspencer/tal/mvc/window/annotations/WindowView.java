package org.tpspencer.tal.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface WindowView {
	public String name();
	public String viewName() default "";
	public Class<?> view() default Object.class;
	public String[] results() default {};
	public boolean defView() default false;
}
