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

package org.tpspencer.tal.mvc.sample.objex.service;


import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.locator.ContainerFactory;
import org.tpspencer.tal.mvc.commons.repository.SimpleRepository;
import org.tpspencer.tal.mvc.sample.model.account.Account;
import org.tpspencer.tal.mvc.sample.model.account.AccountBean;
import org.tpspencer.tal.mvc.sample.model.contact.Caller;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.mvc.sample.service.ContactService;
import org.tpspencer.tal.mvc.sample.service.transfer.SaveContactResult;
import org.tpspencer.tal.util.aspects.annotations.Trace;

/**
 * 
 * @author Tom Spencer
 */
@Trace
public class ContactServiceImpl implements ContactService {

	/** Holds the contact locator */
	private ContainerFactory factory = null;
	/** Holds the account repository */
	private SimpleRepository accountRepository = null;
	
	/**
	 * Simply adds or updates the contact in the repository
	 */
	public SaveContactResult saveContact(Contact contact) {
		if( !(contact instanceof ObjexObj) ) throw new IllegalArgumentException("Cannot save contact as provided contact is not from a container");
		// SUGGEST: Could use id to open the container (or create!?!)
		
		Container c = ((ObjexObj)contact).getContainer();
		if( !c.isOpen() ) throw new IllegalArgumentException("Cannot save contact as not in a open transaction");
		
		c.saveContainer();
		return new SaveContactResult(c.getId(), contact);
	}
	
	/**
	 * Obtains (or creates) contact and updates it with account info
	 */
	public SaveContactResult updateContactAccount(String contactId, Account account) {
		Contact bean = findOrCreateContact(contactId);
		
		// Update
		bean.setAccount(account.getAccountNos());
		bean.setCompany(account.getCompany());
		bean.setAddress(account.getAddress());
		
		return saveContact(bean);
	}
	
	/**
	 * Obtains (or creates) contact and updates it with caller info
	 */
	public SaveContactResult updateContactCaller(String contactId, Caller caller) {
		Contact bean = findOrCreateContact(contactId);
		
		// Update
		bean.setFirstName(caller.getFirstName());
		bean.setLastName(caller.getLastName());
		bean.setAccount(caller.getAccount());
		if( caller.getAccount() != null ) {
			// TODO: Replace with account container!!
			Account acc = accountRepository.findById(caller.getAccount(), AccountBean.class);
			if( acc != null ) {
				bean.setCompany(acc.getCompany());
				bean.setAddress(acc.getAddress());
			}
		}
		
		return saveContact(bean);
	}
	
	/**
	 * Helper method to either get or create the contact object.
	 * 
	 * @param contactId The ID of the current contact (or null if we need to create one)
	 * @return The contact
	 */
	private Contact findOrCreateContact(String contactId) {
		Container container = null;
		if( contactId != null ) container = factory.open(contactId);
		else container = factory.create();
		
		// Get the root bean, which we know is a contact
		Contact bean = container.getRootObject().getBehaviour(Contact.class);
		return bean;
	}

	/**
	 * @return the accountRepository
	 */
	public SimpleRepository getAccountRepository() {
		return accountRepository;
	}

	/**
	 * @param accountRepository the accountRepository to set
	 */
	public void setAccountRepository(SimpleRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	/**
	 * @return the factory
	 */
	public ContainerFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(ContainerFactory factory) {
		this.factory = factory;
	}
	
}
