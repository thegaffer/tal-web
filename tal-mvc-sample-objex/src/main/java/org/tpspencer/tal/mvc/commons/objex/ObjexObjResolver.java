package org.tpspencer.tal.mvc.commons.objex;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexObj;

/**
 * Gets an object from a container. The container is either a 
 * static container or the ID of the container is obtained 
 * from a given model attribute.
 * 
 * @author Tom Spencer
 */
public final class ObjexObjResolver extends BaseObjexResolver {

	private String objectIdAttribute;
	private Class<?> expected;
	
	public Object getModelAttribute(Model model, String name, Object param) {
	    // a. Get the container
	    Container container = getContainer(model);
		
		// b. Get the object (or the root object)
		ObjexObj ret = null;
		if( container != null && objectIdAttribute == null ) ret = container.getRootObject();
		else if( container != null ) ret = container.getObject(model.getAttribute(objectIdAttribute));
		
		return (ret != null && expected != null) ? ret.getBehaviour(expected) : ret;
	}
	
	/**
	 * Represents a DB operation so should not next
	 */
	public boolean canNestResolver() {
		return false;
	}

	/**
	 * @return the objectIdAttribute
	 */
	public String getObjectIdAttribute() {
		return objectIdAttribute;
	}

	/**
	 * @param objectIdAttribute the objectIdAttribute to set
	 */
	public void setObjectIdAttribute(String objectIdAttribute) {
		this.objectIdAttribute = objectIdAttribute;
	}

	/**
	 * @return the expected
	 */
	public Class<?> getExpected() {
		return expected;
	}

	/**
	 * @param expected the expected to set
	 */
	public void setExpected(Class<?> expected) {
		this.expected = expected;
	}
}
