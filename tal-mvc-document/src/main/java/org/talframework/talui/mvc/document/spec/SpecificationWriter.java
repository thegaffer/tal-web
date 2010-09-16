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

import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.DocumentWriterImpl;
import org.tpspencer.tal.mvc.document.writers.ChapterWriter;
import org.tpspencer.tal.mvc.document.writers.ChildWriters;
import org.tpspencer.tal.mvc.document.writers.ConditionalWriters;
import org.tpspencer.tal.mvc.document.writers.SimpleWriters;

public class SpecificationWriter {

private DocumentWriter writer = null;
	
	public SpecificationWriter(String fileName) throws Exception {
		writer = new DocumentWriterImpl(fileName, "org.tpspencer.tal.mvc.document.spec.Specifications");
	}
	
	public void createDocument(AppElement app) {
		writer.start(
			"Specifications", 
			app.getInfo("App", app.getId()),
			"Tom Spencer",
			"Web MVC Document");
		
		writeCopyright(app);
		writeToc(app);
		writePreamble(app);
		
		// Pages
		getPageWriter().write(writer, app, app);
		getWindowWriter().write(writer, app, app);
		getViewWriter().write(writer, app, app);
		
		getBeanWriter().write(writer, app, app);
		
		//getModelWriter().write(writer, app, app);
		//getControllerWriter().write(writer, app, app);
		//getServiceWriter().write(writer, app, app);
		
		writer.end();
	}
	
	/**
	 * Helper to write the copyright page
	 */
	private void writeCopyright(AppElement app) {
		writer.startChapter("doc.notice.title", null, app);
		
		writer.writeQuote("doc.notice.app", app);
		writer.writeQuote("doc.notice.doc", app);
		
		writer.endChapter();
	}
	
	/**
	 * Helper to write the TOC page
	 */
	private void writeToc(AppElement app) {
		writer.startChapter("doc.toc", null, app);
		
		writer.endChapter();
	}
	
	/**
	 * Helper to write the pre-amble page
	 */
	private void writePreamble(AppElement app) {
		writer.startChapter("doc.intro.title", null, app);
		
		// Intro
		writer.startSection("doc.intro.intro.subtitle", app, false);
		writer.writeParagraph("doc.intro.intro", app);
		writer.endSection();
		
		// Purpose
		writer.startSection("doc.intro.purpose.subtitle", app, false);
		writer.writeParagraph("doc.intro.purpose", app);
		writer.startList();
			writer.addListItem("doc.intro.purpose1", app);
			writer.addListItem("doc.intro.purpose2", app);
			writer.addListItem("doc.intro.purpose3", app);
			writer.addListItem("doc.intro.purpose4", app);
		writer.endList();
		writer.endSection();
		
		// Audience
		writer.startSection("doc.intro.audience.subtitle", app, false);
		writer.writeParagraph("doc.intro.audience", app);
		writer.startList();
			writer.addListItem("doc.intro.audience1", app);
			writer.addListItem("doc.intro.audience2", app);
			writer.addListItem("doc.intro.audience3", app);
			writer.addListItem("doc.intro.audience4", app);
		writer.endList();
		writer.endSection();
		
		// Organisation
		writer.startSection("doc.intro.org.subtitle", app, false);
		writer.writeParagraph("doc.intro.org", app);
		writer.startList();
			writer.addListItem("doc.intro.org1", app);
			writer.addListItem("doc.intro.org2", app);
			writer.addListItem("doc.intro.org3", app);
		writer.endList();
		writer.endSection();
		
		writer.endChapter();
	}
	
