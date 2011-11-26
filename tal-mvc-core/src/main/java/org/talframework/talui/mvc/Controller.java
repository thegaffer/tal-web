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

import java.util.Map;

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
	
	/**
	 * This simple object represents the input from a user
	 * (directly or indirectly) into a request. Typically 
	 * this is the request parameters, but they are placed
	 * inside this class to provide easier access.
	 * 
	 * @author Tom Spencer
	 */
	public static interface InputModel {

	    /**
	     * Call to get a parameter as a string. This mimics
	     * request.getParameter in a web model. If the parameter
	     * is not a string, but exists it is converted to a
	     * string by the implementation of this interface (the
	     * simplest mechanism here is toString()). 
	     * 
	     * @param name The name of the parameter
	     * @return The parameters value or its first value if its a String[]
	     */
	    public String getParameter(String name);
	    
	    /**
	     * Call to get a parameter in its natural form. In
	     * the input is coming direct from a web request then
	     * the return type will be a String[], otherwise its
	     * natural type.
	     * 
	     * @param name The name of the parameter
	     * @return The parameters value
	     */
	    public Object getParameterObject(String name);
	    
	    /**
	     * This method is an alternative for getParameterObject
	     * when dealing with a request from a browser. It will
	     * throw an unsupported exception otherwise. This method
	     * is equivilent to request.getParamterValues(param).
	     * 
	     * @param name The name of the parameter
	     * @return The values of the parameter (may be null)
	     */
	    public String[] getParameterValues(String name);
	    
	    /**
	     * Call to get all the input parameters in a map. 
	     * These are returned as a map of whatever the
	     * attributes are. In a web request these will
	     * always be String[] instances.
	     * 
	     * @return The input parameters in a map
	     */
	    public Map<String, ?> getParameters();
	    
	    /**
	     * Call to get all input parameters that begin with
	     * the given prefix into the a new map.
	     * 
	     * @param prefix The prefix
	     * @return The parameters with that prefix
	     */
	    public Map<String, ?> getParameters(String prefix);
	    
	    /**
	     * Helper method to determine if the given parameter
	     * is present
	     * 
	     * @param name The name of the parameter
	     * @return True if it exists, false otherwise
	     */
	    public boolean hasParameter(String name);
	    
	    /**
	     * Helper method to determine if a given parameter
	     * has multi values.
	     * 
	     * @param name The name of the parameter
	     * @return True if the parameter has more than one value
	     */
	    public boolean hasMultiValue(String name);
	}
}
