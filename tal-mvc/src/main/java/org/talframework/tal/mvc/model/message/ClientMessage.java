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

package org.tpspencer.tal.mvc.model.message;

import org.tpspencer.tal.mvc.Model;

/**
 * This interface represents a message to return back to
 * the client as a result of processing an action. It 
 * could indicate an error, a warning or simply a 
 * confirmation back.
 * 
 * <p>Typically use of this interface can be transparent.
 * Usually you would setup in your model a collection
 * attributes called messages or errors. You then add
 * errors (one or a collection at a time) to this model
 * attribute using the {@link Model} addAttribute feature.
 * The model attribute will convert what you add into a
 * {@link ClientMessage} instance. During render you can
 * then iterate around the client messages.</p>
 * 
 * <p>Use of this interface is not mandatory when using
 * the MVC framework. Indeed if you chose to use a 
 * framework such as Spring then spring errors could be
 * used.</p>
 * 
 * @author Tom Spencer
 */
public interface ClientMessage {

	/**
	 * @return The field or object the message is attached to (if any) 
	 */
	public String getField();
	
	/**
	 * @return The message resource code for the message
	 */
	public String getCode();
	
	/**
	 * @return Any parameters stored with the message
	 */
	public Object[] getParams();
}
