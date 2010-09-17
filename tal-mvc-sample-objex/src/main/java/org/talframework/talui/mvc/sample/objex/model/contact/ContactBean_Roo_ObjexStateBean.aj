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

privileged aspect ContactBean_Roo_ObjexStateBean {
    
    declare parents: ContactBean implements ObjexObjStateBean;
    
    declare @type: ContactBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String ContactBean.id;
    
    @Persistent(column = "parentId")
    private String ContactBean.parentId;
    
    @NotPersistent
    private transient boolean ContactBean._editable;
    
    public ContactBean.new() {
        super();
        _editable = false;
    }

    public String ContactBean.getId() {
        return this.id;
    }
    
    public String ContactBean.getParentId() {
        return this.parentId;
    }
    
    public boolean ContactBean.isEditable() {
        return _editable;
    }
    
    public void ContactBean.setEditable() {
        _editable = true;
    }
    
    public String ContactBean.getObjexObjType() {
        return "Contact";
    }
    
    public void ContactBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void ContactBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void ContactBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        address = ObjectUtils.updateTempReferences(address, refs);
    }
    
    public ObjexObjStateBean ContactBean.cloneState() {
        ContactBean ret = new ContactBean();
        ret.firstName = this.firstName;
        ret.lastName = this.lastName;
        ret.account = this.account;
        ret.company = this.company;
        ret.address = this.address;
        ret.previousCrn = this.previousCrn;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
