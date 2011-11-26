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
import org.talframework.talui.mvc.annotations.Page;
import org.talframework.talui.mvc.annotations.PageWindow;
import org.talframework.talui.mvc.annotations.PerformWhen;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.ModelLayerConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.util.annotations.AnnotationUtils;
import org.talframework.util.properties.PropertyUtil;

/**
 * This class compiles a class marked with the {@link Page}
 * annotation into a {@link PageConfig} instance.
 *
 * @author Tom Spencer
 */
public final class DefaultPageCompiler implements ElementCompiler<PageConfig, AppConfig> {
    
    /**
     * Returns true if the page annotation is present
     */
    public boolean canCompile(Class<?> cls, AppConfig context) {
        return cls.isAnnotationPresent(Page.class);
    }
    
    /**
     * Compiles the class into an page config
     */
    public PageConfig compile(Class<?> cls, AppConfig context) {
        if( cls == null ) throw new IllegalArgumentException("Cannot compile a page with a null object");
        
        // Create the page
        Page pageAnnotation = AnnotationUtils.getExpectedAnnotation(Page.class, cls);
        String name = PropertyUtil.getString(pageAnnotation.name(), true);
        if( name == null ) name = cls.getSimpleName();
        String template = PropertyUtil.getString(pageAnnotation.template(), true);
        PageConfig ret = new PageConfig(context, name, template);
        
        // a. Compile Model
        Class<?> modelClass = AnnotationUtils.getAnnotationClass(pageAnnotation.model(), Object.class);
        ModelLayerConfig model = CompilerFactorySingleton.getInstance().compileModel(modelClass, ret);
        if( model != null ) ret.setModel(model);
        
        // b. Add any page level controllers
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
        
        // c. Add any common page events
        // TODO: Add page events
        
        // d. Add in any windows
        Map<Method, PageWindow> windows = AnnotationUtils.getAnnotatedMethods(PageWindow.class, cls);
        for( Method method : windows.keySet() ) {
            PageWindow windowAnnotation = windows.get(method);
            
            if( method.getParameterTypes() != null && method.getParameterTypes().length > 0 ) throw new IllegalArgumentException("Page window method has parameters, this is not allowed: " + cls + method);
            Class<?> windowClass = PropertyUtil.getExpected(method.getReturnType(), "Page {0} window {1} has no type: ", cls, method);
            
            WindowConfig window = CompilerFactorySingleton.getInstance().compileWindow(windowClass, ret);
            window.setNamespace(PropertyUtil.getString(windowAnnotation.shortName(), true));
            ret.addWindow(window);
        }
        
        return ret;
    }
}
