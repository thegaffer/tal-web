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

package org.tpspencer.tal.mvc.document.spec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.tpspencer.tal.mvc.commons.views.AbstractTemplateView;
import org.tpspencer.tal.mvc.commons.views.TemplateView;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.AppElementImpl;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.ElementWriter;
import org.tpspencer.tal.mvc.document.spec.render.SpecificationCompiler;
import org.tpspencer.tal.template.RenderElement;
import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.render.SimpleRenderModel;
import org.tpspencer.tal.template.render.UrlGenerator;

/**
 * This writer writes out a views template - if there is one -
 * otherwise it writes out a message that it is a custom view.
 * 
 * @author Tom Spencer
 */
public class ViewTemplateWriter implements ElementWriter {

	public void write(DocumentWriter writer, AppElement app, AppElement element) {
		Object view = element.getElement();
		
		if( view instanceof AbstractTemplateView ) {
			writeTemplateConfig(writer, element, ((AbstractTemplateView)view).getConfig());
		}
		else if( view instanceof TemplateView ) {
			writeTemplateConfig(writer, element, ((TemplateView)view).getConfig());
		}
		else {
			writer.writeParagraph("view.fields.none", element);
		}
	}
	
	/**
	 * Helper to write out a template configuration
	 * 
	 * @param config The template configu
	 */
	@SuppressWarnings("serial")
	private void writeTemplateConfig(final DocumentWriter writer, AppElement element, TemplateConfiguration config) {
		writer.writeParagraph("view.fields.prelim", element);
		
		SpecificationCompiler compiler = new SpecificationCompiler();
		compiler.compile(config);
		Map<String, RenderElement> templates = compiler.getRenderedTemplates();
		
		SimpleRenderModel renderModel = new SimpleRenderModel(null, (UrlGenerator)null);
		renderModel.setModel(new HashMap<String, Object>() {{
			put("documentWriter", writer);
		}});
		
		try {
			RenderElement re = templates.get(config.getMainTemplate().getName());
			re.render(renderModel);
			
			Iterator<String> it = templates.keySet().iterator();
			while( it.hasNext() ) {
				String name = it.next();
				if( !name.equals(config.getMainTemplate().getName()) ) {
					outputTemplate(writer, renderModel, name, templates.get(name));
				}
			}
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Cannot render template view into specification: " + e.getMessage(), e);
		}
	}
	
	private void outputTemplate(DocumentWriter writer, RenderModel renderModel, String name, RenderElement template) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Name", name);
		AppElementImpl element = new AppElementImpl(name, template, params);
		
		writer.writeParagraph("view.fields.template", element);
		
		template.render(renderModel);
	}
}
