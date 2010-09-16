package org.tpspencer.tal.mvc.sample.objex.model.order;

import org.talframework.objexj.annotations.ObjexObj;
import org.tpspencer.tal.mvc.sample.model.order.OrderSummary;

@ObjexObj(OrderSummaryBean.class)
public class OrderSummaryImpl implements OrderSummary {

    private OrderSummaryBean bean;
    
    public OrderSummaryImpl(OrderSummaryBean bean) {
        this.bean = bean;
    }
}
