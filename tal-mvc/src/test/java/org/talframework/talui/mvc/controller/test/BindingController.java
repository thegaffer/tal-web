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

package org.tpspencer.tal.mvc.controller.test;

import java.util.Date;
import java.util.List;

import org.tpspencer.tal.mvc.controller.InputBinder;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.controller.annotations.Controller;
import org.tpspencer.tal.mvc.controller.annotations.ErrorInput;
import org.tpspencer.tal.mvc.controller.annotations.ModelBindInput;

/**
 * A controller that uses bindings
 * 
 * @author Tom Spencer
 */
@Controller(binder="binder")
public interface BindingController {
	
	public InputBinder getBinder();
	
	public void setBinder(InputBinder binder);

	@Action(validationMethod="validate", errorResult="errors")
	public String magic(@ModelBindInput(prefix="dt", modelAttribute="date", saveAttribute="date") Date dt, @ErrorInput List<Object> errors);
	
	@Action(action="bindClass")
	public String classMagic(@BindInput(type=BindInterface.class) BindInterface obj);
	
	@Action(action="bindInterface")
	public String interfaceMagic(@BindInput BindInterface obj);
	
	public List<Object> validate(Date dt, List<Object> errors);
}
