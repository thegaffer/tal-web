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

package org.tpspencer.tal.mvc.render;


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
public interface RenderModel {
	
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
