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

package org.tpspencer.tal.template.compiler.html;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.compiler.BasicTemplateRenderMold;
import org.tpspencer.tal.template.compiler.SimpleGenericCompiler;
import org.tpspencer.tal.template.compiler.TemplateElementRenderMoldFactory;
import org.tpspencer.tal.template.render.elements.html.Script;
import org.tpspencer.tal.template.render.elements.html.StyleImport;
import org.tpspencer.tal.template.render.elements.html.TemplateScript;
import org.tpspencer.tal.template.render.elements.special.EmptyElement;

/**
 * This class extends the SimpleGenericCompiler to provide a
 * basic HTML compiler. This compiler is configured with
 * all the defaults from the HtmlTemplateElementRenderMoldFactory,
 * so any molds are setup and overridding these will change the
 * behaviour of any newly constructed compiler. You can also
 * provide your own factory as a constructor argument.  
 * 
 * @author Tom Spencer
 */
public class HtmlCompiler extends SimpleGenericCompiler {

	/**
	 * Creates an instance of the compiler using the default
	 * HtmlTemplateElementRenderMoldFactory.
	 * 
	 * @param incForm If true form support is added, false otherwise
	 */
	public HtmlCompiler(boolean incForm) {
		BasicTemplateRenderMold mold = new BasicTemplateRenderMold();
		mold.configureMold(HtmlTemplateElementRenderMoldFactory.getInstance(), incForm);
		setMold(mold);
	}
	
	/**
	 * Creates an instance of the compiler using the provided
	 * TemplateElementRenderMoldFactory instance.
	 * 
	 * :param factory The factory to use
	 * @param incForm If true form support is added, false otherwise
	 */
	public HtmlCompiler(TemplateElementRenderMoldFactory factory, boolean incForm) {
		BasicTemplateRenderMold mold = new BasicTemplateRenderMold();
		mold.configureMold(factory, incForm);
		setMold(mold);
	}
	
	/**
	 * Adds in the main template JS file, the generated JS fils if it exists
	 * in the template as a renderer, the override JS file if it exists and
	 * the override css file if it exists.
	 */
	@Override
	public RenderElement swapRootTemplate(TemplateConfiguration config, RenderElement root) {
		if( config == null ) return root;
		
		EmptyElement newRoot = new EmptyElement();
		
		String name = config.getName();
		String overrideScriptName = name + ".js";
		String overrideStyleName = name + ".css";

		if( checkResource(overrideScriptName) ) newRoot.addElement(new Script("resource/" + overrideScriptName));
		if( checkResource(overrideStyleName) ) newRoot.addElement(new StyleImport("resource/" + overrideStyleName));
		if( config.hasRenderer("js") ) newRoot.addElement(new TemplateScript(name));
		newRoot.addElement(root);
		
		return newRoot;
	}
	
	/**
	 * Helper to determine if a particular resource exists.
	 * 
	 * @param name
	 * @return
	 */
	public boolean checkResource(String name) {
		return this.getClass().getClassLoader().getResource(name) != null;
	}
}
