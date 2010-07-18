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

package org.tpspencer.tal.mvc;

import java.util.Map;


/**
 * This interface represents access to the model that the UI
 * is displaying. The model is pre-configured with components
 * that retrieve the elements of the model as required, thus
 * no special code is needed to retrieve the model in the view.
 * 
 * <p>The model is an aggregate object that is layered from
 * multiple model configurations. This is transparent to the
 * client of the model, but the layers are:</p>
 * <ul>
 * <li>The model that is available to windows in an application</li>
 * <li>The model that is available to all windows on the same page</li>
 * <li>The model that is available only to the current window</li>
 * <li>Optionally a temporary model representing a transaction(s)</li>
 * <li>The temporary model that is available to the current state</li>
 * </ul>
 * 
 * <p>When accessing attributes from the model the model will return
 * the first attribute that matches the given name in reverse order of
 * this list.</p>
 * 
 * @author Tom Spencer
 */
public interface Model extends Map<String, Object> {

	/**
	 * Call to get hold of an object from the model. Note that
	 * obtaining a model element might involve some remote access
	 * to get at the object. Whilst that complexity is hidden 
	 * from the client, care should be taken under profiling to
	 * ensure retrieving the model is efficient. 
	 * 
	 * @param name The name of the model attribute
	 * @return The attribute, or null if it does not currently exist
	 */
	public Object getAttribute(String name);
	
	/**
	 * Call to set an attribute in the model. Note that you
	 * cannot add arbitrary attributes into the model. All 
	 * attributes must be pre-defined inside the relevant
	 * model configuration.
	 * 
	 * <p>It should also be noted that not all attributes 
	 * are 'settable'. Many attributes are retreived from a 
	 * remote source - to update these attributes the relevant
	 * action should update this remote source. An exception
	 * is thrown if an attempt is made to do this.</p>
	 * 
	 * @param name The name of the attribute to set
	 * @param value The value to assign
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * Call to remove an attribute from the model - effectively
	 * setting it to its default value. The attribute must be
	 * predefined in the model configuration.
	 * 
	 * <p>Unlike setAttribute it is safe to remove any attribute,
	 * although attributes that are not 'held' by the model, but
	 * are instead retrieved from a remote source
	 * 
	 * @param name The name of the attribute to remove
	 */
	public void removeAttribute(String name);
}
