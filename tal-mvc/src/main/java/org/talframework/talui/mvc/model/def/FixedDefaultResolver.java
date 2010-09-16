package org.tpspencer.tal.mvc.model.def;

import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.model.DefaultModelResolver;

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
