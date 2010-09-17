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

package org.talframework.talui.mvc.commons.views.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.render.RenderModel;
import org.talframework.talui.mvc.view.AbstractView;
import org.talframework.talui.template.Compiler;
import org.talframework.talui.template.TemplateConfiguration;
import org.talframework.talui.template.core.xml.XmlTemplateConfiguration;

/**
 * This view basically presents out a menu at render time.
 * This list of menu items is configured into this class.
 * Each menu item contains:
 * 
 * <ul>
 * <li>A name
 * <li>A resource label to use
 * <li>An action to submit to
 * <li>The optional parameter name to submit menu name with
 * <li>An optional set of conditions against the model
 * </ul>
 * 
 * <p>If no conditions exist against the menu item then
 * it is rendered out. If they do exist then they must
 * all evaluate to true for the menu item to be displayed.</p>
 * 
 * <p><b>Note: </b>This view assumes HTML as the rendering
 * language and should not be used outside of HTML. The class 
 * is not made final so it could be derived from in other 
 * uses.</p>
 * 
 * TODO: Remove internal app context!!!
 * 
 * @author Tom Spencer
 */
public final class MenuView extends AbstractView {
	private static final ApplicationContext ctx = new ClassPathXmlApplicationContext("/org/talframework/talui/mvc/commons/views/menu/menu-context.xml");
	
	/** Holds the name of the menu */
	private final String name;
	/** Holds the list of menu items */
	private final List<MenuItem> items;
	
	/** Holds the created configuration */
	private final TemplateConfiguration config;
		
	/**
	 * Constructs the menu view with its list of items.
	 * 
	 * @param items The items to use
	 */
	public MenuView(String name, List<MenuItem> items) {
		if( name == null ) throw new IllegalArgumentException("You must specify unique name the menu view");
		if( items == null ) throw new IllegalArgumentException("You must specify menu items for the menu view");

		this.name = name;
		this.items = items;
		
		XmlTemplateConfiguration config = new XmlTemplateConfiguration();
		config.setName(name);
		config.setTemplateResource("/org/talframework/talui/mvc/commons/views/menu/menuTemplate.xml");
		config.setRootTemplate("menu");
		
		Map<String, Compiler> compilers = new HashMap<String, Compiler>();
		compilers.put("html", (Compiler)ctx.getBean("mvccommons.menuCompiler"));
		config.setCompilers(compilers);
		
		config.init();
		
		this.config = config;
	}
	
	/**
	 * Iterates around the menu items and renders them
	 * out. The menu items are surrounded in a div.
	 */
	public void prepareRender(RenderModel renderModel, Model model) {
		renderModel.setAttribute("menuItems", items);
		renderModel.setAttribute("templateConfig", config);
		renderModel.setTemplate(config.getName());
	}
	
	/**
	 * @return The name of the view
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The list of menu items
	 */
	public List<MenuItem> getItems() {
		return items;
	}
}
