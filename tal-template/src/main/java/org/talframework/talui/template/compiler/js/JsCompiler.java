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

package org.talframework.talui.template.compiler.js;

import org.talframework.talui.template.compiler.BasicTemplateRenderMold;
import org.talframework.talui.template.compiler.SimpleGenericCompiler;
import org.talframework.talui.template.compiler.TemplateElementRenderMoldFactory;

/**
 * This class extends the SimpleGenericCompiler to provide a
 * basic Javascript compiler. This compiler is configured with
 * all the defaults from the JsTemplateElementRenderMoldFactory,
 * so any molds are setup and overridding these will change the
 * behaviour of any newly constructed compiler. You can also
 * provide your own factory as a constructor argument. By default
 * the JsCompiler automatically includes support for forms. 
 * 
 * @author Tom Spencer
 */
public final class JsCompiler extends SimpleGenericCompiler {

	/**
	 * Construct a default JsCompiler
	 */
	public JsCompiler(boolean incForm) {
		setRecurseTemplates(true);
		
		BasicTemplateRenderMold mold = new BasicTemplateRenderMold();
		mold.configureMold(JsTemplateElementRenderMoldFactory.getInstance(), incForm);
		setMold(mold);
	}
	
	/**
	 * Construct a JsCompiler using the given factory
	 * 
	 * @param factory The mold factory to use
	 */
	public JsCompiler(TemplateElementRenderMoldFactory factory, boolean incForm) {
		setRecurseTemplates(true);
		
		BasicTemplateRenderMold mold = new BasicTemplateRenderMold();
		mold.configureMold(factory, incForm);
		setMold(mold);
	}
	
	@Override
	public String toString() {
		return "JsCompiler: " + super.toString();
	}
}
