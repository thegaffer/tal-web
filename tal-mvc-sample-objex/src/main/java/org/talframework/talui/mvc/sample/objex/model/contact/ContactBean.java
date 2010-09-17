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

package org.talframework.talui.mvc.sample.objex.model.contact;

import org.talframework.objexj.annotations.ObjexRefProp;
import org.talframework.objexj.annotations.ObjexStateBean;
import org.talframework.talui.mvc.sample.model.common.Address;

@ObjexStateBean(name="Contact")
public class ContactBean {
	private final static long serialVersionUID = 1L;

	private String firstName = null;
	
	private String lastName = null;
	
	private String account = null;
	
	private String company = null;
	
	@ObjexRefProp(owned=true, type=Address.class, newType="Address")
	private String address = null;
	
	private String previousCrn = null;
	
}
