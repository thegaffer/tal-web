package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.String;
import org.tpspencer.tal.mvc.sample.objex.model.contact.CallerBean;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.BaseObjexObj;

privileged aspect CallerImpl_Roo_ObjexObj {
    
    declare parents: CallerImpl extends BaseObjexObj;
    
    public CallerBean CallerImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean CallerImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return new CallerBean(bean);
    }
    
    public String CallerImpl.getFirstName() {
        return bean.getFirstName();
    }
    
    public void CallerImpl.setFirstName(String val) {
        checkUpdateable();
        bean.setFirstName(val);
    }
    
    public String CallerImpl.getLastName() {
        return bean.getLastName();
    }
    
    public void CallerImpl.setLastName(String val) {
        checkUpdateable();
        bean.setLastName(val);
    }
    
    public String CallerImpl.getAccount() {
        return bean.getAccount();
    }
    
    public void CallerImpl.setAccount(String val) {
        checkUpdateable();
        bean.setAccount(val);
    }
    
}
