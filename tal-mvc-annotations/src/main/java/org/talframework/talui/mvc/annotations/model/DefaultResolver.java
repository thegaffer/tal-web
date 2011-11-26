package org.talframework.talui.mvc.annotations.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When applied to a getter method in an interface representing a model
 * this annotation the name of the class that can create a new instance
 * of this model attribute if it is null. The class must either be a
 * DefaultModelResolver or have a single method that returns the same
 * type as this method this annotation is based on.
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultResolver {
    public Class<?> value();
}
