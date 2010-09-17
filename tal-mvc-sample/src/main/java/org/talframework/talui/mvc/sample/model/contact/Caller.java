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