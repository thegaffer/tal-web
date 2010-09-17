package org.talframework.talui.mvc.sample.objex.model.common;

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

privileged aspect AddressBean_Roo_ObjexStateBean {
    
    declare parents: AddressBean implements ObjexObjStateBean;
    
    declare @type: AddressBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String AddressBean.id;
    
    @Persistent(column = "parentId")
    private String AddressBean.parentId;
    
    @NotPersistent
    private transient boolean AddressBean._editable;
    
    public AddressBean.new() {
        super();
        _editable = false;
    }

    public String AddressBean.getId() {
        return this.id;
    }
    
    public String AddressBean.getParentId() {
        return this.parentId;
    }
    
    public boolean AddressBean.isEditable() {
        return _editable;
    }
    
    public void AddressBean.setEditable() {
        _editable = true;
    }
    
    public String AddressBean.getObjexObjType() {
        return "Address";
    }
    
    public void AddressBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void AddressBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void AddressBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
    public ObjexObjStateBean AddressBean.cloneState() {
        AddressBean ret = new AddressBean();
        ret.address = this.address;
        ret.town = this.town;
        ret.postCode = this.postCode;
        ret.country = this.country;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
