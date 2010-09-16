package org.tpspencer.tal.mvc.sample.service.transfer;

import org.tpspencer.tal.mvc.sample.model.order.Order;

/**
 * This simple class is used to return the result of
 * saving or updating a contact from the contact service
 * 
 * @author Tom Spencer
 */
public final class SaveOrderResult {

	/** Holds the ID of the contact */
	private final Object orderId;
	/** Holds the Order itself */
	private final Order order;
	
	public SaveOrderResult(Object orderId, Order order) {
		this.orderId = orderId;
		this.order = order;
	}

	/**
	 * @return the contactId
	 */
	public Object getOrderId() {
		return orderId;
	}

	/**
	 * @return the contact
	 */
	public Order getOrder() {
		return order;
	}
}
