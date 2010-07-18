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
 * This annotation indicates the parameter is a bind
 * parameter, which is bound by either by the controllers
 * binder (@Binder) or the binder given as an attribute.
 * An optional prefix can be given that indicates a 
 * common prefix to all elements, i.e. if the prefix is
 * "form", then the input parameters prefixed "form." are
 * used in the binding. 
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface BindInput {
	/** Holds the prefix to use */
	public String prefix() default "";
	/** The name of the model attribute to retreive/store */
	public String modelAttribute() default "";
	/** The type of object - if default (Object) then type of param is used */
	public Class<?> type() default Object.class;
}
