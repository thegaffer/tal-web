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

import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * This controller very simply returns a set result
 * 
 * @author Tom Spencer
 */
public final class SimpleController implements Controller {

	/** The result the controller returns */
	private String result = null;
	
	public SimpleController() {
		
	}
	
	public SimpleController(String result) {
		this.result = result;
		init();
	}
	
	public void init() {
		if( result == null ) throw new IllegalArgumentException("You must set a result for a simple controller");
	}
	
	/**
	 * Simply returns the result
	 */
	public String performAction(Model model, InputModel input) {
		return result;
	}
	
	@Override
	public String toString() {
		return "SimpleController: result=" + getResult();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		SimpleController other = (SimpleController) obj;
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
}
