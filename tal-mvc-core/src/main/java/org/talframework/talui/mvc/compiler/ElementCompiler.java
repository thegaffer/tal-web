package org.talframework.talui.mvc.compiler;

/**
 * This interface represents something that can compile
 * a class into a MVC element.
 *
 * @author Tom Spencer
 */
public interface ElementCompiler<Element, Context> {

    /**
     * This method is used to determine if this compiler can
     * compile the given class and context into the
     * appropriate MVC element.
     * 
     * @param cls The class to compile
     * @param context The context
     * @return True if it can be compiled
     */
    public boolean canCompile(Class<?> cls, Context context);
    
    /**
     * This method is used to actually compile the class having
     * already passed the canCompile method.
     * 
     * @param cls The class to compile
     * @param context The context
     * @return The compiled element
     */
    public Element compile(Class<?> cls, Context context);
}
