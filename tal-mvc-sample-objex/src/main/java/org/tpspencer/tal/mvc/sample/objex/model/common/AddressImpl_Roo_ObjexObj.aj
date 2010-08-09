package org.tpspencer.tal.mvc.sample.objex.model.common;

import java.lang.String;
import org.tpspencer.tal.mvc.sample.objex.model.common.AddressBean;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;

privileged aspect AddressImpl_Roo_ObjexObj {
    
    declare parents: AddressImpl extends BaseObjexObj;
    
    public AddressBean AddressImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean AddressImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new AddressBean(bean);
    }
    
    public String AddressImpl.getAddress() {
        return bean.getAddress();
    }
    
    public void AddressImpl.setAddress(String val) {
        checkUpdateable();
        bean.setAddress(val);
    }
    
    public String AddressImpl.getTown() {
        return bean.getTown();
    }
    
    public void AddressImpl.setTown(String val) {
        checkUpdateable();
        bean.setTown(val);
    }
    
    public String AddressImpl.getPostCode() {
        return bean.getPostCode();
    }
    
    public void AddressImpl.setPostCode(String val) {
        checkUpdateable();
        bean.setPostCode(val);
    }
    
    public String AddressImpl.getCountry() {
        return bean.getCountry();
    }
    
    public void AddressImpl.setCountry(String val) {
        checkUpdateable();
        bean.setCountry(val);
    }
    
}
