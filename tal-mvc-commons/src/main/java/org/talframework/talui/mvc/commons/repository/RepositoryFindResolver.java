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
 * This class implements the ModelResolver to get a particular
 * object from its connected repository using another model
 * attribute as the ID of the entity.
 * 
 * @author Tom Spencer
 */
public class RepositoryFindResolver extends RepositoryHolder implements ModelResolver {
	
	/** Holds the name of the model attribute queried for the ID of the element to get */
	private String modelAttribute = null;

	/**
	 * @return The name of the model attribute
	 */
	public String getModelAtribute() {
		return modelAttribute;
	}
	
	/**
	 * @param attr The name of the model attribute to use
	 */
	public void setModelAttribute(String attr) {
		this.modelAttribute = attr;
	}
	
	/**
	 * Simply gets the objects from the repository
	 */
	public Object getModelAttribute(Model model, String name, Object param) {
		if( modelAttribute == null ) throw new IllegalArgumentException("You attempted to use the find resolver without a model attribute set");
		
		return getRepository().findById(model.getAttribute(modelAttribute), Object.class);
	}
	
	/**
	 * Cannot nest as this represents a potentially remote call
	 */
	public boolean canNestResolver() {
		return false;
	}
}
