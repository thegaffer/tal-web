package org.tpspencer.tal.mvc.sample.model.account;

import org.tpspencer.tal.mvc.sample.model.common.Address;

public interface Account {

	/**
	 * @return the id
	 */
	public abstract String getAccountNos();

	/**
	 * @param id the id to set
	 */
	public abstract void setAccountNos(String id);

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

}