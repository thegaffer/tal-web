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

package org.talframework.talui.mvc;

import java.util.List;

import org.talframework.talui.mvc.config.EventConfig;
import org.talframework.talui.mvc.input.InputModel;
import org.talframework.talui.mvc.model.ModelConfiguration;


/**
 * This interface represents a window in the UI. A window typically
 * occupies some real estate on a page and provides the view to 
 * the user. A window might have one or more states, each state 
 * being a different view. The window is also responsible for 
 * dispatching any actions the users perform onto an action class
 * and determining which state the window is in.
 * 
 * <p>This interface does not represent anything inside the
 * MVC pattern. A web page might have one or more 'windows' and
 * these delegate users requests to the appropriate action 
 * (or controller in MVC terms) and renders itself through the
 * states (or views in MVC terms).</p>
 * 
 * <p>As with most interfaces in this model there is no need
 * to use this interface directly via the use of well formed
 * names or via annotations. See the online documentation for
 * more information.</p>
 * 
 * @author Tom Spencer
 */
public interface Window {
	
	/**
	 * @return The model for the window itself
	 */
	public ModelConfiguration getModel();
	
	/**
	 * @return The events for the window
	 */
	public List<EventConfig> getEvents();

	/**
	 * Called when an action has been performed on the window.
	 * The window should process the action and adjust its
	 * state accordingly.
	 * 
	 * @param The current model
	 * @param input The raw input
	 * @param action The action that has been performed
	 * @return The result of the action (passed to view if we exit)
	 */
	public String processAction(Model model, InputModel input, String action);
	
	/**
	 * Called to get hold of the state the window is currently
	 * in.
	 * 
	 * @return The current state
	 */
	public View getCurrentState(Model model);
	
}
