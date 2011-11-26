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

package org.talframework.talui.mvc.config;

import java.util.Arrays;

import org.talframework.util.beans.BeanComparison;

/**
 * This class represents a page event. Page events
 * hold the details that turn a particular result
 * from the target window into a request to move to
 * a different page. There are several 'types' of
 * page event:
 * 
 * <p>First there is a standard page event that when
 * a result happens we go to a particular page. 
 * Optionally in this kind of event we can supply a 
 * window and an action on the target page to go to</p>
 * 
 * <p>Then there is the back event. Many pages are kind
 * of hidden and exist to be used at multiple points in
 * the app. If the user has finished on the page then
 * we want to go back to the previous page. The outer
 * container is responsible for holding onto the page we
 * came from.</p>
 * 
 * <p>Finally there is a select event. Very much like the
 * back event we may have selected something in this page
 * and want to wire back that element to the previous page.
 * </p>
 * 
 * <p>The type of page event is governed by the 'type' 
 * property which can have the values NORMAL, BACK or 
 * SELECT. All page events can pass any number of model
 * attributes as part of the call. Typically simple app
 * model events as always passed by the container.</p>
 * 
 * <p>The final parameters allow us to specify the select
 * window and action that a select event would be wired 
 * to. Also we can instruct the container to pass on
 * select and back details instead of generating them on
 * the current page. This allows us to 'chain' actions 
 * together.</p>
 * 
 * @author Tom Spencer
 */
public final class PageEventConfig {
	public static final short TYPE_NORMAL = 1;
	public static final short TYPE_BACK = 2;
	public static final short TYPE_SELECT = 3;

	/** Member holds the type of event */
	private final short type;
	/** The result that triggers this page event */
	private final String result;
	/** The page to go to */
	private final String page;
	
	/** The model attributes to pass to the target page */
	private final String[] modelAttributes;
	/** The window on that page to fire an action at (optional) */
	private final String window;
	/** The action on the above window to fire (optional) */
	private final String action;
	
	/** For normal events determines if we should pass on our back/select details */
	private boolean passThrough = false;
	/** For normal events hold the window on current page a future select event should target */
	private String selectWindow = null;
	/** For normal events holds the action on above window a future select event should target */
	private String selectAction = null;
	
	/**
	 * Constructs a simple normal event that wires through to 
	 * another page.
	 * 
	 * @param result The result that triggers this event
	 * @param page The page to go to
	 * @param modelAttributes The attributes to pass
	 */
	public PageEventConfig(String result, String page, String[] modelAttributes) {
		this.type = TYPE_NORMAL;
		this.result = result;
		this.page = page;
		this.modelAttributes = modelAttributes;
		this.window = null;
		this.action = null;
	}
	
	/**
	 * Constructs a simple normal event that wires through to 
	 * another page.
	 * 
	 * @param result The result that triggers this event
	 * @param page The page to go to
	 * @param modelAttributes The attributes to pass
	 * @param window The window on the target page to fire action into
	 * @param action The action to pass into the target page
	 */
	public PageEventConfig(String result, String page, String[] modelAttributes, String window, String action) {
		this.type = TYPE_NORMAL;
		this.result = result;
		this.page = page;
		this.modelAttributes = modelAttributes;
		this.window = window;
		this.action = action;
	}
	
	/**
	 * Constructs a 'back' page event that simply goes to the
	 * previous page upon a certain result. Effectively this is
	 * a cancel message. It is not valid to pass any params on
	 * a back event.
	 * 
	 * @param result The result to wire to
	 */
	public PageEventConfig(String result) {
		this.type = TYPE_BACK;
		this.result = result;
		this.page = null;
		this.modelAttributes = null;
		this.window = null;
		this.action = null;
	}
	
	/**
	 * Constructs a 'select' event that goes to the previous page
	 * upon a certain result passing a number of parameters.
	 * 
	 * @param result The result that triggers the event
	 * @param modelAttributes The attributes to pass back
	 */
	public PageEventConfig(String result, String[] modelAttributes) {
		this.type = TYPE_SELECT;
		this.result = result;
		this.page = null;
		this.modelAttributes = modelAttributes;
		this.window = null;
		this.action = null;
	}

	/**
	 * @return the passThrough
	 */
	public boolean isPassThrough() {
		return passThrough;
	}

	/**
	 * @param passThrough the passThrough to set
	 */
	public void setPassThrough(boolean passThrough) {
		if( type != TYPE_NORMAL ) throw new IllegalArgumentException("Cannot set pass through on a back or select event");
		this.passThrough = passThrough;
	}

	/**
	 * @return the selectWindow
	 */
	public String getSelectWindow() {
		return selectWindow;
	}

	/**
	 * @param selectWindow the selectWindow to set
	 */
	public void setSelectWindow(String selectWindow) {
		if( type != TYPE_NORMAL ) throw new IllegalArgumentException("Cannot set the select window on a back or select event");
		this.selectWindow = selectWindow;
	}

	/**
	 * @return the selectAction
	 */
	public String getSelectAction() {
		return selectAction;
	}

	/**
	 * @param selectAction the selectAction to set
	 */
	public void setSelectAction(String selectAction) {
		if( type != TYPE_NORMAL ) throw new IllegalArgumentException("Cannot set the select action on a back or select event");
		this.selectAction = selectAction;
	}

	/**
	 * @return the type
	 */
	public short getType() {
		return type;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @return the modelAttributes
	 */
	public String[] getModelAttributes() {
		return modelAttributes;
	}

	/**
	 * @return the window
	 */
	public String getWindow() {
		return window;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	

	///////////////////////////////////////////////////////
    // Standard

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PageEventConfig [page=" + page + ", result=" + result + ", type=" + type + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + Arrays.hashCode(modelAttributes);
        result = prime * result + ((page == null) ? 0 : page.hashCode());
        result = prime * result + (passThrough ? 1231 : 1237);
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
        result = prime * result + ((selectAction == null) ? 0 : selectAction.hashCode());
        result = prime * result + ((selectWindow == null) ? 0 : selectWindow.hashCode());
        result = prime * result + type;
        result = prime * result + ((window == null) ? 0 : window.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        PageEventConfig other = BeanComparison.basic(this, obj);
        boolean ret = other != null;
        if( ret && other != this ) {
            ret = BeanComparison.equals(ret, this.type, other.type);
            ret = BeanComparison.equals(ret, this.result, other.result);
            ret = BeanComparison.equals(ret, this.page, other.page);
            ret = BeanComparison.equals(ret, this.modelAttributes, other.modelAttributes);
            ret = BeanComparison.equals(ret, this.window, other.window);
            ret = BeanComparison.equals(ret, this.action, other.action);
            ret = BeanComparison.equals(ret, this.passThrough, other.passThrough);
            ret = BeanComparison.equals(ret, this.selectWindow, other.selectWindow);
            ret = BeanComparison.equals(ret, this.selectAction, other.selectAction);
        }
        
        return ret;
    }
}
