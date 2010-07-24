package org.tpspencer.tal.mvc.controller.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.tpspencer.tal.mvc.controller.ParameterBinding;

/**
 * This interface represents a class that can create 
 * {@link ParameterBinding} instances of a given type. It
 * is used during construction to create the bindings.
 * 
 * @author Tom Spencer
 */
public interface ParameterBindingFactory {

	/**
	 * This method is called by the compiler when it has an parameter
	 * to bind into. The compiler has a range of these factories and 
	 * the first one to answer with a non-null return wins!
	 * 
	 * @param controller The controller
	 * @param controllerClass The class the method is on (may be a interface or subclass of controller)
	 * @param method The method we are finding bindings for
	 * @param parameter The expected type of the parameter
	 * @param annotations The annotation the parameter has
	 * @return
	 */
	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> parameter, Annotation[] annotations);
}
