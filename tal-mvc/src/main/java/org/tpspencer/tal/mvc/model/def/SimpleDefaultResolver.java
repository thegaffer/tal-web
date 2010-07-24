package org.tpspencer.tal.mvc.model.def;

import java.lang.reflect.Proxy;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.controller.BindingException;
import org.tpspencer.tal.mvc.controller.InterfaceAdaptor;
import org.tpspencer.tal.mvc.model.DefaultModelResolver;

/**
 * This default resolver holds a type of the object to construct
 * and then constructs it when requested.
 * 
 * @author Tom Spencer
 */
public final class SimpleDefaultResolver implements DefaultModelResolver {
	
	/** The type of object to create */
	private Class<?> type;
	
	public SimpleDefaultResolver() {	
	}
	
	public SimpleDefaultResolver(Class<?> type) {
		this.type = type;
	}

	/**
	 * The embedded type
	 */
	public Class<?> getType() {
		return type;
	}
	
	/**
	 * @param type The new type to use
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	/**
	 * Constructs a new instance of the type
	 */
	public Object getDefault(Model model) {
		if( type == null ) return null;
		
		if( type.isInterface() ) {
			return Proxy.newProxyInstance(
					Thread.currentThread().getContextClassLoader(), 
					new Class[]{type}, 
					new InterfaceAdaptor());
		}
		else {
			try {
				return type.newInstance();
			}
			catch( RuntimeException e ) {
				throw e;
			}
			catch( Exception e ) {
				throw new BindingException("Cannot create default value: " + type, e);
			}
		}
	}
}
