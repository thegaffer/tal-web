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

package org.talframework.talui.mvc.model.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.model.ModelAttribute;

/**
 * This special model attribute class represents the
 * model attribute that holds any errors, warnings or
 * messages. This attribute will be a list of 
 * {@link ClientMessage} instances.
 * 
 * <p>Because a messages model attribute is always set
 * to clear on any new action this attribute will add
 * any new messages to a list of existing ones.</p>
 * 
 * FUTURE: Possible better to hold a map of a map instead of map of list (key 2nd map on code)
 * 
 * @author Tom Spencer
 */
public class MessagesModelAttribute implements ModelAttribute {
	
	private final String name;
	private boolean clearOnRender = false;

	public MessagesModelAttribute(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * {@link MessagesModelAttribute} never has aliases
	 */
	public String[] getAliases() {
		return null;
	}
	
	/**
	 * Always returns null, value is whatever is stored
	 */
	public Object getValue(Model model) {
		return null;
	}
	
	/**
	 * Adds the new value to the existing map of messages 
	 * (creates a new map if one does not exist).
	 */
	@SuppressWarnings("unchecked")
	public Object setValue(Model model, Object currentValue, Object newValue) {
		if( newValue == null ) return currentValue;

		// Get or create map of messages
		Map<String, List<ClientMessage>> ret = null;
		if( currentValue != null ) ret = (Map<String, List<ClientMessage>>)currentValue;
		else ret = new HashMap<String, List<ClientMessage>>();
		
		// Add this new one
		addNewValue(ret, newValue);
		return ret;
	}
	
	/**
	 * Helper to add a value to the map of messages
	 * 
	 * @param messages The messages
	 * @param newValue The new value
	 */
	@SuppressWarnings("unchecked")
	private void addNewValue(Map<String, List<ClientMessage>> messages, Object newValue) {
		if( newValue instanceof ClientMessage ) addMessage(messages, (ClientMessage)newValue);
		else if( newValue instanceof List ) {
			List<Object> lst = (List<Object>)newValue;
			Iterator<Object> it = lst.iterator();
			while( it.hasNext() ) {
				addNewValue(messages, it.next());
			}
		}
		else if( newValue instanceof String[] ) {
			String[] arr = (String[])newValue;
			
			// String array with field, code and extra params
			if( arr.length > 2 ) {
				int ln = arr.length - 2;
				Object[] params = new Object[ln];
				System.arraycopy(arr, 2, params, 0, ln);
				addMessage(messages, new ClientMessageImpl(arr[0], arr[1], params));
			}
			// String array with field and code
			else if( arr.length > 1 ) addMessage(messages, new ClientMessageImpl(arr[0], arr[1]));
			// String array with code only
			else if( arr.length > 0 ) addMessage(messages, new ClientMessageImpl(arr[0]));
			// Invalid string array message
			else throw new IllegalArgumentException("Attempt to add a message as a String[] to [" + this.name + "] which has no elements in it. At least 1 is required for the message code");
		}
		else if( newValue instanceof String ) {
			addMessage(messages, new ClientMessageImpl(newValue.toString()));
		}
		else {
			throw new IllegalArgumentException("Attempt to add messages to the [" + this.name + "] attribute that are not ClientMessage instances: " + newValue);
		}
	}
	
	/**
	 * Helper to add the message to the map
	 * 
	 * @param messages The map of all messages
	 * @param message The message to add
	 */
	private void addMessage(Map<String, List<ClientMessage>> messages, ClientMessage message) {
		List<ClientMessage> lst = messages.get(message.getField());
		if( lst == null ) lst = new ArrayList<ClientMessage>();
		lst.add(message);
		messages.put(message.getField(), lst);
	}
	
	/**
	 * Always return null
	 */
	public Object getDefaultValue(Model model) {
		return null;
	}
	
	/**
	 * The messages are always held in a map
	 */
	public Class<?> getType() {
		return Map.class;
	}
	
	/**
	 * Messages are not simply attributes
	 */
	public boolean isSimple() {
		return false;
	}
	
	/**
	 * Messages are not cleared after action
	 */
	public boolean isFlash() {
		return false;
	}
	
	/**
	 * Messages are never resolved
	 */
	public boolean isResolved() {
		return false;
	}
	
	/**
	 * As never resolver not nestable
	 */
	public boolean isResolverNestable() {
		return false;
	}
	
	/**
	 * Messages are always cleared with a new action
	 */
	public boolean isClearOnAction() {
		return true;
	}

	/**
	 * Messages not eventable
	 */
	public boolean isEventable() {
		return false;
	}
	
	/**
	 * Alias neither expected nor allowed - always applied to window (or view)
	 */
	public boolean isAliasable() {
		return false;
	}

	/**
	 * Alias neither expected nor allowed - always applied to window (or view)
	 */
	public boolean isAliasExpected() {
		return false;
	}
	
	/**
	 * Messages should always be added to render model
	 */
	public boolean isAutoRenderAttribute() {
		return true;
	}
	
	/**
	 * Cleared after first view
	 */
	public boolean isClearOnRender() {
		return clearOnRender;
	}
}
