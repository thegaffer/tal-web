package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.lang.String;
import java.util.Date;
import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.objex.model.order.OrderBean;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect OrderImpl_Roo_ObjexObj {
    
    declare parents: OrderImpl extends BaseObjexObj;
    
    public OrderBean OrderImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new OrderBean(bean);
    }
    
    public String OrderImpl.getAccount() {
        return bean.getAccount();
    }
    
    public void OrderImpl.setAccount(String val) {
        checkUpdateable();
        bean.setAccount(val);
    }
    
    public Address OrderImpl.getCollection() {
        return ObjectUtils.getObject(this, bean.getCollection(), Address.class);
    }
    
    public String OrderImpl.getCollectionRef() {
        return bean.getCollection();
    }
    
    public Address OrderImpl.createCollection() {
        checkUpdateable();
        if( bean.getCollection() != null )
        	ObjectUtils.removeObject(this, bean.getCollection());
        ObjexObj val = ObjectUtils.createObject(this, "Address");
        bean.setCollection(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void OrderImpl.removeCollection() {
        checkUpdateable();
        if( bean.getCollection() != null )
        	ObjectUtils.removeObject(this, bean.getCollection());
    }
    
    public Address OrderImpl.getDelivery() {
        return ObjectUtils.getObject(this, bean.getDelivery(), Address.class);
    }
    
    public String OrderImpl.getDeliveryRef() {
        return bean.getDelivery();
    }
    
    public Address OrderImpl.createDelivery() {
        checkUpdateable();
        if( bean.getDelivery() != null )
        	ObjectUtils.removeObject(this, bean.getDelivery());
        ObjexObj val = ObjectUtils.createObject(this, "Address");
        bean.setDelivery(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void OrderImpl.removeDelivery() {
        checkUpdateable();
        if( bean.getDelivery() != null )
        	ObjectUtils.removeObject(this, bean.getDelivery());
    }
    
    public Date OrderImpl.getCollectionDate() {
        return bean.getCollectionDate();
    }
    
    public void OrderImpl.setCollectionDate(Date val) {
        checkUpdateable();
        bean.setCollectionDate(val);
    }
    
    public Date OrderImpl.getCollectionTime() {
        return bean.getCollectionTime();
    }
    
    public void OrderImpl.setCollectionTime(Date val) {
        checkUpdateable();
        bean.setCollectionTime(val);
    }
    
    public String OrderImpl.getService() {
        return bean.getService();
    }
    
    public void OrderImpl.setService(String val) {
        checkUpdateable();
        bean.setService(val);
    }
    
    public String OrderImpl.getGoodsType() {
        return bean.getGoodsType();
    }
    
    public void OrderImpl.setGoodsType(String val) {
        checkUpdateable();
        bean.setGoodsType(val);
    }
    
    public int OrderImpl.getGoodsNumber() {
        return bean.getGoodsNumber();
    }
    
    public void OrderImpl.setGoodsNumber(int val) {
        checkUpdateable();
        bean.setGoodsNumber(val);
    }
    
    public String OrderImpl.getGoodsWeight() {
        return bean.getGoodsWeight();
    }
    
    public void OrderImpl.setGoodsWeight(String val) {
        checkUpdateable();
        bean.setGoodsWeight(val);
    }
    
}
