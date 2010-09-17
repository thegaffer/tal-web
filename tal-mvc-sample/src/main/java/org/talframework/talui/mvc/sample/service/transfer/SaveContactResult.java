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

package org.talframework.talui.mvc.sample.service.transfer;

import org.talframework.talui.mvc.sample.model.contact.Contact;

/**
 * This simple class is used to return the result of
 * saving or updating a contact from the contact service
 * 
 * @author Tom Spencer
 */
public final class SaveContactResult {

	/** Holds the ID of the contact */
	private final Object contactId;
	/** Holds the Contact itself */
	private final Contact contact;
	
	public SaveContactResult(Object contactId, Contact contact) {
		this.contactId = contactId;
		this.contact = contact;
	}

	/**
	 * @return the contactId
	 */
	public Object getContactId() {
		return contactId;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}
}
