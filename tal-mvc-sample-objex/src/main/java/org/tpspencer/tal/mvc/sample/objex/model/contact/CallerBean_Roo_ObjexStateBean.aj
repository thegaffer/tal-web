package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.mvc.sample.objex.model.contact.CallerBean;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect CallerBean_Roo_ObjexStateBean {
    
    declare parents: CallerBean implements ObjexObjStateBean;
    
    declare @type: CallerBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String CallerBean.id;
    
    private String CallerBean.parentId;
    
    public CallerBean.new() {
        super();
        // Nothing
    }

    public CallerBean.new(CallerBean src) {
        super();
        this.firstName = src.firstName;
        this.lastName = src.lastName;
        this.account = src.account;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public CallerBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String CallerBean.getId() {
        return this.id;
    }
    
    public String CallerBean.getParentId() {
        return this.parentId;
    }
    
    public String CallerBean.getObjexObjType() {
        return "Caller";
    }
    
    public void CallerBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void CallerBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
}
