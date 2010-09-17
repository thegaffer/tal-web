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

package org.talframework.talui.mvc.spring.bind;

import org.springframework.beans.PropertyValue;

/**
 * This interface represents something that understands
 * how to combine fields together. An instance of this
 * is registered against submitted fields in an 
 * ObjectDataBinder. This object knows how to form the 
 * name of the new combined field and how to combine it.
 * It may simply return a string back as is, or may return
 * some other object that will convert itself to a string
 * when all the fields are combined. If an object is already
 * registered against the combined field it is supplied to 
 * the combiner - it can return the same object or a new one.
 * 
 * @author Tom Spencer
 */
public interface FieldCombiner {

	/**
	 * Called to get the new combined field name. If null field is ignored
	 * 
	 * @param pv The submitted property value
	 * @param attr The attribute (tip of the properties name)
	 * @return The name of the combined field (replacing the attr, rest of original member is preserved)
	 */
	public String getFieldName(PropertyValue pv, String attr);

	/**
	 * Called to get the combined field value. This is only set at the
	 * end so can be temporary.
	 *  
	 * @param pv The original property value
	 * @param attr The attribute (tip of properties name)
	 * @param existing The existing combination, if any
	 * @return The value of the combined field (so far)
	 */
	public Object getCombinedField(PropertyValue pv, String attr, Object existing);
	
}
