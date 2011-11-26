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
 * This annotation indicates that the given parameter should
 * be set to the result of binding the input to a model
 * attribute. The attribute is optionally stored back into 
 * the model.
 * 
 * TODO: Why do we have this? Sure added modelAttribute to BindInput
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ModelBindInput {
	/** Holds the optional prefix to use */
	public String prefix() default "";
	/** The name of the model attribute to retreive and bind to */
	public String modelAttribute();
	/** The optional name of the model attribute to save bound object into */
	public String saveAttribute() default "";
	/** The name of an attribute holding binder on controller class - if null uses controller default */
	public String binderName() default "";
}
