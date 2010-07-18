package org.tpspencer.tal.mvc.sample.order;

import org.tpspencer.tal.mvc.commons.repository.RepositoryFindResolver;
import org.tpspencer.tal.mvc.sample.model.order.OrderRepository;

/**
 * This class resolves the current order from its ID
 * 
 * @author Tom Spencer
 */
public class CurrentOrderResolver extends RepositoryFindResolver {
	
	public CurrentOrderResolver() {
		super();
		setRepository(OrderRepository.getInstance());
		setModelAttribute(OrderWindow.SELECTED_ORDER);
	}
}