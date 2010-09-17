package org.talframework.talui.mvc.sample.objex.model.contact;

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

privileged aspect CallerBean_Roo_ObjexStateBean {
    
    declare parents: CallerBean implements ObjexObjStateBean;
    
    declare @type: CallerBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String CallerBean.id;
    
    @Persistent(column = "parentId")
    private String CallerBean.parentId;
    
    @NotPersistent
    private transient boolean CallerBean._editable;
    
    public CallerBean.new() {
        super();
        _editable = false;
    }

    public String CallerBean.getId() {
        return this.id;
    }
    
    public String CallerBean.getParentId() {
        return this.parentId;
    }
    
    public boolean CallerBean.isEditable() {
        return _editable;
    }
    
    public void CallerBean.setEditable() {
        _editable = true;
    }
    
    public String CallerBean.getObjexObjType() {
        return "Caller";
    }
    
    public void CallerBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void CallerBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void CallerBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
    public ObjexObjStateBean CallerBean.cloneState() {
        CallerBean ret = new CallerBean();
        ret.firstName = this.firstName;
        ret.lastName = this.lastName;
        ret.account = this.account;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
