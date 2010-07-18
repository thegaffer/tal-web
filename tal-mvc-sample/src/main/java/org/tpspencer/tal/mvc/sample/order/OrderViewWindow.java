package org.tpspencer.tal.mvc.sample.order;

import javax.annotation.Resource;

import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.model.ModelResolver;
import org.tpspencer.tal.mvc.window.annotations.ModelAttr;
import org.tpspencer.tal.mvc.window.annotations.When;
import org.tpspencer.tal.mvc.window.annotations.WindowView;

/**
 * The order window represents a 
 * 
 * @author Tom Spencer
 */
public class OrderViewWindow {
	
	@ModelAttr
	public String selectedOrder;
	
	@ModelAttr
	@Resource(name="currentOrderResolver")
	public ModelResolver currentOrder;
	
	/** Holds the order view for the window */
	@WindowView(name="orderView", results={"viewOrder", "orderUpdated"})
	@Resource(name="orderView")
	public View orderView;
	
	@When(action="submitOrderView")
	@Resource(name="closeOrderViewController")
	public Object submitOrderView;
}
