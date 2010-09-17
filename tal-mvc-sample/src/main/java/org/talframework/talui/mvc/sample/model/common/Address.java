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

package org.talframework.talui.mvc.sample.model.common;

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