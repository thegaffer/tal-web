/*
 * Copyright 2008 Thomas Spencer
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

package org.tpspencer.tal.template.behaviour.property;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.behaviour.DynamicProperty;

/**
 * This interface extends the dynamic property to indicate
 * the property holds a date, or time, or both. This 
 * interface is used by the compilers mostly to extract
 * additional validation & by the renderers to extract 
 * formatting information for the property.
 * 
 * <p><b>Note: </b>This element is a secondary character
 * and is not used by the compiler to determine which
 * render mold to use.</p>
 * 
 * @author Tom Spencer
 */
public interface DateProperty extends DynamicProperty {

	/**
	 * @return True if the field does display a date (if false, just a time)
	 */
	public boolean isDate();
	
	/**
	 * @return True if the field does display a time (if false, just a date)
	 */
	public boolean isTime();
	
	/**
	 * This method is used at render time to get the
	 * formatted date value (excluding time).
	 *  
	 * @param model The current render model
	 * @return The formatted date value
	 */
	public String getDateValue(RenderModel model);
	
	/**
	 * The date format can be full, long, medium and short.
	 * 
	 * @return The date form
	 */
	public String getDateFormat(RenderModel model);
	
	/**
	 * The pattern to use for the date. This is calculated at
	 * render time as may be specific to the locale.
	 * 
	 * @param model
	 * @return The date pattern
	 */
	public String getDatePattern(RenderModel model);
	
	/**
	 * This method is used at render time to get the
	 * formatted time value (excluding date).
	 *  
	 * @param model The current render model
	 * @return The formatted time value
	 */
	public String getTimeValue(RenderModel model);
	
	/**
	 * The time format can be full, long, medium and short.
	 * 
	 * @return The time format
	 */
	public String getTimeFormat(RenderModel model);
	
	/**
	 * The pattern to use for the time. This is calculated at
	 * render time as may be specific to the locale.
	 * 
	 * @param model
	 * @return The time pattern
	 */
	public String getTimePattern(RenderModel model);
}
