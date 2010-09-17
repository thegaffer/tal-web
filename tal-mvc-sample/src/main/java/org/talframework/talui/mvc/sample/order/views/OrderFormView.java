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

package org.talframework.talui.mvc.sample.order.views;

import org.talframework.talui.mvc.commons.views.form.FormView;
import org.talframework.talui.mvc.sample.model.order.Order;

/**
 * This class is the order form view. This view could be 
 * configured in a Spring config or similar, but it
 * makes sense to include it directly.
 * 
 * @author Tom Spencer
 */
public class OrderFormView extends FormView {

	public OrderFormView() {
		super();
		setViewName("orderForm");
		setPrimaryBean(Order.class);
		setTemplateName("org/talframework/talui/mvc/sample/order/OrderForm");
		setTemplateFile("/org/talframework/talui/mvc/sample/order/OrderForm.xml");
		setAsForm(true);
		setInitialFormBean("currentOrder");
		setResourceBase("org.talframework.talui.mvc.sample.order.OrderForm");
		
		init();
	}
}
