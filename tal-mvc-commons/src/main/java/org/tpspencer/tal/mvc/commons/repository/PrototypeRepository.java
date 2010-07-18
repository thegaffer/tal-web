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

package org.tpspencer.tal.mvc.commons.repository;

import java.beans.PropertyDescriptor;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.mvc.commons.util.ObjectCreator;

/**
 * The PrototyeRepository implements the {@link SimpleRepository}
 * interface for use in prototyping. A single instance of this
 * repository should be created and injected in to the relevant
 * UI controllers and services at runtime. Specific features
 * of the PrototyeRepository are ...
 * 
 * <ul>
 * <li>Configurable ID attribute that must be take an {@link Object} 
 * or a {@link String} instance</li>
 * <li>ID's are generated as a numeric value that increments each
 * time an object is added.</li>
 * <li>Optional ability to load an initial CSV file for the input
 * where row 1 of the CSV file is the order of the columns. The
 * file is read from the classpath.</li>
 * <li>Extra command to save the current state into a CSV file.</li>
 * </ul>
 * 
 * <p>The repository defaults the name of the ID attribute to 'ID'.
 * The repository does require the expected type to create instances
 * if using the CSV file - it cannot use the template parameter
 * because if dynamically created (say by Spring) we have an issue.</p>
 * 
 * FUTURE: Create a derived class that loads/saves the repository from a CSV file
 * FUTURE: Create a derived class that loads/saves the repository via Castor from XML
 * 
 * @author Tom Spencer
 *
 * @param <T> The type of object to work with
 */
public class PrototypeRepository implements SimpleRepository {
	private final static Log logger = LogFactory.getLog(PrototypeRepository.class);
	
	/** Holds the name of the getId method */
	private final String idAttribute;
	
	/** Holds the last (or highest) ID found */
	private long lastId = 0;
	/** Holds the instances keyed by ID */
	private Map<Object, Object> objects = null;
	
	/**
	 * Default constructor. ID attribute will be 'id'
	 */
	public PrototypeRepository() {
		logger.warn("*** Prototype Repository in use - ensure this is replaced in Production Environment");
		
		idAttribute = "id";
	}
	
	/**
	 * Constructs a repository using the given id as then
	 * name of the ID attribute of all objects added.
	 * 
	 * @param id The name of the ID attribute of all members
	 */
	public PrototypeRepository(String id) {
		logger.warn("*** Prototype Repository in use - ensure this is replaced in Production Environment");
		
		if( id == null || id.length() == 0 ) throw new IllegalArgumentException("You cannot supply a null ID attribute for the prototype repository");
		idAttribute = id;
	}

	/**
	 * Simple finds the Object by its ID in the map
	 */
	public <T> T findById(Object id, Class<T> expected) {
		if( id == null ) return null;
		if( objects == null ) return null;
		Object ret = objects.get(id);
		return expected.cast(ret);
	}
	
	/**
	 * Converts the values in the map to an array 
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] findAll(Class<T> expected) {
		if( objects == null ) return null;
		else return (T[])objects.values().toArray();
	}
	
	/**
	 * Assigns a new ID to the object and then adds 
	 * it to the map. The map is created if required
	 * as a Hashtable because there could be multiple
	 * threads.
	 */
	public synchronized <T> T create(Class<T> expected) {
		++lastId;
		
		// Create the new object instance
		T obj = ObjectCreator.createObject(expected);
		
		// Set the ID
		Object id = null;
		BeanWrapper wrapper = new BeanWrapperImpl(obj);
		PropertyDescriptor desc = wrapper.getPropertyDescriptor(idAttribute);
		
		if( String.class.isAssignableFrom(desc.getPropertyType()) ||
				desc.getPropertyType().equals(Object.class) ) id = Long.toString(lastId);
		else id = new Long(lastId);
		
		wrapper.setPropertyValue(idAttribute, id);
		
		// Add object to list
		if( objects == null ) objects = new Hashtable<Object, Object>();
		objects.put(id, obj);
		return obj;
	};
	
	/**
	 * Finds the existing object and then updates it
	 * in the repository.
	 */
	public <T> T update(T obj) {
		BeanWrapper wrapper = new BeanWrapperImpl(obj);
		Object id = wrapper.getPropertyValue(idAttribute);
		
		Object existing = objects != null ? objects.get(id) : null;
		if( existing == null ) throw new IllegalArgumentException("The object to update does not appear to exist in the repository so cannot update: " + obj);
		
		objects.put(id, obj);
		return obj;
	}

	/**
	 * @return the objects
	 */
	public Map<Object, Object> getObjects() {
		return objects;
	}

	/**
	 * @param objects the objects to set
	 */
	public void setObjects(Map<Object, Object> objects) {
		this.objects = objects;
	}

	/**
	 * @return the lastId
	 */
	public long getLastId() {
		return lastId;
	}

	/**
	 * @param lastId the lastId to set
	 */
	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

	/**
	 * @return the idAttribute
	 */
	public String getIdAttribute() {
		return idAttribute;
	};
}
