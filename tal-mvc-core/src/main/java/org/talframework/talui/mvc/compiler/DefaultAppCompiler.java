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

import java.lang.reflect.Method;
import java.util.Map;

import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.annotations.App;
import org.talframework.talui.mvc.annotations.PerformWhen;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.ModelLayerConfig;
import org.talframework.util.annotations.AnnotationUtils;
import org.talframework.util.properties.PropertyUtil;

/**
 * This class compiles a class marked with the {@link App}
 * annotation into a {@link AppConfig} instance.
 *
 * @author Tom Spencer
 */
public final class DefaultAppCompiler implements ElementCompiler<AppConfig, Object> {
    
    /**
     * Returns true if the app annotation is present
     */
    public boolean canCompile(Class<?> cls, Object context) {
        return cls.isAnnotationPresent(App.class);
    }
    
    /**
     * Compiles the class into an app config
     */
    public AppConfig compile(Class<?> cls, Object context) {
        if( cls == null ) throw new IllegalArgumentException("Cannot compile an app with a null object");
        
        // Create the app
        App appAnnotation = AnnotationUtils.getExpectedAnnotation(App.class, cls);
        String name = PropertyUtil.getString(appAnnotation.name(), true);
        if( name == null ) name = cls.getSimpleName();
        AppConfig ret = new AppConfig(name);
        
        // a. Compile Model
        Class<?> modelClass = AnnotationUtils.getAnnotationClass(appAnnotation.model(), Object.class);
        ModelLayerConfig model = CompilerFactorySingleton.getInstance().compileModel(modelClass, ret);
        if( model != null ) ret.setModel(model);
        
        // b. Add any controllers
        Map<Method, PerformWhen> controllers = AnnotationUtils.getAnnotatedMethods(PerformWhen.class, cls);
        for( Method method : controllers.keySet() ) {
            PerformWhen whenAnnotation = controllers.get(method);
            
            if( method.getParameterTypes() != null && method.getParameterTypes().length > 0 ) throw new IllegalArgumentException("App action method has parameters, this is not allowed: " + cls + method);
            Class<?> controller = PropertyUtil.getExpected(method.getReturnType(), "App {0} action {1} has no type: ", cls, method);
            String action = PropertyUtil.getString(whenAnnotation.value(), true);
            if( action == null ) action = method.getName();
            
            Controller ctrl = CompilerFactorySingleton.getInstance().compileController(controller, ret);
            ret.addController(action, ctrl);
        }
        
        // c. Add any common page events
        // TODO: Add page events
        
        return ret;
    }
}
