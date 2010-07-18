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

package org.tpspencer.tal.mvc.sample.service;

import org.tpspencer.tal.mvc.sample.model.order.Order;

/**
 * The service interface for orders
 * 
 * @author Tom Spencer
 */
public interface OrderService {

	/**
	 * Call to update an existing order
	 * 
	 * @param order The order
	 * @return The created order is returned back
	 */
	public Order updateOrder(Order order);
	
	/**
	 * Call to create a new order
	 * 
	 * @param order The order
	 * @return The created order is returned back
	 */
	public Order createOrder(Order order);
}
