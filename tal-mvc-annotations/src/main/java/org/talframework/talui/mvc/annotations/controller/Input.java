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

package org.talframework.talui.mvc.annotations.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates the parameter holds unconverted
 * or inspected input.
 * 
 * <p>If a param() is specified on the annotation that the
 * value against that single parameter is passed to the action
 * method. No conversion or further work is done.</p>
 * 
 * <p>If neither a param() nor a prefix() is provided then
 * the entire input is mapped to the actions parameter. This
 * parameter must either be an InputModel or a 
 * Map<String, Object>.</p>
 * 
 * <p>If a prefix() is provided then any input that has that
 * as a previx is put into a Map<String, Object> and then
 * passed to the actions parameter.</p>
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Input {
	/** The name of a specific input parameter */
	public String param() default "";
	/** The prefix to extract all attributes from - ignored if param is provided */
	public String prefix() default "";
}
