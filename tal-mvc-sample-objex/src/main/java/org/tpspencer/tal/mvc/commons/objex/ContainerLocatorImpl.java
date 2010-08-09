package org.tpspencer.tal.mvc.commons.objex;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.locator.ContainerFactory;

/**
 * This is a helper class that finds a container.
 * It will obtain the container using model attributes
 * holding the ID of the container to get and any
 * transaction the container is in.
 * 
 * TODO: The container is always opened, is this right!!!
 * 
 * @author Tom Spencer
 */
public class ContainerLocatorImpl implements ContainerLocator {

	private String containerAttribute = null;
	private boolean containerIsAbsolute = false;
	private ContainerFactory factory = null;
	
	public ContainerLocatorImpl() {
	}
	
	public ContainerLocatorImpl(String containerAttr, ContainerFactory factory) {
		this.containerAttribute = containerAttr;
		this.containerIsAbsolute = false;
		this.factory = factory;
	}
	
	public ContainerLocatorImpl(String containerAttr, boolean containerAbsolute, ContainerFactory factory) {
		this.containerAttribute = containerAttr;
		this.containerIsAbsolute = containerAbsolute;
		this.factory = factory;
	}
	
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.commons.objex.ContainerLocator#getContainer(java.lang.Object)
	 */
	public Container getContainer(Object obj) {
		String id = getContainerId(obj);
		return factory.open(id);
	}
	
	/* (non-Javadoc)
	 * @see org.tpspencer.tal.mvc.commons.objex.ContainerLocator#getEditableContainer(java.lang.Object)
	 */
	public EditableContainer getEditableContainer(Object obj) {
		String id = getContainerId(obj);
		return factory.open(id);
	}
	
	/**
	 * Helper to find the container id
	 * 
	 * @param obj The passed in reference to find the container within
	 * @return The container id
	 */
	private String getContainerId(Object obj) {
		if( containerIsAbsolute ) return containerAttribute;
		
		if( obj instanceof Model ) return (String)((Model)obj).getAttribute(containerAttribute);
		else if( obj instanceof Map<?, ?> ) {
			Object v = ((Map<?, ?>)obj).get(containerAttribute);
			return v != null ? v.toString() : null;
		}
		else {
			BeanWrapper bean = new BeanWrapperImpl(obj);
			if( bean.isReadableProperty(containerAttribute) ) {
				return (String)bean.getPropertyValue(containerAttribute);
			}
			else {
				throw new IllegalArgumentException("Cannot locate container because containerId [" + containerAttribute + "] is not found on passed on reference: " + obj);
			}
		}
	}

	/**
	 * @return the containerAttribute
	 */
	public String getContainerAttribute() {
		return containerAttribute;
	}

	/**
	 * @param containerAttribute the containerAttribute to set
	 */
	public void setContainerAttribute(String containerAttribute) {
		this.containerAttribute = containerAttribute;
	}

	/**
	 * @return the containerIsAbsolute
	 */
	public boolean isContainerIsAbsolute() {
		return containerIsAbsolute;
	}

	/**
	 * @param containerIsAbsolute the containerIsAbsolute to set
	 */
	public void setContainerIsAbsolute(boolean containerIsAbsolute) {
		this.containerIsAbsolute = containerIsAbsolute;
	}

	/**
	 * @return the factory
	 */
	public ContainerFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(ContainerFactory factory) {
		this.factory = factory;
	}
}
