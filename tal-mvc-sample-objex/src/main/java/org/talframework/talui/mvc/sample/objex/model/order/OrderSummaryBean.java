package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.util.Date;

import org.talframework.objexj.annotations.ObjexStateBean;

@ObjexStateBean(name="OrderSummary")
public class OrderSummaryBean {
    private final static long serialVersionUID = 1L;

    private String orderId;
    private Date orderDate;
    private String accountId;
    private String collectionTown;
    private String collectionPostcode;
    private String collectionCountry;
    private Date collectionDate;
    private String service;
}
