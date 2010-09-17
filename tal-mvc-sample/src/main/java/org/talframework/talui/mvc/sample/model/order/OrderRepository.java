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

package org.talframework.talui.mvc.sample.model.order;

import org.talframework.talui.mvc.commons.repository.PrototypeRepository;
import org.talframework.talui.mvc.sample.model.common.AddressBean;

/**
 * This class is a specialised repository that creates some
 * basic Order instances at startup.
 * 
 * TODO: Remove and replace with test CSV or XML!
 * 
 * @author Tom Spencer
 */
public class OrderRepository extends PrototypeRepository {
	private static final OrderRepository INSTANCE = new OrderRepository();
	
	public static OrderRepository getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Hidden private constructor. Creates two orders
	 */
	public OrderRepository() {
		Order order = super.create(OrderBean.class);
		order.setAccount("123456");
		order.setCollection(new AddressBean());
		order.getCollection().setAddress("1 High Street");
		order.getCollection().setCountry("UK");
		order.getCollection().setPostCode("B45 7JK");
		order.setService("Sameday");
		
		order = super.create(OrderBean.class);
		order.setAccount("987654");
		order.setCollection(new AddressBean());
		order.getCollection().setAddress("143 Long Street");
		order.getCollection().setCountry("UK");
		order.getCollection().setPostCode("LE3 6HG");
		order.setService("Next Day");
	}
}
