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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.Window;
import org.talframework.talui.mvc.config.AppConfig;
import org.talframework.talui.mvc.config.EventConfig;
import org.talframework.talui.mvc.config.PageConfig;
import org.talframework.talui.mvc.config.PageEventConfig;
import org.talframework.talui.mvc.config.WindowConfig;
import org.talframework.talui.mvc.input.InputModel;
import org.talframework.talui.mvc.input.SimpleInputModel;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.ModelEvent;
import org.talframework.talui.mvc.model.SimpleModel;
import org.talframework.talui.mvc.model.StandardModel;
import org.tpspencer.tal.util.aspects.annotations.Trace;

/**
 * This class processes the input. It initially gets
 * holds of the current window and processes the action
 * upon it. It will then process any page-level events
 * across those windows.
 * 
 * @author Tom Spencer
 */
@Trace
public final class ActionProcessor {
	private static final Log logger = LogFactory.getLog(ActionProcessor.class);

	/** Holds the app we should operate on */
	private final AppConfig app;
	/** Holds the page we should operate on */
	private final PageConfig page;
	/** Holds the model prepopulated with app and page */
	private final StandardModel model;
	
	
	/**
	 * Constructs the action processor with the model resolver
	 * and the app & page.
	 * 
	 * @param model The model to use
	 * @param app The application
	 * @param page The page (must be valid in app)
	 */
	public ActionProcessor(
			AppConfig app,
			PageConfig page,
			ModelLayerAttributesResolver resolver) {
		if( app == null ) throw new IllegalArgumentException("No app for action to act upon!");
		if( page == null ) throw new IllegalArgumentException("No page for action to act upon!");
		if( resolver == null ) throw new IllegalArgumentException("No model resolver for action to use!");
		
		this.app = app;
		this.page = page;
		this.model = new StandardModel(resolver, true);
		this.model.setAutoClear(true);
	}
	
	/**
	 * Call to process an incoming action. This method processes 
	 * the action and any subsequent model change events.
	 * 
	 * TODO: Should we try/catch here?!?
	 * 
	 * @param input The input for the action
	 * @param window The window to operate on
	 * @param action The action that has been requested
	 */
	public PageEventConfig processAction(InputModel input, WindowConfig window, String action) {
		if( window == null ) throw new IllegalArgumentException("You must supply the window to perform action upon");
		if( action == null ) throw new IllegalArgumentException("You must supply the action to perform");
		
		PageEventConfig event = null;
		
		model.pushLayer(app.getModel());
		model.pushLayer(page.getModel());
		
		model.setSource(window.getName());
		String result = processWindowAction(window, input, action);
		
		String fullResult = window.getName() + "." + result;
		event = page.getEvent(fullResult);
		if( event == null ) event = app.getEvent(fullResult);
		
		processEvents(3);
		
		// Save model layers away
		Map<ModelConfiguration, Map<String, Object> > modelAttributes = model.getModelAttributes();
		if( modelAttributes != null ) {
			Iterator<ModelConfiguration> it = modelAttributes.keySet().iterator();
			while( it.hasNext() ) {
				ModelConfiguration config = it.next();
				model.getResolver().saveModelAttributes(config, modelAttributes.get(config));
			}
		}
		
		// Pop the layers to any cleanup tasks are run
		model.popLayer(page.getModel());
		model.popLayer(app.getModel());
		
		return event;
	}
	
	/**
	 * Internal recursive function that processes events. It repeatedly
	 * calls itself only a certain amount of times to stop any recursive
	 * events endlessly playing.
	 * 
	 * @param model The model
	 * @param tries The number of times to process events
	 */
	private void processEvents(int tries) {
		List<ModelEvent> events = model.getEvents();
		model.clearEvents();
		
		if( events != null && events.size() > 0 ) {
			if( tries <= 0 ) {
				if( logger.isWarnEnabled() ) logger.warn("!!! Events looping more than 3 times, action processor is stopping");
				return;
			}
			
			Iterator<ModelEvent> it = events.iterator();
			while( it.hasNext() ) {
				processEvent(it.next());
			}
			
			processEvents(--tries);
		}
	}
	
	/**
	 * This helper function processes an event by firing them
	 * into all windows (excluding the source window) in the 
	 * page that have a config for the event.
	 * 
	 * @param model The model
	 * @param events The events
	 * @param sourceWindow The source window
	 */
	private void processEvent(ModelEvent event) {
		String sourceWindow = event.getSource();
		String name = event.getEventName();
			
		Iterator<WindowConfig> itWindows = page.getWindows().iterator();
		while( itWindows.hasNext() ) {
			WindowConfig window = itWindows.next();
			if( sourceWindow == null || !window.getName().equals(sourceWindow) ) {
				EventConfig e = window.getEventConfig(name);
				if( e != null ) {
					if( logger.isTraceEnabled() ) logger.trace("\tFiring event [" + e + "] on window: " + window);
					
					Map<String, Object> params = new HashMap<String, Object>();
					if( event.getNewValue() != null ) {
						params.put(e.getNewValueName(), event.getNewValue());
					}
					if( event.getOldValue() != null && e.getOldValueName() != null ) {
						params.put(e.getOldValueName(), event.getOldValue());
					}
					
					InputModel input = new SimpleInputModel(params);
					
					// Process the action
					model.setSource(window.getName());
					processWindowAction(window, input, e.getAction());
				}
			}
		}
	}
	
	/**
	 * Internal helper to process any window action
	 * 
	 * @param model The model as it standards
	 * @param config The configuration
	 * @param input The input for the action
	 * @param action The action to perform
	 * @return The result that occurred
	 */
	private String processWindowAction(WindowConfig config, InputModel input, String action) {
		Window window = config.getWindow();
		model.pushLayer(config.getModel());
		
		View view = window.getCurrentState(model);
		model.pushLayer(view.getModel());
		
		String res = null;
		try {
			res = window.processAction(model, input, action);
			view = postActionProcess(window, view, res);
		}
		catch( RuntimeException e ) {
			throw e;
		}
		finally {
			model.popLayer(view.getModel());
			model.popLayer(config.getModel());
		}
		
		return res;
	}
	
	/**
	 * This internal helper determines if the state (view) has
	 * changed inside the window. If so we exit the old view
	 * and enter the new one. 
	 * 
	 * @param model The standard model
	 * @param window The window we are operating on
	 * @param oldView The 'old' view (i.e. the view we were in)
	 * @param result The result from the action processing (passed to exitView)
	 * @return The view we are now in
	 */
	private View postActionProcess(Window window, View oldView, String result) {
		View newView = window.getCurrentState(model);
		if( newView == null ) throw new NoViewException("No view found given result [" + result + "] on window: " + window);
		
		if( !newView.equals(oldView) ) {
			// Remove the views layer, but keep hold of attributes
			Map<String, Object> oldViewAttrs = model.popLayer(oldView.getModel()); // Not this saves it!
			
			// Exit old view (unless new view has same view model)
			if( newView.getModel() == null || !newView.getModel().equals(oldView.getModel()) ) {
				Model viewModel = null; 
				if( oldView.getModel() != null ) {
					viewModel = new SimpleModel(oldView.getModel(), oldViewAttrs);
				}
				
				oldView.exitView(model, viewModel, result);
				
				// Remove the views model permanently
				if( viewModel != null ) {
					model.getResolver().removeModel(oldView.getModel());
				}
			}
			
			// Push layer for new view
			model.pushLayer(newView.getModel());
			newView.enterView(model);
		}
		
		return newView;
	}
}
