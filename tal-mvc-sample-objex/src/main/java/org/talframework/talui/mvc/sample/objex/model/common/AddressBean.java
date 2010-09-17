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

package org.talframework.talui.mvc.sample.objex.model.common;

import org.talframework.objexj.annotations.ObjexStateBean;

/**
 * Simple bean for sample app
 * 
 * @author Tom Spencer
 */
@ObjexStateBean(name="Address")
public class AddressBean {
	private final static long serialVersionUID = 1L;

	private String address = null;
	private String town = null;
	private String postCode = null;
	private String country = null;
	
}
