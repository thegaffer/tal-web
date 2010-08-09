package org.tpspencer.tal.mvc.sample.objex.model.common;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.mvc.sample.objex.model.common.AddressBean;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect AddressBean_Roo_ObjexStateBean {
    
    declare parents: AddressBean implements ObjexObjStateBean;
    
    declare @type: AddressBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String AddressBean.id;
    
    private String AddressBean.parentId;
    
    public AddressBean.new() {
        super();
        // Nothing
    }

    public AddressBean.new(AddressBean src) {
        super();
        this.address = src.address;
        this.town = src.town;
        this.postCode = src.postCode;
        this.country = src.country;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public AddressBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String AddressBean.getId() {
        return this.id;
    }
    
    public String AddressBean.getParentId() {
        return this.parentId;
    }
    
    public String AddressBean.getObjexObjType() {
        return "Address";
    }
    
    public void AddressBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void AddressBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
    }
    
}
