package org.talframework.talui.mvc.compiler;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.annotations.App;
import org.talframework.talui.mvc.annotations.PerformWhen;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.MVCConfig;
import org.talframework.talui.mvc.config.ModelLayerConfig;

/**
 * Tests out the default app compiler
 *
 * @author Tom Spencer
 */
public class TestDefaultAppCompiler {
    
    private Mockery context = new JUnit4Mockery();
    
    private DefaultAppCompiler underTest = null;
    private ElementCompiler<Controller, MVCConfig> controllerCompiler = null;
    private ElementCompiler<ModelLayerConfig, MVCConfig> modelCompiler = null;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        underTest = new DefaultAppCompiler();
        controllerCompiler = context.mock(ElementCompiler.class, "controllerCompiler");
        modelCompiler = context.mock(ElementCompiler.class, "modelCompiler");
        
        // Set our mock compilers
        List<ElementCompiler<Controller, MVCConfig>> controllerCompilers = new ArrayList<ElementCompiler<Controller,MVCConfig>>();
        controllerCompilers.add(controllerCompiler);
        CompilerFactorySingleton.getInstance().setControllerCompilers(controllerCompilers);
        
        List<ElementCompiler<ModelLayerConfig, MVCConfig>> modelCompilers = new ArrayList<ElementCompiler<ModelLayerConfig, MVCConfig>>();
        modelCompilers.add(modelCompiler);
        CompilerFactorySingleton.getInstance().setModelCompilers(modelCompilers);
    }
    
    /**
     * Tests basic compilation
     */
    @Test
    public void basic() {
        Assert.assertTrue(underTest.canCompile(TestApp.class, null));

        final Controller controller = context.mock(Controller.class);
        context.checking(new Expectations() {{
            exactly(2).of(controllerCompiler).canCompile(with(Object.class), with(any(AppConfig.class)));
                will(returnValue(true));
            exactly(2).of(controllerCompiler).compile(with(Object.class), with(any(AppConfig.class)));
                will(returnValue(controller));
        }});
        
        AppConfig app = underTest.compile(TestApp.class, null);
        Assert.assertNotNull(app);
        Assert.assertEquals("TestApp", app.getName());
        Assert.assertNull(app.getParent());
        // TODO: Test Model
        Assert.assertEquals(2, app.getControllers().size());
        Assert.assertNotNull(app.getControllers().get("test"));
        Assert.assertNotNull(app.getControllers().get("onAnotherAction"));
        // TODO: Page Events
        
        context.assertIsSatisfied();
    }
    
    /**
     * Tests an app interface when the annotation has a name.
     * This also tests branch with no controllers
     */
    @Test
    public void withName() {
        AppConfig app = underTest.compile(TestAppName.class, null);
        Assert.assertNotNull(app);
        Assert.assertEquals("Different", app.getName());
    }

    /**
     * Ensures we fail without a class
     */
    @Test(expected=IllegalArgumentException.class)
    public void nullClass() {
        underTest.compile(null, null);
    }
    
    /**
     * Ensures we fail if no annotation
     */
    @Test
    public void noAnnotation() {
        Assert.assertFalse(underTest.canCompile(TestAppInvalid.class, null));
    }
    
    /**
     * Ensure fails if controller has args
     */
    @Test(expected=IllegalArgumentException.class)
    public void controllerWithArgs() {
        underTest.compile(TestAppControllerArgs.class, null);
    }
    
    /**
     * Ensure fails if controller has void return
     */
    @Test(expected=IllegalArgumentException.class)
    public void controllerNoReturn() {
        underTest.compile(TestAppControllerRet.class, null);
    }
    
    
    
    /**
     * Completely valid test app interface to compile
     */
    @App
    private static interface TestApp {
        @PerformWhen(value="test")
        public Object onAction();
        
        @PerformWhen
        public Object onAnotherAction();
    }
    
    /**
     * Completely valid test app interface to compile
     */
    @App(name="Different")
    private static interface TestAppName {
        
    }
    
    /**
     * Invalid app as controller method takes arguments
     */
    @App
    private static interface TestAppControllerArgs {
        @PerformWhen(value="test")
        public Object onAction(String val);
    }
    
    /**
     * Invalid app as controller method returns nothing
     */
    @App
    private static interface TestAppControllerRet {
        @PerformWhen(value="test")
        public void onAction(String val);
    }
    
    /**
     * An invalid app interface
     */
    private static interface TestAppInvalid {
        
    }
}
