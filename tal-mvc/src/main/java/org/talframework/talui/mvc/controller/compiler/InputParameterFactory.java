package org.tpspencer.tal.mvc.controller.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.ParameterBinding;
import org.tpspencer.tal.mvc.controller.annotations.Input;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This factory handles the input binding
 * 
 * @author Tom Spencer
 */
public final class InputParameterFactory extends BaseParameterFactory implements ParameterBindingFactory {
	
	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> parameter, Annotation[] annotations) {
		Input input = getAnnotation(annotations, Input.class);
		if( input == null ) return null;
		
		if( getValue(input.param()) != null ) {
			return new InputParameterBinding(getValue(input.param()), parameter.isArray());
		}
		else if( getValue(input.prefix()) != null ) {
			testExpected(method, parameter, Map.class);
			return new InputBinding(getValue(input.param()), true);
		}
		else {
			if( testAssignable(method , parameter, InputModel.class) ) return new InputBinding(getValue(input.param()), false); 
			else {
				testExpected(method, parameter, Map.class);
				return new InputBinding(getValue(input.param()), true);
			}
		}
	}

	/**
	 * Parameter binding simply gets the parameter from the
	 * input. Only processing is to return it as an array or
	 * as a single scalar value.
	 * 
	 * @author Tom Spencer
	 */
	public final static class InputParameterBinding implements ParameterBinding {
		
		private final String parameter;
		private final boolean asArray;
		
		public InputParameterBinding(String parameter, boolean asArray) {
			this.parameter = parameter;
			this.asArray = asArray;
		}
		
		public Object bind(Model model, InputModel input) {
			Object obj = input.getParameterObject(parameter);
			if( !asArray && obj instanceof String[] && ((String[])obj).length > 0 ) {
				obj = ((String[])obj)[0];
			}
			return obj;
		}
	}
	
	/**
	 * Parameter binding returns the input directly or will
	 * get a map of all parameters starting with a given prefix.
	 * 
	 * @author Tom Spencer
	 */
	public final static class InputBinding implements ParameterBinding {
		
		private final boolean asMap;
		private final String prefix;
		
		public InputBinding(String prefix, boolean asMap) {
			this.prefix = prefix;
			this.asMap = asMap;
		}
		
		public Object bind(Model model, InputModel input) {
			if( prefix == null ) {
				if( asMap ) return input.getParameters();
				else return input;
			}
			else {
				return input.getParameters(prefix);
			}
		}
	}
}
