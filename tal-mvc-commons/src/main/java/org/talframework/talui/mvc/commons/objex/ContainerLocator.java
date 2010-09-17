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