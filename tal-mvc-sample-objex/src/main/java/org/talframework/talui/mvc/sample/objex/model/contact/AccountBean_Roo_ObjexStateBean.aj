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

privileged aspect AccountBean_Roo_ObjexStateBean {
    
    declare parents: AccountBean implements ObjexObjStateBean;
    
    declare @type: AccountBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String AccountBean.id;
    
    @Persistent(column = "parentId")
    private String AccountBean.parentId;
    
    @NotPersistent
    private transient boolean AccountBean._editable;
    
    public AccountBean.new() {
        super();
        _editable = false;
    }

    public String AccountBean.getId() {
        return this.id;
    }
    
    public String AccountBean.getParentId() {
        return this.parentId;
    }
    
    public boolean AccountBean.isEditable() {
        return _editable;
    }
    
    public void AccountBean.setEditable() {
        _editable = true;
    }
    
    public String AccountBean.getObjexObjType() {
        return "Account";
    }
    
    public void AccountBean.create(ObjexID parentId) {
        this.parentId = parentId != null ? parentId.toString() : null;
    }
    
    public void AccountBean.preSave(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void AccountBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        address = ObjectUtils.updateTempReferences(address, refs);
    }
    
    public ObjexObjStateBean AccountBean.cloneState() {
        AccountBean ret = new AccountBean();
        ret.accountNos = this.accountNos;
        ret.company = this.company;
        ret.address = this.address;
        ret.id = this.id;
        ret.parentId = this.parentId;
        return ret;
    }
    
}
