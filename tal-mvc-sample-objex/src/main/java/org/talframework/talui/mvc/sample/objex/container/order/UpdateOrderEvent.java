/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.talframework.talui.mvc.sample.objex.container.order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.Event;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.locator.SingletonContainerLocator;
import org.talframework.objexj.query.DefaultQueryRequest;
import org.talframework.objexj.query.QueryResult;
import org.talframework.talui.mvc.sample.model.common.Address;
import org.talframework.talui.mvc.sample.model.order.Order;
import org.talframework.talui.mvc.sample.model.order.OrderSummary;
import org.tpspencer.tal.util.aspects.annotations.Trace;

@Trace
public class UpdateOrderEvent implements EventHandler {
    
    public void execute(Container container, Event event) {
        Container orderContainer = SingletonContainerLocator.getInstance().get(event.getSourceContainer());
        Order order = orderContainer.getRootObject().getBehaviour(Order.class);
        Container orders = SingletonContainerLocator.getInstance().open("Orders");
        
        // Query to find this order in orders store or create new one
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("orderId", event.getSourceContainer());
        QueryResult result = orders.executeQuery(new DefaultQueryRequest("order", 0, 1, parameters, null, false, null));
        
        OrderSummary summary = result.getResults() != null && result.getResults().size() > 0 ? result.getResults().get(0).getBehaviour(OrderSummary.class) : null;
        if( summary == null ) throw new IllegalArgumentException("TODO: Creation of object in anon parent!");
        
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
