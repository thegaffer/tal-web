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

/**
 * This interface represents a view that is presented to
 * the user. It is the V in the MVC pattern. It can also
 * be thought as a resting state for the application or
 * a part of it. To move from one view to another a user
 * must do something that invokes a controller which in
 * turn updates the state or view. 
 * 
 * <p>More specifically a view sits inside a window. A 
 * window might have one or more views inside it. The 
 * view has the following jobs to perform:</p>
 * <ul>
 * <li>To provide any initialisation when we enter the 
 * view in the action phase.</li>
 * <li>To provide any cleanup when we leave the state
 * in the action phase.</li>
 * <li>To return the parts of the model that are required
 * by the views template (if there is a template).</li>
 * <li>Or as an alternative to above to provide the 
 * output for the view (providing a template is the
 * recommended mechanism!)</li>
 * </ul>
 * 
 * <p>Additionally a view can express its parent view.
 * This is useful if views are being chained. When a 
 * view has a parent view it can only be entered if the
 * current view is the parent view. When a child view is
 * entered the parent view is not cleaned up until we
 * leave the child view (and only then if not returning
 * to the parent view.</p>
 * 
 * <p>Also note that this interface does not need to be
 * used and annotations or well-formed objects can be 
 * used instead. See the online documentation for more 
 * information.</p>
 * 
 * @author Tom Spencer
 */
public interface View {

	/**
	 * Called when this view is entered.
	 * 
	 * @param model The current model
	 */
	public void enterView(Model model);
	
	/**
	 * Called when we leave this view. This is called
	 * before enterState is called on the newState. It
	 * is not called if leaving this view to enter a
	 * child view.
	 * 
	 * @param model The main model that will exist after this call
	 * @param viewModel The model for this view
	 * @param result The action or result that we left this view under
	 */
	public void exitView(Model model, Model viewModel, String result);
	
	/**
	 * Called to prepare the view for the render. There
	 * are basically two ways to answer this question ...
	 * 
	 * <p>The preferred method is to add relevant model
	 * attributes into the renderModel and provide the
	 * name of a template. The outer container will then
	 * forward the request onto that template providing
	 * all the render model attributes to it.</p>
	 * 
	 * <p>The second method is to access the namespace
	 * and the writer from the renderModel and render
	 * directly. This is not preferred because it 
	 * basically ties your view down to a single 
	 * presentation, rather than forward the request on
	 * to the relevant template (HTML, XML, WML etc) 
	 * based on the client. But in simple cases where
	 * it is just one technology this is ok.</p>
	 * 
	 * <p<b>Note: </b>If you do use the template approach
	 * the actual 'model' is provided to the template
	 * under the name 'model'. If the template wants direct
	 * access to the attributes then the view should copy
	 * those elements into the render model.</p> 
	 * 
	 * @param renderModel The render model to set up for the template
	 * @param model The current model
	 */
	public void prepareRender(RenderModel renderModel, Model model);
	
	/**
	 * This interface represents the model used during
	 * render. The render model is passed into the views
	 * to allow them to set up all the model elements 
	 * they require during render. 
	 * 
	 * <p>The recommended approach is for a view to setup
	 * the render model with attributes and then set the
	 * name of the template to forward to. This allows the
	 * container to use the appropriate template for its
	 * client. i.e. it could use a template for HTML, 
	 * another for WML and another for XML etc, etc. 
	 * The view itself would remain constant.</p>
	 * 
	 * <p>However, it is possible to render directly 
	 * becuase a writer may be provided. I say *may* 
	 * because non J2EE Web based approaches may not
	 * provide the writer. In addition to the writer the
	 * namespace is provided so it can be used in the 
	 * output.</p>
	 *  
	 * @author Tom Spencer
	 */
	public static interface RenderModel {
	    
	    /**
	     * @return The view template if set by the view
	     */
	    public String getTemplate();
	    
	    /**
	     * Call to set the template the container should
	     * delegate to. If not provided it is assumed the
	     * view has rendered directly into the writer.
	     * 
	     * @param template The template to use
	     */
	    public void setTemplate(String template);

	    /**
	     * Call to get a render attribute
	     * 
	     * @param name The name of the attribute
	     * @return The attribute
	     */
	    public Object getAttribute(String name);
	    
	    /**
	     * Call to set a render attribute
	     * 
	     * @param name The name of the attribute
	     * @param attr Its value
	     */
	    public void setAttribute(String name, Object attr);
	    
	    /**
	     * Call to remove a render attribute
	     * 
	     * @param name The name of the attribute.
	     */
	    public void removeAttribute(String name);
	}
}
