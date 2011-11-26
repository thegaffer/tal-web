package org.talframework.talui.mvc.annotations.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation when applied to an interface marks it as
 * being a simple controller. This controller will actually
 * do nothing other than return the annotations value as
 * a result.
 * 
 * <p>This is a very common type of controller when the
 * user is simply navigating where there is no information
 * to pass except that the user has selected something.</p>
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SimpleController {
    /** The result to return */
    public String value();
}
