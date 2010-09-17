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

package org.talframework.talui.mvc.model.def;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.model.DefaultModelResolver;

/**
 * This simple class holds a single value that is considered
 * the default value to use.
 * 
 * @author Tom Spencer
 */
public final class FixedDefaultResolver implements DefaultModelResolver {
	
	/** Holds the fixed default value */
	public final Object value;
	
	public FixedDefaultResolver(Object value) {
		if( value == null ) throw new IllegalArgumentException("The default value is null");
		this.value = value;
	}

	/**
	 * Returns the values type
	 */
	public Class<?> getType() {
		return value.getClass();
	}
	
	/**
	 * Simply returns value
	 */
	public Object getDefault(Model model) {
		return value;
	}
	
	@Override
	public String toString() {
	    return value.toString();
	}

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        FixedDefaultResolver other = (FixedDefaultResolver)obj;
        if( value == null ) {
            if( other.value != null ) return false;
        }
        else if( !value.equals(other.value) ) return false;
        return true;
    }
}
