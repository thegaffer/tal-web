package org.talframework.talui.mvc.sample.objex.model.contact;

import java.lang.String;

privileged aspect AccountBean_Roo_JavaBean {
    
    public String AccountBean.getAccountNos() {
        return this.accountNos;
    }
    
    public void AccountBean.setAccountNos(String accountNos) {
        this.accountNos = accountNos;
    }
    
    public String AccountBean.getCompany() {
        return this.company;
    }
    
    public void AccountBean.setCompany(String company) {
        this.company = company;
    }
    
    public String AccountBean.getAddress() {
        return this.address;
    }
    
    public void AccountBean.setAddress(String address) {
        this.address = address;
    }
    
}
