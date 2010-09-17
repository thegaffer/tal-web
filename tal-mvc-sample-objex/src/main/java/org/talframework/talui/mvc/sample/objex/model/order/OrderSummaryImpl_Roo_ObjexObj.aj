package org.talframework.talui.mvc.sample.objex.model.order;

import java.lang.String;
import java.util.Date;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.StateBeanUtils;
import org.talframework.talui.mvc.sample.objex.model.order.OrderSummaryBean;

privileged aspect OrderSummaryImpl_Roo_ObjexObj {
    
    declare parents: OrderSummaryImpl extends BaseObjexObj;
    
    public OrderSummaryBean OrderSummaryImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderSummaryImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void OrderSummaryImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String OrderSummaryImpl.getOrderId() {
        return cloneValue(bean.getOrderId());
    }
    
    public void OrderSummaryImpl.setOrderId(String val) {
        if( !StateBeanUtils.hasChanged(bean.getOrderId(), val) ) return;
        ensureUpdateable(bean);
        bean.setOrderId(val);
    }
    
    public Date OrderSummaryImpl.getOrderDate() {
        return cloneValue(bean.getOrderDate());
    }
    
    public void OrderSummaryImpl.setOrderDate(Date val) {
        if( !StateBeanUtils.hasChanged(bean.getOrderDate(), val) ) return;
        ensureUpdateable(bean);
        bean.setOrderDate(val);
    }
    
    public String OrderSummaryImpl.getAccountId() {
        return cloneValue(bean.getAccountId());
    }
    
    public void OrderSummaryImpl.setAccountId(String val) {
        if( !StateBeanUtils.hasChanged(bean.getAccountId(), val) ) return;
        ensureUpdateable(bean);
        bean.setAccountId(val);
    }
    
    public String OrderSummaryImpl.getCollectionTown() {
        return cloneValue(bean.getCollectionTown());
    }
    
    public void OrderSummaryImpl.setCollectionTown(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCollectionTown(), val) ) return;
        ensureUpdateable(bean);
        bean.setCollectionTown(val);
    }
    
    public String OrderSummaryImpl.getCollectionPostcode() {
        return cloneValue(bean.getCollectionPostcode());
    }
    
    public void OrderSummaryImpl.setCollectionPostcode(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCollectionPostcode(), val) ) return;
        ensureUpdateable(bean);
        bean.setCollectionPostcode(val);
    }
    
    public String OrderSummaryImpl.getCollectionCountry() {
        return cloneValue(bean.getCollectionCountry());
    }
    
    public void OrderSummaryImpl.setCollectionCountry(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCollectionCountry(), val) ) return;
        ensureUpdateable(bean);
        bean.setCollectionCountry(val);
    }
    
    public Date OrderSummaryImpl.getCollectionDate() {
        return cloneValue(bean.getCollectionDate());
    }
    
    public void OrderSummaryImpl.setCollectionDate(Date val) {
        if( !StateBeanUtils.hasChanged(bean.getCollectionDate(), val) ) return;
        ensureUpdateable(bean);
        bean.setCollectionDate(val);
    }
    
    public String OrderSummaryImpl.getService() {
        return cloneValue(bean.getService());
    }
    
    public void OrderSummaryImpl.setService(String val) {
        if( !StateBeanUtils.hasChanged(bean.getService(), val) ) return;
        ensureUpdateable(bean);
        bean.setService(val);
    }
    
}
