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

package org.talframework.talui.template.core.props;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.behaviour.property.CodedProperty;
import org.talframework.talui.template.render.codes.CodeType;
import org.talframework.talui.template.render.codes.CodeTypeFactoryLocator;

/**
 * This class extends simple property to provide a 
 * property who takes on one of a set of choices.
 * 
 * @author Tom Spencer
 */
public class ChoiceProperty extends SimpleProperty implements CodedProperty {

	/** Holds the search url (in case not std) */
	private String searchUrl = null;
	/** Member holds the type of choices offered to user */
	private String codeType = null;
	/** If true then the user is able to add his/her own value */
	private boolean unbounded = false;
	/** Determines if the list of options is likely to be variable */
	private boolean dynamic = false;
	
	@Override
	public String getType() {
		return "choice-prop";
	}
	
	/**
	 * Just returns the code type unchanged
	 */
	public String getCodeType(RenderModel model) {
		return getCodeType();
	}
	
	/**
	 * Gets the search url
	 */
	public String getSearchUrl(RenderModel model) {
		if( searchUrl == null ) {
			return model.getUrlGenerator().generateResourceUrl("search/" + codeType);
		}
		else return searchUrl;
	}
	
	/**
	 * Converts code value to string if appropriate
	 * 
	 * FUTURE: What about parameters??? Assume code type is in model??
	 */
	@Override
	public String getValue(RenderModel model) {
		String ret = super.getValue(model);
		if( !unbounded ) {
			CodeType type = CodeTypeFactoryLocator.getCodeType(codeType, model, null);
			String desc = type.getCodeDescription(ret);
			if( desc != null ) ret = desc;
		}
		return ret;
	}
	
	/**
	 * Gets the converted display value
	 */
	public String getCodeValue(RenderModel model) {
		return super.getValue(model);
	}
	
	/**
	 * @return the choiceType
	 */
	public String getCodeType() {
		return codeType;
	}
	
	/**
	 * @param choiceType the choiceType to set
	 */
	public void setCodeType(String choiceType) {
		this.codeType = choiceType;
	}
	
	/**
	 * @return the unbounded
	 */
	public boolean isUnbounded() {
		return unbounded;
	}
	
	/**
	 * @param unbounded the unbounded to set
	 */
	public void setUnbounded(boolean unbounded) {
		this.unbounded = unbounded;
	}

	/**
	 * @return the dynamic
	 */
	public boolean isDynamic() {
		return dynamic;
	}

	/**
	 * @param dynamic the dynamic to set
	 */
	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	/**
	 * @return the searchUrl
	 */
	public String getSearchUrl() {
		return searchUrl;
	}

	/**
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
}
