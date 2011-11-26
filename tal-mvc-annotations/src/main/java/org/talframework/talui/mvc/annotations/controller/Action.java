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
 * This annotation indicates the method should be called to
 * perform the user action. If the controller has no
 * subActionParameter and there is only 1 action method on
 * the class then this will always be called. If, however,
 * the controller does have a subActionParameter this value
 * is matched against the action() value of this annotation.
 * If there are no matches then the action with no name will
 * be used.
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
    /** If a sub-action, then name of the sub-action */
	public String action() default "";
	/** The result if there are no exceptions - if the method returns a string, this is ignored */
	public String result() default "ok";
	/** The result if there are exceptions */
	public String errorResult() default "fail";
	/** The name of a method taking same arguments that is used to pre-validate this action can be performed */
	public String validationMethod() default "";
}
