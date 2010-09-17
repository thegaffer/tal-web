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

package org.talframework.talui.mvc.window.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base attribute for all model attributes
 * 
 * Deprecated for now in favour of single Model Attr
 * 
 * @author Tom Spencer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Deprecated
public @interface ModelAttr {
	/** The name of the attribute */
	public String name() default "";
	/** The expected type (default is string if simple, otherwise plain object) */
	public Class<?> type() default Object.class;
	/** The resolver, without which the attributre is a simple attribute */
	public Class<?> resolver() default Object.class;
	/** Determines if attribute is only ever temporary within request */
	public boolean flash() default false;
	/** Determines if attributes can have events raised on it */
	public boolean evantable() default false;
	/** Determines if attribute should always be in render model for all views */
	public boolean autoRender() default false;
	/** Determines if attribute should be reset when the window gets an action */
	public boolean clearOnAction() default false;
	/** Determines if attribute should be cleared when we render */
	public boolean clearOnRender() default false;
	/** Determines if we expect the attribute to be alised to a page/app attr */
	public boolean aliasExpected() default false;
	/** Determines if the attribute can be aliased (default is true) */
	public boolean aliasable() default true;
}
