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

package org.talframework.talui.mvc.process;

import java.util.HashMap;
import java.util.Map;

import org.talframework.talui.mvc.model.ModelConfiguration;

/**
 * This class very simply holds model attributes inside a
 * map keyed by the model configuration. This class has been
 * created for test purposes, but it might be useful in 
 * certain real (but simple!) situations.
 * 
 * @author Tom Spencer
 */
public class SimpleModelAttributeResolver implements ModelLayerAttributesResolver {
	
	private Map<ModelConfiguration, Map<String, Object>> modelAttrs = null;

	/**
	 * Simply get the attributes from its internal map
	 */
	public Map<String, Object> getModelAttributes(ModelConfiguration model) {
		if( modelAttrs == null ) return null;
		return modelAttrs.get(model);
	}

	/**
	 * Simply saves the attributes on its internal map
	 */
	public void saveModelAttributes(ModelConfiguration model, Map<String, Object> attrs) {
		if( modelAttrs == null ) modelAttrs = new HashMap<ModelConfiguration, Map<String,Object>>();
		modelAttrs.put(model, attrs);
	}
	
	/**
	 * Simply removes the model config from stored list
	 */
	public void removeModel(ModelConfiguration model) {
		if( modelAttrs != null ) modelAttrs.remove(model);
	}

	/**
	 * A no-op in here
	 */
	public void setSaveMode(boolean on) {
		
	}
}
