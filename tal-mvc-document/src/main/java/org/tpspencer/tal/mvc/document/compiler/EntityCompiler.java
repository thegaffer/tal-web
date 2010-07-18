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

package org.tpspencer.tal.mvc.document.compiler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.AppElementImpl;

/**
 * This class compiles an entity in the application into an
 * {@link AppElement}.
 * 
 * TODO: Accessors for dynamic info
 * 
 * @author Tom Spencer
 */
public class EntityCompiler {
	private static final Log logger = LogFactory.getLog(EntityCompiler.class);

	/** The type of entity we are compiling */
	private final Class<?> expected;
	/** Holds the type of this element in contents or parent app element */
	private final String type;
	/** Determines if this entity is a root element or not in the contents */
	private final boolean isRoot;
	/** The compiler for the ID property (mandatory) */
	private PropertyAccessor idCompiler;
	/** The accessors for any extra info */
	private Map<String, PropertyAccessor> infoAccessors;
	/** Holds any default parameters */
	private Map<String, String> defaultParameters;
	/** Holds the child compilers */
	private ChildCompiler[] childCompilers;
	/** The specification indexes to set on the element */
	private Map<String, Integer> specIndexes;
	
	public EntityCompiler(String type, boolean root, Class<?> expected) {
		this.type = type;
		this.isRoot = root;
		this.expected = expected;
	}
	
	/**
	 * Internal check that derived classes can hook into to
	 * stop compilation if required
	 * 
	 * @param obj The object we are about to compile
	 * @return True if compilation should continue, false otherwise
	 */
	protected boolean checkCompile(Object obj) {
		return true;
	}
	
	/**
	 * Call to compile the given element
	 * 
	 * @param compiler The compiler
	 * @param app The current application contents
	 * @param parent The parent element to this one
	 * @param obj The object to compile
	 * @param name The name of the object in relation to its parent (often null)
	 */
	public void compile(AppCompiler compiler, AppElement app, AppElement parent, Object obj, String name) {
		if( !checkCompile(obj) ) return;
		
		if( logger.isTraceEnabled() ) logger.trace(">>> Compiling [" + name + "]: " + obj);
		String id = idCompiler.getProperty(compiler, expected, obj, name);
		if( logger.isTraceEnabled() ) logger.trace("\tID=" + id);
		
		// Compile parameters
		Map<String, String> params = defaultParameters != null ? new HashMap<String, String>(defaultParameters) : null;
		if( infoAccessors != null ) {
			if( params == null ) params = new HashMap<String, String>();
			Iterator<String> it = infoAccessors.keySet().iterator();
			while( it.hasNext() ) {
				String n = it.next();
				String val = infoAccessors.get(n).getProperty(compiler, expected, obj, name);
				if( val != null ) params.put(n, val);
			}
		}
		
		createElement(compiler, app, parent, obj, id, params);
	}
	
	/**
	 * Helper to actually construct the app element. The default
	 * will create against the app or the parent depending if its
	 * set to root or not. Child compilers will only be run if we
	 * have not seen the object before during construction.
	 * 
	 * @param compiler The compiler
	 * @param app The app element
	 * @param parent The parent element
	 * @param obj The object in question
	 * @param id The ID of the object
	 * @param params The parameters
	 * @return The new element
	 */
	protected AppElement createElement(AppCompiler compiler, AppElement app, AppElement parent, Object obj, String id, Map<String, String> params) {
		// Create or update element
		boolean children = true;
		AppElement current = isRoot ? app.getChild(type, id) : parent.getChild(type, id);
		if( current == null ) {
			current = new AppElementImpl(id, obj, params);
			if( specIndexes != null ) current.setSpecIndexes(specIndexes);
			if( isRoot ) app.add(type, current);
			else parent.add(type, current);
		}
		// Ensure all info set in other object
		else if( params != null ) {
			if( current.getElement() == null ) {
				current.addElement(obj);
				Map<String, String> info = current.getExtraInfo();
				Iterator<String> it = params.keySet().iterator();
				while( it.hasNext() ) {
					String n = it.next();
					if( !info.containsKey(n) ) info.put(n, params.get(n));
				}
			}
			else {
				children = false;
			}
		}
		
		// Get child app elements (only if we need to)
		if( children ) compileChildren(compiler, app, current);
		
		return current;
	}
	
