package org.talframework.talui.mvc.sample.objex.model.order;

import java.lang.String;
import java.util.Date;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ObjectUtils;
import org.talframework.objexj.object.StateBeanUtils;
import org.talframework.talui.mvc.sample.model.common.Address;
import org.talframework.talui.mvc.sample.objex.model.order.OrderBean;

privileged aspect OrderImpl_Roo_ObjexObj {
    
    declare parents: OrderImpl extends BaseObjexObj;
    
    public OrderBean OrderImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean OrderImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void OrderImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String OrderImpl.getAccount() {
        return cloneValue(bean.getAccount());
    }
    
    public void OrderImpl.setAccount(String val) {
        if( !StateBeanUtils.hasChanged(bean.getAccount(), val) ) return;
        ensureUpdateable(bean);
        bean.setAccount(val);
    }
    
    public Address OrderImpl.getCollection() {
        return ObjectUtils.getObject(this, bean.getCollection(), Address.class);
    }
    
    public String OrderImpl.getCollectionRef() {
        return bean.getCollection();
    }
    
    public Address OrderImpl.createCollection() {
        ensureUpdateable(bean);
        if( bean.getCollection() != null )
        	ObjectUtils.removeObject(this, bean, bean.getCollection());
        ObjexObj val = ObjectUtils.createObject(this, bean, "Address");
        bean.setCollection(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void OrderImpl.removeCollection() {
        ensureUpdateable(bean);
        if( bean.getCollection() != null )
        	ObjectUtils.removeObject(this, bean, bean.getCollection());
    }
    
    public Address OrderImpl.getDelivery() {
        return ObjectUtils.getObject(this, bean.getDelivery(), Address.class);
    }
    
    public String OrderImpl.getDeliveryRef() {
        return bean.getDelivery();
    }
    
    public Address OrderImpl.createDelivery() {
        ensureUpdateable(bean);
        if( bean.getDelivery() != null )
        	ObjectUtils.removeObject(this, bean, bean.getDelivery());
        ObjexObj val = ObjectUtils.createObject(this, bean, "Address");
        bean.setDelivery(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void OrderImpl.removeDelivery() {
        ensureUpdateable(bean);
        if( bean.getDelivery() != null )
        	ObjectUtils.removeObject(this, bean, bean.getDelivery());
    }
    
    public Date OrderImpl.getCollectionDate() {
        return cloneValue(bean.getCollectionDate());
    }
    
    public void OrderImpl.setCollectionDate(Date val) {
        if( !StateBeanUtils.hasChanged(bean.getCollectionDate(), val) ) return;
        ensureUpdateable(bean);
        bean.setCollectionDate(val);
    }
    
    public Date OrderImpl.getCollectionTime() {
        return cloneValue(bean.getCollectionTime());
    }
    
    public void OrderImpl.setCollectionTime(Date val) {
        if( !StateBeanUtils.hasChanged(bean.getCollectionTime(), val) ) return;
        ensureUpdateable(bean);
        bean.setCollectionTime(val);
    }
    
    public String OrderImpl.getService() {
        return cloneValue(bean.getService());
    }
    
    public void OrderImpl.setService(String val) {
        if( !StateBeanUtils.hasChanged(bean.getService(), val) ) return;
        ensureUpdateable(bean);
        bean.setService(val);
    }
    
    public String OrderImpl.getGoodsType() {
        return cloneValue(bean.getGoodsType());
    }
    
    public void OrderImpl.setGoodsType(String val) {
        if( !StateBeanUtils.hasChanged(bean.getGoodsType(), val) ) return;
        ensureUpdateable(bean);
        bean.setGoodsType(val);
    }
    
    public int OrderImpl.getGoodsNumber() {
        return bean.getGoodsNumber();
    }
    
    public void OrderImpl.setGoodsNumber(int val) {
        if( !StateBeanUtils.hasChanged(bean.getGoodsNumber(), val) ) return;
        ensureUpdateable(bean);
        bean.setGoodsNumber(val);
    }
    
    public String OrderImpl.getGoodsWeight() {
        return cloneValue(bean.getGoodsWeight());
    }
    
    public void OrderImpl.setGoodsWeight(String val) {
        if( !StateBeanUtils.hasChanged(bean.getGoodsWeight(), val) ) return;
        ensureUpdateable(bean);
        bean.setGoodsWeight(val);
    }
    
}
