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

package org.talframework.talui.mvc.commons.views.form;

import java.util.ArrayList;
import java.util.List;

import org.talframework.talui.mvc.Model;
import org.talframework.talui.mvc.commons.views.AbstractTemplateView;
import org.talframework.talui.mvc.controller.ObjectCreator;
import org.talframework.talui.mvc.model.ModelAttribute;
import org.talframework.talui.mvc.model.ModelConfiguration;
import org.talframework.talui.mvc.model.SimpleModelAttribute;
import org.talframework.talui.template.core.BasicTemplateConfiguration;

/**
 * This class represents a form view. This class handles the basic
 * mechanics of getting the form bean from the model, putting it
 * in the render model and delegating to a template. It also 
 * contains the main submitAction for the form and a list of the
 * commands that can be performed (buttons at the bottom).
 * 
 * <p>The template is created dynamically based on the form bean
 * class configured. You can provide overrides to the basics in
 * terms of either a set of field orders or by providing you own
 * XML template configuration.</p>
 * 
 * <p>For the rendering the default HTML Form compilers are used, 
 * but these can be overridden if desired.</p>
 * 
 * @author Tom Spencer
 */
public class FormView extends AbstractTemplateView {
	
	/** Holds the primary action to submit form under */
	private String primaryAction = null;
	/** Holds the list of commands/buttons to display */			
	private List<String> commands = null;
	/** Holds the name of the errors model attribute, default is 'errors' */
	private String errorsAttribute = "errors";
	/** Holds the name of model attribute of the initial form bean */
	private String initialFormBean = null;
	/** Determines if initial form is copied into view bean attribute when we enter */
	private boolean initialFormOnEnter = false;
	
	/**
	 * Public constructor set turn the form view on by
	 * default - can still be changed before init.
	 */
	public FormView() {
		setAsForm(true);
	}
	
	public void init() {
		if( getViewBeanName() == null ) {
			setViewBeanName("form");
		}
		else if( primaryAction == null ) {
			primaryAction = getDefaultPrimaryAction(getViewBeanName());
		}
		
		super.init();
	}
	
	/**
	 * Overrides to suffix the default view name with "Form"
	 */
	@Override
	protected String getDefaultViewName() {
		String ret = super.getDefaultViewName();
		if( ret != null ) ret += "Form";
		return ret;
	}
	
	/**
	 * Called when no model is set. We need to ensure we
	 * have a view level model for the form bean and the
	 * errors
	 */
	@Override
	protected ModelConfiguration createDefaultModel() {
		List<ModelAttribute> attrs = new ArrayList<ModelAttribute>();
		
		/*SimpleModelAttribute errors = new SimpleModelAttribute(errorsAttribute, List.class);
		errors.setClearOnAction(true);
		errors.setFlash(false);
		attrs.add(errors);*/
		
		SimpleModelAttribute bean = new SimpleModelAttribute(getViewBeanName(), Object.class);
		bean.setClearOnAction(false);
		bean.setFlash(false);
		attrs.add(bean);
		
		return new ModelConfiguration(getViewName(), attrs);
	}
	
	/**
	 * Generates a default primary action by taking the form
	 * bean name, i.e. orderBeanForm, making sure the initial 
	 * character is uppercase and prefixing it with submit,
	 * i.e. submitOrderBeanForm.
	 *  
	 * @return The default primary action
	 * @throws IllegalArgumentException If the view name is null or empty
	 */
	protected String getDefaultPrimaryAction(String base) {
		String name = base == null ? getViewName() : base;
		if( name == null || name.length() == 0 ) throw new IllegalArgumentException("The form view cannot generate a default primary action name");
		
		if( name.length() > 1 ) name = name.substring(0, 1).toUpperCase() + name.substring(1);
		else name = name.toUpperCase();
		name = "submit" + name;
		return name;
	}
	
	/**
	 * Askes the template config to generate a form template
	 */
	@Override
	protected void preInitConfig(BasicTemplateConfiguration config) {
		if( primaryAction == null ) primaryAction = getDefaultPrimaryAction(null);
		if( primaryAction == null ) throw new IllegalArgumentException("A form view requires a primary action to be set");
		
		// Create default commands of submit and cancel
		if( commands == null ) {
			commands = new ArrayList<String>();
			if( isAsForm() ) {
				commands.add("submit");
			}
			commands.add("cancel");
		}
		
		// Field Order overrides!?!
		config.addFormTemplate(getViewName(), getViewBeanName(), isAsForm(), getPrimaryBean(), primaryAction, commands);
	}
	
	/**
	 * Copy over initial form bean if there is one
	 */
	@Override
	public void enterView(Model model) {
		// Copy over initial bean if required
		if( initialFormOnEnter && initialFormBean != null ) {
			Object bean = model.getAttribute(initialFormBean);
			model.setAttribute(getViewBeanName(), bean);
		}
		
		super.enterView(model);
	}
	
	/**
	 * Helper to get the form bean. If it is present in the
	 * model then great, otherwise we turn to the beanCreator
	 * class and finally the bean itself.
	 * 
	 * @param model The model
	 * @return The bean
	 */
	@Override
	protected Object getBean(Model model) {
		Object ret = null;
		
		if( model.containsValueFor(getViewBeanName()) ) {
			ret = model.get(getViewBeanName());
		}
		
		// Get the initial version if set
		if( ret == null && initialFormBean != null ) {
			ret = model.get(initialFormBean);
		}
		
		if( ret == null ) {
		    ret = ObjectCreator.createObject(getPrimaryBean());
		}
	
		return ret;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("FormView: ");
		buf.append("name=").append(getViewName());
		if( getViewBeanName() != null ) buf.append(", form=").append(getViewBeanName());
		buf.append(", action=").append(getPrimaryAction());
		buf.append(", bean=").append(getPrimaryBean());
		
		return buf.toString();
	}
	
	////////////////////////////////////////////////////////////
	// Getters/Setters
	
	/**
	 * @return the primaryAction
	 */
	public String getPrimaryAction() {
		return primaryAction;
	}

	/**
	 * @param primaryAction the primaryAction to set
	 */
	public void setPrimaryAction(String primaryAction) {
		this.primaryAction = primaryAction;
	}

	/**
	 * @return the commands
	 */
	public List<String> getCommands() {
		return commands;
	}

	/**
	 * @param commands the commands to set
	 */
	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	/**
	 * @return the errorsAttribute
	 */
	public String getErrorsAttribute() {
		return errorsAttribute;
	}

	/**
	 * @param errorsAttribute the errorsAttribute to set
	 */
	public void setErrorsAttribute(String errorsAttribute) {
		this.errorsAttribute = errorsAttribute;
	}

	/**
	 * @return the initialFormBean name
	 */
	public String getInitialFormBean() {
		return initialFormBean;
	}

	/**
	 * @param initialFormBean the initialFormName to set
	 */
	public void setInitialFormBean(String initialFormBean) {
		this.initialFormBean = initialFormBean;
	}

	/**
	 * @return the initialFormOnEnter
	 */
	public boolean isInitialFormOnEnter() {
		return initialFormOnEnter;
	}

	/**
	 * @param initialFormOnEnter the initialFormOnEnter to set
	 */
	public void setInitialFormOnEnter(boolean initialFormOnEnter) {
		this.initialFormOnEnter = initialFormOnEnter;
	}
}
