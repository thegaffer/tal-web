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

package org.tpspencer.tal.mvc.document.boq;

import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.DocumentWriter;
import org.tpspencer.tal.mvc.document.DocumentWriterImpl;
import org.tpspencer.tal.mvc.document.writers.ChapterWriter;
import org.tpspencer.tal.mvc.document.writers.ChildWriters;
import org.tpspencer.tal.mvc.document.writers.ConditionalWriters;
import org.tpspencer.tal.mvc.document.writers.SimpleWriters;

/**
 * This is the outer class that implements the document
 * writer interface to write out the Bill of Quantity
 * document for the Web App.
 * 
 * @author Tom Spencer
 */
public class BillOfQuantityWriter {
	
	private DocumentWriter writer = null;
	
	public BillOfQuantityWriter(String fileName) throws Exception {
		writer = new DocumentWriterImpl(fileName, "org.tpspencer.tal.mvc.document.boq.billOfQuantity");
	}
	
	public void createDocument(AppElement app) {
		writer.start(
			"Bill Of Quantity", 
			app.getId(),
			"Tom Spencer",
			"Web MVC Document");
		
		// B. Copyright
		writeCopyright(app);
		
		// C. TOC
		writeToc(app);
		
		// D. Pre-amble
		writePreamble(app);
		
		// E. Sub-structure
		getBeanWriter().write(writer, app, app);
		getModelWriter().write(writer, app, app);
		getControllerWriter().write(writer, app, app);
		getServiceWriter().write(writer, app, app);
		
		// F. Super-structure
		getPageWriter().write(writer, app, app);
		getWindowWriter().write(writer, app, app);
		getViewWriter().write(writer, app, app);
		
		// G. Model
		// writeModel(app);
		
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
	
	private ChapterWriter getBeanWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Beans", 
			"chapter.title", 
			"bean", 
			new String[]{"beans.prelim", "beans.prelim2"}, 
			new SimpleWriters.SectionWriter("bean.subtitle", false, 
				new SimpleWriters.SimpleWriter("bean.desc"),
				new ChildWriters.SummaryBulletWriter("attr", "bean.noattrs", "bean.bullet"))
		);
						
		
		return writer;
	}
	
	private ChapterWriter getModelWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Models", 
			"chapter.title", 
			"model", 
			new String[]{"model.prelim", "model.prelim2"}, 
			new SimpleWriters.SectionWriter("model.subtitle", false,  
				new SimpleWriters.SimpleWriter("model.desc"),
				new ChildWriters.SummaryBulletWriter("attr", "model.noattrs", "model.bullet"))
		);
		
		return writer;
	}
	
	private ChapterWriter getControllerWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Controllers", 
			"chapter.title", 
			"controller", 
			new String[]{"ctrl.prelim"}, 
			new SimpleWriters.SectionWriter("ctrl.subtitle", false, new SimpleWriters.SimpleWriter("ctrl.desc"))
		);
		
		return writer;
	}
	
	private ChapterWriter getServiceWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Services", 
			"chapter.title", 
			"service", 
			new String[]{"svc.prelim", "svc.prelim2"}, 
			new SimpleWriters.SectionWriter("svc.subtitle", false, new SimpleWriters.SimpleWriter("svc.desc"))
		);
		
		return writer;
	}
	
	private ChapterWriter getPageWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Pages", 
			"chapter.title", 
			"page", 
			new String[]{"pages.prelim"}, 
			new SimpleWriters.SectionWriter("page.subtitle", false,  
				new SimpleWriters.SimpleWriter("page.desc"),
				new ConditionalWriters.ChildConditionalWriter("window",
						new SimpleWriters.MultiWriter(
							new SimpleWriters.SimpleWriter("page.windows"),
							new ChildWriters.ListWriter("window", "page.window"))
				)
			)
		);

		
		return writer;
	}
	
	private ChapterWriter getWindowWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Windows", 
			"chapter.title", 
			"window", 
			new String[]{"windows.prelim"}, 
			new SimpleWriters.SectionWriter("window.subtitle", false,  
				new SimpleWriters.SimpleWriter("window.desc"),
				new ConditionalWriters.ChildConditionalWriter("view",
					new SimpleWriters.MultiWriter(
						new SimpleWriters.SimpleWriter("window.views"),
						new ChildWriters.ListWriter("view", "window.view")),
					new SimpleWriters.SimpleWriter("window.noviews")),
				new ConditionalWriters.ChildConditionalWriter("controller",
						new SimpleWriters.MultiWriter(
							new SimpleWriters.SimpleWriter("window.ctrls"),
							new ChildWriters.ListWriter("controller", "window.ctrl")),
						new SimpleWriters.SimpleWriter("window.noctrls"))
			)
		);
		
		return writer;
	}
	
	private ChapterWriter getViewWriter() {
		ChapterWriter writer = new ChapterWriter(
			"org.tpspencer.tal.mvc.document.boq.Views", 
			"chapter.title", 
			"view", 
			new String[]{"views.prelim"}, 
			new SimpleWriters.SectionWriter("view.subtitle", false, new SimpleWriters.SimpleWriter("view.desc"))
		);
		
		return writer;
	}
}
