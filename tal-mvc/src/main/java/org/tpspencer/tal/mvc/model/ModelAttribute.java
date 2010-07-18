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

package org.tpspencer.tal.mvc.model;

import org.tpspencer.tal.mvc.Model;


/**
 * This interface represents a description of a model
 * attribute. Model attributes come in 3 different default
 * flavours, simple, resolved (from a remote source) and
 * configuration (from a properties file or similar). 
 * This interface provides enough information about the 
 * attribute to allow the action processor to process it 
 * and the outer framework to save it
 * 
 * @author Tom Spencer
 */
public interface ModelAttribute {
	
	/**
	 * @return the name of the attribute
	 */
	public String getName();
	
	/**
	 * Any aliases for this attribute. During configuration
	 * alias names are used to match attributes in higher
	 * model layers so that the same attributes in to 
	 * different layers can be shared.
	 * 
	 * @return the alias names for the attribute
	 */
	public String[] getAliases();
	
	/**
	 * Call to get the current value of the attribute if
	 * there is one. Attributes should not return default
	 * attributes here. The model is passed for resolvers
	 * to use, but it may be null (this is to prevent
	 * circular dependencies). If it is null and is required
	 * we suggest an IllegalArgumentException because this
	 * is a problem in your configuration. 
	 * 
	 * @param model The model (which may be null)
	 * @return The value of the attribute (or null if not set)
	 */
	public Object getValue(Model model);
	
	/**
	 * Called by a model when it needs to set an attribute.
	 * This allows the attribute to perform any conversation
	 * on the newValue as appropriate and then return the
	 * real new value that is used.
	 * 
	 * @param model The model
	 * @param currentValue The current value of the attribute (or null)
	 * @param newValue The new value of the attribute
	 * @return The real new value the model should record (may just be newValue)
	 */
	public Object setValue(Model model, Object currentValue, Object newValue);
	
	/**
	 * This method should return the default value. This
	 * is called typically if the call to the getValue 
	 * methods return null.
	 * 
	 * @param model The model (in case default it is calculated)
	 * @return The default value
	 */
	public Object getDefaultValue(Model model);
	
	/**
	 * @return The type of the attribute
	 */
	public Class<?> getType();
	
	/**
	 * @return True if the attribute is simple (String, Number, Primitive)
	 */
	public boolean isSimple();
	
	/**
	 * Flash attributes are held on temporarily in the request.
	 * 
	 * @return True if the attribute is a flash attribute
	 */
	public boolean isFlash();
	
	/**
	 * If the attribute is marked as clear on action
	 * it is automatically cleared down when we perform
	 * an action involving the model configuration that
	 * this model is in.
	 * 
	 * @return True if the attribute is a clear on action
	 */
	public boolean isClearOnAction();
	
	/**
	 * Some attributes should be added to each and every
	 * render. These only apply at the level of a window
	 * (page and app attributes must be explicitly added)
	 * 
	 * @return True if the attribute should be added always
	 */
	public boolean isAutoRenderAttribute();
	
	/**
	 * Some attributes are intended for one-time rendering
	 * only. This is a hint to the outer controller that
	 * is should clear down these attributes after rendering
	 * with them once.
	 * 
	 * @return True if the attribute is intended to be shown only once
	 */
	public boolean isClearOnRender();

	/**
	 * Eventable attributes are those which can be
	 * attached to events. This is typically explictly
	 * set to allow a degree of control.
	 * 
	 * @return the eventable
	 */
	public boolean isEventable();
	
	/**
	 * Indicates that the attribute is safe to alias to
	 * another attribute in a 'higher' model.
	 * 
	 * @return True if the attribute is aliasable
	 */
	public boolean isAliasable();
	
	/**
	 * Indicates that the attribute expects to be aliased
	 * and if it is not then there is a configuration 
	 * error.
	 * 
	 * @return True if the attribute must be aliased
	 */
	public boolean isAliasExpected();
}
