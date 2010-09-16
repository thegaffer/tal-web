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

package org.tpspencer.tal.template.compiler;


/**
 * This interface represents a class that can create/return
 * all the molds supporting the different template elements
 * that the base web-template library supports. Each type
 * of compiler implements this interface to return these
 * molds.
 * 
 * @author Tom Spencer
 */
public interface TemplateElementRenderMoldFactory {

	/**
	 * @return The default element mold to use
	 */
	public TemplateElementRenderMold getDefaultMold();
	
	/**
	 * @return The special inner template mold (inner templates just refer to another template directly)
	 */
	public TemplateElementRenderMold getInnerTemplateMold();
	
	/**
	 * @return The mold to use for messages
	 */
	public TemplateElementRenderMold getMessagesMold();
	
	/**
	 * @return The default mold for dynamic properties
	 */
	public TemplateElementRenderMold getDefaultPropMold();
	
	/**
	 * @return The default mold to use for groups
	 */
	public TemplateElementRenderMold getDefaultGroupMold();
	
	/**
	 * @return The default mold to use for member properties
	 */
	public TemplateElementRenderMold getDefaultMemberMold();
	
	/**
	 * @return The default mold to use for commands/actions
	 */
	public TemplateElementRenderMold getDefaultCommandMold();
	
	/**
	 * @return The mold to use for a table group
	 */
	public TemplateElementRenderMold getTableGroupMold();
	
	/**
	 * @return The mold to use for members directly inside the member mold
	 */
	public TemplateElementRenderMold getTableMemberMold();
	
	/**
	 * @return The mold to use for members inside a table (not directly inside)
	 */
	public TemplateElementRenderMold getTableInnerMemberMold();
	
	/**
	 * @return The mold to use for properties inside a table
	 */
	public TemplateElementRenderMold getTableInnerPropMold();
	
	/**
	 * @return The mold to use for groups inside the table
	 */
	public TemplateElementRenderMold getTableInnerGroupMold();
	
	/**
	 * @return The mold to use for commands inside the table
	 */
	public TemplateElementRenderMold getTableInnerCommandMold();
	
	/**
	 * @return The mold to use for a form group
	 */
	public TemplateElementRenderMold getFormGroupMold();
	
	/**
	 * @return The mold to use for properties inside a form
	 */
	public TemplateElementRenderMold getFormInnerPropMold();
	
	/**
	 * @return The mold to use for groups inside a form
	 */
	public TemplateElementRenderMold getFormInnerGroupMold();
	
	/**
	 * @return The mold to use for member props inside a form
	 */
	public TemplateElementRenderMold getFormInnerMemberMold();
	
	/**
	 * @return The mold to use inside for commands inside a form
	 */
	public TemplateElementRenderMold getFormInnerCommandMold();
}
