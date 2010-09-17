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
 * This interface represents a simple repository of an
 * arbitrary set of objects. 
 * 
 * <p>This interface has been created with prototyping
 * in mind and there is the {@link PrototypeRepository} 
 * implementation. However, it is a common in many web
 * apps that are orientated towards the maintenance of
 * entities that using the idea of a repository in the
 * UI element is often useful. If so then this interface
 * can be used initially and you can implement it with
 * a version that accesses your objects from the business
 * tier in time.</p>
 * 
 * <p>One word of caution this is that you should be
 * accessing multiple repositories from within your
 * UI controllers. If you do you are limited your 
 * location choices for the business tier in the future
 * because mutliple remote calls with very likely be a
 * constraining feature. Instead you should write small
 * service classes to perform this work and call these
 * from your controllers. Then in the future you can
 * re-locate these services and ensure each controller
 * only makes 1 call to the repository.</p>
 * 
 * @author Tom Spencer
 */
public interface SimpleRepository {

	/**
	 * Call to find the instance of the obejct with the
	 * given ID.
	 * 
	 * @param id The ID we require
	 * @return The object (or null if not found)
	 */
	public <T> T findById(Object id, Class<T> expected);
	
	/**
	 * Call to get all instances held in the repository
	 * 
	 * <p>Although useful in prototyping use of this
	 * method for real is inherently going to be a problem.
	 * It is better to use a more refined search via a
	 * service that uses this method to search through
	 * the return from this method to provide only those
	 * needed.</p>
	 * 
	 * @return All instances as an array (or null if there are none).
	 */
	public <T> T[] findAll(Class<T> expected);
	
	/**
	 * Call to update an existing object in the repository.
	 * 
	 * @param obj The version of the object to update
	 * @return The object back, which may have been udpated
	 * @throws IllegalArgumentException The object has no ID or is not in this repository
	 */
	public <T> T update(T obj);
	
	/**
	 * Call to create an object in the repository. The 
	 * supplied input will be augmented by the creation of
	 * a unique ID (unique to the repository).
	 * 
	 * @param expected The object to add to the repository
	 * @return The object with any augmentation
	 * @throws IllegalArgumentException The object has an ID or is already in the repository
	 */
	public <T> T create(Class<T> expected);
}
