package org.tpspencer.tal.mvc.sample.model.contact;

import org.tpspencer.tal.mvc.sample.model.common.Address;

public interface Contact {

	/**
	 * @return the id
	 */
	public abstract Object getId();

	/**
	 * @return the firstName
	 */
	public abstract String getFirstName();

	/**
	 * @param firstName the firstName to set
	 */
	public abstract void setFirstName(String firstName);

	/**
	 * @return the lastName
	 */
	public abstract String getLastName();

	/**
	 * @param lastName the lastName to set
	 */
	public abstract void setLastName(String lastName);

	/**
	 * @return the account
	 */
	public abstract String getAccount();

	/**
	 * @param account the account to set
	 */
	public abstract void setAccount(String account);

	/**
	 * @return the company
	 */
	public abstract String getCompany();

	/**
	 * @param company the company to set
	 */
	public abstract void setCompany(String company);

	/**
	 * @return the address
	 */
	public abstract Address getAddress();

	/**
	 * @param address the address to set
	 */
	public abstract void setAddress(Address address);

	/**
	 * @return the previousCrn
	 */
	public abstract String getPreviousCrn();

	/**
	 * @param previousCrn the previousCrn to set
	 */
	public abstract void setPreviousCrn(String previousCrn);

}