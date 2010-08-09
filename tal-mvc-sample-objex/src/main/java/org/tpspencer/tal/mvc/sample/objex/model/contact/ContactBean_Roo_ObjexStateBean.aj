package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.mvc.sample.objex.model.contact.ContactBean;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect ContactBean_Roo_ObjexStateBean {
    
    declare parents: ContactBean implements ObjexObjStateBean;
    
    declare @type: ContactBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String ContactBean.id;
    
    private String ContactBean.parentId;
    
    public ContactBean.new() {
        super();
        // Nothing
    }

    public ContactBean.new(ContactBean src) {
        super();
        this.firstName = src.firstName;
        this.lastName = src.lastName;
        this.account = src.account;
        this.company = src.company;
        this.address = src.address;
        this.previousCrn = src.previousCrn;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public ContactBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String ContactBean.getId() {
        return this.id;
    }
    
    public String ContactBean.getParentId() {
        return this.parentId;
    }
    
    public String ContactBean.getObjexObjType() {
        return "Contact";
    }
    
    public void ContactBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void ContactBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        address = ObjectUtils.updateTempReferences(address, refs);
    }
    
}
