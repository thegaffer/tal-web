package org.tpspencer.tal.mvc.sample.service.transfer;

import org.tpspencer.tal.mvc.sample.model.contact.Contact;

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
