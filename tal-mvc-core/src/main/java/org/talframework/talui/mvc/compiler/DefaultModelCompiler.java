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

package org.talframework.talui.mvc.compiler;

import org.talframework.talui.mvc.annotations.model.DefaultResolver;
import org.talframework.talui.mvc.annotations.model.Model;
import org.talframework.talui.mvc.annotations.model.ModelAttribute;
import org.talframework.talui.mvc.annotations.model.ModelResolver;
import org.talframework.talui.mvc.config.MVCConfig;
import org.talframework.talui.mvc.config.ModelAttributeConfig;
import org.talframework.talui.mvc.config.ModelLayerConfig;
import org.talframework.talui.mvc.config.ModelAttributeConfig.ModelAttributeLifecycle;
import org.talframework.talui.mvc.model.DefaultModelResolver;
import org.talframework.util.beans.BeanDefinition;
import org.talframework.util.beans.definition.BeanDefinitionsSingleton;
import org.talframework.util.properties.PropertyUtil;

/**
 * This class compiles a class indicates as a Model into
 * a {@link ModelLayerConfig} object.
 *
 * @author Tom Spencer
 */
public final class DefaultModelCompiler implements ElementCompiler<ModelLayerConfig, MVCConfig> {
    
    /**
     * Returns true if the app annotation is present
     */
    public boolean canCompile(Class<?> cls, MVCConfig context) {
        // TODO:
        return true;
    }
    
    /**
     * Compiles the class into an {@link ModelLayerConfig}
     */
    public ModelLayerConfig compile(Class<?> cls, MVCConfig context) {
        if( cls == null ) throw new IllegalArgumentException("Cannot compile an model with a null object");
        
        Model modelAnnotation = cls.getAnnotation(Model.class);
        String name = modelAnnotation != null ? PropertyUtil.getString(modelAnnotation.name(), true) : null;
        if( name == null ) name = cls.getSimpleName();
        ModelLayerConfig ret = new ModelLayerConfig(name);
        
        BeanDefinition def = BeanDefinitionsSingleton.getInstance().getDefinition(cls);
        for( String prop : def.getProperties() ) {
            if( !def.canRead(prop) ) continue; // property must be readable to be considered
            
            Class<?> type = def.getPropertyType(prop);
            ModelAttributeConfig attr = new ModelAttributeConfig(prop, type);
            
            ModelAttribute attrAnnotation = def.getReadAnnotation(prop, ModelAttribute.class);
            attr.setEventable(isEventable(attr, attrAnnotation, context));
            attr.setLifecycle(getLifecycle(attr, attrAnnotation, context));
            attr.setProxyName(findProxyName(attr, attrAnnotation, context));
            
            ModelResolver resolver = def.getReadAnnotation(prop, ModelResolver.class);
            if( resolver != null ) attr.setResolver(getResolver(resolver));
            
            DefaultResolver defaultResolver = def.getReadAnnotation(prop, DefaultResolver.class);
            if( defaultResolver != null ) attr.setDefaultResolver(getResolver(defaultResolver));
            
            ret.addAttribute(attr);
        }
        
        return ret;
    }
    
    /**
     * Helper to determine if attribute is eventable
     */
    private boolean isEventable(ModelAttributeConfig attr, ModelAttribute attrAnnotation, MVCConfig context) {
        if( attrAnnotation != null && attrAnnotation.eventable() ) return true;
        // TODO: Should there be some that are eventable by default??
        return false;
    }
    
    /**
     * Helper to determine lifecycle
     */
    private ModelAttributeLifecycle getLifecycle(ModelAttributeConfig attr, ModelAttribute attrAnnotation, MVCConfig context) {
        ModelAttributeLifecycle ret = null;
        
        if( attrAnnotation == null ) ret = ModelAttributeLifecycle.PERSIST;
        else if( attrAnnotation.flash() ) ret = ModelAttributeLifecycle.FLASH;
        else if( attrAnnotation.flashFirstRender() ) ret = ModelAttributeLifecycle.RENDER;
        else if( attrAnnotation.flashAction() ) ret = ModelAttributeLifecycle.ACTION;
        else ret = ModelAttributeLifecycle.PERSIST;
        
        return ret;
    }
    
    /**
     * Helper to find the proxy name for an attribute
     */
    private String findProxyName(ModelAttributeConfig attr, ModelAttribute attrAnnotation, MVCConfig context) {
        ModelAttributeConfig ret = null;
        
        ModelLayerConfig parentLayer = null;
        if( context != null && context.getParent() != null ) parentLayer = context.getParent().getModel();

        if( parentLayer != null ) {
            for( String parentAttribute : context.getModel().getAttributes().keySet() ) {
                ModelAttributeConfig parentAttr = context.getModel().getAttributes().get(parentAttribute);
                
                if( !parentAttr.getType().equals(attr.getType()) ) continue;
                
                if( attr.getName().equals(parentAttr.getName()) ) ret = parentAttr;
                else if( attrAnnotation != null ) {
                    for( String alias : attrAnnotation.aliases() ) {
                        ModelAttributeConfig candidate = parentLayer.findAttribute(alias);
                        if( candidate != null && attr.getType().isAssignableFrom(candidate.getType()) ) {
                            ret = candidate;
                            break;
                        }
                    }
                }
            }
        }

        // If expecting alias, fail
        if( ret == null && attrAnnotation != null && attrAnnotation.expectAlias() ) {
            throw new IllegalArgumentException("The attribute [" + attr.getName() + "] expected to be aliased/proxied to higher level attribute");
        }
        
        return ret != null ? ret.getName() : null;
    }
    
    /**
     * Helper to get the model resolver
     * 
     * TODO: Class does not need to implement interface
     */
    private org.talframework.talui.mvc.model.ModelResolver getResolver(ModelResolver resolver) {
        try {
            Object ret = resolver.value().newInstance();
            return org.talframework.talui.mvc.model.ModelResolver.class.cast(ret);
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Helper to get the default resolver
     * 
     * TODO: Class does not need to implement interface
     */
    private DefaultModelResolver getResolver(DefaultResolver resolver) {
        try {
            Object ret = resolver.value().newInstance();
            return DefaultModelResolver.class.cast(ret);
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }
}
