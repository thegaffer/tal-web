package org.tpspencer.tal.mvc.commons.objex;

import org.talframework.objexj.Container;


/**
 * Use this interface to obtain Objex containers.
 * 
 * @author Tom Spencer
 */
public interface ContainerLocator {

	/**
	 * Opens a container. The container is found either
	 * because the locator has a reference to it directly, or because
	 * it can find a named property on the passed in obj. The
	 * object can be a model, a bean containing the relevant
	 * property or a map.
	 * 
	 * @param obj The optional reference
	 * @return The container
	 */
	public abstract Container getContainer(Object obj);

	/**
	 * Opens an editable container. The container is found either
	 * because the locator has a reference to it directly, or because
	 * it can find a named property on the passed in obj. The
	 * object can be a model, a bean containing the relevant
	 * property or a map.
	 * 
	 * @param obj The optional reference
	 * @return The editable container
	 */
	public abstract Container getOpenContainer(Object obj);

}