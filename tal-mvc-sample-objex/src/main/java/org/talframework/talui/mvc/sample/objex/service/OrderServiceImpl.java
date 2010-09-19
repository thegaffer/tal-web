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

package org.talframework.talui.mvc.sample.objex.service;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.mvc.commons.repository.RepositoryHolder;
import org.talframework.talui.mvc.sample.model.order.Order;
import org.talframework.talui.mvc.sample.service.OrderService;
import org.talframework.talui.mvc.sample.service.transfer.SaveOrderResult;

/**
 * Simple implementation of the order service using the
 * Order repository
 * 
 * @author Tom Spencer
 */
public class OrderServiceImpl extends RepositoryHolder implements OrderService {

	/**
	 * Simply uses the repository to create the order
	 * and then copies the supplied order into it.
	 */
    @Trace
    public SaveOrderResult createOrder(Order order) {
	    return updateOrder(order);
	}
	
	/**
	 * Gets the object from the repository and then updates
	 * it from order. If order is the object from the repo
	 * then nothing happens.
	 */
    @Trace
    public SaveOrderResult updateOrder(Order order) {
	    if( !(order instanceof ObjexObj) ) throw new IllegalArgumentException("Cannot save order as provided order is not from a container");
        
        Container c = ((ObjexObj)order).getContainer();
        if( !c.isOpen() ) throw new IllegalArgumentException("Cannot save order as not in a open transaction");
        
        c.saveContainer();
        
        return new SaveOrderResult(c.getId(), order);
	}
}
