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

package org.talframework.talui.mvc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates the method returns a controller
 * that should be used when the incoming action matches the
 * value of this annotation. The annotation only makes sense
 * when added to a method on a class/interface marked as 
 * either @App, @Page, @Window or @View.
 * 
 * <p>This annotation should be placed on a method that
 * takes no arguments and returns either a Controller interface
 * directly or a type marked with the @Controller annotation.</p>
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PerformWhen {
    /** The incoming action name to map this controller to, default is bean name of property */
    public String value() default "";
}
