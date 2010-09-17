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

import java.util.ArrayList;
import java.util.List;

import org.talframework.talui.mvc.commons.views.table.TableAction;
import org.talframework.talui.mvc.commons.views.table.TableView;
import org.talframework.talui.mvc.sample.model.order.OrderSummary;

/**
 * This class is the list view. This view could be 
 * configured in a Spring config or similar, but it
 * makes sense to include it directly.
 * 
 * @author Tom Spencer
 */
public class OrderListView extends TableView {

	public OrderListView() {
		super();
		setViewName("orderList");
		setPrimaryBean(OrderSummary.class);
		setViewBeanName("orders");
		setTemplateFile("/org/talframework/talui/mvc/sample/order/OrderList.xml");
		setTableHeadings(new String[]{"icon", "account", "date", "collection", "collectionDate", "service", "actions"});
		setRowActions(getOrderRowActions());
		setTableActions(getOrderTableActions());
		setIdExpression("${this.orderId}");
		
		init();
	}
	
	private List<TableAction> getOrderRowActions() {
		List<TableAction> ret = new ArrayList<TableAction>();
	
		ret.add(new TableAction("viewOrder", null));
		ret.add(new TableAction("editOrder", null));
		
		return ret;
	}
	
	private List<TableAction> getOrderTableActions() {
		List<TableAction> ret = new ArrayList<TableAction>();
		
		ret.add(new TableAction("createOrder", null));
		
		return ret;
	}
}
