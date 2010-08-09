package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.String;
import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.objex.model.contact.ContactBean;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect ContactImpl_Roo_ObjexObj {
    
    declare parents: ContactImpl extends BaseObjexObj;
    
    public ContactBean ContactImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean ContactImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new ContactBean(bean);
    }
    
    public String ContactImpl.getFirstName() {
        return bean.getFirstName();
    }
    
    public void ContactImpl.setFirstName(String val) {
        checkUpdateable();
        bean.setFirstName(val);
    }
    
    public String ContactImpl.getLastName() {
        return bean.getLastName();
    }
    
    public void ContactImpl.setLastName(String val) {
        checkUpdateable();
        bean.setLastName(val);
    }
    
    public String ContactImpl.getAccount() {
        return bean.getAccount();
    }
    
    public void ContactImpl.setAccount(String val) {
        checkUpdateable();
        bean.setAccount(val);
    }
    
    public String ContactImpl.getCompany() {
        return bean.getCompany();
    }
    
    public void ContactImpl.setCompany(String val) {
        checkUpdateable();
        bean.setCompany(val);
    }
    
    public Address ContactImpl.getAddress() {
        return ObjectUtils.getObject(this, bean.getAddress(), Address.class);
    }
    
    public String ContactImpl.getAddressRef() {
        return bean.getAddress();
    }
    
    public Address ContactImpl.createAddress() {
        checkUpdateable();
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean.getAddress());
        ObjexObj val = ObjectUtils.createObject(this, "Address");
        bean.setAddress(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void ContactImpl.removeAddress() {
        checkUpdateable();
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean.getAddress());
    }
    
    public String ContactImpl.getPreviousCrn() {
        return bean.getPreviousCrn();
    }
    
    public void ContactImpl.setPreviousCrn(String val) {
        checkUpdateable();
        bean.setPreviousCrn(val);
    }
    
}
