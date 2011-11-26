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
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.annotations.PerformWhen;
import org.talframework.talui.mvc.annotations.ShowWhen;
import org.talframework.talui.mvc.annotations.Window;
import org.talframework.talui.mvc.config.ModelLayerConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.util.annotations.AnnotationUtils;
import org.talframework.util.properties.PropertyUtil;

/**
 * This class compiles a class marked with the {@link Window}
 * annotation into a {@link WindowConfig} instance.
 *
 * @author Tom Spencer
 */
public final class DefaultWindowCompiler implements ElementCompiler<WindowConfig, PageConfig> {
    
    /**
     * Returns true if the window annotation is present
     */
    public boolean canCompile(Class<?> cls, PageConfig context) {
        return cls.isAnnotationPresent(Window.class);
    }
    
    /**
     * Compiles the class into an window config
     */
    public WindowConfig compile(Class<?> cls, PageConfig context) {
        if( cls == null ) throw new IllegalArgumentException("Cannot compile an app with a null object");
        
        // Create the page
        Window windowAnnotation = AnnotationUtils.getExpectedAnnotation(Window.class, cls);
        String name = PropertyUtil.getString(windowAnnotation.name(), true);
        if( name == null ) name = cls.getSimpleName();
        WindowConfig ret = new WindowConfig(context, name);
        
        // a. Compile Model
        Class<?> modelClass = AnnotationUtils.getAnnotationClass(windowAnnotation.model(), Object.class);
        ModelLayerConfig model = CompilerFactorySingleton.getInstance().compileModel(modelClass, ret);
        if( model != null ) ret.setModel(model);
        
        // b. Add in views
        Map<Method, ShowWhen> views = AnnotationUtils.getAnnotatedMethods(ShowWhen.class, cls);
        for( Method method : views.keySet() ) {
            ShowWhen viewAnnotation = views.get(method);
            
            if( method.getParameterTypes() != null && method.getParameterTypes().length > 0 ) throw new IllegalArgumentException("Window view method has parameters, this is not allowed: " + cls + method);
            Class<?> viewClass = PropertyUtil.getExpected(method.getReturnType(), "Window {0} view {1} has no type: ", cls, method);
            boolean defaultView = viewAnnotation.defaultView();
            String[] results = viewAnnotation.value();
            
            View view = CompilerFactorySingleton.getInstance().compileView(viewClass, ret);
            if( defaultView ) ret.setDefaultView(view);
            if( results != null && results.length > 0 ) {
                for( String result : results ) {
                    ret.addView(result, view);
                }
            }
        }
        if( ret.getDefaultView() == null ) throw new IllegalArgumentException("Window has no default view: " + cls);
        
        // c. Add any controllers
        Map<Method, PerformWhen> controllers = AnnotationUtils.getAnnotatedMethods(PerformWhen.class, cls);
        for( Method method : controllers.keySet() ) {
            PerformWhen whenAnnotation = controllers.get(method);
            
            if( method.getParameterTypes() != null && method.getParameterTypes().length > 0 ) throw new IllegalArgumentException("Page action method has parameters, this is not allowed: " + cls + method);
            Class<?> controller = PropertyUtil.getExpected(method.getReturnType(), "Page {0} action {1} has no type: ", cls, method);
            String action = PropertyUtil.getString(whenAnnotation.value(), true);
            if( action == null ) action = method.getName();
            
            Controller ctrl = CompilerFactorySingleton.getInstance().compileController(controller, ret);
            ret.addController(action, ctrl);
        }
        
        // d. Add any events
        // TODO: Add events
        
        return ret;
    }
}
