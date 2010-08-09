package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.String;
import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.objex.model.contact.AccountBean;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect AccountImpl_Roo_ObjexObj {
    
    declare parents: AccountImpl extends BaseObjexObj;
    
    public AccountBean AccountImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean AccountImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new AccountBean(bean);
    }
    
    public String AccountImpl.getAccountNos() {
        return bean.getAccountNos();
    }
    
    public void AccountImpl.setAccountNos(String val) {
        checkUpdateable();
        bean.setAccountNos(val);
    }
    
    public String AccountImpl.getCompany() {
        return bean.getCompany();
    }
    
    public void AccountImpl.setCompany(String val) {
        checkUpdateable();
        bean.setCompany(val);
    }
    
    public Address AccountImpl.getAddress() {
        return ObjectUtils.getObject(this, bean.getAddress(), Address.class);
    }
    
    public String AccountImpl.getAddressRef() {
        return bean.getAddress();
    }
    
    public Address AccountImpl.createAddress() {
        checkUpdateable();
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean.getAddress());
        ObjexObj val = ObjectUtils.createObject(this, "Address");
        bean.setAddress(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void AccountImpl.removeAddress() {
        checkUpdateable();
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean.getAddress());
    }
    
}