	/**
	 * @return The specification writer for pages
	 */
	private ChapterWriter getPageWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.spec.Pages", 
			"chapter.title", 
			"page", 
			new String[]{"pages.prelim"}, 
			new SimpleWriters.SectionWriter("page.subtitle", true, 
				new SimpleWriters.SimpleWriter("page.desc"),
				new SimpleWriters.ImageWriter("/org/tpspencer/tal/mvc/document/spec/test.png", null),
				
				// Model Attributes
				new SimpleWriters.SectionWriter("page.model", false, 
					new SimpleWriters.SimpleWriter("page.model.desc"),
					new ConditionalWriters.ChildConditionalWriter("model", 
						new ChildWriters.ChildTableWriter(
							new ChildWriters.RootMatchChildAccessor("model", "model", "attr"), 
							"page.attr.name", "page.attr.desc", "page.attr.type", "page.attr.flash", "page.attr.event", "page.attr.render", "page.attr.alias", "page.attr.clear"), 
						new SimpleWriters.SimpleWriter("page.model.none"))
				), 
				
				// Page Events
				new SimpleWriters.SectionWriter("page.events", false, 
					new SimpleWriters.SimpleWriter("page.events.desc"),
					new ConditionalWriters.ChildConditionalWriter("view", 
						new ChildWriters.ChildTableWriter("pageEvent", "page.event.result", "page.event.page", "page.event.window", "page.event.action"), 
						new SimpleWriters.SimpleWriter("page.event.none"))
				),
				
				// Windows
				new SimpleWriters.SectionWriter("page.windows", false,
					new SimpleWriters.SimpleWriter("page.windows.desc"),
					new ConditionalWriters.ChildConditionalWriter("window", 
						new ChildWriters.ChildTableWriter("window", "page.window.window", "page.window.name", "page.window.namespace"),
						new SimpleWriters.SimpleWriter("page.window.none"))
				)
			)
		);
		
		return writer;
	}
	
	/**
	 * @return The specification writer for windows
	 */
	private ChapterWriter getWindowWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.spec.Windows", 
			"chapter.title", 
			"window", 
			new String[]{"windows.prelim"}, 
			new SimpleWriters.SectionWriter("window.subtitle", true, 
				new SimpleWriters.SimpleWriter("window.desc"),
				
				// Model
				new SimpleWriters.SectionWriter("window.model", false, 
					new SimpleWriters.SimpleWriter("window.model.desc"),
					new ConditionalWriters.ChildConditionalWriter("model", 
						new ChildWriters.ChildTableWriter(
							new ChildWriters.RootMatchChildAccessor("model", "model", "attr"), 
							"window.attr.name", "window.attr.desc", "window.attr.type", "window.attr.flash", "window.attr.event", "window.attr.render", "window.attr.alias", "window.attr.clear"),
						new SimpleWriters.SimpleWriter("window.model.none"))
				), 
						
				// Views
				new SimpleWriters.SectionWriter("window.views", false, 
					new SimpleWriters.SimpleWriter("window.views.desc"),
					new ConditionalWriters.ChildConditionalWriter("view", 
						new ChildWriters.ChildTableWriter("view", "window.view.name", "window.view.view", "window.view.default"),
						new SimpleWriters.SimpleWriter("window.views.none"))
				),
								
				// Controllers
				new SimpleWriters.SectionWriter("window.controllers", false, 
					new SimpleWriters.SimpleWriter("window.controllers.desc"),
					new ConditionalWriters.ChildConditionalWriter("controller", 
						new ChildWriters.ChildTableWriter("controller", "window.ctrl.action", "window.ctrl.name"),
						new SimpleWriters.SimpleWriter("window.controllers.none"))
				),
						
				// Results
				new SimpleWriters.SectionWriter("window.results", false, 
					new SimpleWriters.SimpleWriter("window.results.desc"),
					new ConditionalWriters.ChildConditionalWriter("result", 
						new ChildWriters.ChildTableWriter("result", "window.result.result", "window.result.view"),
						new SimpleWriters.SimpleWriter("window.results.none"))
				),
						
				// Events
				new SimpleWriters.SectionWriter("window.events", false, 
					new SimpleWriters.SimpleWriter("window.events.desc"),
					new ConditionalWriters.ChildConditionalWriter("event", 
						new ChildWriters.ChildTableWriter("event", "window.event.action", "window.event.newValue", "window.event.oldValue"),
						new SimpleWriters.SimpleWriter("window.events.none"))
				)
			)
		);
		
		return writer;
	}
	
	/**
	 * @return The specification writer for windows
	 */
	private ChapterWriter getViewWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.spec.Views", 
			"chapter.title", 
			"view", 
			new String[]{"views.prelim", "views.prelim.img", "views.prelim.model", "views.prelim.fields", "views.prelim.end"}, 
			new SimpleWriters.SectionWriter("view.subtitle", true,  
				new SimpleWriters.SimpleWriter("view.desc"),
				
				// Model
				new SimpleWriters.SectionWriter("view.model", false, 
					new SimpleWriters.SimpleWriter("view.model.desc"),
					new ConditionalWriters.ChildConditionalWriter("model", 
						new ChildWriters.ChildTableWriter(
							new ChildWriters.RootMatchChildAccessor("model", "model", "attr"), 
							"view.attr.name", "view.attr.desc", "view.attr.type", "view.attr.flash", "view.attr.render", "view.attr.clear"),
						new SimpleWriters.SimpleWriter("view.model.none"))
				),
						
				// Render Attributes
				new SimpleWriters.SectionWriter("view.render", false, 
					new ConditionalWriters.ChildConditionalWriter("renderAttributes", 
						new SimpleWriters.MultiWriter(
							new SimpleWriters.SimpleWriter("view.render.prelim"),
							new ChildWriters.ListWriter("renderAttributes", "view.render.attr")
						), 
						new SimpleWriters.SimpleWriter("view.render.none"))
				),
					
						
				// Fields
				new SimpleWriters.SectionWriter("view.fields", false,
						new ViewTemplateWriter())
			)
		);
		
		return writer;
	}
	
	/**
	 * @return The specification writer for beans
	 */
	private ChapterWriter getBeanWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.spec.Beans", 
			"chapter.title", 
			"bean", 
			new String[]{"beans.prelim", "beans.prelim2", "beans.prelim3"}, 
			new SimpleWriters.SectionWriter("bean.subtitle", true,  
				new SimpleWriters.SimpleWriter(new String[]{"bean.desc"}),
					
				// Fields
				new SimpleWriters.SectionWriter("bean.fields", false, 
					new SimpleWriters.SimpleWriter("bean.fields.desc"),
					new ConditionalWriters.ChildConditionalWriter("attr", 
						new ChildWriters.ChildTableWriter("attr", "bean.field.name", "bean.field.type", "bean.field.desc", "bean.field.val"),
						new SimpleWriters.SimpleWriter("bean.fields.none"))
				)
			)
		);
		
		return writer;
	}
}
