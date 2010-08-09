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

package org.tpspencer.tal.mvc.sample.objex.service;

import org.tpspencer.tal.mvc.commons.repository.RepositoryHolder;
import org.tpspencer.tal.mvc.sample.model.order.Order;
import org.tpspencer.tal.mvc.sample.service.OrderService;
import org.tpspencer.tal.mvc.sample.service.transfer.SaveOrderResult;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.util.aspects.annotations.Trace;

/**
 * Simple implementation of the order service using the
 * Order repository
 * 
 * @author Tom Spencer
 */
@Trace
public class OrderServiceImpl extends RepositoryHolder implements OrderService {

	/**
	 * Simply uses the repository to create the order
	 * and then copies the supplied order into it.
	 */
	public SaveOrderResult createOrder(Order order) {
	    return updateOrder(order);
	}
	
	/**
	 * Gets the object from the repository and then updates
	 * it from order. If order is the object from the repo
	 * then nothing happens.
	 */
	public SaveOrderResult updateOrder(Order order) {
	    if( !(order instanceof ObjexObj) ) throw new IllegalArgumentException("Cannot save order as provided order is not from a container");
        
        Container c = ((ObjexObj)order).getContainer();
        if( !(c instanceof EditableContainer) ) throw new IllegalArgumentException("Cannot save order as not in a open transaction");
        
        ((EditableContainer)c).saveContainer();
        
        return new SaveOrderResult(c.getId(), order);
	}
}
