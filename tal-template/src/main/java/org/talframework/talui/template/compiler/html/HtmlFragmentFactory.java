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

/**
 * This interface represents the class that creates all the 
 * default fragments. A default implementation of this interface
 * using the singleton pattern exists which can either be
 * overridden completely, or different fragments can be injected
 * in.
 * 
 * @author Tom Spencer
 */
public interface HtmlFragmentFactory {

	/**
	 * @return the wrapperMold
	 */
	public FragmentMold getWrapperMold();
	
	/**
	 * @param wrapperMold the wrapperMold to set
	 */
	public void setWrapperMold(FragmentMold wrapperMold);
	
	/**
	 * @return The inner template mold
	 */
	public FragmentMold getTemplateMold();
	
	/**
	 * @param templateMold The new template mold
	 */
	public void setTemplateMold(FragmentMold templateMold);
	
	/**
	 * @return the cellWrapperMold
	 */
	public FragmentMold getCellWrapperMold();
	
	/**
	 * @param cellWrapperMold the cellWrapperMold to set
	 */
	public void setCellWrapperMold(FragmentMold cellWrapperMold);
	
	/**
	 * @return the spanWrapperMold
	 */
	public FragmentMold getSpanWrapperMold();
	
	/**
	 * @param spanWrapperMold the spanWrapperMold to set
	 */
	public void setSpanWrapperMold(FragmentMold spanWrapperMold);
	
	/**
	 * @return the linkWrapperMold
	 */
	public FragmentMold getLinkWrapperMold();
	
	/**
	 * @param linkWrapperMold the linkWrapperMold to set
	 */
	public void setLinkWrapperMold(FragmentMold linkWrapperMold);
	
	/**
	 * @return the childrenMold
	 */
	public FragmentMold getChildrenMold();
	
	/**
	 * @param childrenMold the childrenMold to set
	 */
	public void setChildrenMold(FragmentMold childrenMold);
	
	/**
	 * @return the memberMold
	 */
	public FragmentMold getMemberMold();
	
	/**
	 * @param memberMold the memberMold to set
	 */
	public void setMemberMold(FragmentMold memberMold);
	
	/**
	 * @return the memberNoLabelMold
	 */
	public FragmentMold getMemberNoLabelMold();
	
	/**
	 * @param memberNoLabelMold the memberNoLabelMold to set
	 */
	public void setMemberNoLabelMold(FragmentMold memberNoLabelMold);
	
	/**
	 * @return the formGroupMold
	 */
	public FragmentMold getFormGroupMold();
	
	/**
	 * @param formGroupMold the formGroupMold to set
	 */
	public void setFormGroupMold(FragmentMold formGroupMold);
	
	/**
	 * @return the tableGroupMold
	 */
	public FragmentMold getTableGroupMold();
	
	/**
	 * @param tableGroupMold the tableGroupMold to set
	 */
	public void setTableGroupMold(FragmentMold tableGroupMold);
	
	/**
	 * @return The table member mold
	 */
	public FragmentMold getTableMemberMold();

	/**
	 * @param tableMemberMold the tableMemberMold to set
	 */
	public void setTableMemberMold(FragmentMold tableMemberMold);
	
	/**
	 * @return the labelMold
	 */
	public FragmentMold getLabelMold();
	
	/**
	 * @param labelMold the labelMold to set
	 */
	public void setLabelMold(FragmentMold labelMold);
	
	/**
	 * @return the valueMold
	 */
	public FragmentMold getValueMold();
	
	/**
	 * @param valueMold the valueMold to set
	 */
	public void setValueMold(FragmentMold valueMold);
	
	/**
	 * @return the inputMold
	 */
	public FragmentMold getInputMold();
	
	/**
	 * @param inputMold the inputMold to set
	 */
	public void setInputMold(FragmentMold inputMold);
}
