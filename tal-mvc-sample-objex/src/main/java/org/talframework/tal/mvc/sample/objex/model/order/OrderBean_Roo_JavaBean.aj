package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.lang.String;
import java.util.Date;

privileged aspect OrderBean_Roo_JavaBean {
    
    public String OrderBean.getAccount() {
        return this.account;
    }
    
    public void OrderBean.setAccount(String account) {
        this.account = account;
    }
    
    public String OrderBean.getCollection() {
        return this.collection;
    }
    
    public void OrderBean.setCollection(String collection) {
        this.collection = collection;
    }
    
    public String OrderBean.getDelivery() {
        return this.delivery;
    }
    
    public void OrderBean.setDelivery(String delivery) {
        this.delivery = delivery;
    }
    
    public Date OrderBean.getCollectionDate() {
        return this.collectionDate;
    }
    
    public void OrderBean.setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }
    
    public Date OrderBean.getCollectionTime() {
        return this.collectionTime;
    }
    
    public void OrderBean.setCollectionTime(Date collectionTime) {
        this.collectionTime = collectionTime;
    }
    
    public String OrderBean.getService() {
        return this.service;
    }
    
    public void OrderBean.setService(String service) {
        this.service = service;
    }
    
    public String OrderBean.getGoodsType() {
        return this.goodsType;
    }
    
    public void OrderBean.setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
    
    public int OrderBean.getGoodsNumber() {
        return this.goodsNumber;
    }
    
    public void OrderBean.setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }
    
    public String OrderBean.getGoodsWeight() {
        return this.goodsWeight;
    }
    
    public void OrderBean.setGoodsWeight(String goodsWeight) {
        this.goodsWeight = goodsWeight;
    }
    
}
