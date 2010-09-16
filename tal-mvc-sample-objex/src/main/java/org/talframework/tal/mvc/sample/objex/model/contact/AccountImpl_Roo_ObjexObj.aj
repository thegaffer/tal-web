package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.String;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ObjectUtils;
import org.talframework.objexj.object.StateBeanUtils;
import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.objex.model.contact.AccountBean;

privileged aspect AccountImpl_Roo_ObjexObj {
    
    declare parents: AccountImpl extends BaseObjexObj;
    
    public AccountBean AccountImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean AccountImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void AccountImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String AccountImpl.getAccountNos() {
        return cloneValue(bean.getAccountNos());
    }
    
    public void AccountImpl.setAccountNos(String val) {
        if( !StateBeanUtils.hasChanged(bean.getAccountNos(), val) ) return;
        ensureUpdateable(bean);
        bean.setAccountNos(val);
    }
    
    public String AccountImpl.getCompany() {
        return cloneValue(bean.getCompany());
    }
    
    public void AccountImpl.setCompany(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCompany(), val) ) return;
        ensureUpdateable(bean);
        bean.setCompany(val);
    }
    
    public Address AccountImpl.getAddress() {
        return ObjectUtils.getObject(this, bean.getAddress(), Address.class);
    }
    
    public String AccountImpl.getAddressRef() {
        return bean.getAddress();
    }
    
    public Address AccountImpl.createAddress() {
        ensureUpdateable(bean);
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean, bean.getAddress());
        ObjexObj val = ObjectUtils.createObject(this, bean, "Address");
        bean.setAddress(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void AccountImpl.removeAddress() {
        ensureUpdateable(bean);
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean, bean.getAddress());
    }
    
}
