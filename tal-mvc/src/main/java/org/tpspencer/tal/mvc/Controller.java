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

package org.tpspencer.tal.mvc;

import org.tpspencer.tal.mvc.input.InputModel;


/**
 * This interface represents a class that 'controls' or performs
 * a users request. This is the C in the MVC pattern. It can
 * also be thought of as an action. The interface has a sole
 * job of performing that action and then returning the result
 * of the action as a string.
 * 
 * <p>It is important to understand that this interface has a 
 * sole responsibility. It is not this guys job to know anything
 * about the view we are in, or the view we should go to. This
 * guy should simple act on the users request, updating the model
 * as appropriate.</p>
 * 
 * <p>I've thought long and hard about whether to even call this
 * interface a Controller because the term is in such widespread
 * use outside of the MVC pattern, i.e. Front Controller. It is
 * also widely used incorrectly when referring to the MVC pattern.
 * I've decided in the end to keep with the term, but to try and
 * point out what it should and should not do.</p>
 * 
 * <p>Note: there is not an absolute need to use this interface.
 * An action could be any class with a method that at a minimum
 * takes a map. See the documentation for more information.</p>
 * 
 * @author Tom Spencer
 */
public interface Controller {

	/**
	 * When called the action performs its relevant action based
	 * on the parameters passed in and the current model. The 
	 * action may update or change the model.
	 * 
	 * <p>The action should return a string representing the result
	 * of the action. This should <b>not</b> be the name of a state
	 * to move to - actions should not be aware of previous/next 
	 * states (controllers should be independent of views in MVC
	 * terms).</p>
	 * 
	 * @param model The current model
	 * @param input The input model holding the parameters
	 * @return The result of the action
	 */
	public String performAction(Model model, InputModel input);
	
}
