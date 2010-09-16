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

package org.tpspencer.tal.mvc.commons.views.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tpspencer.tal.mvc.commons.views.AbstractTemplateView;
import org.tpspencer.tal.template.TemplateElement;
import org.tpspencer.tal.template.core.BasicTemplateConfiguration;

/**
 * This class represents a table view in a window. It will 
 * show a table containing the same item inside it, a
 * configurable set of row actions and a similar set of
 * table actions that apply to the whole table. 
 * 
 * @author Tom Spencer
 */
public class TableView extends AbstractTemplateView {

	/** Headings to use on table if not supplying template/config */
	private String[] tableHeadings = null;
	/** Name of the ID attribute submitted against row - id by default */
	private String idAttribute = "id";
	/** Expression that evaluates the id of each row - ${this.id} by default */
	private String idExpression = "${this.id}";
	/** List of actions to apply to rows if not supplying template/config */
	private List<TableAction> rowActions = null;
	/** List of actions to add at the end of table if not supplying template/config */
	private List<TableAction> tableActions = null;
	/** Determines if we should add paging actions if not supplying template/config */
	private boolean showPaging = false;
	
	public void init() {
		if( getViewBeanName() == null ) setViewBeanName("results");
		
		// Clone the row row actions
		if( rowActions != null ) {
			if( idAttribute == null ) throw new IllegalArgumentException("A table view must contain the name of the ID attribute used in row actions");
			if( idExpression == null ) throw new IllegalArgumentException("A table view must contain the name of the ID expression used in row actions");
			
			List<TableAction> newActions = new ArrayList<TableAction>();
			Iterator<TableAction> it = rowActions.iterator();
			while( it.hasNext() ) {
				newActions.add(it.next().clone(this, true));
			}
			rowActions = newActions;
		}
		
		// Clone to table actions
		if( tableActions != null ) {
			List<TableAction> newActions = new ArrayList<TableAction>();
			Iterator<TableAction> it = tableActions.iterator();
			while( it.hasNext() ) {
				newActions.add(it.next().clone(this, false));
			}
			tableActions = newActions;
		}
		
		super.init();
	}
	
	/**
	 * Overrides to add Results to the normal default
	 * view name (the primary beans simple name)
	 */
	@Override
	protected String getDefaultViewName() {
		String ret = super.getDefaultViewName();
		if( ret != null ) ret += "Results";
		return ret;
	}
	
	/**
	 * Adds on the main table template
	 */
	@Override
	protected void preInitConfig(BasicTemplateConfiguration config) {
		config.addTableTemplate(
				getViewName(), 
				getViewBeanName(),
				tableHeadings, 
				getPrimaryBean().getSimpleName(), 
				rowActions != null ? new ArrayList<TemplateElement>(rowActions) : null, 
				tableActions != null ? new ArrayList<TemplateElement>(tableActions) : null, 
				showPaging);
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("TableView: ");
		buf.append("name=").append(getViewName());
		buf.append("results=").append(getViewBeanName());
		buf.append(", bean=").append(getPrimaryBean());
		
		return buf.toString();
	}
	
	
	/////////////////////////////////////////////////
	// Getters / Setters
	
	/**
	 * @return the tableHeadings
	 */
	public String[] getTableHeadings() {
		return tableHeadings;
	}

	/**
	 * @param tableHeadings the tableHeadings to set
	 */
	public void setTableHeadings(String[] tableHeadings) {
		this.tableHeadings = tableHeadings;
	}

	/**
	 * @return the rowActions
	 */
	public List<TableAction> getRowActions() {
		return rowActions;
	}

	/**
	 * @param rowActions the rowActions to set
	 */
	public void setRowActions(List<TableAction> rowActions) {
		this.rowActions = rowActions;
	}

	/**
	 * @return the tableActions
	 */
	public List<TableAction> getTableActions() {
		return tableActions;
	}

	/**
	 * @param tableActions the tableActions to set
	 */
	public void setTableActions(List<TableAction> tableActions) {
		this.tableActions = tableActions;
	}

	/**
	 * @return the showPaging
	 */
	public boolean isShowPaging() {
		return showPaging;
	}

	/**
	 * @param showPaging the showPaging to set
	 */
	public void setShowPaging(boolean showPaging) {
		this.showPaging = showPaging;
	}

	/**
	 * @return the idAttribute
	 */
	public String getIdAttribute() {
		return idAttribute;
	}

	/**
	 * @param idAttribute the idAttribute to set
	 */
	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	/**
	 * @return the idExpression
	 */
	public String getIdExpression() {
		return idExpression;
	}

	/**
	 * @param idExpression the idExpression to set
	 */
	public void setIdExpression(String idExpression) {
		this.idExpression = idExpression;
	}
	
}
