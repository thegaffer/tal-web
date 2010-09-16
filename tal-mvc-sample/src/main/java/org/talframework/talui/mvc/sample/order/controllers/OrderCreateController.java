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

package org.tpspencer.tal.mvc.sample.order.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.tpspencer.tal.mvc.controller.InputBinder;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ModelBindInput;
import org.tpspencer.tal.mvc.sample.model.order.Order;
import org.tpspencer.tal.mvc.sample.service.OrderService;

@Controller(binder="binder")
public class OrderCreateController {
	
	/** Holds the service instance the controller should use */
	private OrderService service = null;
	/** Holds the input binder for this controller */
	private InputBinder binder = null;
	
	@Action(result="orderCreated", errorResult="orderCreateFailed", validationMethod="validate")
	public void submit(@ModelBindInput(prefix="newOrder", modelAttribute="newOrder") Order order) {
		getService().createOrder(order);
	}
	
	@Action(action="cancel", result="orderCancelled")
	public void cancel() {
	}
	
	public List<Object> validate(Order order) {
		return OrderValidationHelper.validate(order);
	}
	
	/**
	 * @return The order service the controller should use
	 */
	public OrderService getService() {
		return service;
	}
	
	/**
	 * Set the service for the controller to use
	 * 
	 * @param service The service
	 */
	@Autowired
	public void setService(OrderService service) {
		this.service = service;
	}
	
	/**
	 * @return the input binder for controller
	 */
	public InputBinder getBinder() {
		return binder;
	}

	/**
	 * @param binder The binder to use
	 */
	@Autowired
	public void setBinder(InputBinder binder) {
		this.binder = binder;
	}
}
