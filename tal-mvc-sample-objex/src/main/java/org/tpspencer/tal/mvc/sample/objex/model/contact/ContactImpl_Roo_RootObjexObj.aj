package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.objexj.RootObjexObj;

privileged aspect ContactImpl_Roo_RootObjexObj {
    
    declare parents: ContactImpl implements RootObjexObj;
    
    public String ContactImpl.getStatus() {
        // TODO: Should be a property of the contact as its the root
        return null;
    }
    
    public Map<String, String> ContactImpl.getHeader() {
        Map<String, String> ret = new HashMap<String, String>();
        
        if( bean.getFirstName() != null ) ret.put("firstName", bean.getFirstName());
        if( bean.getLastName() != null ) ret.put("lastName", bean.getLastName());
        if( bean.getAccount() != null ) ret.put("account", bean.getAccount());
        
        return ret;
    }
    
    public boolean ContactImpl.validate() {
        return true;
    }
}
