package org.tpspencer.tal.mvc.sample.order.views;

import org.tpspencer.tal.mvc.commons.views.form.FormView;
import org.tpspencer.tal.mvc.sample.model.order.Order;

/**
 * This class is the new order view. This view could be 
 * configured in a Spring config or similar, but it
 * makes sense to include it directly.
 * 
 * @author Tom Spencer
 */
public class NewOrderFormView extends FormView {

	public NewOrderFormView() {
		super();
		setViewName("newOrderFormView");
		setViewBeanName("newOrder");
		setPrimaryBean(Order.class);
		setTemplateName("org/tpspencer/tal/mvc/sample/order/NewOrderForm");
		setTemplateFile("/org/tpspencer/tal/mvc/sample/order/OrderForm.xml");
		setAsForm(true);
		setResourceBase("org.tpspencer.tal.mvc.sample.order.OrderForm");
		
		init();
	}
}
