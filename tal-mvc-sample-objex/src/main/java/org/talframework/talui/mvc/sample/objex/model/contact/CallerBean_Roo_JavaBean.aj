package org.talframework.talui.mvc.sample.objex.model.contact;

import java.lang.String;

privileged aspect CallerBean_Roo_JavaBean {
    
    public String CallerBean.getFirstName() {
        return this.firstName;
    }
    
    public void CallerBean.setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String CallerBean.getLastName() {
        return this.lastName;
    }
    
    public void CallerBean.setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String CallerBean.getAccount() {
        return this.account;
    }
    
    public void CallerBean.setAccount(String account) {
        this.account = account;
    }
    
}
