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

package org.tpspencer.tal.mvc.document.spec.render;

import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.Renderer;
import org.tpspencer.tal.template.Template;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.behaviour.CommandElement;
import org.tpspencer.tal.template.behaviour.DynamicProperty;
import org.tpspencer.tal.template.behaviour.GroupElement;
import org.tpspencer.tal.template.behaviour.MemberProperty;
import org.tpspencer.tal.template.compiler.BasicTemplateRenderMold;
import org.tpspencer.tal.template.compiler.SimpleGenericCompiler;

/**
 * The compiler for taking a template view and turning it into
 * a set of specifications. 
 * 
 * @author Tom Spencer
 */
public class SpecificationCompiler extends SimpleGenericCompiler {

	public SpecificationCompiler() {
		BasicTemplateRenderMold mold = new BasicTemplateRenderMold();
		
		mold.setDefaultMold(new GroupRenderMold());
		mold.addBehaviourMold(DynamicProperty.class, null, new PropertyRenderMold());
		mold.addBehaviourMold(GroupElement.class, null, new GroupRenderMold());
		mold.addBehaviourMold(MemberProperty.class, null, new PropertyRenderMold());
		mold.addBehaviourMold(CommandElement.class, null, new PropertyRenderMold());
		
		setMold(mold);
	}
	
	@Override
	protected RenderElement createTemplateElement(Template template) {
		return new SpecTableRenderElement();
	}
	
	@Override
	public Renderer compile(TemplateConfiguration config) {
		Renderer renderer = super.compile(config);
		
		return renderer;
	}
}
