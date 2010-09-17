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

package org.talframework.talui.template.compiler.js;

import org.talframework.talui.template.compiler.TemplateElementRenderMold;
import org.talframework.talui.template.compiler.TemplateElementRenderMoldFactory;

/**
 * Implements the template element factory. Also acts a singleton that 
 * allows each mold to be overridden in a Spring config file.
 * 
 * @author Tom Spencer
 */
public class JsTemplateElementRenderMoldFactory implements TemplateElementRenderMoldFactory {
	/** Singleton instance (can be updated) - see end for instance methods */
	private static final TemplateElementRenderMoldFactory INSTANCE = new JsTemplateElementRenderMoldFactory();
	
	/** The default mold */
	public TemplateElementRenderMold defaultMold = new BaseJsElementMold();
	
	/** The inner template mold (specific) */
	public TemplateElementRenderMold innerTemplateMold = new BaseJsElementMold();
	/** Mold to use for messages */
	private TemplateElementRenderMold messagesMold = null;
	
	/** The default property mold */
	public TemplateElementRenderMold defaultPropMold = new JsElementMold();
	/** The default group mold */
	public TemplateElementRenderMold defaultGroupMold = new JsElementMold();
	/** The default member mold */
	public TemplateElementRenderMold defaultMemberMold = new JsElementMold();
	/** The default command mold */
	public TemplateElementRenderMold defaultCommandMold;
	
	/** The table group mold - specific */
	public TemplateElementRenderMold tableGroupMold = new JsElementMold(new String[]{"table"}, new String[]{"table-member"});
	/** A member prop directly within the group mold */
	public TemplateElementRenderMold tableMemberMold = new BaseJsElementMold();
	/** A property within a table */
	public TemplateElementRenderMold tableInnerPropMold = tableMemberMold;
	/** A member within a table */
	public TemplateElementRenderMold tableInnerMemberMold = tableMemberMold;
	/** A group within a table */
	public TemplateElementRenderMold tableInnerGroupMold = tableMemberMold;
	/** A command within a table */
	public TemplateElementRenderMold tableInnerCommandMold = tableMemberMold;
	
	/** The form group mold - specific */
	public TemplateElementRenderMold formGroupMold = new BaseJsElementMold(new String[]{"form"}, null);
	/** A property within a form */
	public TemplateElementRenderMold formInnerPropMold = new DynamicFieldElementMold();
	/** A member within a form */
	public TemplateElementRenderMold formInnerMemberMold = new BaseJsElementMold();
	/** A group within a form */
	public TemplateElementRenderMold formInnerGroupMold = formInnerMemberMold;
	/** A command within a form */
	public TemplateElementRenderMold formInnerCommandMold = formInnerMemberMold;
	
	//////////////////////////////////////////////////////////

	/**
	 * @return The single (per classloader) fragment factory
	 */
	public static TemplateElementRenderMoldFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Hidden constructor
	 */
	private JsTemplateElementRenderMoldFactory() {
	}

	//////////////////////////////////////////////////////////
	// Interface Methods
	
	/**
	 * @return the defaultMold
	 */
	public TemplateElementRenderMold getDefaultMold() {
		return defaultMold;
	}

	/**
	 * @param defaultMold the defaultMold to set
	 */
	public void setDefaultMold(TemplateElementRenderMold defaultMold) {
		this.defaultMold = defaultMold;
	}

	/**
	 * @return the innerTemplateMold
	 */
	public TemplateElementRenderMold getInnerTemplateMold() {
		return innerTemplateMold;
	}

	/**
	 * @param innerTemplateMold the innerTemplateMold to set
	 */
	public void setInnerTemplateMold(TemplateElementRenderMold innerTemplateMold) {
		this.innerTemplateMold = innerTemplateMold;
	}
	
	/**
	 * @return The mold to use for messages
	 */
	public TemplateElementRenderMold getMessagesMold() {
		return messagesMold;
	}
	
	/**
	 * @param messagesMold The new mold to use for messages
	 */
	public void setMessagesMold(TemplateElementRenderMold messagesMold) {
		this.messagesMold = messagesMold;
	}

	/**
	 * @return the defaultPropMold
	 */
	public TemplateElementRenderMold getDefaultPropMold() {
		return defaultPropMold;
	}

	/**
	 * @param defaultPropMold the defaultPropMold to set
	 */
	public void setDefaultPropMold(TemplateElementRenderMold defaultPropMold) {
		this.defaultPropMold = defaultPropMold;
	}

	/**
	 * @return the defaultGroupMold
	 */
	public TemplateElementRenderMold getDefaultGroupMold() {
		return defaultGroupMold;
	}

	/**
	 * @param defaultGroupMold the defaultGroupMold to set
	 */
	public void setDefaultGroupMold(TemplateElementRenderMold defaultGroupMold) {
		this.defaultGroupMold = defaultGroupMold;
	}

	/**
	 * @return the defaultMemberMold
	 */
	public TemplateElementRenderMold getDefaultMemberMold() {
		return defaultMemberMold;
	}

