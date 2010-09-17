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

package org.talframework.talui.mvc.sample.order;

import javax.annotation.Resource;

import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.sample.order.views.NewOrderFormView;
import org.talframework.talui.mvc.sample.order.views.OrderFormView;
import org.talframework.talui.mvc.window.annotations.Model;
import org.talframework.talui.mvc.window.annotations.When;
import org.talframework.talui.mvc.window.annotations.WindowView;

/**
 * This class represents the configuration of the Order Window.
 * 
 * <p>The class is added to the sample project to demonstrate how
 * a window can be configured without lots of XML. It is entirely
 * possible to express in XML if you prefer. By using a combination
 * of public fields and annotations it is still possible to use
 * dependency injection to actually setup this window.</p>
 * 
 * <p>In this example we also use Spring auto-wiring to make all
 * the actual views and controllers configurable.</p> 
 * 
 * @author Tom Spencer
 */
public class OrderWindow {
	public static final String STATE = "state";
	public static final String ORDERS = "orders";
	public static final String SELECTED_ORDER = "selectedOrder";
	public static final String CURRENT_ORDER = "currentOrder";
	
	//////////////////////////////////////////////////
	// Model
	
	@Model
	@Resource(name="sample.order.orderWindowModel")
	public ModelConfiguration model;
	
	//////////////////////////////////////////////////
	// Views
	
	/** Holds the order list view for the window */
	@WindowView(name="listView", results={"orderCreated", "orderCancelled"})
	@Resource(name="sample.order.orderList")
	public View listView;
	
	/** Holds the order view for the window */
	@WindowView(name="orderView", results={"viewOrder", "orderUpdated"})
	@Resource(name="sample.order.orderView")
	public View orderView;
	
	/** Holds the order form view for the window */
	@WindowView(name="orderForm", view=OrderFormView.class, results={"editOrder"})
	@Resource(name="sample.order.orderForm")
	public View orderForm;
	
	/** Holds the new order view for the window */
	@WindowView(name="newOrderForm", view=NewOrderFormView.class, results={"createOrder"})
	@Resource(name="sample.order.newOrderForm")
	public View newOrderForm;
	
	//////////////////////////////////////////////////
	// Controllers
	
	@When(action="viewOrder")
	@Resource(name="sample.order.selectOrderViewController")
	public Object selectOrderView;
	
	@When(action="editOrder")
	@Resource(name="sample.order.selectOrderEditController")
	public Object selectOrderEdit;
	
	@When(action="createOrder")
	@Resource(name="sample.order.selectCreateOrderController")
	public Object selectCreateOrder;
	
	@When(action="submitOrderView")
	@Resource(name="sample.order.closeOrderViewController")
	public Object submitOrderView;
	
	@When(action="submitOrderForm")
	@Resource(name="sample.order.orderUpdateController")
	public Object submitOrderForm;
	
	@When(action="submitNewOrder")
	@Resource(name="sample.order.orderCreateController")
	public Object submitNewOrder;
}
