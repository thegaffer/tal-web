/*
 * Copyright 2008 Thomas Spencer
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

package org.tpspencer.tal.template.render.apacheel;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

import org.tpspencer.tal.template.RenderNode;

/**
 * This class is an ELResolver that resolvers the current node.
 * If the base is null and the property is 'this' or 'parent'
 * then this class returns the object from the current node or
 * from the current nodes parent.
 * 
 * @author Tom Spencer
 */
public class RenderNodeELResolver extends ELResolver {
	private RenderNode current = null;
	
	/**
	 * @return the current
	 */
	public RenderNode getCurrent() {
		return current;
	}
	
	/**
	 * @param current the current to set
	 */
	public void setCurrent(RenderNode current) {
		this.current = current;
	}
	
	/**
	 * Simply returns null
	 */
	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return null;
	}
	
	/**
	 * Simply returns null
	 */
	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}
	
	/**
	 * Always just returns null. You cannot set a nodes bean
	 */
	@Override
	public Class<?> getType(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
		return null;
	}
	
	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
		if( base == null && current != null &&
				("this".equals(property) ||
				 "parent".equals(property)) ) {
			context.setPropertyResolved(true);
			return false;
		}

		return false;
	}
	
	/**
	 * Determines if the object is 'this' or 'parent' and
	 * if so returns from the current node. Otherwise uses
	 * the model using the delegate Map resolver.
	 */
	@Override
	public Object getValue(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
		if( base != null ) return null;
		if( current == null ) return null;
		
		Object ret = null;
		if( "this".equals(property) && current != null ) {
			context.setPropertyResolved(true);
			ret = current.getObject();
		}
		else if( "parent".equals(property) && 
				current != null &&
				current.getParentNode() != null ) {
			context.setPropertyResolved(true);
			ret = current.getParentNode().getObject();
		}
		
		return ret;
	}
	
	/**
	 * Throws an exception because we cannot change nodes during
	 * rendering. You could possibly change a beans value, but this
	 * is not via this resolver.
	 */
	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
		if( base == null && current != null &&
				("this".equals(property) ||
				 "parent".equals(property)) ) {
			throw new PropertyNotWritableException("Cannot change a node value during rendering");
		}
	}
}
