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
 * This annotation when applied to an interface marks the interface
 * as representing a page. A page can have a model, one or more
 * windows and even controllers for actions that are not dealt
 * with by the view or window directly.
 * 
 * <p>Note that this annotation can be applied to a class, but
 * the preference is to create an interface.</p>
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Page {
    /** Holds the name of the page - defaults to the name of the interface */
    public String name() default "";
    /** Holds the template of the page */
    public String template() default "";
    /** Holds the type of the model for the page */
    public Class<?> model() default Object.class;
}
