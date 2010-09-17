package org.talframework.talui.mvc.sample.objex.model.order;

import java.lang.String;
import java.util.Date;

privileged aspect OrderSummaryBean_Roo_JavaBean {
    
    public String OrderSummaryBean.getOrderId() {
        return this.orderId;
    }
    
    public void OrderSummaryBean.setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public Date OrderSummaryBean.getOrderDate() {
        return this.orderDate;
    }
    
    public void OrderSummaryBean.setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    public String OrderSummaryBean.getAccountId() {
        return this.accountId;
    }
    
    public void OrderSummaryBean.setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public String OrderSummaryBean.getCollectionTown() {
        return this.collectionTown;
    }
    
    public void OrderSummaryBean.setCollectionTown(String collectionTown) {
        this.collectionTown = collectionTown;
    }
    
    public String OrderSummaryBean.getCollectionPostcode() {
        return this.collectionPostcode;
    }
    
    public void OrderSummaryBean.setCollectionPostcode(String collectionPostcode) {
        this.collectionPostcode = collectionPostcode;
    }
    
    public String OrderSummaryBean.getCollectionCountry() {
        return this.collectionCountry;
    }
    
    public void OrderSummaryBean.setCollectionCountry(String collectionCountry) {
        this.collectionCountry = collectionCountry;
    }
    
    public Date OrderSummaryBean.getCollectionDate() {
        return this.collectionDate;
    }
    
    public void OrderSummaryBean.setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
    
    public String OrderSummaryBean.getService() {
        return this.service;
    }
    
    public void OrderSummaryBean.setService(String service) {
        this.service = service;
    }
    
}
