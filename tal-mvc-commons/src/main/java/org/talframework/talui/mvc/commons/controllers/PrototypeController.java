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

package org.talframework.talui.mvc.commons.controllers;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This controller can be useful in early stages of
 * development and prototyping. It is configured to
 * return a result when it is fired. Optionally it
 * can also support a set of custom results which are
 * mapping to input values. If those values are present
 * (regardless of value) then that result is returned.
 * This enables you to very quickly mock up a JSP with
 * a set of links/buttons hooked up to this controller
 * and so get some basic dynamic behaviour without any
 * real code.
 * 
 * @author Tom Spencer
 */
public final class PrototypeController implements Controller {
	private final static Log logger = LogFactory.getLog(PrototypeController.class);
	
	/** Holds the default result */
	private String defaultResult = null;
	/** Holds the map of custom results */
	private Map<String, String> customResults = null;
	/** Holds a map of parameter to model mappings */
	private Map<String, String> modelMappings = null;
	
	public PrototypeController() {
		logger.warn("*** Prototype Controller in use - ensure this is replaced in Production Environment");
	}
	
	public PrototypeController(String defaultResult) {
		logger.warn("*** Prototype Controller in use - ensure this is replaced in Production Environment");
		
		this.defaultResult = defaultResult;
	}
	
	public PrototypeController(String defaultResult, Map<String, String> customResults) {
		logger.warn("*** Prototype Controller in use - ensure this is replaced in Production Environment");
		
		this.defaultResult = defaultResult;
		this.customResults = customResults;
	}
	
	/**
	 * If there are any customResults defined the key of the
	 * customResults is searched for in the input model. If 
	 * found that result is returned. If there are no matches
	 * or there are no customResults then the defaultResult
	 * is returned.
	 */
	public String performAction(Model model, InputModel input) {
		// Set any model attributes as configured
		if( modelMappings != null ) {
			Iterator<String> it = modelMappings.keySet().iterator();
			while( it.hasNext() ) {
				String name = it.next();
				if( input.hasParameter(name) ) model.setAttribute(modelMappings.get(name), input.getParameter(name));
			}
		}
		
		if( customResults != null ) {
			Iterator<String> it = customResults.keySet().iterator();
			while( it.hasNext() ) {
				String attr = it.next();
				if( input.hasParameter(attr) ) {
					return customResults.get(attr);
				}
			}
		}

		// Simply return the default if we get here
		return defaultResult;
	}
	
	@Override
	public String toString() {
		return "PrototypeController: result=" + getDefaultResult();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customResults == null) ? 0 : customResults.hashCode());
		result = prime * result
				+ ((defaultResult == null) ? 0 : defaultResult.hashCode());
		result = prime * result
				+ ((modelMappings == null) ? 0 : modelMappings.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrototypeController other = (PrototypeController) obj;
		if (customResults == null) {
			if (other.customResults != null)
				return false;
		} else if (!customResults.equals(other.customResults))
			return false;
		if (defaultResult == null) {
			if (other.defaultResult != null)
				return false;
		} else if (!defaultResult.equals(other.defaultResult))
			return false;
		if (modelMappings == null) {
			if (other.modelMappings != null)
				return false;
		} else if (!modelMappings.equals(other.modelMappings))
			return false;
		return true;
	}

	/**
	 * @return the defaultResult
	 */
	public String getDefaultResult() {
		return defaultResult;
	}

	/**
	 * @param defaultResult the defaultResult to set
	 */
	public void setDefaultResult(String defaultResult) {
		this.defaultResult = defaultResult;
	}

	/**
	 * @return the customResults
	 */
	public Map<String, String> getCustomResults() {
		return customResults;
	}

	/**
	 * @param customResults the customResults to set
	 */
	public void setCustomResults(Map<String, String> customResults) {
		this.customResults = customResults;
	}
	
	/**
	 * @return the modelMappings
	 */
	public Map<String, String> getModelMappings() {
		return modelMappings;
	}

	/**
	 * @param modelMappings the modelMappings to set
	 */
	public void setModelMappings(Map<String, String> modelMappings) {
		this.modelMappings = modelMappings;
	}
}
