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
 * This annotation marks the class is representing a controller.
 * When this controller is fired is a function of the view,
 * window, page or app that contained the mapping. The class
 * this annotation is applied to can then use controller
 * annotations on its methods and the parameters to those methods
 * to control what is passed in to the controllers.
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    /** Holds the name of the controller - defaults to the name of the class */
    public String name() default "";
    /** @return The name of the subaction parameter (if empty there can be only 1 action method) */
    public String subActionParameter() default "";
    /** @return The default binder for this controller */
    public Class<?> binder() default Object.class;
    /** @return The name of the model attribute to store errors away */
    public String errorAttribute() default "errors";
}
