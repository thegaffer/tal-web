package org.talframework.talui.mvc.annotations.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation when applied to an interface marks it as
 * being a simple select controller. This controller will
 * take a value from the input and set it on the model under
 * a specific name.
 * 
 * <p>This is a very common type of controller when the
 * user is selecting something in a list. The ID of the
 * element gets selected and then events are fired on
 * other windows - or perhaps even page - reflecting
 * that change.</p>
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SelectController {
    /** The name of the input parameter to 'select' */
    public String input();
    /** The name of the model attribute to set the input on */
    public String model();
    /** The result to return if input was present and set on model */
    public String success() default "ok";
    /** The result to return if input was not present */
    public String failure() default "fail";
}
