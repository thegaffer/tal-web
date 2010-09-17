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

/**
 * Simple bean for sample app
 * 
 * @author Tom Spencer
 */
public class AddressBean implements Address {

	private String address = null;
	private String town = null;
	private String postCode = null;
	private String country = null;
	
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#getAddress()
	 */
	public String getAddress() {
		return address;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#setAddress(java.lang.String)
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#getTown()
	 */
	public String getTown() {
		return town;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#setTown(java.lang.String)
	 */
	public void setTown(String town) {
		this.town = town;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#getPostCode()
	 */
	public String getPostCode() {
		return postCode;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#setPostCode(java.lang.String)
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#getCountry()
	 */
	public String getCountry() {
		return country;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.common.Address#setCountry(java.lang.String)
	 */
	public void setCountry(String country) {
		this.country = country;
	}
}
