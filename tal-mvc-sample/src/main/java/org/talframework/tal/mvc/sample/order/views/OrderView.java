package org.tpspencer.tal.mvc.sample.order.views;

import org.tpspencer.tal.mvc.commons.views.form.FormView;
import org.tpspencer.tal.mvc.sample.model.order.Order;

/**
 * This class is the order view. This view could be 
 * configured in a Spring config or similar, but it
 * makes sense to include it directly.
 * 
 * @author Tom Spencer
 */
public class OrderView extends FormView {

	public OrderView() {
		super();
		setViewName("orderView");
		setPrimaryBean(Order.class);
		setTemplateFile("/org/tpspencer/tal/mvc/sample/order/OrderForm.xml");
		setTemplateName("org/tpspencer/tal/mvc/sample/order/OrderView");
		setAsForm(false);
		setInitialFormBean("currentOrder");
		setResourceBase("org.tpspencer.tal.mvc.sample.order.OrderForm");
		
		init();
	}
}
