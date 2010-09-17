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

package org.talframework.talui.mvc.commons.repository;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.model.ModelResolver;

/**
 * This class is a generic list resolver that obtains all 
 * objects from the repository it is connected to.
 * 
 * @author Tom Spencer
 */
public class RepositoryListResolver extends RepositoryHolder implements ModelResolver {
	
	/**
	 * Simply gets the objects from the repository
	 */
	public Object getModelAttribute(Model model, String name, Object param) {
		return getRepository().findAll(Object.class);
	}
	
	/**
	 * Cannot nest as this represents a remote call
	 */
	public boolean canNestResolver() {
		return false;
	}
}
