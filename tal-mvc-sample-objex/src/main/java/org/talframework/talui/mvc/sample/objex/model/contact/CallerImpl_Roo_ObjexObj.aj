package org.talframework.talui.mvc.sample.objex.model.contact;

import java.lang.String;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.object.BaseObjexObj;
import org.talframework.objexj.object.StateBeanUtils;
import org.talframework.talui.mvc.sample.objex.model.contact.CallerBean;

privileged aspect CallerImpl_Roo_ObjexObj {
    
    declare parents: CallerImpl extends BaseObjexObj;
    
    public CallerBean CallerImpl.getLocalState() {
        return bean;
    }
    
    public ObjexObjStateBean CallerImpl.getStateObject() {
        if( isUpdateable() ) return bean;
        else return bean.cloneState();
    }
    
    public void CallerImpl.validate(ValidationRequest request) {
        return;
    }
    
    public String CallerImpl.getFirstName() {
        return cloneValue(bean.getFirstName());
    }
    
    public void CallerImpl.setFirstName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getFirstName(), val) ) return;
        ensureUpdateable(bean);
        bean.setFirstName(val);
    }
    
    public String CallerImpl.getLastName() {
        return cloneValue(bean.getLastName());
    }
    
    public void CallerImpl.setLastName(String val) {
        if( !StateBeanUtils.hasChanged(bean.getLastName(), val) ) return;
        ensureUpdateable(bean);
        bean.setLastName(val);
    }
    
    public String CallerImpl.getAccount() {
        return cloneValue(bean.getAccount());
    }
    
    public void CallerImpl.setAccount(String val) {
        if( !StateBeanUtils.hasChanged(bean.getAccount(), val) ) return;
        ensureUpdateable(bean);
        bean.setAccount(val);
    }
    
}
