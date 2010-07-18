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

package org.tpspencer.tal.template.core.xml;

import org.tpspencer.tal.template.core.BasicTemplateConfiguration;

/**
 * This simple class implementats the template configuration
 * interface.
 * 
 * @author Tom Spencer
 */
public final class XmlTemplateConfiguration extends BasicTemplateConfiguration {
	
	/** Member holds the file, classpath or url reference to the template resource file */
	private String templateResource = null;
	/** Member holds the template reader to use, by default the default one */
	private XmlTemplateReader templateReader = XmlTemplateReader.getStdReader();
	
	/**
	 * Loads the template and optionally initialises the
	 * renderers.
	 */
	@Override
	public void init() {
		// a. Load the templates
		setTemplates(templateReader.loadTemplates(templateResource));
		
		super.init();
	}
	
	////////////////////////////////////////////////
	// Getters / Setters

	/**
	 * @return the templateResource
	 */
	public String getTemplateResource() {
		return templateResource;
	}

	/**
	 * @param templateResource the templateResource to set
	 */
	public void setTemplateResource(String templateResource) {
		this.templateResource = templateResource;
	}

	/**
	 * @return the templateReader
	 */
	public XmlTemplateReader getTemplateReader() {
		return templateReader;
	}

	/**
	 * @param templateReader the templateReader to set
	 */
	public void setTemplateReader(XmlTemplateReader templateReader) {
		this.templateReader = templateReader;
	}
}
