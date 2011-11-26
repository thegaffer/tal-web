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

import java.util.HashMap;
import java.util.Map;

import org.talframework.util.beans.BeanComparison;

/**
 * This class describes a layer of the model found against
 * either an app, page, window or view.
 *
 * @author Tom Spencer
 */
public final class ModelLayerConfig {

    /** Holds the name of the layer */
    private final String name;
    /** Holds the interface type */
    private final Class<?> layerType;
    /** Holds the attributes */
    private Map<String, ModelAttributeConfig> attributes;
    
    public ModelLayerConfig(String name) {
        if( name == null ) throw new IllegalArgumentException("You must supply a name to an model layer config");
        
        this.name = name;
        this.layerType = null;
    }
    
    public ModelLayerConfig(String name, Class<?> type) {
        if( name == null ) throw new IllegalArgumentException("You must supply a name to an model layer config");
        if( !type.isInterface() ) throw new IllegalArgumentException("The model layer type must be an interface");
        
        this.name = name;
        this.layerType = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return The layer interface type
     */
    public Class<?> getLayerType() {
        return layerType;
    }

    /**
     * @return the attributes
     */
    public Map<String, ModelAttributeConfig> getAttributes() {
        return attributes;
    }
    
    /**
     * Call to find a given attribute by name
     * 
     * @param name The name of the attribute
     * @return The attribute
     */
    public ModelAttributeConfig findAttribute(String name) {
        return attributes != null ? attributes.get(name) : null;
    }

    /**
     * Setter for the attributes field
     *
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, ModelAttributeConfig> attributes) {
        this.attributes = attributes;
    }
    
    /**
     * Adds an attribute to this layer
     * 
     * @param attribute The attribute to add
     */
    public void addAttribute(ModelAttributeConfig attribute) {
        if( this.attributes == null ) this.attributes = new HashMap<String, ModelAttributeConfig>();
        this.attributes.put(attribute.getName(), attribute);
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
        buf.append("ModelLayerConfig [");
        buf.append("name=").append(name);
        buf.append(", layerType=").append(layerType);
        buf.append(", attributes=").append(attributes);
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
        result = prime * result + ((layerType == null) ? 0 : layerType.hashCode());
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        ModelLayerConfig other = BeanComparison.basic(this, obj);
        boolean ret = other != null;
        if( ret && other != this ) {
            ret = BeanComparison.equals(ret, this.name, other.name);
            ret = BeanComparison.equals(ret, this.layerType, other.layerType);
            ret = BeanComparison.equals(ret, this.attributes, other.attributes);
        }
           
        return ret;
    }
}
