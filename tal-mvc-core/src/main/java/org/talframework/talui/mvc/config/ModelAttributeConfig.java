/*
 * Copyright 2010 Thomas Spencer
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

package org.talframework.talui.mvc.config;

import org.talframework.talui.mvc.model.DefaultModelResolver;
import org.talframework.talui.mvc.model.ModelResolver;
import org.talframework.util.beans.BeanComparison;

/**
 * This class describes a model attribute sufficiently for
 * a framework implementation of Tal MVC to understand it.
 *
 * @author Tom Spencer
 */
public final class ModelAttributeConfig {

    /** The name of the attribute */
    private final String name;
    /** The name of this attribute in a lower level layer */
    private String proxyName = null;
    /** The type of attribute */
    private final Class<?> type;
    /** Determines if the attribute is simple */
    private final boolean simple;
    
    /** Holds the resolver that will get the attribute */
    private ModelResolver resolver = null;
    /** Holds the default value resolver to use when not held */
    private DefaultModelResolver defaultResolver = null;
    
    /** If true the attribute can have events on it */
    private boolean eventable = false;
    /** Holds the life-cycle of the attribute */
    private ModelAttributeLifecycle lifecycle = ModelAttributeLifecycle.PERSIST;
    
    public ModelAttributeConfig(String name, Class<?> type) {
        if( name == null ) throw new IllegalArgumentException("You must supply a name to an model attribute config");
        if( type == null ) throw new IllegalArgumentException("You must supply a type to an model attribute config");
        
        this.name = name;
        this.type = type;
        
        // Determine if simple
        if( resolver == null &&
                (type.isPrimitive() ||
                 Number.class.isAssignableFrom(type) ||
                 String.class.isAssignableFrom(type)) ) {
            this.simple = true;
        }
        else {
            this.simple = false;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }
    
    /**
     * @return the proxyName
     */
    public String getProxyName() {
        return proxyName;
    }

    /**
     * Setter for the proxyName field
     *
     * @param proxyName the proxyName to set
     */
    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    /**
     * @return the resolver
     */
    public ModelResolver getResolver() {
        return resolver;
    }

    /**
     * Setter for the resolver field
     *
     * @param resolver the resolver to set
     */
    public void setResolver(ModelResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * @return the defaultResolver
     */
    public DefaultModelResolver getDefaultResolver() {
        return defaultResolver;
    }

    /**
     * Setter for the defaultResolver field
     *
     * @param defaultResolver the defaultResolver to set
     */
    public void setDefaultResolver(DefaultModelResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    /**
     * @return the eventable
     */
    public boolean isEventable() {
        return eventable;
    }

    /**
     * Setter for the eventable field
     *
     * @param eventable the eventable to set
     */
    public void setEventable(boolean eventable) {
        this.eventable = eventable;
    }

    /**
     * @return the lifecycle
     */
    public ModelAttributeLifecycle getLifecycle() {
        return lifecycle;
    }

    /**
     * Setter for the lifecycle field
     *
     * @param lifecycle the lifecycle to set
     */
    public void setLifecycle(ModelAttributeLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }
    
    /**
     * @return the simple
     */
    public boolean isSimple() {
        return simple;
    }


    ///////////////////////////////////////////////////////
    // Standard

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("ModelAttributeConfig [");
        buf.append("name=").append(name);
        buf.append(", proxyName=").append(proxyName);
        buf.append(", type=").append(type);
        buf.append(", resolver=").append(resolver);
        buf.append(", defaultResolver=").append(defaultResolver);
        buf.append(", eventable=").append(eventable);
        buf.append(", lifecycle=").append(lifecycle);
        buf.append("]");

        return buf.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((proxyName == null) ? 0 : proxyName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
        result = prime * result + ((defaultResolver == null) ? 0 : defaultResolver.hashCode());
        result = prime * result + ((lifecycle == null) ? 0 : lifecycle.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        ModelAttributeConfig other = BeanComparison.basic(this, obj);
        boolean ret = other != null;
        if( ret && other != this ) {
            ret = BeanComparison.equals(ret, this.name, other.name);
            ret = BeanComparison.equals(ret, this.proxyName, other.proxyName);
            ret = BeanComparison.equals(ret, this.type, other.type);
            ret = BeanComparison.equals(ret, this.resolver, other.resolver);
            ret = BeanComparison.equals(ret, this.defaultResolver, other.defaultResolver);
            ret = BeanComparison.equals(ret, this.eventable, other.eventable);
            ret = BeanComparison.equals(ret, this.lifecycle, other.lifecycle);
        }
           
        return ret;
    }

    /**
     * This enum represents the various lifetimes of a model
     * attribute, from the short-lived flash, to the live until
     * explictly removed persist setting.
     *
     * @author Tom Spencer
     */
    public static enum ModelAttributeLifecycle {
        /** Indicates the attribute is cleared at end of action & render */
        FLASH,
        /** Indicates the attribute is cleared at end of render */
        RENDER,
        /** Indicates the attribute is clear when we receive the next action */
        ACTION,
        /** Indicates the attribute is persisted */
        PERSIST;
    }
}