	/**
	 * Internal helper to compile the children.
	 * 
	 * @param compiler
	 * @param app
	 * @param current
	 */
	protected void compileChildren(AppCompiler compiler, AppElement app, AppElement current) {
		// Get child app elements (only if we need to)
		if( childCompilers != null ) {
			for( int i = 0 ; i < childCompilers.length ; i++ ) {
				childCompilers[i].compile(compiler, app, current);
			}
		}
	}

	/**
	 * @return the expected
	 */
	public Class<?> getExpected() {
		return expected;
	}

	/**
	 * @return the idCompiler
	 */
	public PropertyAccessor getIdCompiler() {
		return idCompiler;
	}

	/**
	 * @param idCompiler the idCompiler to set
	 */
	public void setIdCompiler(PropertyAccessor idCompiler) {
		this.idCompiler = idCompiler;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the isRoot
	 */
	public boolean isRoot() {
		return isRoot;
	}

	/**
	 * @return the infoAccessor
	 */
	public Map<String, PropertyAccessor> getInfoAccessor() {
		return infoAccessors;
	}

	/**
	 * @param infoAccessor the infoAccessor to set
	 */
	public void setInfoAccessor(Map<String, PropertyAccessor> infoAccessor) {
		this.infoAccessors = infoAccessor;
	}
	
	/**
	 * Add an info accessor to the list of info accessors
	 * 
	 * @param name The name of the info parameter
	 * @param accessor The mechanism to get the parameter
	 */
	public void addInfoAccessor(String name, PropertyAccessor accessor) {
		if( accessor == null ) return;
		if( this.infoAccessors == null ) this.infoAccessors = new HashMap<String, PropertyAccessor>();
		this.infoAccessors.put(name, accessor);
	}

	/**
	 * @return the defaultParameters
	 */
	public Map<String, String> getDefaultParameters() {
		return defaultParameters;
	}

	/**
	 * @param defaultParameters the defaultParameters to set
	 */
	public void setDefaultParameters(Map<String, String> defaultParameters) {
		this.defaultParameters = defaultParameters;
	}
	
	/**
	 * Add a default parameter to this compiler.
	 * 
	 * @param name The name of the compiler
	 * @param val The value
	 */
	public void addDefaultParameter(String name, String val) {
		if( this.defaultParameters == null ) defaultParameters = new HashMap<String, String>();
		this.defaultParameters.put(name, val);
	}

	/**
	 * @return the childCompilers
	 */
	public ChildCompiler[] getChildCompilers() {
		return childCompilers;
	}

	/**
	 * @param childCompilers the childCompilers to set
	 */
	public void setChildCompilers(ChildCompiler[] childCompilers) {
		this.childCompilers = childCompilers;
	}
	
	/**
	 * Add a {@link ChildCompiler} to this compilers list 
	 * of child compilers.
	 * 
	 * @param compiler The compiler to add
	 */
	public void addChildCompiler(ChildCompiler compiler) {
		int ln = childCompilers != null ? childCompilers.length : 0;
		if( childCompilers == null ) childCompilers = new ChildCompiler[1];
		else {
			ChildCompiler[] temp = new ChildCompiler[ln + 1];
			System.arraycopy(childCompilers, 0, temp, 0, ln);
			childCompilers = temp;
		}
		
		childCompilers[ln] = compiler;
	}

	/**
	 * @return the specIndexes
	 */
	public Map<String, Integer> getSpecIndexes() {
		return specIndexes;
	}

	/**
	 * @param specIndexes the specIndexes to set
	 */
	public void setSpecIndexes(Map<String, Integer> specIndexes) {
		this.specIndexes = specIndexes;
	}
}
