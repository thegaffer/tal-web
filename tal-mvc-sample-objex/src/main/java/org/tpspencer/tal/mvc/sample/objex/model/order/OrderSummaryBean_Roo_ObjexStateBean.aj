package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.mvc.sample.objex.model.order.OrderSummaryBean;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect OrderSummaryBean_Roo_ObjexStateBean {
    
    declare parents: OrderSummaryBean implements ObjexObjStateBean;
    
    declare @type: OrderSummaryBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderSummaryBean.id;
    
    private String OrderSummaryBean.parentId;
    
    public OrderSummaryBean.new() {
        super();
        // Nothing
    }

    public OrderSummaryBean.new(OrderSummaryBean src) {
        super();
        this.orderId = src.orderId;
        this.orderDate = src.orderDate;
        this.accountId = src.accountId;
        this.collectionTown = src.collectionTown;
        this.collectionPostcode = src.collectionPostcode;
        this.collectionCountry = src.collectionCountry;
        this.collectionDate = src.collectionDate;
        this.service = src.service;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public OrderSummaryBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String OrderSummaryBean.getId() {
        return this.id;
    }
    
    public String OrderSummaryBean.getParentId() {
        return this.parentId;
    }
    
    public String OrderSummaryBean.getObjexObjType() {
        return "OrderSummary";
    }
    
    public void OrderSummaryBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void OrderSummaryBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
}
