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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ModelInput;
import org.tpspencer.tal.mvc.sample.model.order.Order;
import org.tpspencer.tal.mvc.sample.model.order.OrderBean;
import org.tpspencer.tal.mvc.sample.service.OrderService;
import org.tpspencer.tal.mvc.spring.controller.SpringInputBinder;

@Controller(binderType=SpringInputBinder.class)
public class OrderUpdateController {
	
	/** Holds the service instance the controller should use */
	private OrderService service = null;
	
	@Action(result="orderUpdated", errorResult="orderSubmitFailed", validationMethod="validate")
	public void submit(
			@ModelInput Map<String, Object> model,
			@BindInput(prefix="form", type=OrderBean.class) Order order) {
		order = getService().updateOrder(order);
		model.put("currentOrder", order); // Update model so we can get events done
		model.put("messages", new String[]{"message.order.updated"});
	}
	
	@Action(action="cancel", result="orderCancelled")
	public void cancel() {
	}
	
	public List<Object> validate(Map<String, Object> model, Order order) {
		return OrderValidationHelper.validate(model, order);
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
}
