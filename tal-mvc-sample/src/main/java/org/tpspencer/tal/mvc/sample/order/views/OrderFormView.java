package org.tpspencer.tal.mvc.sample.order.views;

import org.tpspencer.tal.mvc.commons.views.form.FormView;
import org.tpspencer.tal.mvc.sample.model.order.Order;

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
		setTemplateName("org/tpspencer/tal/mvc/sample/order/OrderForm");
		setTemplateFile("/org/tpspencer/tal/mvc/sample/order/OrderForm.xml");
		setAsForm(true);
		setInitialFormBean("currentOrder");
		setResourceBase("org.tpspencer.tal.mvc.sample.order.OrderForm");
		
		init();
	}
}
