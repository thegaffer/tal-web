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

package org.talframework.talui.mvc.sample.objex.model.order;

import java.util.Map;

import org.talframework.objexj.annotations.ObjexObj;
import org.talframework.talui.mvc.sample.model.common.Address;
import org.talframework.talui.mvc.sample.model.order.Order;

/**
 * TODO: Need to implement RootObjexObj
 *
 * @author Tom Spencer
 */
@ObjexObj(OrderBean.class)
public class OrderImpl implements Order {

	private OrderBean bean;
	
	public OrderImpl(OrderBean bean) {
		this.bean = bean;
	}

	public void setCollection(Address collection) {
		if( collection == null ) return; // Nothing to do
		Address current = getCollection();
		if( current == collection ) return; // Nothing to do
		
		//if( current == null ) current = createCollection();
		
		current.setAddress(collection.getAddress());
		current.setTown(collection.getTown());
		current.setPostCode(collection.getPostCode());
		current.setCountry(collection.getCountry());
	}
	
	public void setDelivery(Address delivery) {
		if( delivery == null ) return; // Nothing to do
		Address current = getDelivery();
		if( current == delivery ) return; // Nothing to do
		
		if( current == null ) current = createDelivery();
		
		current.setAddress(delivery.getAddress());
		current.setTown(delivery.getTown());
		current.setPostCode(delivery.getPostCode());
		current.setCountry(delivery.getCountry());
	}
	
	public String getStatus() {
	    return "Created";
	}
	
	public Map<String, String> getHeader() {
	    // TODO: Add in header
	    return null;
	}
}
