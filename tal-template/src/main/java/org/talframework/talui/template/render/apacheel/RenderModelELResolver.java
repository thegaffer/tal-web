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
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.MapELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

public class RenderModelELResolver extends ELResolver {
	private final ELResolver delegate = new MapELResolver();
	private final Map<String, Object> model;
	
	public RenderModelELResolver(Map<String, Object> model) {
		this.model = model;
	}
	
	/**
	 * @return the model
	 */
	public Map<String, Object> getModel() {
		return model;
	}
	
	/**
	 * Delegates to Map resolver
	 */
	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if( base != null ) return null;
		return delegate.getCommonPropertyType(context, base);
	}
	
	/**
	 * Delegates to Map resolver
	 */
	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		if( base != null ) return null;
		return delegate.getFeatureDescriptors(context, base);
	}
	
	/**
	 * Delegates to Map resolver
	 */
	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
		if( base != null ) return false;
		return delegate.isReadOnly(context, base, property);
	}
	
	/**
	 * Simply uses the map EL resolver internally on the model
	 * set in the constructor. If the base is not null then this
	 * method returns null
	 */
	@Override
	public Class<?> getType(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
		if( base != null ) return null;
		return delegate.getType(context, model, property);
	}
	
	/**
	 * Simply uses the map EL resolver internally on the model
	 * set in the constructor. If the base is not null then this
	 * method returns null
	 */
	@Override
	public Object getValue(ELContext context, Object base, Object property) throws NullPointerException, PropertyNotFoundException, ELException {
		if( base != null ) return null;
		return delegate.getValue(context, model, property);
	}
	
	/**
	 * Delegates to Map resolver
	 */
	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
		if( base != null ) return;
		delegate.setValue(context, model, property, value);
	}
}
