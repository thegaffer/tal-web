package org.talframework.talui.mvc.compiler;

import java.util.List;

import org.talframework.talui.mvc.Controller;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.MVCConfig;
import org.talframework.talui.mvc.config.ModelLayerConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.WindowConfig;

/**
 * This class serves up an appropriate compiler for a
 * class. This singleton class can be injected with
 * appropriate compilers before use.
 *
 * @author Tom Spencer
 */
public class CompilerFactorySingleton {
    private static final CompilerFactorySingleton INSTANCE = new CompilerFactorySingleton();
    
    /** Holds the possible app compilers */
    private List<ElementCompiler<AppConfig, Object>> appCompilers;
    /** Holds the possible page compilers */
    private List<ElementCompiler<PageConfig, AppConfig>> pageCompilers;
    /** Holds the possible window compilers */
    private List<ElementCompiler<WindowConfig, PageConfig>> windowCompilers;
    
    /** Holds the possible model compilers */
    private List<ElementCompiler<ModelLayerConfig, MVCConfig>> modelCompilers;
    /** Holds the possible controller compilers */
    private List<ElementCompiler<Controller, MVCConfig>> controllerCompilers;
    /** Holds the possible view compilers */
    private List<ElementCompiler<View, MVCConfig>> viewCompilers;
    
    /**
     * Hidden constructor that sets up the default annotation
     * based compilers.
     */
    private CompilerFactorySingleton() {
        
    }
    
    /**
     * @return The one and only (per classloader) compiler factory singleton instance
     */
    public static CompilerFactorySingleton getInstance() {
        return INSTANCE;
    }
    
    /**
     * @param cls The interface class to compile
     * @return The compiled app
     */
    public AppConfig compileApp(Class<?> cls) {
        return compile(cls, null, appCompilers);
    }
    
    /**
     * @param cls The interface class to compile
     * @param context The context or prefix for this element
     * @return The compiled page
     */
    public PageConfig compilePage(Class<?> cls, AppConfig context) {
        return compile(cls, context, pageCompilers);
    }
    
    /**
     * @param cls The interface class to compile
     * @param context The context or prefix for this element
     * @return The compiled window
     */
    public WindowConfig compileWindow(Class<?> cls, PageConfig context) {
        return compile(cls, context, windowCompilers);
    }
    
    /**
     * @param cls The interface class to compile
     * @param context The context or prefix for this element
     * @return The compiled model (or null if the interface cls is null)
     */
    public ModelLayerConfig compileModel(Class<?> cls, MVCConfig context) {
        if( cls == null ) return null;
        return compile(cls, context, modelCompilers);
    }
    
    /**
     * @param cls The interface class to compile
     * @param context The context or prefix for this element
     * @return The compiled controller
     */
    public Controller compileController(Class<?> cls, MVCConfig context) {
        return compile(cls, context, controllerCompilers);
    }
    
    /**
     * @param cls The interface class to compile
     * @param context The context or prefix for this element
     * @return The compiled view
     */
    public View compileView(Class<?> cls, MVCConfig context) {
        return compile(cls, context, viewCompilers);
    }
    
    
    ////////////////////////////////////////////////////
    // Generic Method
    
    /**
     * Finds the relevant compiler or throws a runtime exception
     */
    public <Element, Context> Element compile(Class<?> cls, Context context, List<ElementCompiler<Element, Context>> compilers) {
        ElementCompiler<Element, Context> compiler = null;
        
        // Find compiler
        for( ElementCompiler<Element, Context> c : compilers ) {
            if( c.canCompile(cls, context) ) {
                compiler = c;
                break;
            }
        }
        
        // Now compile
        if( compiler == null ) throw new IllegalArgumentException("Unable to compile class as no compiler found: " + cls);
        return compiler.compile(cls, context);
    }
    
    
    ////////////////////////////////////////////////////
    // Getter / Setter
    
    /**
     * @return the appCompilers
     */
    public List<ElementCompiler<AppConfig, Object>> getAppCompilers() {
        return appCompilers;
    }

    /**
     * Setter for the appCompilers field
     *
     * @param appCompilers the appCompilers to set
     */
    public void setAppCompilers(List<ElementCompiler<AppConfig, Object>> appCompilers) {
        this.appCompilers = appCompilers;
    }

    /**
     * @return the pageCompilers
     */
    public List<ElementCompiler<PageConfig, AppConfig>> getPageCompilers() {
        return pageCompilers;
    }

    /**
     * Setter for the pageCompilers field
     *
     * @param pageCompilers the pageCompilers to set
     */
    public void setPageCompilers(List<ElementCompiler<PageConfig, AppConfig>> pageCompilers) {
        this.pageCompilers = pageCompilers;
    }

    /**
     * @return the windowCompilers
     */
    public List<ElementCompiler<WindowConfig, PageConfig>> getWindowCompilers() {
        return windowCompilers;
    }

    /**
     * Setter for the windowCompilers field
     *
     * @param windowCompilers the windowCompilers to set
     */
    public void setWindowCompilers(List<ElementCompiler<WindowConfig, PageConfig>> windowCompilers) {
        this.windowCompilers = windowCompilers;
    }

    /**
     * @return the modelCompilers
     */
    public List<ElementCompiler<ModelLayerConfig, MVCConfig>> getModelCompilers() {
        return modelCompilers;
    }

    /**
     * Setter for the modelCompilers field
     *
     * @param modelCompilers the modelCompilers to set
     */
    public void setModelCompilers(List<ElementCompiler<ModelLayerConfig, MVCConfig>> modelCompilers) {
        this.modelCompilers = modelCompilers;
    }

    /**
     * @return the controllerCompilers
     */
    public List<ElementCompiler<Controller, MVCConfig>> getControllerCompilers() {
        return controllerCompilers;
    }

    /**
     * Setter for the controllerCompilers field
     *
     * @param controllerCompilers the controllerCompilers to set
     */
    public void setControllerCompilers(List<ElementCompiler<Controller, MVCConfig>> controllerCompilers) {
        this.controllerCompilers = controllerCompilers;
    }

    /**
     * @return the viewCompilers
     */
    public List<ElementCompiler<View, MVCConfig>> getViewCompilers() {
        return viewCompilers;
    }

    /**
     * Setter for the viewCompilers field
     *
     * @param viewCompilers the viewCompilers to set
     */
    public void setViewCompilers(List<ElementCompiler<View, MVCConfig>> viewCompilers) {
        this.viewCompilers = viewCompilers;
    }
}
