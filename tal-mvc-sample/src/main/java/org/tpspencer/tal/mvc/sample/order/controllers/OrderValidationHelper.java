package org.tpspencer.tal.mvc.sample.order.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.mvc.sample.model.order.Order;

/**
 * Helper class contains static validation methods
 * 
 * @author Tom Spencer
 */
public class OrderValidationHelper {

	/**
	 * Common validate method to check an order prior 
	 * to actual submit (create or update).
	 * 
	 * @param order The order
	 * @return The errors (if any)
	 */
	public static List<Object> validate(Order order) {
		if( order.getGoodsNumber() == 0 ) {
			List<Object> errors = new ArrayList<Object>();
			errors.add(new String[]{"form.goodsNumber", "error.invalidGoods"});
			return errors;
		}
		
		return null;
	}
	
	/**
	 * Validate with the model. Just passes to default version
	 * 
	 * @param model The model
	 * @param order The order
	 * @return The errors (if any)
	 */
	public static List<Object> validate(Map<String, Object> model, Order order) {
		// Testing
		if( order.getGoodsNumber() > 1 ) model.put("warnings", new String[]{"form.goodsNumber", "warning.goodsok"});
		return validate(order);
	}
}
