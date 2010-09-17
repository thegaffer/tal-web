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

package org.talframework.talui.mvc.commons.views.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talframework.talui.template.RenderModel;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.CommandElement;
import org.talframework.talui.template.behaviour.supporting.ResourceProperty;

/**
 * Represents an individual table action
 * 
 * @author Tom Spencer
 */
public class TableAction implements CommandElement, ResourceProperty {

	/** Holds the view that holds this action */
	private final TableView view;
	/** Determines if action is for a row */
	private final boolean rowAction;
	
	/** Holds the name of the action */
	private String action = null;
	/** Holds the message resource to use - default is action.[action] */
	private String message = null;
	
	/**
	 * Default public constructor
	 */
	public TableAction() {
		this.view = null;
		this.rowAction = false;
	}
	
	public TableAction(String action, String message) {
		this.view = null;
		this.rowAction = false;
		this.action = action;
		this.message = message;
	}
	
	/**
	 * Protected constructor used to assign view to action
	 * 
	 * @param view The view action belongs to
	 * @param rowAction If true then the view considers this a row action
	 * @param action The action itself
	 */
	protected TableAction(TableView view, boolean rowAction, TableAction action) {
		this.view = view;
		this.rowAction = rowAction;
		this.action = action.getAction();
		this.message = action.getMessage();
	}
	
	/**
	 * Init does nothing
	 */
	public void init(Template template, List<TemplateElement> children) {
		// No-op
	}
	
	/**
	 * Returns the action as this elements name
	 */
	public String getName() {
		return action;
	}
	
	/**
	 * Always returns null, no properties possible
	 */
	public Map<String, String> getPropertySet(String name) {
		return null;
	}
	
	/**
	 * No dynamic behaviours used so return what we implement
	 */
	public <T> T getBehaviour(Class<T> behaviour) {
		if( behaviour.isInstance(this) ) return behaviour.cast(this);
		return null;
	}
	
	/**
	 * Always returns false - no settings
	 */
	public boolean hasSetting(String name) {
		return false;
	}
	
	/**
	 * Always returns null, no settings possible
	 */
	public <T> T getSetting(String name, Class<T> expected) {
		return null;
	}
	
	/**
	 * Returns table-action
	 */
	public String getType() {
		if( rowAction ) return "row-action";
		return "table-action";
	}
	
	/**
	 * Helper to clone the TableAction in terms of the action
	 * name, but to assign it to a particular {@link TableView} 
	 * 
	 * @param view The view to assign new action to
	 * @return The new table action
	 */
	protected TableAction clone(TableView view, boolean rowAction) {
		return new TableAction(view, rowAction, this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.talframework.talui.template.behaviour.ResourceProperty#getResource()
	 */
	public String getResource() {
		if( message == null ) return "label.action." + getName().toLowerCase();
		else return message;
	}

	/*
	 * (non-Javadoc)
	 * @see org.talframework.talui.template.behaviour.CommandElement#getAction(org.talframework.talui.template.RenderModel)
	 */
	public String getAction(RenderModel model) {
		return action;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.talframework.talui.template.behaviour.CommandElement#getActionParameters(org.talframework.talui.template.RenderModel)
	 */
	public Map<String, String> getActionParameters(RenderModel model) {
		// Add on ID of row if this is a row
		if( rowAction ) {
			if( model.getCurrentNode() != null ) {
				Object id = model.evaluateExpression(view.getIdExpression());
				
				if( id != null ) {
					Map<String, String> params = new HashMap<String, String>();
					params.put(view.getIdAttribute(), id.toString());
					return params;
				}
			}
		}
		
		return null;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the view
	 */
	public TableView getView() {
		return view;
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
	 * @return the rowAction
	 */
	public boolean isRowAction() {
		return rowAction;
	}
}
