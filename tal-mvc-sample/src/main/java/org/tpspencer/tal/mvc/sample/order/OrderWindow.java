package org.tpspencer.tal.mvc.sample.order;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.sample.order.views.NewOrderFormView;
import org.tpspencer.tal.mvc.sample.order.views.OrderFormView;
import org.tpspencer.tal.mvc.window.annotations.ModelAttr;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

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
	
	@ModelAttr
	public String state;
	
	@ModelAttr
	@Resource(name="ordersResolver")
	public ModelResolver orders;
	
	@ModelAttr
	public String selectedOrder;
	
	@ModelAttr
	@Resource(name="currentOrderResolver")
	public ModelResolver currentOrder;
	
	//////////////////////////////////////////////////
	// Views
	
	/** Holds the order list view for the window */
	@WindowView(name="listView", results={"orderCreated", "orderCancelled"})
	@Resource(name="orderList")
	public View listView;
	
	/** Holds the order view for the window */
	@WindowView(name="orderView", results={"viewOrder", "orderUpdated"})
	@Resource(name="orderView")
	public View orderView;
	
	/** Holds the order form view for the window */
	@WindowView(name="orderForm", view=OrderFormView.class, results={"editOrder"})
	@Resource(name="orderForm")
	public View orderForm;
	
	/** Holds the new order view for the window */
	@WindowView(name="newOrderForm", view=NewOrderFormView.class, results={"createOrder"})
	@Resource(name="newOrderForm")
	public View newOrderForm;
	
	//////////////////////////////////////////////////
	// Controllers
	
	@When(action="viewOrder")
	@Resource(name="selectOrderViewController")
	public Object selectOrderView;
	
	@When(action="editOrder")
	@Resource(name="selectOrderEditController")
	public Object selectOrderEdit;
	
	@When(action="createOrder")
	@Resource(name="selectCreateOrderController")
	public Object selectCreateOrder;
	
	@When(action="submitOrderView")
	@Resource(name="closeOrderViewController")
	public Object submitOrderView;
	
	@When(action="submitOrderForm")
	@Resource(name="orderUpdateController")
	public Object submitOrderForm;
	
	@When(action="submitNewOrder")
	@Resource(name="orderCreateController")
	public Object submitNewOrder;
}
