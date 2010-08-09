package org.tpspencer.tal.mvc.sample.objex.model.contact;

import java.lang.Object;
import java.lang.String;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import org.tpspencer.tal.mvc.sample.objex.model.contact.AccountBean;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.ObjectUtils;

privileged aspect AccountBean_Roo_ObjexStateBean {
    
    declare parents: AccountBean implements ObjexObjStateBean;
    
    declare @type: AccountBean: @PersistenceCapable;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String AccountBean.id;
    
    private String AccountBean.parentId;
    
    public AccountBean.new() {
        super();
        // Nothing
    }

    public AccountBean.new(AccountBean src) {
        super();
        this.accountNos = src.accountNos;
        this.company = src.company;
        this.address = src.address;
        this.id = src.id;
        this.parentId = src.parentId;
    }

    public AccountBean.new(ObjexID parentId) {
        super();
        this.parentId = parentId != null ? parentId.toString() : null;
    }

    public String AccountBean.getId() {
        return this.id;
    }
    
    public String AccountBean.getParentId() {
        return this.parentId;
    }
    
    public String AccountBean.getObjexObjType() {
        return "Account";
    }
    
    public void AccountBean.init(Object id) {
        this.id = id != null ? id.toString() : null;
    }
    
    public void AccountBean.updateTemporaryReferences(java.util.Map<ObjexID, ObjexID> refs) {
        parentId = ObjectUtils.updateTempReferences(parentId, refs);
        address = ObjectUtils.updateTempReferences(address, refs);
    }
    
}
