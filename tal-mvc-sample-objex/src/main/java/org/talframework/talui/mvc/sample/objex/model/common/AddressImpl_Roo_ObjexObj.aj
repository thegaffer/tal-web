package org.tpspencer.tal.mvc.sample.objex.model.common;

import java.lang.String;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.StateBeanUtils;
import org.tpspencer.tal.mvc.sample.objex.model.common.AddressBean;

privileged aspect AddressImpl_Roo_ObjexObj {
    
    declare parents: AddressImpl extends BaseObjexObj;
    
    public AddressBean AddressImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean AddressImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void AddressImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String AddressImpl.getAddress() {
        return cloneValue(bean.getAddress());
    }
    
    public void AddressImpl.setAddress(String val) {
        if( !StateBeanUtils.hasChanged(bean.getAddress(), val) ) return;
        ensureUpdateable(bean);
        bean.setAddress(val);
    }
    
    public String AddressImpl.getTown() {
        return cloneValue(bean.getTown());
    }
    
    public void AddressImpl.setTown(String val) {
        if( !StateBeanUtils.hasChanged(bean.getTown(), val) ) return;
        ensureUpdateable(bean);
        bean.setTown(val);
    }
    
    public String AddressImpl.getPostCode() {
        return cloneValue(bean.getPostCode());
    }
    
    public void AddressImpl.setPostCode(String val) {
        if( !StateBeanUtils.hasChanged(bean.getPostCode(), val) ) return;
        ensureUpdateable(bean);
        bean.setPostCode(val);
    }
    
    public String AddressImpl.getCountry() {
        return cloneValue(bean.getCountry());
    }
    
    public void AddressImpl.setCountry(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCountry(), val) ) return;
        ensureUpdateable(bean);
        bean.setCountry(val);
    }
    
}
