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

package org.tpspencer.tal.mvc.controller.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates the parameter is bound from
 * the input. A new instance of the class is created and
 * optionally saved onto the model (replacing any value
 * at that place in the model). The input will be bound
 * using the named binder on the controller or using the
 * controllers default binder. 
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface BindInput {
	/** Holds the prefix to use */
	public String prefix() default "";
	/** The type of object - if default (Object) then type of param is used */
	public Class<?> type() default Object.class;
	/** The optional name of the model attribute to save bound object into */
	public String saveAttribute() default "";
	/** The name of an attribute holding binder on controller class - if null uses controller default */
	public String binderName() default "";
}
