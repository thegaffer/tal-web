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

package org.talframework.talui.mvc.window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.talframework.talui.mvc.View;
import org.talframework.talui.mvc.Window;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.window.annotations.Mappings;
import org.talframework.talui.mvc.window.annotations.Model;
import org.talframework.talui.mvc.window.annotations.OnChange;
import org.talframework.talui.mvc.window.annotations.When;
import org.talframework.talui.mvc.window.annotations.WindowView;

/**
 * Given a class that is a possible window this class
 * contains helpers to construct it into a real class.
 * 
 * @author Tom Spencer
 */
public class WindowCompiler {
	/** Holds single instance of compiler */
	private static final WindowCompiler INSTANCE = new WindowCompiler();
	
	public static WindowCompiler getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor, which is public if required, but otherwise
	 * can use getInstance to get the main instance.
	 */
	public WindowCompiler() {
	}
	
	/**
	 * Call to compiler the possibleWindow into a real window object.
	 * 
	 * @param possibleWindow The possible window
	 * @return The compiled window
	 */
	public Window compileWindow(Object possibleWindow) {
		BaseWindow ret = null;
		
		// Get elements of window
		View defaultView = getDefaultView(possibleWindow);
		Map<String, View> views = getViews(possibleWindow);
		
		// Validation
		if( defaultView == null ) throw new IllegalArgumentException("Cannot create window from class as it has no views: " + possibleWindow);
		
		// Construction
		if( views == null ) {
			SimpleWindow window = new SimpleWindow();
			window.setView(defaultView);
			window.setName(possibleWindow.getClass().getSimpleName());
			ret = window;
		}
		else {
			MultiViewWindow window = new MultiViewWindow();
			window.setDefaultView(defaultView);
			window.setName(window.getClass().getSimpleName());
			
			for( String viewName : views.keySet() ) {
				window.addView(viewName, views.get(viewName));
			}
			
			addMappings(window, possibleWindow);
			ret = window;
		}
		
		addModel(ret, possibleWindow);
		addControllers(ret, possibleWindow);
		
		ret.init();
		return ret;
	}
	
	/**
	 * Internal helper to find the default view of window
	 * 
	 * @param possibleWindow The candidate window object
	 * @return The default view for window
	 */
	private View getDefaultView(Object possibleWindow) {
		View ret = null;
		
		Field[] fields = possibleWindow.getClass().getFields();
		if( fields != null ) {
			Field view = null;
			
			for( int i = 0 ; i < fields.length ; i++ ) {
				WindowView v = fields[i].getAnnotation(WindowView.class);
				if( v != null ) {
					if( view == null ) view = fields[i];
					else if( v.defView() ) view = fields[i];
				}
			}
			
			// Get injected value or the implementing class if null
			ret = getField(possibleWindow, view, View.class);
			if( ret == null ) {
				WindowView v = view.getAnnotation(WindowView.class);
				ret = createView(v.view());
			}
		}
			
		return ret;
	}
	
	/**
	 * Helper to get all views of a multi-view window
	 * 
	 * @param possibleWindow The candidate window object
	 * @return All views if there is more than 1, otherwise null
	 */
	private Map<String, View> getViews(Object possibleWindow) {
		Map<String, View> ret = new HashMap<String, View>();
		
		Field[] fields = possibleWindow.getClass().getFields();
		if( fields != null ) {
			for( int i = 0 ; i < fields.length ; i++ ) {
				WindowView v = fields[i].getAnnotation(WindowView.class);
				
				if( v != null ) {
					View view = getField(possibleWindow, fields[i], View.class);
					if( view == null ) view = createView(v.view());
					
					if( view != null ) ret.put(fields[i].getName(), view);
					else throw new IllegalArgumentException("Cannot create view from field, either injected value is null or is view annotation is missing a view: " + fields[i]);
				}
			}
		}
		
		return ret.size() > 1 ? ret : null;
	}
	
	/**
	 * Helper to add the model
	 * 
	 * @param window
	 * @param possibleWindow
	 */
	private void addModel(BaseWindow window, Object possibleWindow) {
		Field[] fields = possibleWindow.getClass().getFields();
		if( fields != null ) {
			for( int i = 0 ; i < fields.length ; i++ ) {
				if( fields[i].getAnnotation(Model.class) != null ||
						ModelConfiguration.class.isAssignableFrom(fields[i].getType()) ) {
					ModelConfiguration val = getField(possibleWindow, fields[i], ModelConfiguration.class);
					window.setModel(val);
				}
			}
		}
	}
	
