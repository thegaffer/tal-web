package org.tpspencer.tal.mvc.sample.objex.model.common;

import java.lang.String;

privileged aspect AddressBean_Roo_JavaBean {
    
    public String AddressBean.getAddress() {
        return this.address;
    }
    
    public void AddressBean.setAddress(String address) {
        this.address = address;
    }
    
    public String AddressBean.getTown() {
        return this.town;
    }
    
    public void AddressBean.setTown(String town) {
        this.town = town;
    }
    
    public String AddressBean.getPostCode() {
        return this.postCode;
    }
    
    public void AddressBean.setPostCode(String postCode) {
        this.postCode = postCode;
    }
    
    public String AddressBean.getCountry() {
        return this.country;
    }
    
    public void AddressBean.setCountry(String country) {
        this.country = country;
    }
    
}
