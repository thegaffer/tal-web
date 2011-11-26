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

package org.talframework.talui.mvc.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.talframework.talui.mvc.annotations.controller.Action;
import org.talframework.talui.mvc.annotations.controller.Controller;
import org.talframework.talui.mvc.annotations.test.SampleApp;
import org.talframework.talui.mvc.annotations.test.model.SampleAppModel;
import org.talframework.talui.mvc.annotations.test.model.SamplePageModel;
import org.talframework.talui.mvc.annotations.test.model.SampleTransactionModel;
import org.talframework.talui.mvc.annotations.test.model.SampleWindowModel;
import org.talframework.talui.mvc.annotations.test.samplepage.CustomView;
import org.talframework.talui.mvc.annotations.test.samplepage.SampleController;
import org.talframework.talui.mvc.annotations.test.samplepage.SamplePage;
import org.talframework.talui.mvc.annotations.test.samplepage.SampleView;
import org.talframework.talui.mvc.annotations.test.samplepage.SampleWindow;
import org.talframework.talui.mvc.annotations.view.View;

/**
 * Simple test class that just ensures the annotations are
 * present. In reality not really testing nothing more than
 * whether the annotations can be used on the intended
 * targets.
 *
 * @author Tom Spencer
 */
public class TestAnnotations {

    @Test
    public void app() {
        App appAnnotation = findAnnotationOnClass(SampleApp.class, App.class);
        Assert.assertNotNull(appAnnotation);
        Assert.assertEquals(SampleAppModel.class, appAnnotation.model());
    }
    
    @Test
    public void page() {
        Page pageAnnotation = findAnnotationOnClass(SamplePage.class, Page.class);
        Assert.assertNotNull(pageAnnotation);
        Assert.assertEquals(SamplePageModel.class, pageAnnotation.model());
        
        PageWindow window1 = findAnnotationOnMethod(SamplePage.class, PageWindow.class, "getWindow1");
        Assert.assertNotNull(window1);
        Assert.assertEquals("win1", window1.shortName());
        Assert.assertNotNull(findAnnotationOnMethod(SamplePage.class, PageWindow.class, "getWindow2"));
        
        PerformWhen ctrl1 = findAnnotationOnMethod(SamplePage.class, PerformWhen.class, "getSubmitForm");
        Assert.assertNotNull(ctrl1);
        Assert.assertEquals("", ctrl1.value());
    }
    
    @Test
    public void window() {
        Window windowAnnotation = findAnnotationOnClass(SampleWindow.class, Window.class);
        Assert.assertNotNull(windowAnnotation);
        Assert.assertEquals(SampleWindowModel.class, windowAnnotation.model());
        
        ShowWhen show1 = findAnnotationOnMethod(SampleWindow.class, ShowWhen.class, "getSampleView");
        Assert.assertNotNull(show1);
        Assert.assertEquals(2, show1.value().length);
        Assert.assertNotNull(findAnnotationOnMethod(SampleWindow.class, ShowWhen.class, "getCustomView"));
        
        
        PerformWhen ctrl1 = findAnnotationOnMethod(SampleWindow.class, PerformWhen.class, "getOnSubmitForm");
        Assert.assertNotNull(ctrl1);
        Assert.assertEquals("/submitForm", ctrl1.value());
    }
    
    @Test
    public void simpleView() {
        View viewAnnotation = findAnnotationOnClass(SampleView.class, View.class);
        Assert.assertNotNull(viewAnnotation);
        Assert.assertEquals(Object.class, viewAnnotation.model());
        Assert.assertEquals("/jsp/simple.jsp", viewAnnotation.templateName());
        Assert.assertEquals(2, viewAnnotation.renderAttributes().length);
    }
    
    @Test
    public void complexView() {
        View viewAnnotation = findAnnotationOnClass(CustomView.class, View.class);
        Assert.assertNotNull(viewAnnotation);
        Assert.assertEquals(SampleTransactionModel.class, viewAnnotation.model());
        Assert.assertEquals("/jsp/custom.jsp", viewAnnotation.templateName());
        Assert.assertEquals(0, viewAnnotation.renderAttributes().length);
    }
    
    @Test
    public void controller() {
        Controller ctrlAnnotation = findAnnotationOnClass(SampleController.class, Controller.class);
        Assert.assertNotNull(ctrlAnnotation);
        
        Action action = findAnnotationOnMethod(SampleController.class, Action.class, "onTest", String.class, Object.class);
        Assert.assertNotNull(action);
    }
    
    @Test
    @Ignore("Not yet added model annotations")
    public void model() {
        
    }
    
    /**
     * Helper to get the annotation on a class without casting etc
     */
    private <T extends Annotation> T findAnnotationOnClass(Class<?> type, Class<T> annotation) {
        return type.getAnnotation(annotation);
    }
    
    /**
     * Helper to get an annotation on a method with casting etc
     */
    private <T extends Annotation> T findAnnotationOnMethod(Class<?> type, Class<T> annotation, String method, Class<?>... paramTypes) {
        T ret = null;
        
        try {
            Method m = type.getDeclaredMethod(method, paramTypes);
            if( m != null ) ret = m.getAnnotation(annotation); 
        }
        catch( Exception e ) {
            // Ignore return is null
        }
        
        return ret;
    }
}
