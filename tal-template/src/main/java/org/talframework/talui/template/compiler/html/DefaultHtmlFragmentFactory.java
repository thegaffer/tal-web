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

package org.talframework.talui.template.compiler.html;

import org.talframework.talui.template.compiler.FragmentMold;
import org.talframework.talui.template.compiler.html.fragments.ActionFragment;
import org.talframework.talui.template.compiler.html.fragments.ChildrenFragment;
import org.talframework.talui.template.compiler.html.fragments.FormGroupFragment;
import org.talframework.talui.template.compiler.html.fragments.InputFragment;
import org.talframework.talui.template.compiler.html.fragments.LabelFragment;
import org.talframework.talui.template.compiler.html.fragments.MemberFragment;
import org.talframework.talui.template.compiler.html.fragments.TableGroupFragment;
import org.talframework.talui.template.compiler.html.fragments.TableMemberFragment;
import org.talframework.talui.template.compiler.html.fragments.TemplateFragment;
import org.talframework.talui.template.compiler.html.fragments.ValueFragment;
import org.talframework.talui.template.compiler.html.fragments.WrapperFragment;

public class DefaultHtmlFragmentFactory implements HtmlFragmentFactory {
	private static HtmlFragmentFactory INSTANCE = new DefaultHtmlFragmentFactory();
	
	/**
	 * @return The single (per classloader) fragment factory
	 */
	public static HtmlFragmentFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Method to set the factory in case we want a different one.
	 * 
	 * @param factory
	 */
	public synchronized static void setInstance(HtmlFragmentFactory factory) {
		INSTANCE = factory;
	}
	
	private DefaultHtmlFragmentFactory() {
	}
	
	////////////////////////////////////////////////////
	
	private FragmentMold wrapperMold = new WrapperFragment();
	private FragmentMold cellWrapperMold = new WrapperFragment(WrapperFragment.AS_CELL, WrapperFragment.AS_SPAN);
	private FragmentMold spanWrapperMold = new WrapperFragment(WrapperFragment.AS_SPAN, WrapperFragment.AS_SPAN);
	private FragmentMold linkWrapperMold = new ActionFragment();
	
	/** The default label mold */
	private FragmentMold labelMold = new LabelFragment();
	/** The default value mold */
	private FragmentMold valueMold = new ValueFragment();
	
	private FragmentMold templateMold = new TemplateFragment();
	
	/** The default mold for handling template element children */
	private FragmentMold childrenMold = new ChildrenFragment();
	
	private FragmentMold memberMold = new MemberFragment();
	private FragmentMold memberNoLabelMold = new MemberFragment(new String[]{"noLabel"}, null);
	
	private FragmentMold tableGroupMold = new TableGroupFragment();
	private FragmentMold tableMemberMold = new TableMemberFragment();
	
	private FragmentMold formGroupMold = new FormGroupFragment();
	/** The default input mold */
	private FragmentMold inputMold = new InputFragment();
	
	/**
	 * @return the wrapperMold
	 */
	public FragmentMold getWrapperMold() {
		return wrapperMold;
	}
	/**
	 * @param wrapperMold the wrapperMold to set
	 */
	public void setWrapperMold(FragmentMold wrapperMold) {
		this.wrapperMold = wrapperMold;
	}
	/**
	 * Simple returns the template mold
	 */
	public FragmentMold getTemplateMold() {
		return templateMold;
	}
	/**
	 * Sets the template mold
	 */
	public void setTemplateMold(FragmentMold templateMold) {
		this.templateMold = templateMold;
	}
	/**
	 * @return the cellWrapperMold
	 */
	public FragmentMold getCellWrapperMold() {
		return cellWrapperMold;
	}
	/**
	 * @param cellWrapperMold the cellWrapperMold to set
	 */
	public void setCellWrapperMold(FragmentMold cellWrapperMold) {
		this.cellWrapperMold = cellWrapperMold;
	}
	/**
	 * @return the spanWrapperMold
	 */
	public FragmentMold getSpanWrapperMold() {
		return spanWrapperMold;
	}
	/**
	 * @param spanWrapperMold the spanWrapperMold to set
	 */
	public void setSpanWrapperMold(FragmentMold spanWrapperMold) {
		this.spanWrapperMold = spanWrapperMold;
	}
	/**
	 * @return the linkWrapperMold
	 */
	public FragmentMold getLinkWrapperMold() {
		return linkWrapperMold;
	}
	/**
	 * @param linkWrapperMold the linkWrapperMold to set
	 */
	public void setLinkWrapperMold(FragmentMold linkWrapperMold) {
		this.linkWrapperMold = linkWrapperMold;
	}
	/**
	 * @return the childrenMold
	 */
	public FragmentMold getChildrenMold() {
		return childrenMold;
	}
	/**
	 * @param childrenMold the childrenMold to set
	 */
	public void setChildrenMold(FragmentMold childrenMold) {
		this.childrenMold = childrenMold;
	}
	/**
	 * @return the memberMold
	 */
	public FragmentMold getMemberMold() {
		return memberMold;
	}
	/**
	 * @param memberMold the memberMold to set
	 */
	public void setMemberMold(FragmentMold memberMold) {
		this.memberMold = memberMold;
	}
	/**
	 * @return the memberNoLabelMold
	 */
	public FragmentMold getMemberNoLabelMold() {
		return memberNoLabelMold;
	}
	/**
	 * @param memberNoLabelMold the memberNoLabelMold to set
	 */
	public void setMemberNoLabelMold(FragmentMold memberNoLabelMold) {
		this.memberNoLabelMold = memberNoLabelMold;
	}
	/**
	 * @return the formGroupMold
	 */
	public FragmentMold getFormGroupMold() {
		return formGroupMold;
	}
	/**
	 * @param formGroupMold the formGroupMold to set
	 */
	public void setFormGroupMold(FragmentMold formGroupMold) {
		this.formGroupMold = formGroupMold;
	}
	/**
	 * @return the tableGroupMold
	 */
	public FragmentMold getTableGroupMold() {
		return tableGroupMold;
	}
	/**
	 * @param tableGroupMold the tableGroupMold to set
	 */
	public void setTableGroupMold(FragmentMold tableGroupMold) {
		this.tableGroupMold = tableGroupMold;
	}
	/**
	 * @return the tableMemberMold
	 */
	public FragmentMold getTableMemberMold() {
		return tableMemberMold;
	}

	/**
	 * @param tableMemberMold the tableMemberMold to set
	 */
	public void setTableMemberMold(FragmentMold tableMemberMold) {
		this.tableMemberMold = tableMemberMold;
	}
	/**
	 * @return the labelMold
	 */
	public FragmentMold getLabelMold() {
		return labelMold;
	}
	/**
	 * @param labelMold the labelMold to set
	 */
	public void setLabelMold(FragmentMold labelMold) {
		this.labelMold = labelMold;
	}
	/**
	 * @return the valueMold
	 */
	public FragmentMold getValueMold() {
		return valueMold;
	}
	/**
	 * @param valueMold the valueMold to set
	 */
	public void setValueMold(FragmentMold valueMold) {
		this.valueMold = valueMold;
	}
	/**
	 * @return the inputMold
	 */
	public FragmentMold getInputMold() {
		return inputMold;
	}
	/**
	 * @param inputMold the inputMold to set
	 */
	public void setInputMold(FragmentMold inputMold) {
		this.inputMold = inputMold;
	}
}
