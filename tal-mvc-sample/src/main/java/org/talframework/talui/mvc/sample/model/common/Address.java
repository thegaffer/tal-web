package org.tpspencer.tal.mvc.sample.model.common;

public interface Address {

	/**
	 * @return the address
	 */
	public abstract String getAddress();

	/**
	 * @param address the address to set
	 */
	public abstract void setAddress(String address);

	/**
	 * @return the town
	 */
	public abstract String getTown();

	/**
	 * @param town the town to set
	 */
	public abstract void setTown(String town);

	/**
	 * @return the postCode
	 */
	public abstract String getPostCode();

	/**
	 * @param postCode the postCode to set
	 */
	public abstract void setPostCode(String postCode);

	/**
	 * @return the country
	 */
	public abstract String getCountry();

	/**
	 * @param country the country to set
	 */
	public abstract void setCountry(String country);

}