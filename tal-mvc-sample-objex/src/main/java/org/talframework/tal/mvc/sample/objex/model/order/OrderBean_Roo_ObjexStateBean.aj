package org.tpspencer.tal.mvc.sample.objex.model.order;

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

privileged aspect OrderBean_Roo_ObjexStateBean {
    
    declare parents: OrderBean implements ObjexObjStateBean;
    
    declare @type: OrderBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String OrderBean.id;
    
    @Persistent(column = "parentId")
    private String OrderBean.parentId;
    
    @NotPersistent
    private transient boolean OrderBean._editable;
    
    public OrderBean.new() {
        super();
        _editable = false;
    }

    public String OrderBean.getId() {
        return this.id;
    }
    
    public String OrderBean.getParentId() {
        return this.parentId;
    }
    
    public boolean OrderBean.isEditable() {
        return _editable;
    }
    
    public void OrderBean.setEditable() {
        _editable = true;
    }
    
    public String OrderBean.getObjexObjType() {
        return "Order";
    }
    
    public void OrderBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void OrderBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void OrderBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        collection = ObjectUtils.updateTempReferences(collection, refs);
        delivery = ObjectUtils.updateTempReferences(delivery, refs);
    }
    
    public ObjexObjStateBean OrderBean.cloneState() {
        OrderBean ret = new OrderBean();
        ret.account = this.account;
        ret.collection = this.collection;
        ret.delivery = this.delivery;
        ret.collectionDate = this.collectionDate;
        ret.collectionTime = this.collectionTime;
        ret.service = this.service;
        ret.goodsType = this.goodsType;
        ret.goodsNumber = this.goodsNumber;
        ret.goodsWeight = this.goodsWeight;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
