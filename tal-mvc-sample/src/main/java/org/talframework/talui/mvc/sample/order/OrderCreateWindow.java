package org.tpspencer.tal.mvc.sample.order;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.sample.order.views.NewOrderFormView;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

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
