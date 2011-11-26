package org.talframework.talui.mvc.annotations.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All attributes (represented by the JavaBean accessor methods) on
 * a model interface are typically stored as simple attributes. However,
 * by adding this attribute some of the default behaviour can be
 * changed.
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ModelAttribute {
    /** If false then attribute is always stored in memory */
    public boolean simple() default true;
    /** If true then it is possible to generate events when this attribute changes */
    public boolean eventable() default false;
    /** If true then the value is not stored at all */
    public boolean flash() default false;
    /** If true then the value is cleared when we get a new action, but is stored in between */
    public boolean flashAction() default false;
    /** If true then the value is cleared after we have rendered it the first time */
    public boolean flashFirstRender() default false;
    /** If true the attribute is always added to the render model */
    public boolean autoRender() default false;
    /** Holds alternative names this attribute may be known as in higher model layers */
    public String[] aliases() default {};
    /** Indicates that we expect this attribute to be provided by higher model layer */
    public boolean expectAlias() default false;
}
