/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.talui.mvc.commons.objex;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.talui.mvc.Model;

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
