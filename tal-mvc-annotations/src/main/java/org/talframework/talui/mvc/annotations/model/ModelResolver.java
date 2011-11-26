package org.talframework.talui.mvc.annotations.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When applied to a getter method in an interface representing a model
 * this annotation indicates that the attribute of the model is not 
 * stored, but is resolved via the resolver given in the class. The
 * class mentioned must either implement the ModelResolver interface
 * or simply have have a method that returns the same type as the 
 * method this annotation is on
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelResolver {
    public Class<?> value();
}
