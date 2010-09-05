package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.util.Map;

import org.talframework.objexj.annotations.ObjexObj;
import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.model.order.Order;

/**
 * TODO: Need to implement RootObjexObj
 *
 * @author Tom Spencer
 */
@ObjexObj(OrderBean.class)
public class OrderImpl implements Order {

	private OrderBean bean;
	
	public OrderImpl(OrderBean bean) {
		this.bean = bean;
	}

	public void setCollection(Address collection) {
		if( collection == null ) return; // Nothing to do
		Address current = getCollection();
		if( current == collection ) return; // Nothing to do
		
		//if( current == null ) current = createCollection();
		
		current.setAddress(collection.getAddress());
		current.setTown(collection.getTown());
		current.setPostCode(collection.getPostCode());
		current.setCountry(collection.getCountry());
	}
	
	public void setDelivery(Address delivery) {
		if( delivery == null ) return; // Nothing to do
		Address current = getDelivery();
		if( current == delivery ) return; // Nothing to do
		
		if( current == null ) current = createDelivery();
		
		current.setAddress(delivery.getAddress());
		current.setTown(delivery.getTown());
		current.setPostCode(delivery.getPostCode());
		current.setCountry(delivery.getCountry());
	}
	
	public String getStatus() {
	    return "Created";
	}
	
	public Map<String, String> getHeader() {
	    // TODO: Add in header
	    return null;
	}
}
