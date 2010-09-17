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

package org.talframework.talui.mvc.commons.objex;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.talframework.objexj.Container;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.talui.mvc.Model;

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
	 * @see org.talframework.talui.mvc.commons.objex.ContainerLocator#getContainer(java.lang.Object)
	 */
	public Container getContainer(Object obj) {
		String id = getContainerId(obj);
		return factory.get(id);
	}
	
	/* (non-Javadoc)
	 * @see org.talframework.talui.mvc.commons.objex.ContainerLocator#getEditableContainer(java.lang.Object)
	 */
	public Container getOpenContainer(Object obj) {
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
