package org.talframework.talui.mvc.sample.objex.model.order;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.object.ObjectUtils;

privileged aspect OrderSummaryBean_Roo_ObjexStateBean {
    
    declare parents: OrderSummaryBean implements ObjexObjStateBean;
    
    declare @type: OrderSummaryBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderSummaryBean.id;
    
    @Persistent(column = "parentId")
    private String OrderSummaryBean.parentId;
    
    @NotPersistent
    private transient boolean OrderSummaryBean._editable;
    
    public OrderSummaryBean.new() {
        super();
        _editable = false;
    }

    public String OrderSummaryBean.getId() {
        return this.id;
    }
    
    public String OrderSummaryBean.getParentId() {
        return this.parentId;
    }
    
    public boolean OrderSummaryBean.isEditable() {
        return _editable;
    }
    
    public void OrderSummaryBean.setEditable() {
        _editable = true;
    }
    
    public String OrderSummaryBean.getObjexObjType() {
        return "OrderSummary";
    }
    
    public void OrderSummaryBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void OrderSummaryBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void OrderSummaryBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
    public ObjexObjStateBean OrderSummaryBean.cloneState() {
        OrderSummaryBean ret = new OrderSummaryBean();
        ret.orderId = this.orderId;
        ret.orderDate = this.orderDate;
        ret.accountId = this.accountId;
        ret.collectionTown = this.collectionTown;
        ret.collectionPostcode = this.collectionPostcode;
        ret.collectionCountry = this.collectionCountry;
        ret.collectionDate = this.collectionDate;
        ret.service = this.service;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
