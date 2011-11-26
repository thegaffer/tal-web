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
 * When applied to an interface this annotation marks that
 * interface as representing a Window. A window occupies 
 * real estate on a page, but it delegates rendering to
 * its views and user action processing to the controllers.
 * 
 * <p>On the interface views are marked with as @ShowWhen
 * annotion on a simple getter method that returns either
 * a View interface directly or another interface or
 * class marked as a @View.</p>
 * 
 * <p>On the interface controllers are marked with a
 * @PerformWhen annotation on a simple gett method that
 * returns either a Controller interface directly or
 * another interface/class marked as a @Controller</p>
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Window {
    /** Holds the name of the window - defaults to the name of the interface */
    public String name() default "";
    /** Holds the type of the model for the window */
    public Class<?> model() default Object.class;
}
