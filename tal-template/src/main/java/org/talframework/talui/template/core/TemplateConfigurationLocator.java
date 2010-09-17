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

package org.talframework.talui.template.core;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.template.TemplateConfiguration;

/**
 * This class holds all known instances of the template
 * configuration. There is only a single instance of this
 * class inside the classloader. Each template configuration
 * registers itself against this instance and the default
 * WebTemplateServlet serves up the templates from here. 
 * 
 * @author Tom Spencer
 */
public class TemplateConfigurationLocator {
	private static final TemplateConfigurationLocator INSTANCE = new TemplateConfigurationLocator();
	private static final Log logger = LogFactory.getLog(TemplateConfigurationLocator.class);

	private Map<String, TemplateConfiguration> config = new HashMap<String, TemplateConfiguration>();
	
	/**
	 * @return The singleton (per classloader) instance of this class
	 */
	public static TemplateConfigurationLocator getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Hidden constructor
	 */
	private TemplateConfigurationLocator() {
	}
	
	/**
	 * Returns the template configuration given its name.
	 * No ill effects if the config is not found, just 
	 * returns null.
	 * 
	 * @param name The name of the template
	 * @return The template (or null)
	 */
	public TemplateConfiguration getTemplate(String name) {
		return config.get(name);
	}
	
	/**
	 * Sets the template inside the locator.
	 * 
	 * <p>This version of the method is synchronised. The idea is that
	 * all your templates will be created and register themselves at
	 * startup. The BasicTemplateConfiguration does this automatically.
	 * This follows the fail-fast principle. Although this method is
	 * synchronised so only one thread can do it at a time, the
	 * underlying map is a Hashmap and not sync'd - therefore, the 
	 * get performance in the running system is high. There is another
	 * version of this method below that is used when added configs to
	 * a known running system. If that is used it turns the hashmap
	 * into a hashtable for safety.</p>  
	 * 
	 * @param name The name of the template
	 * @param config The template
	 */
	public synchronized void setTemplate(String name, TemplateConfiguration config) {
		this.config.put(name, config);
	}
	
	/**
	 * Sets the template inside the locator
	 * 
	 * <p>Unlike the above method this turns the internal hashmap into a
	 * hashtable, so get performance is lower from this point on. A log
	 * warning is created. This just means we've detected a missing template
	 * in a running system.</p>
	 * 
	 * <p>For the total purist this is not totally 100% safe. It will be
	 * on most platforms, Intel based anyway. The solution would be to make
	 * the map a hashtable from the start, but that is a task I leave to
	 * the person that has this problem - my solution would be to register
	 * all template configs during startup and thus not have this issue.</p>
	 * 
	 * @param name The name of the template
	 * @param config The template
	 */
	public void setTemplateWhenRunning(String name, TemplateConfiguration config) {
		Map<String, TemplateConfiguration> configMap = null;
		
		synchronized (this) {
			configMap = new Hashtable<String, TemplateConfiguration>(this.config);
			configMap.put(name, config);
		}
		
		logger.warn("The template config locator has switched to a hashtable config for the following template: " + name);
		this.config = configMap;
	}
}
