package org.tpspencer.tal.mvc.model;

import org.tpspencer.tal.mvc.Model;

/**
 * This interface represents a class that can produce the default
 * value of a model attribute.
 * 
 * @author Tom Spencer
 */
public interface DefaultModelResolver {

	/**
	 * @return The type of object that will be returned
	 */
	public Class<?> getType();
	
	/**
	 * Called when we must provide the default object. The
	 * model is provided in case the default value is 
	 * dependent on other model attributes. However, at 
	 * least initially there is no protection from a 
	 * recursive call here, so beware!
	 * 
	 * @param model The current model
	 * @return The default value
	 */
	public Object getDefault(Model model);
}
