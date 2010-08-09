/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tpspencer.tal.mvc.sample.objex.model.order;

import java.util.Date;

import org.tpspencer.tal.mvc.sample.model.common.Address;
import org.tpspencer.tal.objexj.annotations.ObjexRefProp;
import org.tpspencer.tal.objexj.annotations.ObjexStateBean;

/**
 * Simple Bean for prototyping
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="Order")
public class OrderBean {
	private final static long serialVersionUID = 1L;
	
	//@ObjexRefProp(owned=false, type=AccountImpl.class, newType="Account")
	private String account = null;
	
	@ObjexRefProp(owned=true, type=Address.class, newType="Address")
	private String collection = null;
	
	@ObjexRefProp(owned=true, type=Address.class, newType="Address")
	private String delivery = null;
	
	private Date collectionDate = null;
	
	private Date collectionTime = null;
	
	private String service = null;
	
	private String goodsType = null;
	
	private int goodsNumber = 1;
	
	private String goodsWeight = null;	
}
