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

package org.tpspencer.tal.mvc.sample.objex.model.contact;

import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.model.contact.Contact;
import org.tpspencer.tal.objexj.annotations.ObjexObj;

@ObjexObj(ContactBean.class)
public class ContactImpl implements Contact {
	
	private ContactBean bean;
	
	public ContactImpl(ContactBean bean) {
		this.bean = bean;
	}
	
	public void setAddress(Address address) {
		if( address == null ) return; // Nothing to do
		Address current = getAddress();
		if( current == address ) return; // Nothing to do
		
		if( current == null ) current = createAddress();
		
		current.setAddress(address.getAddress());
		current.setTown(address.getTown());
		current.setPostCode(address.getPostCode());
		current.setCountry(address.getCountry());
	}
	
	
}
