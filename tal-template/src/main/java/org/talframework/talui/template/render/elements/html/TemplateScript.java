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

package org.tpspencer.tal.template.render.elements.html;

import org.tpspencer.tal.template.RenderModel;

public final class TemplateScript extends Script {
	
	public TemplateScript(String template) {
		super(template);
	}
	
	/**
	 * Overrides to treat the resource as a template name
	 * and get the template resource URL.
	 */
	@Override
	public String getUrl(RenderModel model) {
		return model.getUrlGenerator().generateTemplateUrl(getResource(), "js");
	}
	
	@Override
	public String toString() {
		return "TemplateScript: template=" + getResource();
	}
}
