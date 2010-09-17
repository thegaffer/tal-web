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

package org.talframework.talui.mvc.sample.service;

import org.talframework.talui.mvc.sample.model.account.Account;
import org.talframework.talui.mvc.sample.model.contact.Caller;
import org.talframework.talui.mvc.sample.model.contact.Contact;
import org.talframework.talui.mvc.sample.service.transfer.SaveContactResult;

/**
 * This interface represents the contact service that
 * provides elements to create and update contacts.
 * 
 * @author Tom Spencer
 */
public interface ContactService {
	
	/**
	 * Call to create or update the contact
	 * 
	 * @param contact The contact to create or update
	 * @return The contact
	 */
	public SaveContactResult saveContact(Contact contact);

	/**
	 * Updates the contact with given account details.
	 * 
	 * @param id The ID of the contact to update (or null if we should create)
	 * @param account The account to update with
	 */
	public SaveContactResult updateContactAccount(String contactId, Account account);
	
	/**
	 * Updates the contact with given caller details.
	 * 
	 * @param id The ID of the contact to update (or null if we should create)
	 * @param caller The caller to update with
	 */
	public SaveContactResult updateContactCaller(String contactId, Caller caller);
}
