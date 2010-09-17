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

package org.talframework.talui.template.core.xml;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;
import org.talframework.talui.template.Compiler;
import org.talframework.talui.template.Renderer;
import org.talframework.talui.template.compiler.html.HtmlCompiler;
import org.talframework.talui.template.core.xml.XmlTemplateConfiguration;

/**
 * This test, which is more of a system test accross
 * the template project tests that the template config
 * loads the templates and then forms the renderers
 * as configured.
 * 
 * @author Tom Spencer
 */
public class TestXmlTemplateConfiguration {

	@SuppressWarnings("serial")
	@Test
	public void basic() {
		XmlTemplateConfiguration config = new XmlTemplateConfiguration();
		config.setName(this.getClass().getName().replace(".", "/"));
		config.setRootTemplate("SimpleBeanA");
		config.setCompilers(new HashMap<String, Compiler>() {{ put("html", new HtmlCompiler(false)); }});
		config.setTemplateResource("/testTemplate.xml");
		
		config.init();
		
		Renderer r = config.getRenderer("html");
		assertNotNull(r);
	}
}
