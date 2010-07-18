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

package org.tpspencer.tal.mvc.commons.views.prototype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tpspencer.tal.mvc.Model;
import org.tpspencer.tal.mvc.render.RenderModel;
import org.tpspencer.tal.mvc.view.AbstractView;
import org.tpspencer.tal.template.Compiler;
import org.tpspencer.tal.template.TemplateConfiguration;
import org.tpspencer.tal.template.core.BasicTemplateConfiguration;
import org.tpspencer.tal.template.core.xml.XmlTemplateReader;

/**
 * A prototype view is one which outputs a form,
 * table or simple view together which a set of
 * pre-defined actions that are output either as
 * a set of links, a drop-down or as buttons. The
 * prototype view can be used to quickly and 
 * easily put up some thing to give a flavour of
 * how the app will look.
 * 
 * @author Tom Spencer
 */
public final class PrototypeView extends AbstractView implements BeanNameAware {
	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("/org/tpspencer/tal/mvc/commons/views/prototype/proto-context.xml");
	
	public enum EViewType {
		DEFAULT,
		FORM,
		TABLE
	}
	
	/** The unique name of the view */
	private String name = null;
	/** Holds the type of view to display */
	private EViewType type = EViewType.DEFAULT;
	/** Holds the commands that can be generated */
	private List<PrototypeCommand> commands = null;
	
	/** The template configuration - not set publically */
	private TemplateConfiguration config = null;
	
	public void setBeanName(String name) {
		this.name = name;
	}
	
	public void init() {
		if( name == null ) throw new IllegalArgumentException("You must supply a unique name for the prototype view");
		if( commands == null || commands.size() == 0 ) throw new IllegalArgumentException("You must provide some commands to a prototype view");
		
		BasicTemplateConfiguration config = new BasicTemplateConfiguration();
		config.setName(name);
		config.setTemplates(XmlTemplateReader.getStdReader().loadTemplates("/org/tpspencer/tal/mvc/commons/views/prototype/defaultProtoTemplate.xml"));
		config.setRootTemplate("view");
		
		Map<String, Compiler> compilers = new HashMap<String, Compiler>();
		compilers.put("html", (Compiler)ctx.getBean("mvccommons.defaultProtoCompiler"));
		config.setCompilers(compilers);
		
		config.init();
		this.config = config;
	}

	/**
	 * Adds the commands to the model and forwards to template
	 */
	public void prepareRender(RenderModel renderModel, Model model) {
		renderModel.setAttribute("commands", commands);
		renderModel.setAttribute("templateConfig", config);
		renderModel.setTemplate(config.getName());
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("PrototypeView: ");
		Iterator<PrototypeCommand> it = commands.iterator();
		boolean first = true;
		while( it.hasNext() ) {
			if( !first ) buf.append(", ");
			buf.append("command=[").append(it.next()).append("]");
			first = false;
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commands == null) ? 0 : commands.hashCode());
		result = prime * result + ((config == null) ? 0 : config.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrototypeView other = (PrototypeView) obj;
		if (commands == null) {
			if (other.commands != null)
				return false;
		} else if (!commands.equals(other.commands))
			return false;
		if (config == null) {
			if (other.config != null)
				return false;
		} else if (!config.equals(other.config))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * @return the type
	 */
	public EViewType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(EViewType type) {
		this.type = type;
	}

	/**
	 * @return the commands
	 */
	public List<PrototypeCommand> getCommands() {
		return commands;
	}

	/**
	 * @param commands the commands to set
	 */
	public void setCommands(List<PrototypeCommand> commands) {
		this.commands = commands;
	}

	/**
	 * @return the config
	 */
	public TemplateConfiguration getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(TemplateConfiguration config) {
		this.config = config;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
