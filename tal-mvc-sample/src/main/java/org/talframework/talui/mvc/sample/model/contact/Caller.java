package org.tpspencer.tal.mvc.sample.model.contact;

public interface Caller {

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

}