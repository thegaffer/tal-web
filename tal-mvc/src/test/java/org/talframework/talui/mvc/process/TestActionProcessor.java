/*
 * Copyright 2009 Thomas Spencer
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

package org.talframework.talui.mvc.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.Window;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.EventConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.PageEventConfig;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.talui.mvc.input.InputModel;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.SimpleModelAttribute;
import org.talframework.talui.mvc.process.ActionProcessor;
import org.talframework.talui.mvc.process.ModelLayerAttributesResolver;
import org.talframework.talui.mvc.process.NoViewException;

/**
 * This class tests the action process
 * 
 * @author Tom Spencer
 */
@SuppressWarnings("serial")
public class TestActionProcessor {
	
	private Mockery context = new JUnit4Mockery();
	
	/** The test app [attrs=app1, app2] */
	private AppConfig app = null;
	/** A test page (added to app in setup) [attrs=page1, page2]*/
	private PageConfig page = null;
	/** The test window1 instance */
	private WindowConfig window1 = null;
	/** The test window2 instance */
	private WindowConfig window2 = null;
	/** A (mocked) resolver that by default is setup to return null to any request */
	private ModelLayerAttributesResolver resolver = null;
	
	/** 
	 * A (mocked) window setup in the config during setup<br />
	 * - Is setup with a model, expectations are getModel called any number of times.<br />
	 * - Is setup with two events, page1 and page2 with auto proxy on windowconfig. Expectations setup for this.<br />
	 * Note: A second window (window2) is setup on the page with events listening for page1 and page2
	 */
	private Window w1 = null;
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		// An eventable attr for the event tests
		final SimpleModelAttribute appPage1 = new SimpleModelAttribute("page1");
		appPage1.setEventable(true);
		
		app = new AppConfig("app", new ModelConfiguration(
				"appModel", (List<ModelAttribute>)new ArrayList<ModelAttribute>() {{
					add(new SimpleModelAttribute("app1"));
					add(new SimpleModelAttribute("app2"));
				}}));
		((SimpleModelAttribute)app.getModel().getAttribute("app1")).setEventable(true);
		
		page = new PageConfig();
		page.setName("page");
		page.setTemplate("template");
		page.setModel(new ModelConfiguration(
				"pageModel", (List<ModelAttribute>)new ArrayList<ModelAttribute>() {{
					add(appPage1);
					add(new SimpleModelAttribute("page2"));
				}}));
		page.setEvents(new ArrayList<PageEventConfig>() {{
			add(new PageEventConfig("window1.pageResult"));
		}});
		
		app.setPages(new ArrayList<PageConfig>(){{ add(page); }});
		
		final ModelConfiguration window1Model = new ModelConfiguration(
				"window1Model", (List<ModelAttribute>)new ArrayList<ModelAttribute>() {{
					add(new SimpleModelAttribute("page1"));
					add(new SimpleModelAttribute("page2"));
				}});
		
		// The first window - we test on this window
		w1 = context.mock(Window.class);
		final List<EventConfig> window1Events = new ArrayList<EventConfig>() {{
			add(new EventConfig("app1", "onApp", "newValue"));
			add(new EventConfig("page2", "onPage", "newValue"));
		}};
		context.checking(new Expectations() {{
			allowing(w1).getModel(); will(returnValue(window1Model));
			allowing(w1).getEvents(); will(returnValue(window1Events));
		}});
		this.window1 = new WindowConfig("window1", w1);
		
		// The second window - used for event tests
		final Window w2 = context.mock(Window.class, "window2");
		final List<EventConfig> window2Events = new ArrayList<EventConfig>() {{
			add(new EventConfig("app1", "onApp", "newValue"));
			add(new EventConfig("page2", "onPage", "newValue"));
		}};
		context.checking(new Expectations() {{
			allowing(w2).getModel(); will(returnValue(window1Model));
			allowing(w2).getEvents(); will(returnValue(window2Events));
		}});
		this.window2 = new WindowConfig("window2", w2);
		
		page.setWindows(new ArrayList<WindowConfig>(){{
			add(window1);
			add(window2);
		}});
		
		app.init();
		page = app.getPage("page");
		window1 = page.getWindow("window1");
		window2 = page.getWindow("window2");
		
