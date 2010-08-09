package org.tpspencer.tal.mvc.commons.objex;

import org.tpspencer.tal.mvc.Model;

/**
 * This resolver will resolve a container given either an
 * ID of the container or a model attribute that holds the
 * container. This class can be used as-is, or more likely
 * this is the base class 
 * 
 * @author Tom Spencer
 */
public final class ContainerResolver extends BaseObjexResolver {

	public Object getModelAttribute(Model model, String name, Object param) {
		return getContainer(model);
	}
	
	/**
	 * Getting the container is consider cheap
	 */
	public boolean canNestResolver() {
		return true;
	}
}
