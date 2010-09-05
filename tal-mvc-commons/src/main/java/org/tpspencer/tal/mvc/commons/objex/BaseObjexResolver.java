package org.tpspencer.tal.mvc.commons.objex;

import org.talframework.objexj.Container;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.locator.SingletonContainerLocator;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.Model.ModelCleanupTask;
import org.tpspencer.tal.mvc.model.ModelResolver;

/**
 * This class is the base class for other objex resolvers.
 * Specifically this class takes care of getting the container
 * given either its ID or the name of a model attribute that
 * contains the container ID. The container may be retreived
 * read-only or editable. If it is editable then a cleanup
 * task is registered against 
 *
 * @author Tom Spencer
 */
public abstract class BaseObjexResolver implements ModelResolver {

    /** The container factory - if null uses the singleton locator */
    private ContainerFactory factory;
    /** The name of the model attribute of the container (or the fixed ID) */
    private String containerAttribute;
    /** If true the container attribute is the named of the container */
    private boolean fixedContainer = false;
    /** If true then the container should be opened */
    private boolean editable = false;
    /** If true and the container ID is null (after resolving) we create the container */
    private boolean createIfEmpty = false;
    /** If container is editable, the cleanup task will either suspend or save the container */
    private boolean suspendOnCleanup = true;
    
    /**
     * Default constructor
     */
    public BaseObjexResolver() {
    }

    /**
     * Helper constructor to construct in one line.
     * 
     * @param factory The factory
     * @param container The container attribute or id
     * @param fixed If true container represents the ID of the fixed container
     * @param editable If true container is opened read-only
     * @param create If true container is created if no containerId (must be a factory)
     * @param suspend If true the container suspends on cleanup
     */
    public BaseObjexResolver(ContainerFactory factory, String container, boolean fixed, boolean editable, boolean create, boolean suspend) {
        this.factory = factory;
        this.containerAttribute = container;
        this.fixedContainer = fixed;
        this.editable = editable;
        this.createIfEmpty = create;
        this.suspendOnCleanup = suspend;
    }
    
    public Container getContainer(Model model) {
        Container ret = null;
        
        String containerId = fixedContainer ? containerAttribute : null;
        if( !fixedContainer ) containerId = (String)model.getAttribute(containerAttribute);
        
        // TODO: See if this container has already been obtained on thread!
        
        if( editable ) {
            Container container = null;
            if( factory != null && containerId != null ) container = factory.open(containerId);
            else if( factory != null && containerId == null && createIfEmpty ) container = factory.create();
            else if( factory == null && containerId != null ) container = SingletonContainerLocator.getInstance().open(containerId);
            
            // Register cleanup task
            if( container != null ) {
                model.registerCleanupTask(new ContainerCleanupTask(container, containerAttribute, suspendOnCleanup), containerAttribute);
            }
            
            ret = container;
        }
        
        else if( containerId != null ) {
            if( factory != null ) ret = factory.get(containerId);
            else ret = SingletonContainerLocator.getInstance().get(containerId);
        }
        
        return ret;
    }

    /**
     * @return the factory
     */
    public ContainerFactory getFactory() {
        return factory;
    }

    /**
     * Setter for the factory field
     *
     * @param factory the factory to set
     */
    public void setFactory(ContainerFactory factory) {
        this.factory = factory;
    }

    /**
     * @return the containerAttribute
     */
    public String getContainerAttribute() {
        return containerAttribute;
    }

    /**
     * Setter for the containerAttribute field
     *
     * @param containerAttribute the containerAttribute to set
     */
    public void setContainerAttribute(String containerAttribute) {
        this.containerAttribute = containerAttribute;
    }

    /**
     * @return the fixedContainer
     */
    public boolean isFixedContainer() {
        return fixedContainer;
    }

    /**
     * Setter for the fixedContainer field
     *
     * @param fixedContainer the fixedContainer to set
     */
    public void setFixedContainer(boolean fixedContainer) {
        this.fixedContainer = fixedContainer;
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for the editable field
     *
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return the createIfEmpty
     */
    public boolean isCreateIfEmpty() {
        return createIfEmpty;
    }

    /**
     * Setter for the createIfEmpty field
     *
     * @param createIfEmpty the createIfEmpty to set
     */
    public void setCreateIfEmpty(boolean createIfEmpty) {
        this.createIfEmpty = createIfEmpty;
    }
    
    /**
     * This class is a cleanup task that will suspend or save
     * the container if it is open when the model attribute holding
     * the container ID is removed.
     *
     * @author Tom Spencer
     */
    private static class ContainerCleanupTask implements ModelCleanupTask {
        
        private final Container container;
        private final String containerAttribute;
        private final boolean suspend;
        
        public ContainerCleanupTask(Container container, String attribute, boolean suspend) {
            this.container = container;
            this.containerAttribute = attribute;
            this.suspend = suspend;
        }
        
        public void cleanup(Model model) {
            if( container.isOpen() ) {
                String containerId = suspend ? container.suspend() : container.saveContainer();
                model.setAttribute(containerAttribute, containerId);
            }
        }
    }
}
