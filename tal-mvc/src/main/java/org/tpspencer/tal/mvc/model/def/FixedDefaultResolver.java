package org.tpspencer.tal.mvc.model.def;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.model.DefaultModelResolver;

/**
 * This simple class holds a single value that is considered
 * the default value to use.
 * 
 * @author Tom Spencer
 */
public final class FixedDefaultResolver implements DefaultModelResolver {
	
	/** Holds the fixed default value */
	public final Object value;
	
	public FixedDefaultResolver(Object value) {
		if( value == null ) throw new IllegalArgumentException("The default value is null");
		this.value = value;
	}

	/**
	 * Returns the values type
	 */
	public Class<?> getType() {
		return value.getClass();
	}
	
	/**
	 * Simply returns value
	 */
	public Object getDefault(Model model) {
		return value;
	}
}