		resolver = context.mock(ModelLayerAttributesResolver.class);
		context.checking(new Expectations() {{
			allowing(resolver).getModelAttributes(with(any(ModelConfiguration.class))); 
				will(returnValue(null));
			allowing(resolver).saveModelAttributes(with(any(ModelConfiguration.class)), (Map<String, Object>)with(anything()));
		}});
	}

	/**
	 * This ensures that the processor handles a basic action
	 * on the target window.
	 */
	@Test
	public void basic() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		
		final View currentView = createView("current");
		addActionExpectations(
				w1, "action1", 
				new UpdateModelAction("app2", "test", "action1ok"), 
				currentView, currentView);
		
		proc.processAction(null, window1, "action1");
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests we get an exception if the window is invalid
	 */
	@Test(expected = IllegalArgumentException.class)
	public void invalidWindow() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		proc.processAction(null, null, "action1");
	}
	
	/**
	 * This ensures that the processor handles a state change
	 * successfully
	 */
	@Test
	public void testStateChange() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		
		final ModelConfiguration config = new ModelConfiguration("test",
				new ArrayList<ModelAttribute>() {{
					add(new SimpleModelAttribute("test1"));
					add(new SimpleModelAttribute("test2"));
				}});
		
		final View currentView = createView("current", config);
		final View newView = createView("new");
		addActionExpectations(w1, "action1", "action1ok", currentView, newView);
		addStateChangeExpectations(w1, currentView, newView);
		
		context.checking(new Expectations() {{
			oneOf(resolver).removeModel(config);
		}});
		
		proc.processAction(null, window1, "action1");
		context.assertIsSatisfied();
	}
	
	/**
	 * This ensures that the processor handles the case when
	 * the state changes, but to a child view
	 */
	@Test
	public void testInnerStateChange() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		
		ModelConfiguration config = new ModelConfiguration("test",
				new ArrayList<ModelAttribute>() {{
					add(new SimpleModelAttribute("test1"));
					add(new SimpleModelAttribute("test2"));
				}});
		
		final View currentView = createView("current", config);
		final View newView = createView("new", config);
		addActionExpectations(w1, "action1", "action1ok", currentView, newView);
		
		// Add in the enter of child view (no exit of parent view)
		context.checking(new Expectations() {{
			oneOf(newView).enterView(with(any(Model.class)));
		}});
		
		proc.processAction(null, window1, "action1");
		context.assertIsSatisfied();
	}
	
	/**
	 * This ensures that the events are processed
	 */
	@Test
	public void testEvents() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		
		// The original user action
		final View currentView = createView("current");
		addActionExpectations(
				w1, "action1", 
				new UpdateModelAction("app1", "test", "action1ok"), 
				currentView, currentView);
		
		// The event
		final Window window2 = page.getWindow("window2").getWindow();
		addActionExpectations(window2, "onApp", "onAppOk", currentView, currentView);
		
		proc.processAction(null, page.getWindow("window1"), "action1");
		context.assertIsSatisfied();
	}
	
	/**
	 * This ensures that recursive events cause an error
	 */
	@Test
	public void testRecursiveEvents() {
		final View currentView = createView("current");
		
		// 1st Action - the user action on window1
		addActionExpectations(
				w1, "action1", 
				new UpdateModelAction("app1", "action", "action1ok"), 
				currentView, currentView);
		
		// 2nd Action - first event on window2
		final Window window2 = page.getWindow("window2").getWindow();
		addActionExpectations(
				window2, "onApp", 
				new UpdateModelAction("app1", "event1", "action1ok"), 
				currentView, currentView);
		
		// 3rd Action - second event back on window1
		addActionExpectations(
				w1, "onApp", 
				new UpdateModelAction("app1", "event2", "action1ok"), 
				currentView, currentView);
		
		// 4th Action - third event back on window2
		addActionExpectations(
				window2, "onApp", 
				new UpdateModelAction("app1", "event3", "action1ok"), 
				currentView, currentView);
		
		// Should be no further attempts, even though last action will raise an event
		// NOTE: There is no error, but if logging is enabled we will get a log statement
		
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		proc.processAction(null, this.window1, "action1");
		context.assertIsSatisfied();
	}
	
	/**
	 * Tests that a page event is returned on a matching
	 * result.
	 */
	@Test
	public void testPageEvent() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		
		final View currentView = createView("current");
		addActionExpectations(
				w1, "action1", 
				new UpdateModelAction("app2", "test", "pageResult"), 
				currentView, currentView);
		
		PageEventConfig event = proc.processAction(null, window1, "action1");
		context.assertIsSatisfied();
		assertNotNull(event);
		assertEquals(event.getResult(), "window1.pageResult");
	}
	
	@Test(expected = NoViewException.class)
	public void noNewView() {
		ActionProcessor proc = new ActionProcessor(app, page, resolver);
		
		final View currentView = createView("current");
		addActionExpectations(
				w1, "action1", 
				new UpdateModelAction("app1", "test", "action1ok"), 
				currentView, null);
		
		proc.processAction(null, window1, "action1");
	}
	
	/**
	 * Ensures we get an error if we do not supply 
	 * an pagename
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noApp() {
		new ActionProcessor(null, page, resolver);
	}
	
	/**
	 * Ensures we get an error if we do not supply 
	 * an pagename
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noPage() {
		new ActionProcessor(app, null, resolver);
	}
	
	/**
	 * Ensures we get an error if we do not supply
	 * a model
	 */
	@Test(expected = IllegalArgumentException.class)
	public void noResolver() {
		new ActionProcessor(app, page, null);
	}
	
	/**
	 * Creates a mocked view and adds expections that getModel
	 * will be called any number of times.
	 * 
	 * @param name The name of the view (optional)
	 * @return The view
	 */
	private View createView(String name) {
		View view = null;
		if( name != null ) view = context.mock(View.class, name);
		else view = context.mock(View.class);
		
		final View ret = view;
		context.checking(new Expectations() {{
			atLeast(1).of(ret).getModel(); will(returnValue(null));
		}});
		
		return ret;
	}
	
	/**
	 * Creates a mocked view and adds expections that getModel
	 * will be called any number of times.
	 * 
	 * @param name The name of the view (optional)
	 * @return The view
	 */
	private View createView(String name, final ModelConfiguration model) {
		View view = null;
		if( name != null ) view = context.mock(View.class, name);
		else view = context.mock(View.class);
		
		final View ret = view;
		context.checking(new Expectations() {{
			atLeast(1).of(ret).getModel(); will(returnValue(model));
		}});
		
		return ret;
	}
	
	/**
	 * Internal helper to the expected actions given an 
	 * action (upto and including the post-action call
	 * to get the current state) 
	 */
	private void addActionExpectations(final Window window, final String action, final String result, final View currentView, final View newView) {
		context.checking(new Expectations() {{
			// The action
			oneOf(window).getCurrentState(with(any(Model.class)));
			  will(returnValue(currentView));
			oneOf(window).processAction(with(any(Model.class)), with(any(InputModel.class)), with(action)); 
			  will(returnValue(result));
			oneOf(window).getCurrentState(with(any(Model.class))); will(returnValue(newView));
		}});
	}
	
	/**
	 * Internal helper to the expected actions given an 
	 * action and an instance of the action to perform
	 * when called. 
	 */
	private void addActionExpectations(final Window window, final String action, final Action result, final View currentView, final View newView) {
		context.checking(new Expectations() {{
			// The action
			oneOf(window).getCurrentState(with(any(Model.class)));
			  will(returnValue(currentView));
			oneOf(window).processAction(with(any(Model.class)), with(any(InputModel.class)), with(action)); 
			  will(result);
			oneOf(window).getCurrentState(with(any(Model.class))); will(returnValue(newView));
		}});
	}
	
	/**
	 * Internal helper to setup post action expectations
	 * in terms of state changes 
	 */
	private void addStateChangeExpectations(final Window window, final View currentView, final View newView) {
		context.checking(new Expectations() {{
			oneOf(currentView).exitView(with(any(Model.class)), with(any(Model.class)), with("action1ok"));
			oneOf(newView).enterView(with(any(Model.class)));
		}});
	}
	
	/**
	 * Inner action class that updates a model
	 * 
	 * @author Tom Spencer
	 */
	private class UpdateModelAction implements Action {
		private final String attrName;
		private final Object newValue;
		private final String result;
		
		public UpdateModelAction(String attr, Object newValue, String result) {
			this.attrName = attr;
			this.newValue = newValue;
			this.result = result;
		}
		
		/**
		 * Writes the description
		 */
		public void describeTo(Description description) {
			description.appendText("sets ")
					   .appendValue(newValue)
					   .appendText(" on attr: ")
					   .appendText(attrName);
		}
		
		/**
		 * Sets the model
		 */
		public Object invoke(Invocation invocation) throws Throwable {
			Model model = (Model)invocation.getParameter(0);
			model.setAttribute(attrName, newValue);
			return result;
		}
	}
}
