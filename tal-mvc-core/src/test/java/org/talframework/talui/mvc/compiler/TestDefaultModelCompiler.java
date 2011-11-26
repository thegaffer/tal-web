package org.talframework.talui.mvc.compiler;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.annotations.model.Model;
import org.talframework.talui.mvc.annotations.model.ModelAttribute;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.ModelLayerConfig;
import org.talframework.talui.mvc.config.ModelAttributeConfig.ModelAttributeLifecycle;

/**
 * This class tests we can compile an interface representing a model
 *
 * @author Tom Spencer
 */
public class TestDefaultModelCompiler {
    
    private Mockery context = new JUnit4Mockery();
    
    private DefaultModelCompiler underTest = null;
    
    @Before
    public void setup() {
        underTest = new DefaultModelCompiler();
    }

    @Test(expected=IllegalArgumentException.class)
    public void noClass() {
        underTest.compile(null, null);
    }
    
    @Test
    public void basic() {
        Assert.assertTrue(underTest.canCompile(SimpleModel.class, null));
        
        ModelLayerConfig config = underTest.compile(SimpleModel.class, null);
        Assert.assertNotNull(config);
        Assert.assertEquals(expected, actual)
        Assert.assertNotNull(config.findAttribute("id"));
        Assert.assertTrue(config.findAttribute("id").isSimple());
        Assert.assertEquals(String.class, config.findAttribute("id").getType());
        Assert.assertNotNull(config.findAttribute("app"));
        Assert.assertFalse(config.findAttribute("app").isSimple());
        Assert.assertEquals(AppConfig.class, config.findAttribute("app").getType());
        Assert.assertNotNull(config.findAttribute("page"));
        Assert.assertEquals(long.class, config.findAttribute("page").getType());
        Assert.assertTrue(config.findAttribute("page").isEventable());
        // Auto Render: Assert.assertTrue(config.findAttribute("id").is());
        Assert.assertNotNull(config.findAttribute("pageFlash"));
        Assert.assertTrue(config.findAttribute("pageFlash").getLifecycle() == ModelAttributeLifecycle.FLASH);
        Assert.assertNotNull(config.findAttribute("pageRender"));
        Assert.assertTrue(config.findAttribute("pageRender").getLifecycle() == ModelAttributeLifecycle.RENDER);
        Assert.assertNotNull(config.findAttribute("pageError"));
        Assert.assertTrue(config.findAttribute("pageError").getLifecycle() == ModelAttributeLifecycle.ACTION);
    }
    
    //////////////////////////////////////
    // Test Interfaces
    
    private static interface SimpleModel {
        
        public String getId();
        
        public AppConfig getApp();
        
        @ModelAttribute(eventable=true, autoRender=true)
        public long getPage();
        
        @ModelAttribute(eventable=true, flash=true)
        public long getPageFlash();
        
        @ModelAttribute(eventable=true, flashFirstRender=true)
        public long getPageRender();
        
        @ModelAttribute(eventable=true, flashAction=true)
        public long getPageError();
    }
    
    @Model(name="SomeModel")
    private static interface NamedModel {
        
        public String getId();
    }
    
    private static interface AliasModel {
    
        
        public String getId();
    }
}
