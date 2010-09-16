package org.tpspencer.tal.mvc.controller.compiler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.ParameterBinding;
import org.tpspencer.tal.mvc.controller.annotations.ErrorInput;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.input.InputModel;

public final class ModelParameterFactory extends BaseParameterFactory implements ParameterBindingFactory {

	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> expected, Annotation[] annotations) {
		ModelInput annotation = getAnnotation(annotations, ModelInput.class);
		if( annotation != null ) {
			String attribute = getValue(annotation.value());
			if( attribute == null ) testAssignable(method, expected, Map.class);
		
			return new ModelBinding(attribute);
		}
		
		ErrorInput error = getAnnotation(annotations, ErrorInput.class);
		if( error != null ) {
			String errorAttribute = getErrorAttribute(controllerClass);
			return new ModelBinding(errorAttribute);
		}
		
		return null;
	}
	
	/**
	 * Inner class is the actual model binding class.
	 * 
	 * @author Tom Spencer
	 */
	public final static class ModelBinding implements ParameterBinding {
		
		/** Holds the attribute in model (if any) to bind to */
		private final String attribute;
		
		public ModelBinding(String attribute) {
			this.attribute = attribute;
		}
		
		public Object bind(Model model, InputModel input) {
			if( attribute != null ) return model.getAttribute(attribute);
			else return model;
		}
	}
}
