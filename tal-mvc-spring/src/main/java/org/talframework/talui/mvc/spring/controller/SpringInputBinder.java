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

package org.talframework.talui.mvc.spring.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.controller.InputBinder;
import org.talframework.talui.mvc.input.InputModel;
import org.talframework.talui.mvc.model.message.ClientMessageImpl;
import org.talframework.talui.mvc.spring.bind.ObjectDataBinder;

/**
 * This class implements the Web MVC InputBinder interface
 * using the Spring data binding concepts.
 * 
 * @author Tom Spencer
 */
public class SpringInputBinder implements InputBinder {
	
	/**
	 * Checks against configuration
	 */
	public boolean supports(Class<?> bindType) {
		return true;
	}
	
	/**
	 * Bind and return the errors
	 */
	@SuppressWarnings("unchecked")
	public List<Object> bind(Model model, InputModel input, String prefix, Object obj) {
		DataBinder binder = prefix != null ? new ObjectDataBinder(obj, prefix) : new ObjectDataBinder(obj);
		binder.bind(new MutablePropertyValues(prefix != null ? input.getParameters(prefix) : input.getParameters()));
		
		List<Object> ret = null;
		if( binder.getBindingResult().hasErrors() ) {
			ret = new ArrayList<Object>();
			
			List<Object> errs = binder.getBindingResult().getAllErrors();
			Iterator<Object> it = errs.iterator();
			while( it.hasNext() ) {
				Object e = it.next();
				
				String field = null;
				String code = null;
				Object[] params = null;
				
				if( e instanceof ObjectError ) {
					field = ((ObjectError)e).getObjectName();
					code = "error" + "." + ((ObjectError)e).getCode();
					params = ((ObjectError)e).getArguments();
				}
				if( e instanceof FieldError ) {
					field = field + "." + ((FieldError)e).getField();
					
					Object rejected = ((FieldError)e).getRejectedValue();
					if( rejected != null ) {
						if( rejected instanceof Object[] ) {
							Object[] vals = (Object[])rejected;
							StringBuilder buf = new StringBuilder();
							for( int i = 0 ; i < vals.length ; i++ ) {
								if( i != 0 ) buf.append(", ");
								buf.append(vals[i]);
							}
							rejected = buf.toString();
						}
					}
					
					params = new Object[rejected != null ? 2 : 1];
					params[0] = "#label." + ((FieldError)e).getField();
					if( rejected != null ) params[1] = rejected;
				}
				
				if( code != null ) ret.add(new ClientMessageImpl(field, code, params));
			}
		}
		return ret;
	}
}
