package org.tpspencer.tal.mvc.sample.order;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.sample.order.views.OrderFormView;
import org.tpspencer.tal.mvc.window.annotations.ModelAttr;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

/**
 * The order window represents a 
 * 
 * @author Tom Spencer
 */
public class OrderEditWindow {
	
	@ModelAttr
	public String selectedOrder;
	
	@ModelAttr
	@Resource(name="currentOrderResolver")
	public ModelResolver currentOrder;
	
	/** Holds the order form view for the window */
	@WindowView(name="orderForm", view=OrderFormView.class, results={"editOrder"})
	@Resource(name="orderForm")
	public View orderForm;
	
	@When(action="submitOrderForm")
	@Resource(name="orderUpdateController")
	public Object submitOrderForm;
}
