/*
 * Copyright 2008 Thomas Spencer
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

package org.talframework.talui.template.core.groups;

import java.util.Map;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.CommandElement;
import org.talframework.talui.template.behaviour.supporting.ReferenceElement;
import org.talframework.talui.template.behaviour.supporting.ResourceProperty;
import org.talframework.talui.template.core.BaseElement;

public class ActionGroup extends SimpleGroup implements CommandElement, ResourceProperty, ReferenceElement {

	/** Holds the resource to output as the actions name - default is action.name */
	private String message = null;
	/** Holds the action to invoke on command (treated as an expression) */
	private String action = null;
	/** Holds an expression that evaluates to action parameters */
	private String params = null;
	/** Holds the reference page to refer reader to (treated as an expression) */
	private String referencePage = null;
	/** Holds an expression that evaluates to model parameters to reference page */
	private String referenceParams = null;
	
	/**
	 * Overridden to prevent resource or reference behaviour leaking
	 * unless set.
	 */
	@Override
	public <T> T getBehaviour(Class<T> behaviour) {
		if( message == null && behaviour.equals(ResourceProperty.class) ) return null;
		if( referencePage == null && behaviour.equals(ReferenceElement.class) ) return null;
		
		return super.getBehaviour(behaviour);
	}
	
	/**
	 * Simply returns the message if there is one
	 */
	public String getResource() {
		return message;
	}
	
	/**
	 * Treats the action as dynamic and evaluates it
	 */
	public String getAction(RenderModel model) {
		return model.evaluateExpression(getAction(), String.class);
	}
	
	/**
	 * Simply evaluates the reference params and turns them
	 * into a map using the {@link BaseElement} helper function.
	 */
	public Map<String, String> getActionParameters(RenderModel model) {
		return evaluateParameterExpression(model, getParams());
	}
	
	/**
	 * Turns the reference page and parameters into a page url
	 */
	public String getReferenceUrl(RenderModel model) {
		Map<String, String> params = evaluateParameterExpression(model, getReferenceParams());
		return model.getUrlGenerator().generatePageUrl(referencePage, null, null, params);
	}
	
	/**
	 * @return the actionExpr
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param actionExpr the actionExpr to set
	 */
	public void setAction(String actionExpr) {
		this.action = actionExpr;
	}

	/**
	 * @return the paramsExpr
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param paramsExpr the paramsExpr to set
	 */
	public void setParams(String paramsExpr) {
		this.params = paramsExpr;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the referencePage
	 */
	public String getReferencePage() {
		return referencePage;
	}

	/**
	 * @param referencePage the referencePage to set
	 */
	public void setReferencePage(String referencePage) {
		this.referencePage = referencePage;
	}

	/**
	 * @return the referenceParams
	 */
	public String getReferenceParams() {
		return referenceParams;
	}

	/**
	 * @param referenceParams the referenceParams to set
	 */
	public void setReferenceParams(String referenceParams) {
		this.referenceParams = referenceParams;
	}

	/*
	 * (non-Javadoc)
	 * @see org.talframework.talui.template.core.groups.SimpleGroup#getType()
	 */
	@Override
	public String getType() {
		return "action-group";
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.talframework.talui.template.core.groups.SimpleGroup#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("ActionGroup: ");
		buf.append("name=").append(getName());
		if( action != null ) buf.append(", action=").append(getAction());
		if( params != null ) buf.append(", params=").append(getParams());
		if( referencePage != null ) buf.append(", page=").append(getReferencePage());
		if( referenceParams != null ) buf.append(", model=").append(getReferenceParams());
		return buf.toString();
	}
}
