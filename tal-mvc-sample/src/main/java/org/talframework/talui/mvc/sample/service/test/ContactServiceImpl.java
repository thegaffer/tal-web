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

package org.talframework.talui.mvc.sample.service.test;


import org.talframework.talui.mvc.commons.repository.RepositoryHolder;
import org.talframework.talui.mvc.commons.repository.SimpleRepository;
import org.talframework.talui.mvc.commons.util.SimpleObjectCloner;
import org.talframework.talui.mvc.sample.model.account.Account;
import org.talframework.talui.mvc.sample.model.account.AccountBean;
import org.talframework.talui.mvc.sample.model.contact.Caller;
import org.talframework.talui.mvc.sample.model.contact.Contact;
import org.talframework.talui.mvc.sample.model.contact.ContactBean;
import org.talframework.talui.mvc.sample.service.ContactService;
import org.talframework.talui.mvc.sample.service.transfer.SaveContactResult;
import org.tpspencer.tal.util.aspects.annotations.Trace;

/**
 * 
 * @author Tom Spencer
 */
@Trace
public class ContactServiceImpl extends RepositoryHolder implements ContactService {

	/** Holds the account repository */
	private SimpleRepository accountRepository = null;
	
	/**
	 * Simply adds or updates the contact in the repository
	 */
	public SaveContactResult saveContact(Contact contact) {
		ContactBean bean = findOrCreateContact(contact.getId() != null ? contact.getId().toString() : null);
		if( !bean.equals(contact) ) SimpleObjectCloner.getInstance().clone(contact, bean, "id");
		return new SaveContactResult(bean.getId(), bean);
	}
	
	/**
	 * Obtains (or creates) contact and updates it with account info
	 */
	public SaveContactResult updateContactAccount(String contactId, Account account) {
		ContactBean bean = findOrCreateContact(contactId);
		
		// Update
		bean.setAccount(account.getAccountNos());
		bean.setCompany(account.getCompany());
		bean.setAddress(account.getAddress());
		
		return new SaveContactResult(bean.getId(), bean);
	}
	
	/**
	 * Obtains (or creates) contact and updates it with caller info
	 */
	public SaveContactResult updateContactCaller(String contactId, Caller caller) {
		ContactBean bean = findOrCreateContact(contactId);
		
		// Update
		bean.setFirstName(caller.getFirstName());
		bean.setLastName(caller.getLastName());
		bean.setAccount(caller.getAccount());
		if( caller.getAccount() != null ) {
			Account acc = accountRepository.findById(caller.getAccount(), AccountBean.class);
			if( acc != null ) {
				bean.setCompany(acc.getCompany());
				bean.setAddress(acc.getAddress());
			}
		}
		
		return new SaveContactResult(bean.getId(), bean);
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
	
	private ContactBean findOrCreateContact(String id) {
		if( id == null ) return getRepository().create(ContactBean.class);
		else return getRepository().findById(id, ContactBean.class);
	}
}
