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

import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This class is a more configurable version of the select
 * controller in that multiple input parameters can be
 * configured to be extract and set on the model. Also any
 * number of input parameters can be defined as mandatory
 * and if they are not will result in an alternative result
 * code.
 * 
 * @author Tom Spencer
 */
public final class UpdateController implements Controller {
	
	/** Holds the normal result code */
	private String result = null;
	/** Holds the input params to store on the model */
	private Map<String, String> inputToModelMappings = null;
	/** Holds the results if particular inputs are missing */
	private Map<String, String> missingInputResults = null;
	
	public void init() {
		if( result == null ) throw new IllegalArgumentException("You must supply a result code to a UpdateController");
	}
	
	/**
	 * Tests for any missing inputs and copies the input into the 
	 * model.
	 */
	@Trace
    public String performAction(Model model, InputModel input) {
		String ret = null;

		// Test the missing inputs
		if( missingInputResults != null ) {
			Iterator<String> it = missingInputResults.keySet().iterator();
			while( it.hasNext() ) {
				String param = it.next();
				if( !input.hasParameter(param) ) {
					ret = missingInputResults.get(param);
					if( ret != null ) break;
				}
			}
		}

		// Bind any model attributes
		if( ret == null && inputToModelMappings != null ) {
			Iterator<String> it = inputToModelMappings.keySet().iterator();
			while( it.hasNext() ) {
				String param = it.next();
				String attr = inputToModelMappings.get(param);
				if( param != null && attr != null && input.hasParameter(param) ) {
					if( input.hasMultiValue(param) ) model.setAttribute(attr, input.getParameterValues(param));
					else model.setAttribute(attr, input.getParameter(param));
				}
			}
		}
		
		return ret == null ? result : ret;
	}
	
	@Override
	public String toString() {
		return "UpdateController: result=" + getResult();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((inputToModelMappings == null) ? 0 : inputToModelMappings
						.hashCode());
		result = prime
				* result
				+ ((missingInputResults == null) ? 0 : missingInputResults
						.hashCode());
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
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
		UpdateController other = (UpdateController) obj;
		if (inputToModelMappings == null) {
			if (other.inputToModelMappings != null)
				return false;
		} else if (!inputToModelMappings.equals(other.inputToModelMappings))
			return false;
		if (missingInputResults == null) {
			if (other.missingInputResults != null)
				return false;
		} else if (!missingInputResults.equals(other.missingInputResults))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the inputToModelMappings
	 */
	public Map<String, String> getInputToModelMappings() {
		return inputToModelMappings;
	}

	/**
	 * @param inputToModelMappings the inputToModelMappings to set
	 */
	public void setInputToModelMappings(Map<String, String> inputToModelMappings) {
		this.inputToModelMappings = inputToModelMappings;
	}

	/**
	 * @return the missingInputResults
	 */
	public Map<String, String> getMissingInputResults() {
		return missingInputResults;
	}

	/**
	 * @param missingInputResults the missingInputResults to set
	 */
	public void setMissingInputResults(Map<String, String> missingInputResults) {
		this.missingInputResults = missingInputResults;
	}
}
