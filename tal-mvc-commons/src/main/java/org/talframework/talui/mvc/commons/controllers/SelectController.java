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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This controllers primary function is to update the
 * model with the ID of the newly selected element and
 * then return a configured result. 
 * 
 * @author Tom Spencer
 */
public final class SelectController implements Controller {
	private static final Log logger = LogFactory.getLog(SelectController.class);

	/** The incoming parameter holding the ID */
	private String parameter = null;
	/** The attribute on model to save ID into */
	private String attribute = null;
	/** The result to return */
	private String result = null;
	/** The faulure result */
	private String failureResult = "fail";
	
	public SelectController() {
	}
	
	public SelectController(String idParameter, String modelAttribute, String result) {
		this.parameter = idParameter;
		this.attribute = modelAttribute;
		this.result = result;
	}
	
	public void init() {
		if( parameter == null ) throw new IllegalArgumentException("Must provide a parameter to a SelectController");
		if( attribute == null ) throw new IllegalArgumentException("Must provide a attribute to a SelectController");
		if( result == null ) throw new IllegalArgumentException("Must provide a result to a SelectController");
		if( failureResult == null ) throw new IllegalArgumentException("Must provide a failure result to a SelectController");
	}
	
	/**
	 * Public static method to use the functionality of this controller
	 * directly.
	 * 
	 * @param parameter The parameter to read from the input
	 * @param attribute The attribute to store that parameter within
	 * @param result The result
	 * @param failureResult The failure result if the param does not exist
	 * @param model The current model
	 * @param input The input
	 * @return The result or the failure result
	 */
	public static String perform(String parameter, String attribute, String result, String failureResult, Model model, InputModel input) {
		if( input.hasParameter(parameter) ) {
			model.setAttribute(attribute, input.getParameter(parameter));
			return result;
		}
		else {
			logger.debug("Select controller chose failure result because parameter was not present in input");
			return failureResult;
		}
	}
	
	/**
	 * Simply sets the parameter to the attribute and returns result
	 */
	public String performAction(Model model, InputModel input) {
		return SelectController.perform(parameter, attribute, result, failureResult, model, input);
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("SelectController: ");
		buf.append("result=").append(getResult());
		buf.append(", param=").append(getParameter());
		buf.append(", attr=").append(getAttribute());
		buf.append(", failureResult=").append(getFailureResult());
		return buf.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result
				+ ((failureResult == null) ? 0 : failureResult.hashCode());
		result = prime * result
				+ ((parameter == null) ? 0 : parameter.hashCode());
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
		SelectController other = (SelectController) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (failureResult == null) {
			if (other.failureResult != null)
				return false;
		} else if (!failureResult.equals(other.failureResult))
			return false;
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}

	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}
	/**
	 * @param parameter the parameter to set
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
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
	 * @return the failureResult
	 */
	public String getFailureResult() {
		return failureResult;
	}
	/**
	 * @param failureResult the failureResult to set
	 */
	public void setFailureResult(String failureResult) {
		this.failureResult = failureResult;
	}
}
