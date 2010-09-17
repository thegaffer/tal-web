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

package org.talframework.talui.mvc.commons.objex;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexObj;
import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.input.InputModel;

/**
 * Standard controller selects an objex object and then puts
 * it in the model. Care should be taken with this controller
 * because it potentially involves a remote lookup for the
 * container and object.
 * 
 * @author Tom Spencer
 */
public class ContainerSelectController implements Controller {

	/** Holds the name of the parameter that becomes the ID */
	private String idParameter = "id";
	/** Holds the name of the attribute to add object to (if not null) */
	private String modelAttribute = null;
	/** Holds the result */
	private String result = "selected";
	
	private ContainerLocator locator;
	
	public String performAction(Model model, InputModel input) {
		String id = input.getParameter(idParameter);
		
		if( id != null ) {
			Container container = locator.getContainer(model);
			ObjexObj obj = container.getObject(id);
			model.setAttribute(modelAttribute, obj);
		}
		else {
			model.removeAttribute(modelAttribute);
		}
		
		return result;
	}
}
