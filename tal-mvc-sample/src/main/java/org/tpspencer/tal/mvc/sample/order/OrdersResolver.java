package org.tpspencer.tal.mvc.sample.order;

import org.tpspencer.tal.mvc.commons.repository.RepositoryListResolver;
import org.tpspencer.tal.mvc.sample.model.order.OrderRepository;

/**
 * This class retrieves all the orders.
 * 
 * @author Tom Spencer
 */
public class OrdersResolver extends RepositoryListResolver {
	public OrdersResolver() {
		super();
		setRepository(OrderRepository.getInstance());
	}
}
