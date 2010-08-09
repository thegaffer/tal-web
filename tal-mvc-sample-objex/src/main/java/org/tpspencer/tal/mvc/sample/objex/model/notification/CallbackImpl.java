package org.tpspencer.tal.mvc.sample.objex.model.notification;

import org.tpspencer.tal.mvc.sample.model.notification.Callback;
import org.tpspencer.tal.objexj.annotations.ObjexObj;

//@ObjexObj(CallbackBean.class)
public class CallbackImpl implements Callback {

	private CallbackBean bean;
	
	public CallbackImpl(CallbackBean bean) { 
		this.bean = bean;
	}
}
