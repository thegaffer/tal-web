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

package org.tpspencer.tal.template.compiler.html;

import org.tpspencer.tal.template.compiler.FragmentMold;
import org.tpspencer.tal.template.compiler.SimpleTemplateElementMold;
import org.tpspencer.tal.template.compiler.TemplateElementRenderMold;
import org.tpspencer.tal.template.compiler.TemplateElementRenderMoldFactory;

public class HtmlTemplateElementRenderMoldFactory implements TemplateElementRenderMoldFactory {
	private static final HtmlTemplateElementRenderMoldFactory INSTANCE = new HtmlTemplateElementRenderMoldFactory();
	
	/** The default mold */
	public TemplateElementRenderMold defaultMold;
	
	/** The inner template mold (specific) */
	public TemplateElementRenderMold innerTemplateMold;
	private TemplateElementRenderMold messagesMold = new MessagesElementMold();
	
	/** The default property mold */
	public TemplateElementRenderMold defaultPropMold;
	/** The default group mold */
	public TemplateElementRenderMold defaultGroupMold;
	/** The default member mold */
	public TemplateElementRenderMold defaultMemberMold;
	/** The default command mold */
	public TemplateElementRenderMold defaultCommandMold;
	
	/** The table group mold - specific */
	public TemplateElementRenderMold tableGroupMold;
	/** A member prop directly within the group mold */
	public TemplateElementRenderMold tableMemberMold;
	/** A property within a table */
	public TemplateElementRenderMold tableInnerPropMold;
	/** A member within a table */
	public TemplateElementRenderMold tableInnerMemberMold;
	/** A group within a table */
	public TemplateElementRenderMold tableInnerGroupMold;
	/** A command within a table */
	public TemplateElementRenderMold tableInnerCommandMold;
	
	/** The form group mold - specific */
	public TemplateElementRenderMold formGroupMold;
	/** A property within a form */
	public TemplateElementRenderMold formInnerPropMold;
	/** A member within a form */
	public TemplateElementRenderMold formInnerMemberMold;
	/** A group within a form */
	public TemplateElementRenderMold formInnerGroupMold;
	/** A command within a form */
	public TemplateElementRenderMold formInnerCommandMold;
	
	///////////////////////////////////////////////////////
	// Constructor
	
	/**
	 * Hidden constructor
	 */
	private HtmlTemplateElementRenderMoldFactory() {
		HtmlFragmentFactory factory = DefaultHtmlFragmentFactory.getInstance();
		
		defaultMold = createMold(
				factory.getWrapperMold(), 
				new FragmentMold[]{
					factory.getLabelMold(), 
					factory.getValueMold(), 
					factory.getMemberMold(), 
					factory.getChildrenMold()});
		
		// Template
		innerTemplateMold = createMold(factory.getTemplateMold(), null);
		
		// The defaults
		defaultPropMold = null;
		defaultGroupMold = null;
		defaultMemberMold = null;
		defaultCommandMold = createMold(
				factory.getLinkWrapperMold(), 
				new FragmentMold[]{
					factory.getChildrenMold()});
		
		
		// Tables
		SimpleTemplateElementMold temp = createMold(
				factory.getTableGroupMold(), 
				new FragmentMold[]{
					factory.getLabelMold(), 
					factory.getChildrenMold()});
		temp.setStyles(new String[]{HtmlElementConstants.STYLE_IN_TABLE});
		temp.setTemplateStyles(new String[]{HtmlElementConstants.STYLE_IN_TABLE_MEMBER});
		tableGroupMold = temp;
		
		tableMemberMold = createMold(factory.getTableMemberMold(), null);
		
		tableInnerPropMold = createMold(
				factory.getSpanWrapperMold(), 
				new FragmentMold[]{
					factory.getValueMold(), 
					factory.getMemberMold(), 
					factory.getChildrenMold()});
		
		tableInnerGroupMold = tableInnerPropMold;
		tableInnerMemberMold = tableInnerPropMold;
		tableInnerCommandMold = defaultCommandMold; // TODO: Extend link to put out cell like wrapper??
		
		
		// Forms
		temp = createMold(
				factory.getFormGroupMold(), 
				new FragmentMold[]{
					factory.getChildrenMold()});
		temp.setStyles(new String[]{HtmlElementConstants.STYLE_IN_FORM});
		formGroupMold = temp;
		
		formInnerPropMold = createMold(
				factory.getWrapperMold(), 
				new FragmentMold[]{
					factory.getLabelMold(), 
					factory.getInputMold()});
		
		formInnerGroupMold = createMold(
				factory.getWrapperMold(), 
				new FragmentMold[]{
					factory.getLabelMold(), 
					factory.getChildrenMold()});
		
		formInnerMemberMold = createMold(
				factory.getWrapperMold(), 
				new FragmentMold[]{
					factory.getLabelMold(), 
					factory.getMemberMold()});
		
		formInnerCommandMold = createMold(
				factory.getWrapperMold(), 
				new FragmentMold[]{
					factory.getInputMold(), 
					factory.getMemberMold(), 
					factory.getChildrenMold()});
	}
	
	
	
	/**
	 * Internal helper to create a simple template element model.
	 * 
	 * @param wrapper
	 * @param fragments
	 * @return
	 */
	private SimpleTemplateElementMold createMold(FragmentMold wrapper, FragmentMold[] fragments) {
		SimpleTemplateElementMold ret = new SimpleTemplateElementMold();
		ret.setWrapper(wrapper);
		if( fragments != null ) {
			for( int i = 0 ; i < fragments.length ; i++ ) {
				ret.addFragment(fragments[i]);
			}
		}
		return ret;
	}
	
	/**
	 * Access to the single HtmlTemplateElementFactory
	 * 
	 * @return The factory
	 */
	public static HtmlTemplateElementRenderMoldFactory getInstance() {
		return INSTANCE;
	}

	///////////////////////////////////////////////////////////////////
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
