package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.String;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.ObjectUtils;
import org.talframework.objexj.object.StateBeanUtils;
import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.mvc.sample.objex.model.contact.ContactBean;

privileged aspect ContactImpl_Roo_ObjexObj {
    
    declare parents: ContactImpl extends BaseObjexObj;
    
    public ContactBean ContactImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean ContactImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void ContactImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String ContactImpl.getFirstName() {
        return cloneValue(bean.getFirstName());
    }
    
    public void ContactImpl.setFirstName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getFirstName(), val) ) return;
        ensureUpdateable(bean);
        bean.setFirstName(val);
    }
    
    public String ContactImpl.getLastName() {
        return cloneValue(bean.getLastName());
    }
    
    public void ContactImpl.setLastName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getLastName(), val) ) return;
        ensureUpdateable(bean);
        bean.setLastName(val);
    }
    
    public String ContactImpl.getAccount() {
        return cloneValue(bean.getAccount());
    }
    
    public void ContactImpl.setAccount(String val) {
        if( !StateBeanUtils.hasChanged(bean.getAccount(), val) ) return;
        ensureUpdateable(bean);
        bean.setAccount(val);
    }
    
    public String ContactImpl.getCompany() {
        return cloneValue(bean.getCompany());
    }
    
    public void ContactImpl.setCompany(String val) {
        if( !StateBeanUtils.hasChanged(bean.getCompany(), val) ) return;
        ensureUpdateable(bean);
        bean.setCompany(val);
    }
    
    public Address ContactImpl.getAddress() {
        return ObjectUtils.getObject(this, bean.getAddress(), Address.class);
    }
    
    public String ContactImpl.getAddressRef() {
        return bean.getAddress();
    }
    
    public Address ContactImpl.createAddress() {
        ensureUpdateable(bean);
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean, bean.getAddress());
        ObjexObj val = ObjectUtils.createObject(this, bean, "Address");
        bean.setAddress(val.getId().toString());
        return val.getBehaviour(Address.class);
    }
    
    public void ContactImpl.removeAddress() {
        ensureUpdateable(bean);
        if( bean.getAddress() != null )
        	ObjectUtils.removeObject(this, bean, bean.getAddress());
    }
    
    public String ContactImpl.getPreviousCrn() {
        return cloneValue(bean.getPreviousCrn());
    }
    
    public void ContactImpl.setPreviousCrn(String val) {
        if( !StateBeanUtils.hasChanged(bean.getPreviousCrn(), val) ) return;
        ensureUpdateable(bean);
        bean.setPreviousCrn(val);
    }
    
}
