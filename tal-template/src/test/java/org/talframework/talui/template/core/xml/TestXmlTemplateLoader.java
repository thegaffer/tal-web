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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.core.xml.XmlTemplateReader;

/**
 * This class tests that we can load XML based template
 * definitions using the XmlTemplateLoader.
 * 
 * @author Tom Spencer
 */
public class TestXmlTemplateLoader {

	/**
	 * The happy path test
	 */
	@Test
	public void basic() {
		List<Template> res = XmlTemplateReader.getStdReader().loadTemplates("/testTemplate.xml");
		assertNotNull(res);
		assertEquals(4, res.size());
	}
	
	/**
	 * As basic, but passes the resource as a file
	 */
	public void asFile() {
		
	}
	
	/**
	 * As basic, but passes the resource as a url
	 */
	public void asUrl() {
		
	}
	
	/**
	 * Ensures we fail with no resource specified
	 */
	public void noResource() {
		
	}
	
	/**
	 * Ensures we fail with an empty resource specified
	 */
	public void emptyResource() {
		
	}
	
	/**
	 * Ensures we fail if there is an element with no mapping
	 */
	public void invalidElement() {
		
	}
	
	/**
	 * Ensures we fail if there is a class that is not found
	 */
	public void invalidClass() {
		
	}
}
