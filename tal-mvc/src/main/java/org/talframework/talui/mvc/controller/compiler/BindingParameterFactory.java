package org.tpspencer.tal.mvc.controller.compiler;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.BindingException;
import org.tpspencer.tal.mvc.controller.InputBinder;
import org.tpspencer.tal.mvc.controller.InterfaceAdaptor;
import org.tpspencer.tal.mvc.controller.ParameterBinding;
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ModelBindInput;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This factory constructs binders the bind from the input
 * into an object via the use of an Input Binder.
 * 
 * @author Tom Spencer
 */
public final class BindingParameterFactory extends BaseParameterFactory implements ParameterBindingFactory {
	
	public ParameterBinding getBinding(Object controller, Class<?> controllerClass, Method method, Class<?> parameter, Annotation[] annotations) {
		String errorAttribute = getErrorAttribute(controllerClass);
		
		ModelBindInput modelBind = getAnnotation(annotations, ModelBindInput.class);
		if( modelBind != null ) {
			String attribute = getValue(modelBind.modelAttribute());
			InputBinder binder = getBinder(controller, controllerClass, modelBind.binderName());
			String prefix = getValue(modelBind.prefix());
			String saveAttribute = getValue(modelBind.saveAttribute());
			
			return new ModelBinding(attribute, binder, prefix, saveAttribute, errorAttribute);
		}
		
		BindInput pureBind = getAnnotation(annotations, BindInput.class);
		if( pureBind != null ) {
			Class<?> type = pureBind.type();
			if( type.equals(Object.class) ) type = parameter;
			InputBinder binder = getBinder(controller, controllerClass, pureBind.binderName());
			String prefix = getValue(pureBind.prefix());
			String saveAttribute = getValue(pureBind.saveAttribute());
			
			return new SimpleBinding(type, binder, prefix, saveAttribute, errorAttribute);
		}
		
		return null;
	}
	
	/**
	 * Helper to get the InputBinder. This may be specified as an
	 * arg or it may be the default one on the controller. In all
	 * cases the input binder contains the name of a property on
	 * the controller class that holds the binder.
	 * 
	 * <p>The binder is obtained through reflection, which is a bit
	 * slow, but should not be a problem as this happens only at
	 * startup.</p>
	 */
	private InputBinder getBinder(Object controller, Class<?> controllerClass, String binder) {
		if( binder == null || binder.length() == 0 ) {
			Controller annotation = controllerClass.getAnnotation(Controller.class);
			binder = getValue(annotation.binder());
		}
		
		if( binder == null ) {
			throw new IllegalArgumentException("Cannot provide binding parameter because there is no binder set against the parameter or the controller");
		}
		
		try {
			PropertyDescriptor prop = new PropertyDescriptor(binder, controller.getClass());
			Object obj = prop.getReadMethod().invoke(controller, (Object[])null);
			return (InputBinder)obj;
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Unable to get the binder [" + binder + "] from the controller: " + controller, e);
		}
	}

	/**
	 * Base binding class that will bind the input into the object
	 * via the use of an InputBinder. Derived classes must define
	 * what the 'object' is.
	 * 
	 * @author Tom Spencer
	 */
	public abstract static class Binding implements ParameterBinding {
		
		private final InputBinder binder;
		private final String prefix;
		private final String saveAttribute;
		private final String errorAttribute;
		
		public Binding(InputBinder binder, String prefix, String saveAttribute, String errorArrtribute) {
			this.binder = binder;
			this.prefix = prefix;
			this.saveAttribute = saveAttribute;
			this.errorAttribute = errorArrtribute;
		}
		
		public Object bind(Model model, InputModel input) {
			Object obj = getBindObject(model, input);
			
			// Bind
			List<Object> errors = binder.bind(model, input, prefix, obj);
			if( errors != null && errors.size() > 0 ) {
				model.setAttribute(errorAttribute, errors);
			}
			
			if( saveAttribute != null ) model.setAttribute(saveAttribute, obj);
			
			return obj;
		}
		
		protected abstract Object getBindObject(Model model, InputModel input);
	}
	
	/**
	 * A simple binder binds input into an arbitraty object that is 
	 * created. 
	 * 
	 * @author Tom Spencer
	 */
	public final static class SimpleBinding extends Binding {
		
		private final Class<?> type;
		
		public SimpleBinding(Class<?> type, InputBinder binder, String prefix, String saveAttribute, String errorAttribute) {
			super(binder, prefix, saveAttribute, errorAttribute);
			this.type = type;
		}
		
		@Override
		protected Object getBindObject(Model model, InputModel input) {
			Object obj = null;
			
			if( type.isInterface() ) {
				obj = Proxy.newProxyInstance(
						Thread.currentThread().getContextClassLoader(), 
						new Class[]{type}, 
						new InterfaceAdaptor());
			}
			else {
				try {
					obj = type.newInstance();
				}
				catch( RuntimeException e ) {
					throw e;
				}
				catch( Exception e ) {
					throw new BindingException("Cannot create bind object: " + type, e);
				}
			}
			
			return obj;
		}
	}
	
	/**
	 * This more useful model binder binds into an object is first
	 * obtains from the model. This leaves the model responsible
	 * for the type of object being bound into.
	 * 
	 * @author Tom Spencer
	 */
	public final static class ModelBinding extends Binding {
		
		private final String modelAttribute;
		
		public ModelBinding(String modelAttribute, InputBinder binder, String prefix, String saveAttribute, String errorAttribute) {
			super(binder, prefix, saveAttribute, errorAttribute);
			this.modelAttribute = modelAttribute;
		}
		
		@Override
		protected Object getBindObject(Model model, InputModel input) {
			return model.getAttribute(modelAttribute);
		}
	}
}
