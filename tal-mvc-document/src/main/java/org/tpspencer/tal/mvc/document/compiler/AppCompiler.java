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

package org.tpspencer.tal.mvc.document.compiler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.tpspencer.tal.mvc.Controller;
import org.tpspencer.tal.mvc.View;
import org.tpspencer.tal.mvc.config.AppConfig;
import org.tpspencer.tal.mvc.config.PageConfig;
import org.tpspencer.tal.mvc.config.WindowConfig;
import org.tpspencer.tal.mvc.controller.GenericController;
import org.tpspencer.tal.mvc.controller.annotations.Action;
import org.tpspencer.tal.mvc.controller.annotations.BindInput;
import org.tpspencer.tal.mvc.document.AppElement;
import org.tpspencer.tal.mvc.document.AppElementImpl;
import org.tpspencer.tal.mvc.model.ModelAttribute;
import org.tpspencer.tal.mvc.model.ModelConfiguration;
import org.tpspencer.tal.mvc.window.BaseWindow;
import org.tpspencer.tal.mvc.window.MultiViewWindow;

/**
 * This class compiles the application, given its name
 * into the {@link AppElement} object.
 * 
 * @author Tom Spencer
 */
public class AppCompiler extends EntityCompiler {
	
	/** Holds the Application Name we are compiling */
	private final String appName;
	/** Holds the Application Context */
	private final ApplicationContext ctx;
	/** Holds the compiled app */
	private AppElement app = null;
	
	/**
	 * Hidden constructor, right now not extending this
	 * to outside world.
	 */
	private AppCompiler(String appName, ApplicationContext ctx) {
		super("app", true, AppConfig.class);
		
		this.appName = appName;
		this.ctx = ctx;
		
		Map<String, Integer> specIndexes = new HashMap<String, Integer>();
		specIndexes.put("page", 4);
		specIndexes.put("window", 5);
		specIndexes.put("view", 6);
		specIndexes.put("controller", 8);
		//specIndexes.put("model", 5);
		specIndexes.put("bean", 7);
		specIndexes.put("service", 9);
		this.setSpecIndexes(specIndexes);
	}
	
	/**
	 * Call to compile an application.
	 * 
	 * @param appName The application name
	 * @param ctx The Spring Config holding the beans
	 * @return The application itself
	 */
	public static AppElement compileApp(String appName, ApplicationContext ctx) {
		AppCompiler compiler = new AppCompiler(appName, ctx);
		return compiler.compile();
	}
	
	/**
	 * Compiles the application into a new {@link AppElement} instance.
	 * 
	 * @return The new {@link AppElement} instance
	 */
	public AppElement compile() {
		getAppCompiler().compile(this, null, null, ctx.getBean(appName), appName);
		return app;
	}
	
	@Override
	protected AppElement createElement(AppCompiler compiler, AppElement app, AppElement parent, Object obj, String id, Map<String, String> params) {
		if( app != null ) throw new IllegalArgumentException("Cannot compile app unless its the first element");
		
		this.app = new AppElementImpl(id, obj, params);
		if( getSpecIndexes() != null ) this.app.setSpecIndexes(getSpecIndexes());
		compileChildren(compiler, this.app, this.app);
		return this.app;
	}
	
	/**
	 * Helper to find the ID of the given object. This
	 * search the Spring Config for the object
	 * 
	 * @param instance The instance to find ID for
	 * @return The ID of that object
	 */
	@SuppressWarnings("unchecked")
	public String findId(Object instance) {
		String ret = null;
		
		Map<String, Object> beans = ctx.getBeansOfType(instance.getClass());
		Iterator<String> it = beans.keySet().iterator();
		while( it.hasNext() ) {
			String n = it.next();
			if( beans.get(n).equals(instance) ) {
				ret = n;
				break;
			}
		}
		
		return ret;
	}
	
