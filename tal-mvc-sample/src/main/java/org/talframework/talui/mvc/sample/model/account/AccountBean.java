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

package org.talframework.talui.mvc.sample.model.account;

import org.talframework.talui.mvc.sample.model.common.Address;

public class AccountBean implements Account {

	private String accountNos = null;
	private String company = null;
	private Address address = null;
	
	public String getId() {
		return accountNos;
	}
	
	public void setId(String id) {
		this.accountNos = id;
	}
	
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Account#getId()
	 */
	public String getAccountNos() {
		return accountNos;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Account#setId(java.lang.String)
	 */
	public void setAccountNos(String id) {
		this.accountNos = id;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Account#getCompany()
	 */
	public String getCompany() {
		return company;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Account#setCompany(java.lang.String)
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Account#getAddress()
	 */
	public Address getAddress() {
		return address;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Account#setAddress(org.talframework.talui.mvc.sample.model.common.Address)
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
}