	/**
	 * @param defaultMemberMold the defaultMemberMold to set
	 */
	public void setDefaultMemberMold(TemplateElementRenderMold defaultMemberMold) {
		this.defaultMemberMold = defaultMemberMold;
	}

	/**
	 * @return the defaultCommandMold
	 */
	public TemplateElementRenderMold getDefaultCommandMold() {
		return defaultCommandMold;
	}

	/**
	 * @param defaultCommandMold the defaultCommandMold to set
	 */
	public void setDefaultCommandMold(TemplateElementRenderMold defaultCommandMold) {
		this.defaultCommandMold = defaultCommandMold;
	}

	/**
	 * @return the tableGroupMold
	 */
	public TemplateElementRenderMold getTableGroupMold() {
		return tableGroupMold;
	}

	/**
	 * @param tableGroupMold the tableGroupMold to set
	 */
	public void setTableGroupMold(TemplateElementRenderMold tableGroupMold) {
		this.tableGroupMold = tableGroupMold;
	}

	/**
	 * @return the tableMemberMold
	 */
	public TemplateElementRenderMold getTableMemberMold() {
		return tableMemberMold;
	}

	/**
	 * @param tableMemberMold the tableMemberMold to set
	 */
	public void setTableMemberMold(TemplateElementRenderMold tableMemberMold) {
		this.tableMemberMold = tableMemberMold;
	}

	/**
	 * @return the tableInnerPropMold
	 */
	public TemplateElementRenderMold getTableInnerPropMold() {
		return tableInnerPropMold;
	}

	/**
	 * @param tableInnerPropMold the tableInnerPropMold to set
	 */
	public void setTableInnerPropMold(TemplateElementRenderMold tableInnerPropMold) {
		this.tableInnerPropMold = tableInnerPropMold;
	}

	/**
	 * @return the tableInnerMemberMold
	 */
	public TemplateElementRenderMold getTableInnerMemberMold() {
		return tableInnerMemberMold;
	}

	/**
	 * @param tableInnerMemberMold the tableInnerMemberMold to set
	 */
	public void setTableInnerMemberMold(
			TemplateElementRenderMold tableInnerMemberMold) {
		this.tableInnerMemberMold = tableInnerMemberMold;
	}

	/**
	 * @return the tableInnerGroupMold
	 */
	public TemplateElementRenderMold getTableInnerGroupMold() {
		return tableInnerGroupMold;
	}

	/**
	 * @param tableInnerGroupMold the tableInnerGroupMold to set
	 */
	public void setTableInnerGroupMold(TemplateElementRenderMold tableInnerGroupMold) {
		this.tableInnerGroupMold = tableInnerGroupMold;
	}

	/**
	 * @return the tableInnerCommandMold
	 */
	public TemplateElementRenderMold getTableInnerCommandMold() {
		return tableInnerCommandMold;
	}

	/**
	 * @param tableInnerCommandMold the tableInnerCommandMold to set
	 */
	public void setTableInnerCommandMold(
			TemplateElementRenderMold tableInnerCommandMold) {
		this.tableInnerCommandMold = tableInnerCommandMold;
	}

	/**
	 * @return the formGroupMold
	 */
	public TemplateElementRenderMold getFormGroupMold() {
		return formGroupMold;
	}

	/**
	 * @param formGroupMold the formGroupMold to set
	 */
	public void setFormGroupMold(TemplateElementRenderMold formGroupMold) {
		this.formGroupMold = formGroupMold;
	}

	/**
	 * @return the formInnerPropMold
	 */
	public TemplateElementRenderMold getFormInnerPropMold() {
		return formInnerPropMold;
	}

	/**
	 * @param formInnerPropMold the formInnerPropMold to set
	 */
	public void setFormInnerPropMold(TemplateElementRenderMold formInnerPropMold) {
		this.formInnerPropMold = formInnerPropMold;
	}

	/**
	 * @return the formInnerMemberMold
	 */
	public TemplateElementRenderMold getFormInnerMemberMold() {
		return formInnerMemberMold;
	}

	/**
	 * @param formInnerMemberMold the formInnerMemberMold to set
	 */
	public void setFormInnerMemberMold(TemplateElementRenderMold formInnerMemberMold) {
		this.formInnerMemberMold = formInnerMemberMold;
	}

	/**
	 * @return the formInnerGroupMold
	 */
	public TemplateElementRenderMold getFormInnerGroupMold() {
		return formInnerGroupMold;
	}

	/**
	 * @param formInnerGroupMold the formInnerGroupMold to set
	 */
	public void setFormInnerGroupMold(TemplateElementRenderMold formInnerGroupMold) {
		this.formInnerGroupMold = formInnerGroupMold;
	}

	/**
	 * @return the formInnerCommandMold
	 */
	public TemplateElementRenderMold getFormInnerCommandMold() {
		return formInnerCommandMold;
	}

	/**
	 * @param formInnerCommandMold the formInnerCommandMold to set
	 */
	public void setFormInnerCommandMold(
			TemplateElementRenderMold formInnerCommandMold) {
		this.formInnerCommandMold = formInnerCommandMold;
	}
}