	////////////////////////////////////////////////
	//
	public EntityCompiler getAppCompiler() {
		//EntityCompiler app = new EntityCompiler("app", true, AppConfig.class);
		this.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		this.addInfoAccessor("App", new PropertyAccessors.SimplePropertyAccessor("name", true));
		this.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler page = new EntityCompiler("page", true, PageConfig.class);
		page.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		page.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		page.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler multiWindow = new EntityCompiler("window", true, MultiViewWindow.class);
		multiWindow.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		multiWindow.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		multiWindow.addDefaultParameter("Description", "[!Description not found!]");
		EntityCompiler baseWindow = new EntityCompiler("window", true, BaseWindow.class);
		baseWindow.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		baseWindow.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		baseWindow.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler view = new EntityCompiler("view", true, View.class);
		view.setIdCompiler(new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		view.addInfoAccessor("Name", new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(true), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		view.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler model = new EntityCompiler("model", true, ModelConfiguration.class);
		model.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		model.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		model.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler bean = new BeanEntityCompiler("bean", true, null);
		bean.setIdCompiler(new PropertyAccessors.ClassNamePropertyAccessor());
		bean.addInfoAccessor("Name", new PropertyAccessors.ClassNamePropertyAccessor(true));
		bean.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler controller = new EntityCompiler("controller", true, null);
		controller.setIdCompiler(new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		controller.addInfoAccessor("Name", new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(true), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		controller.addDefaultParameter("Description", "[!Description not found!]");
		
		EntityCompiler service = new EntityCompiler("service", true, null);
		service.setIdCompiler(new PropertyAccessors.BeanIdPropertyAccessor());
		service.addInfoAccessor("Name", new PropertyAccessors.BeanIdPropertyAccessor(true));
		service.addDefaultParameter("Description", "[!Description not found!]");
		
		evaluatePages(this, page);
		evaluateWindows(page, new EntityCompiler[]{multiWindow, baseWindow});
		evaluateViews(multiWindow, baseWindow, view);
		evaluateModel(this, new EntityCompiler[]{page, multiWindow, baseWindow, view}, model, bean);
		evaluateBeans(bean);
		evaluateControllers(new EntityCompiler[]{multiWindow, baseWindow}, controller, bean);
		evaluateServices();
		
		return this;
	}
	
	/**
	 * Adds child compiler to app to produce pages
	 */
	private void evaluatePages(EntityCompiler app, EntityCompiler page) {
		app.addChildCompiler(new ChildCompilers.SimpleChildCompiler("pages", page));
	}

	/**
	 * Adds window evaluation to the page
	 */
	private void evaluateWindows(EntityCompiler page, EntityCompiler[] windows) {
		EntityCompiler windowConfig = new EntityCompiler("window", false, WindowConfig.class);
		windowConfig.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		windowConfig.addInfoAccessor("Window", new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor("window", true), new PropertyAccessors.ClassNamePropertyAccessor("window", true)));
		windowConfig.addInfoAccessor("WindowName", new PropertyAccessors.SimplePropertyAccessor("name", true));
		windowConfig.addInfoAccessor("Namespace", new PropertyAccessors.SimplePropertyAccessor("namespace", false));
		
		windowConfig.addChildCompiler(new ChildCompilers.SimpleChildCompiler("window", windows));
		page.addChildCompiler(new ChildCompilers.SimpleChildCompiler("windows", windowConfig));
	}
	
	/**
	 * Adds view evaluation to windows
	 */
	private void evaluateViews(EntityCompiler multiWindow, EntityCompiler baseWindow, EntityCompiler view) {
		EntityCompiler viewConfig = new EntityCompiler("view", false, View.class);
		viewConfig.setIdCompiler(new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		viewConfig.addInfoAccessor("View", new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(true), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		viewConfig.addInfoAccessor("Name", new PropertyAccessors.ParentNamePropertyAccessor());
		
		viewConfig.addChildCompiler(new ChildCompilers.SimpleChildCompiler(null, view));
		multiWindow.addChildCompiler(new ChildCompilers.SimpleChildCompiler("views", viewConfig));
		baseWindow.addChildCompiler(new ChildCompilers.SimpleChildCompiler("defaultView", viewConfig));
	}

	/**
	 * Adds model evaluation to app, page, windows and views
	 */
	private void evaluateModel(EntityCompiler app, EntityCompiler[] modelElements, EntityCompiler model, EntityCompiler bean) {
		EntityCompiler modelConfig = new EntityCompiler("model", false, ModelConfiguration.class);
		modelConfig.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		modelConfig.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		modelConfig.addDefaultParameter("Description", "[!Description not found!]");
		
		// Attributes of the model
		EntityCompiler attr = new EntityCompiler("attr", false, ModelAttribute.class);
		attr.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		attr.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		
		attr.addInfoAccessor("Type", new PropertyAccessors.ClassNamePropertyAccessor("type", true));
		attr.addInfoAccessor("Flash", new PropertyAccessors.BoolPropertyAccessor("flash"));
		attr.addInfoAccessor("Event", new PropertyAccessors.BoolPropertyAccessor("eventable"));
		attr.addInfoAccessor("Render", new PropertyAccessors.BoolPropertyAccessor("autoRenderAttribute"));
		attr.addInfoAccessor("Alias", new PropertyAccessors.MultiBoolPropertyAccessor(new String[][]{new String[]{"aliasExpected", "Yes"}, new String[]{"aliasable", "Optional"}}, "No"));
		attr.addInfoAccessor("Clear", new PropertyAccessors.MultiBoolPropertyAccessor(new String[][]{new String[]{"clearOnAction", "Action"}, new String[]{"clearOnRender", "Render"}}, "None"));
		
		attr.addDefaultParameter("Description", "[!Description not found!]");
		attr.addChildCompiler(new ChildCompilers.SimpleChildCompiler("type", bean));
	
		model.addChildCompiler(new ChildCompilers.SimpleChildCompiler("attributes", attr));
		modelConfig.addChildCompiler(new ChildCompilers.SimpleChildCompiler(null, model));
		
		// Add model config to each of app, page, window and view
		for( int i = 0 ; i < modelElements.length ; i++ ) {
			modelElements[i].addChildCompiler(new ChildCompilers.SimpleChildCompiler("model", modelConfig));
		}
		app.addChildCompiler(new ChildCompilers.SimpleChildCompiler("model", model));
	}

	/**
	 * Adds controller evaluation to windows
	 */
	private void evaluateControllers(EntityCompiler[] windows, EntityCompiler controller, EntityCompiler bean) {
		// Controllers that implement the main interface
		EntityCompiler ctrl = new EntityCompiler("controller", false, Controller.class);
		ctrl.setIdCompiler(new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(false), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		ctrl.addInfoAccessor("Name", new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor(true), new PropertyAccessors.ClassNamePropertyAccessor(true)));
		ctrl.addInfoAccessor("Action", new PropertyAccessors.ParentNamePropertyAccessor());
		ctrl.addChildCompiler(new ChildCompilers.SimpleChildCompiler(null, controller));
		
		// Generic Controllers
		EntityCompiler genericCtrl = new EntityCompiler("controller", false, GenericController.class);
		genericCtrl.setIdCompiler(new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor("controller", true), new PropertyAccessors.ClassNamePropertyAccessor("controller", true)));
		genericCtrl.addInfoAccessor("Name", new PropertyAccessors.ChainedPropertyAccessor(new PropertyAccessors.BeanIdPropertyAccessor("controller", true), new PropertyAccessors.ClassNamePropertyAccessor("controller", true)));
		genericCtrl.addInfoAccessor("Action", new PropertyAccessors.ParentNamePropertyAccessor());
		genericCtrl.addChildCompiler(new ChildCompilers.SimpleChildCompiler("controller", controller));
		
		// Sub Actions of Generic Controllers
		EntityCompiler subAction = new EntityCompiler("subAction", false, Method.class);
		subAction.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		subAction.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		subAction.addInfoAccessor("Result", new PropertyAccessors.AnnotationParamPropertyAccessor(Action.class, "result"));
		subAction.addInfoAccessor("ErrorResult", new PropertyAccessors.AnnotationParamPropertyAccessor(Action.class, "errorResult"));
		subAction.addDefaultParameter("Description", "[!Description not found!]");
		subAction.addChildCompiler(new ChildCompilers.ParamAnnotationTypeChildCompiler(BindInput.class, bean));
		
		controller.addChildCompiler(new ChildCompilers.MethodChildCompiler(Action.class, subAction));

		for( int i = 0 ; i < windows.length ; i++ ) {
			windows[i].addChildCompiler(new ChildCompilers.SimpleChildCompiler("controllers", new EntityCompiler[]{genericCtrl, ctrl}));
		}
	}
	
	/**
	 * Adds attribute compilation to beans
	 */
	private void evaluateBeans(EntityCompiler bean) {
		EntityCompiler attrs = new EntityCompiler("attr", false, PropertyDescriptor.class);
		attrs.setIdCompiler(new PropertyAccessors.SimplePropertyAccessor("name", false));
		attrs.addInfoAccessor("Name", new PropertyAccessors.SimplePropertyAccessor("name", true));
		attrs.addInfoAccessor("Type", new PropertyAccessors.ClassNamePropertyAccessor("propertyType", true));
		attrs.addDefaultParameter("Description", "[!Description not found!]"); // TODO: Might have annotation!
		attrs.addChildCompiler(new ChildCompilers.SimpleChildCompiler("propertyType", bean));
		
		bean.addChildCompiler(new ChildCompilers.BeanPropertyChildCompiler(attrs));
	}
	
	private void evaluateServices() {
		// TODO: How do we get at the services!!
	}
}
