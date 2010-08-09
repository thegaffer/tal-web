package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.String;

privileged aspect ContactBean_Roo_JavaBean {
    
    public String ContactBean.getFirstName() {
        return this.firstName;
    }
    
    public void ContactBean.setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String ContactBean.getLastName() {
        return this.lastName;
    }
    
    public void ContactBean.setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String ContactBean.getAccount() {
        return this.account;
    }
    
    public void ContactBean.setAccount(String account) {
        this.account = account;
    }
    
    public String ContactBean.getCompany() {
        return this.company;
    }
    
    public void ContactBean.setCompany(String company) {
        this.company = company;
    }
    
    public String ContactBean.getAddress() {
        return this.address;
    }
    
    public void ContactBean.setAddress(String address) {
        this.address = address;
    }
    
    public String ContactBean.getPreviousCrn() {
        return this.previousCrn;
    }
    
    public void ContactBean.setPreviousCrn(String previousCrn) {
        this.previousCrn = previousCrn;
    }
    
}
