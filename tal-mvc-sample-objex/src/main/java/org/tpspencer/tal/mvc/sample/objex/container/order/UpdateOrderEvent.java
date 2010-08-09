package org.tpspencer.tal.mvc.sample.objex.container.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.model.order.Order;
import org.tpspencer.tal.mvc.sample.model.order.OrderSummary;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.events.Event;
import org.tpspencer.tal.objexj.events.EventHandler;
import org.tpspencer.tal.objexj.locator.SingletonContainerLocator;
import org.tpspencer.tal.objexj.query.DefaultQueryRequest;
import org.tpspencer.tal.objexj.query.QueryResult;
import org.tpspencer.tal.util.aspects.annotations.Trace;

@Trace
public class UpdateOrderEvent implements EventHandler {
    
    public void execute(Container container, Event event) {
        Container orderContainer = SingletonContainerLocator.getInstance().get(event.getSourceContainer());
        Order order = orderContainer.getRootObject().getBehaviour(Order.class);
        EditableContainer orders = SingletonContainerLocator.getInstance().open("Orders");
        
        // Query to find this order in orders store or create new one
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("orderId", event.getSourceContainer());
        QueryResult result = orders.executeQuery(new DefaultQueryRequest("order", 0, 1, parameters, null, false, null));
        
        OrderSummary summary = result.getResults() != null && result.getResults().size() > 0 ? result.getResults().get(0).getBehaviour(OrderSummary.class) : null;
        if( summary == null ) summary = orders.newObject("OrderSummary", orders.getRootObject()).getBehaviour(OrderSummary.class);
        
        // Update the order
        summary.setOrderId(event.getSourceContainer());
        if( summary.getOrderDate() == null ) summary.setOrderDate(new Date());
        summary.setAccountId(order.getAccount());
        Address collection = order.getCollection();
        if( collection != null ) {
            summary.setCollectionTown(collection.getTown());
            summary.setCollectionPostcode(collection.getPostCode());
            summary.setCollectionCountry(collection.getCountry());
        }
        summary.setCollectionDate(order.getCollectionDate());
        summary.setService(order.getService());
        
        // TODO: Remove this, should really reject if no account
        if( summary.getAccountId() == null ) summary.setAccountId("12345");
        
        orders.saveContainer();
    }

}
