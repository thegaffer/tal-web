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

package org.tpspencer.tal.mvc.process;

import java.util.Map;

import org.tpspencer.tal.mvc.model.ModelConfiguration;

/**
 * This interface represents a class that can retrieve 
 * any stored state against a ModelConfiguration. This
 * is always specific to the running environment we 
 * are in.
 * 
 * @author Tom Spencer
 */
public interface ModelLayerAttributesResolver {

	/**
	 * Called to get hold of this attributes for a model 
	 * configuration.
	 *  
	 * @param model The model to get the attributes for
	 * @return A map of stored objects (return null if there are no stored attributes)
	 */
	public Map<String, Object> getModelAttributes(ModelConfiguration model);

	/**
	 * Called to save the model attributes. Typically done by the model
	 * when a layer is removed.
	 * 
	 * @param model The model to save
	 * @param attrs The attributes of the model
	 */
	public void saveModelAttributes(ModelConfiguration model, Map<String, Object> attrs);
	
	/**
	 * Called when we are permanently removing a model layer. 
	 * This occurs when we exit view in the action resolver
	 * 
	 * @param model The model to remove
	 */
	public void removeModel(ModelConfiguration model);
	
	/**
	 * Called to set the save mode on (true) or off (false). This
	 * allows the outer container to turn off any automatic saving
	 * of model attributes. Not all model attribute resolvers 
	 * support saving in which case this becomes a no-op.
	 * 
	 * <p><b>Note: </b>This only affects externally saving of the
	 * attributes. Typically a resolver holds the model attributes
	 * so it can re-serve them in a future call to getModelAttributes.
	 * This behaviour occurs regardless of the saveMode.</p>
	 * 
	 * @param on True to turn auto-saving on, false otherwise.
	 */
	public void setSaveMode(boolean on);
}
