package org.tpspencer.tal.mvc.sample.model.order;

import java.util.Date;

public interface OrderSummary {

    public String getOrderId();
    public void setOrderId(String val);
    
    public Date getOrderDate();
    public void setOrderDate(Date val);
    
    public String getAccountId();
    public void setAccountId(String val);
    
    public String getCollectionTown();
    public void setCollectionTown(String val);
    
    public String getCollectionPostcode();
    public void setCollectionPostcode(String val);
    
    public String getCollectionCountry();
    public void setCollectionCountry(String val);
    
    public Date getCollectionDate();
    public void setCollectionDate(Date val);
    
    public String getService();
    public void setService(String svc);
}
