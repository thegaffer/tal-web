package org.tpspencer.tal.mvc.sample.order.views;

import java.util.ArrayList;
import java.util.List;

import org.tpspencer.tal.mvc.commons.views.table.TableAction;
import org.tpspencer.tal.mvc.commons.views.table.TableView;
import org.tpspencer.tal.mvc.sample.model.order.OrderSummary;

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
		setTemplateFile("/org/tpspencer/tal/mvc/sample/order/OrderList.xml");
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