	/*
	 * TPS: Initially I envisiaged putting attributes directly on the
	 * window config java classes. In trying to implement the same UI
	 * using the simple repositories and an Objex back-end I've realised
	 * that the model needs to be more generic, therefore, I've removed
	 * this method of compilation for now. I am not totally convinced
	 * that there is not a use for this style of compilation so I am 
	 * being naughty and leaving this in code, but commented out. If this
	 * is here still commented in a significant period of time (its now
	 * Jul 09) then we should get rid.
	 */
	/*private void addAttributes(BaseWindow window, Object possibleWindow) {
		Field[] fields = possibleWindow.getClass().getFields();
		if( fields != null ) {
			for( int i = 0 ; i < fields.length ; i++ ) {
				ModelAttr attr = fields[i].getAnnotation(ModelAttr.class);
				
				if( attr != null ) {
					String name = fields[i].getName();
					Object val = getField(possibleWindow, fields[i], Object.class);
					
					// FUTURE: If val is not set then might miss its meant to be a resolved attr?
					if( val instanceof ModelResolver ) {
						window.addModelAttr(name, val);
					}
					else if( val instanceof SimpleModelResolver ) {
						window.addModelAttr(name, val);
					}
					else {
						window.addModelAttr(name, fields[i].getType(), val);
					}
					
					// Events
					OnChange handler = fields[i].getAnnotation(OnChange.class);
					if( handler != null ) {
						window.addEvent(fields[i].getName(), handler.action(), handler.newValueParam(), handler.oldValueParam());
					}
				}
			}
		}
	}*/
	
	/**
	 * Adds the controllers to the real window
	 */
	private void addControllers(BaseWindow window, Object possibleWindow) {
		// First check fields for injected controllers
		Field[] fields = possibleWindow.getClass().getFields();
		if( fields != null ) {
			for( int i = 0 ; i < fields.length ; i++ ) {
				When when = fields[i].getAnnotation(When.class);
				if( when == null ) continue;
				
				String action = when.action();
				Object ctrl = getField(possibleWindow, fields[i], Object.class);
				if( ctrl == null ) ctrl = createController(when.controller());
				
				window.addController(action, ctrl);
				
				// Events
				OnChange handler = fields[i].getAnnotation(OnChange.class);
				if( handler != null ) {
					window.addEvent(handler.attribute(), when.action(), handler.newValueParam(), handler.oldValueParam());
				}
			}
		}
	}

	/**
	 * Adds the mappings from the window to the real window
	 */
	private void addMappings(MultiViewWindow window, Object possibleWindow) {
		Field[] fields = possibleWindow.getClass().getFields();
		if( fields != null ) {
			for( int i = 0 ; i < fields.length ; i++ ) {
				WindowView v = fields[i].getAnnotation(WindowView.class);
				if( v == null ) continue;
				
				String[] results = v.results();
				for( String result : results ) {
					window.addMapping(result, fields[i].getName());
				}
			}
		}
		
		Mappings mappings = possibleWindow.getClass().getAnnotation(Mappings.class);
		if( mappings != null && mappings.value() != null ) {
			for( int i = 0 ; i < mappings.value().length ; i++ ) {
				window.addMapping(mappings.value()[i].result(), mappings.value()[i].view());
			}
		}
	}
	
	private <T> T getField(Object obj, Field field, Class<T> expected) {
		try {
			Object ret = field.get(obj);
			return expected.cast(ret);
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Unable to extract field [" + field + "] from object: " + obj);
		}
	}
	
	/**
	 * Helper to create the view instance for the window
	 */
	private View createView(Class<?> view) {
		if( View.class.isAssignableFrom(view) ) {
			return View.class.cast(createInitObject(view));
		}
		else {
			// FUTURE: Annotate views
		}
		
		throw new IllegalArgumentException("Given view is not a view: " + view);
	}
	
	/**
	 * Helper to create the controller instance for the window
	 */
	private Object createController(Class<?> controller) {
		return createInitObject(controller);
	}
	
	private <T> T createInitObject(Class<T> expected) {
		T ret = null;
		try {
			ret = expected.newInstance();
			Method init = ret.getClass().getMethod("init", (Class<?>[])null); 
			init.invoke(ret, (Object[])null); 
		}
		catch( NoSuchMethodException e ) {
			// No problem, just has no init method
		}
		catch( Exception e ) {
			throw new IllegalArgumentException("Cannot create window element: " + expected, e);
		}
		
		return ret;
	}
}
