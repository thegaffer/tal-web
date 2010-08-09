package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.lang.String;
import java.util.Date;
import org.tpspencer.tal.mvc.sample.objex.model.order.OrderSummaryBean;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;

privileged aspect OrderSummaryImpl_Roo_ObjexObj {
    
    declare parents: OrderSummaryImpl extends BaseObjexObj;
    
    public OrderSummaryBean OrderSummaryImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderSummaryImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new OrderSummaryBean(bean);
    }
    
    public String OrderSummaryImpl.getOrderId() {
        return bean.getOrderId();
    }
    
    public void OrderSummaryImpl.setOrderId(String val) {
        checkUpdateable();
        bean.setOrderId(val);
    }
    
    public Date OrderSummaryImpl.getOrderDate() {
        return bean.getOrderDate();
    }
    
    public void OrderSummaryImpl.setOrderDate(Date val) {
        checkUpdateable();
        bean.setOrderDate(val);
    }
    
    public String OrderSummaryImpl.getAccountId() {
        return bean.getAccountId();
    }
    
    public void OrderSummaryImpl.setAccountId(String val) {
        checkUpdateable();
        bean.setAccountId(val);
    }
    
    public String OrderSummaryImpl.getCollectionTown() {
        return bean.getCollectionTown();
    }
    
    public void OrderSummaryImpl.setCollectionTown(String val) {
        checkUpdateable();
        bean.setCollectionTown(val);
    }
    
    public String OrderSummaryImpl.getCollectionPostcode() {
        return bean.getCollectionPostcode();
    }
    
    public void OrderSummaryImpl.setCollectionPostcode(String val) {
        checkUpdateable();
        bean.setCollectionPostcode(val);
    }
    
    public String OrderSummaryImpl.getCollectionCountry() {
        return bean.getCollectionCountry();
    }
    
    public void OrderSummaryImpl.setCollectionCountry(String val) {
        checkUpdateable();
        bean.setCollectionCountry(val);
    }
    
    public Date OrderSummaryImpl.getCollectionDate() {
        return bean.getCollectionDate();
    }
    
    public void OrderSummaryImpl.setCollectionDate(Date val) {
        checkUpdateable();
        bean.setCollectionDate(val);
    }
    
    public String OrderSummaryImpl.getService() {
        return bean.getService();
    }
    
    public void OrderSummaryImpl.setService(String val) {
        checkUpdateable();
        bean.setService(val);
    }
    
}
