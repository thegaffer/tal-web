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

/**
 * This simple class can be used as the base for any 
 * UI controller or service that requires a 
 * {@link SimpleRepository} for it's use. This class
 * holds the repository and the getter/setter for 
 * setup.
 * 
 * @author Tom Spencer
 */
public abstract class RepositoryHolder {

	/** Holds the repository to use */
	private SimpleRepository repository = null;
	
	/**
	 * @return The order repository
	 */
	public SimpleRepository getRepository() {
		return repository;
	}
	
	/**
	 * Sets the order repository
	 * 
	 * @param repository the repository to use
	 */
	public void setRepository(SimpleRepository repository) {
		this.repository = repository;
	}
	
}
