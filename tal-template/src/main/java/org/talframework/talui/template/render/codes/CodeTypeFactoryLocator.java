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

package org.talframework.talui.template.render.codes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.talframework.talui.template.RenderModel;

/**
 * This class holds a reference to all known code type
 * factory instances. A code type just represents a 
 * reference field that is converted to a description
 * for the end user. A code type factory creates 
 * instances of the code type for a request (where 
 * we have the locale and user information). All
 * code type factories should register themselves with
 * this singleton locator class if they are to be
 * found automatically by the Web Template classes.
 * 
 * <p><b>Note: </b>At render time the render model is
 * first inspected for a resource bundle, map or 
 * {@link CodeType} instance. If you place these in
 * the render model before rendering the template then
 * there is no need to register the factory unless 
 * you want AJAX style refreshes.</p>
 * 
 * <p>This class assumes all code types will be setup
 * at initialisation time and therefore uses a 
 * HashMap - not thread safe. If you are initialising
 * factories on an adhoc basis and get some wierd
 * results this may be why. Factories should be created
 * at start up time to observe the fail fast principle
 * though.</p>
 * 
 * @author Tom Spencer
 */
public class CodeTypeFactoryLocator {
	/** Single instance (per (parent) classloader) of this class */
	private static final CodeTypeFactoryLocator INSTANCE = new CodeTypeFactoryLocator();
	
	/** Holds the factories */
	private Map<String, CodeTypeFactory> codeFactories = new HashMap<String, CodeTypeFactory>();
	
	/**
	 * Hidden constructor - you cannot create an instance of this class
	 */
	private CodeTypeFactoryLocator() {
	}
	
	/**
	 * @return The single instance of the locator
	 */
	public static CodeTypeFactoryLocator getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Call to add the factory to the locator. All default
	 * factories do this automatically in their constructors.
	 * So as long as you create these at startup your good.
	 * This method is synchronised to afford some protection
	 * from multiple threads, but the call to get a factory
	 * is not synchronised.
	 * 
	 * @param name The name of the factory
	 * @param factory The factory
	 */
	public synchronized void addCodeFactory(String name, CodeTypeFactory factory) {
		codeFactories.put(name, factory);
	}
	
	/**
	 * Determines if a factory exists
	 * 
	 * @param name The name of the code type
	 * @return True if it exists, false otherwise
	 */
	public boolean hasCodeFactory(String name) {
		return codeFactories.containsKey(name);
	}
	
	/**
	 * Call to obtain the {@link CodeTypeFactory} given the
	 * name of the code type.
	 * 
	 * @param name The type of codes ultimately required
	 * @return The factory
	 * @throws IllegalArgumentException If the code type is not known
	 */
	public CodeTypeFactory getCodeFactory(String name) {
		if( codeFactories.containsKey(name) ) {
			return codeFactories.get(name);
		}
		throw new IllegalArgumentException("The code type is not known to the code type factory lookup: " + name);
	}
	
	/**
	 * Helper method to get the code type instance directly
	 * without needing to get the factory and then get the
	 * code type from there.
	 * 
	 * @param name The type of codes required
	 * @param model The render model
	 * @param params Any additional parameters
	 * @return The CodeType
	 * @throws IllegalArgumentException If the code type is not known
	 */
	@SuppressWarnings("unchecked")
	public static CodeType getCodeType(String name, RenderModel model, Map<String, String> params) {
		CodeType ret = null;
		
		// First check the model
		Object obj = model.getObject(name);
		if( obj != null ) {
			if( obj instanceof CodeType ) ret = (CodeType)obj;
			else if( obj instanceof ResourceBundle ) ret = new ResourceCodeType(name, (ResourceBundle)obj);
			else if( obj instanceof Map ) ret = new SimpleCodeType(name, (Map<String, String>)obj);
		}
		
		if( ret == null ) {
			CodeTypeFactory factory = getInstance().getCodeFactory(name);
			ret = factory.getCodeType(model.getLocale(), model.getUser(), params);
		}
		
		return ret;
	}
	
	/**
	 * Helper method to get the code type instance directly
	 * without needing to get the factory and then get the
	 * code type from there.
	 * 
	 * @param name The type of codes required
	 * @param locale The locale we want
	 * @param user The current user
	 * @param params Any additional parameters
	 * @return The CodeType
	 * @throws IllegalArgumentException If the code type is not known
	 */
	public static CodeType getCodeType(String name, Locale locale, Principal user, Map<String, String> params) {
		CodeTypeFactory factory = getInstance().getCodeFactory(name);
		return factory.getCodeType(locale, user, params);
	}
}
