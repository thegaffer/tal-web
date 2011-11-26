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

package org.talframework.talui.mvc.annotations.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks the class or interface as representing
 * a view. If applied to a interface in order to be useful the
 * template name must be entered - then the framework will simply
 * delegate to that template for the action rendering. You
 * can also specify a number of model attributes that will be
 * added as renderAttributes automatically (you can even change
 * their name by use the equals, see below). An example is:
 * 
 * <code><pre>
 * @View(templateName="/jsp/view.jsp", renderAttributes={"name=anotherName", "selected"})
 * public interface MyView {
 * }
 * </pre></code>
 * 
 * <p>In the above when this view is active (as determined by
 * the window) the framework will delegate render to /jsp/view.jsp,
 * but before doing so will extract from the model the attributes
 * "name" and "selected". It will add to the request the value of
 * "name" under the render name of "anotherName", whilst "selected"
 * will be stored under the same name.</p>
 *
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface View {
    /** Holds the name of the view - defaults to the name of the interface */
    public String name() default "";
    /** Holds the type of the model for the view */
    public Class<?> model() default Object.class;
    /** Holds the name of the template this view should delegate to */
    public String templateName() default "";
    /** Holds any render attributes to extract from model and add to request */
    public String[] renderAttributes() default {};
}
