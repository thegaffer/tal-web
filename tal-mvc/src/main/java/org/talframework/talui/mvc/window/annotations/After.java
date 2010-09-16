package org.tpspencer.tal.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation holds a custom mapping of a result to
 * a view. This is useful if there are lots of results
 * that go to a view - otherwise mappings can be added
 * to the view.
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface After {
	public String result();
	public String view();
}
