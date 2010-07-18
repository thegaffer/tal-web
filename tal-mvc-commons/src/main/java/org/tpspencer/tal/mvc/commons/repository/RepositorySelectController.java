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

import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.input.InputModel;

/**
 * This controller will take an ID from the input, use it
 * to query the repository and finally add the resulting
 * object to the specified model attribute.
 * 
 * TODO: Should the illegal arg exception if no repo object be a custom one?
 * 
 * @author Tom Spencer
 */
public class RepositorySelectController extends RepositoryHolder implements Controller {

	/** Holds the name of the parameter that becomes the ID */
	private String idParameter = "id";
	/** Holds the name of the attribute to add object to (if not null) */
	private String modelAttribute = null;
	/** Holds the result */
	private String result = "selected";
	
	/**
	 * Obtains the input, gets the object from the repository with
	 * that ID and then adds it to the model if it exists
	 */
	public String performAction(Model model, InputModel input) {
		if( modelAttribute == null ) throw new IllegalArgumentException("The RepositorySelectController has no model attribute set to add the object to");
		
		Object id = input.getParameter(idParameter);
		if( id != null ) {
			Object obj = getRepository().findById(id, Object.class);
			if( obj != null ) model.put(modelAttribute, obj);
			else throw new IllegalArgumentException("The object to select does not exist in the repository: " + id);
		}
		else {
			model.removeAttribute(modelAttribute);
		}
		
		return result;
	}

	/**
	 * @return the idParameter
	 */
	public String getIdParameter() {
		return idParameter;
	}

	/**
	 * @param idParameter the idParameter to set
	 */
	public void setIdParameter(String idParameter) {
		this.idParameter = idParameter;
	}

	/**
	 * @return the modelAttribute
	 */
	public String getModelAttribute() {
		return modelAttribute;
	}

	/**
	 * @param modelAttribute the modelAttribute to set
	 */
	public void setModelAttribute(String modelAttribute) {
		this.modelAttribute = modelAttribute;
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
