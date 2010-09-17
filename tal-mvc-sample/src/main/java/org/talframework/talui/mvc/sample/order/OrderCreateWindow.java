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
import org.talframework.talui.mvc.sample.order.views.NewOrderFormView;
import org.talframework.talui.mvc.window.annotations.When;
import org.talframework.talui.mvc.window.annotations.WindowView;

/**
 * The order window represents a 
 * 
 * @author Tom Spencer
 */
public class OrderCreateWindow {
	
	/** Holds the new order view for the window */
	@WindowView(name="newOrderForm", view=NewOrderFormView.class, results={"createOrder"})
	@Resource(name="newOrderForm")
	public View newOrderForm;
	
	@When(action="submitNewOrderView")
	@Resource(name="orderCreateController")
	public Object submitNewOrder;
}
