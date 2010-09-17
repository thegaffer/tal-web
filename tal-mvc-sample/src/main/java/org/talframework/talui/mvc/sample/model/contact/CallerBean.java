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

package org.talframework.talui.mvc.sample.model.contact;

public class CallerBean implements Caller {

	private String id = null;
	private String firstName = null;
	private String lastName = null;
	private String account = null;
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#getId()
	 */
	public Object getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#setId(java.lang.String)
	 */
	public void setId(Object id) {
		if( id == null ) this.id = null;
		else if( id instanceof Object[] ) this.id = ((Object[])id)[0].toString();
		else this.id = id.toString();
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#getFirstName()
	 */
	public String getFirstName() {
		return firstName;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#setFirstName(java.lang.String)
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#getLastName()
	 */
	public String getLastName() {
		return lastName;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#setLastName(java.lang.String)
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#getAccount()
	 */
	public String getAccount() {
		return account;
	}
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.sample.model.contact.Caller#setAccount(java.lang.String)
	 */
	public void setAccount(String account) {
		this.account = account;
	}
}
