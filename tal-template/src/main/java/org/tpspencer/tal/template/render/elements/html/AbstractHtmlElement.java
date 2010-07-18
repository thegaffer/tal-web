/*
 * Copyright 2008 Thomas Spencer
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

package org.tpspencer.tal.template.render.elements.html;

import static org.tpspencer.tal.util.htmlhelper.HtmlConstants.ATTR_ACCESS;
import static org.tpspencer.tal.util.htmlhelper.HtmlConstants.ATTR_CLASS;
import static org.tpspencer.tal.util.htmlhelper.HtmlConstants.ATTR_ID;
import static org.tpspencer.tal.util.htmlhelper.HtmlConstants.ATTR_STYLE;
import static org.tpspencer.tal.util.htmlhelper.HtmlConstants.ATTR_TAB;
import static org.tpspencer.tal.util.htmlhelper.HtmlConstants.ATTR_TITLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.tpspencer.tal.template.RenderModel;
import org.tpspencer.tal.template.render.elements.AbstractRenderElement;
import org.tpspencer.tal.template.render.elements.html.attributes.ExpressionAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.HtmlAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.IDAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.NameAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.ResourceAttribute;
import org.tpspencer.tal.template.render.elements.html.attributes.SimpleAttribute;
import org.tpspencer.tal.util.htmlhelper.GenericElement;

/**
 * This class is the foundation for most HTML render
 * elements. It handles a lot of common HTML attributes
 * allowing the derived class to only handle what it
 * needs to.
 * 
 * @author Tom Spencer
 */
public abstract class AbstractHtmlElement extends AbstractRenderElement {
	
	/** Holds the name of the element (set by derived class) */
	private String elementName = null;
	/** Holds the base for the ID field */
	private String id = null;
	/** Holds the style, which is treated differently for the show/hide functionality */
	private String style = null;
	/** Holds the style class, which is often set automatically */
	private String styleClass = null;
	
	/** Holds the template role for element, i.e. wrapper or prop */
	private String templateRole = null;
	/** Holds the template type of element - this is a special Web Template attribute, extending DOM via XHTML */
	private String templateType = null;
	
	/** Member holds an expression, which if true, means we show the element (otherwise we hide) */
	private String showExpr = null;
	/** Member holds an expression, which if true, means we hide the element (otherwise we show) */
	private String hideExpr = null;
	/** Member holds an expression, which if true, means we ignore this element and don't output */
	private String ignoreExpr = null;
	/** Member holds the field we should check whether there are any errors again */
	private String errorField = null;
	
	/** If true there will be a new line after the start of the element */
	private boolean newLineAfterStart = false;
	/** If true there will be a new line after the end of the element */
	private boolean newLineAfterTerminate = false;
	
	/** Holds the additional attributes */
	private List<HtmlAttribute> attributes = null;
	
	/**
	 * This constructor can be used when the derived class
	 * does not want the base class to take the element. This
	 * means there will not be a id on the element by default.
	 * Any properties are copied into the relevant std html
	 * attributes.
	 * 
	 * @param props The properties of the element
	 */
	public AbstractHtmlElement(String elementName) {
		this.elementName = elementName;
	}
	
	/**
	 * This constructor can be used to construct an html
	 * element with a specified name (which is the id
	 * attribute). Any properties are copied into the 
	 * relevant std html attributes.
	 * 
	 * @param elementName The name of the element to output
	 * @param id The ID of the element
	 * @param props The properties of the element
	 */
	public AbstractHtmlElement(String elementName, String id) {
		this.elementName = elementName;
		this.id = id;
	}
	
	/**
	 * Internal helper to set the extra HTML props from a set
	 * of properties.
	 * 
	 * @param props The props
	 */
	public void setAttributes(Map<String, String> props) {
		if( props != null ) {
			style = props.containsKey("style") ? props.get("style") : style;
			if( props.containsKey("styleClass") ) addStyleClasses(props.get("styleClass"));
			
			addSimpleAttribute("class", ATTR_CLASS, props, false);
			addResourceAttribute("title", ATTR_TITLE, props);
			addSimpleAttribute("tabIndex", ATTR_TAB, props, false);
			addResourceAttribute("accessKey", ATTR_ACCESS, props);
		}	
	}
	
	/**
	 * Call to add an attribute to this element. This method
	 * makes a check to see if an attribute of that name already
	 * exists (in which case it is first removed). This check is
	 * not particularly efficient, so make sure this is only called
	 * when setting up the element (i.e. during its construction).
	 * 
	 * @param attr The attribute to add
	 */
	public void addAttribute(HtmlAttribute attr) {
		if( attributes == null ) attributes = new ArrayList<HtmlAttribute>();
		else {
			Iterator<HtmlAttribute> it = attributes.iterator();
			while( it.hasNext() ) {
				HtmlAttribute a = it.next();
				if( a.getName().equals(attr.getName()) ) {
					attributes.remove(a);
					break;
				}
			}
		}
		
		attributes.add(attr);
	}
	
	/**
	 * Adds a simple attribute if it exists in the set of properties
	 * 
	 * @param propName The name of the property to look for
	 * @param attrName The attributes name (if different)
	 * @param props The properties to look within
	 */
	public void addSimpleAttribute(String propName, String attrName, Map<String, String> props, boolean escape) {
		if( props.containsKey(propName) ) {
			if( attrName == null ) attrName = propName;
			addAttribute(new SimpleAttribute(attrName, (String)props.get(propName), escape));
		}
	}
	
	/**
	 * Adds a id attribute if it exists in the set of properties.
	 * An ID attribute is expanded with the full name of the object
	 * at the current node in the model + the namespace.
	 * 
	 * @param propName The name of the property to look for
	 * @param attrName The attributes name (if different)
	 * @param props The properties to look within
	 */
	public void addIdAttribute(String propName, String attrName, Map<String, String> props) {
		if( props.containsKey(propName) ) {
			if( attrName == null ) attrName = propName;
			addAttribute(new IDAttribute(attrName, (String)props.get(propName)));
		}
	}
	
	/**
	 * Adds a name attribute if it exists in the set of properties.
	 * An name attribute is expanded with the full name of the object
	 * at the current node in the model.
	 * 
	 * @param propName The name of the property to look for
	 * @param attrName The attributes name (if different)
	 * @param props The properties to look within
	 */
	public void addNameAttribute(String propName, String attrName, Map<String, String> props) {
		if( props.containsKey(propName) ) {
			if( attrName == null ) attrName = propName;
			addAttribute(new NameAttribute(attrName, (String)props.get(propName)));
		}
	}
	
	/**
	 * Adds a resource attribute if it exists in the set of properties.
	 * A resource attribute is obtained from the resource bundle
	 * 
	 * @param propName The name of the property to look for
	 * @param attrName The attributes name (if different)
	 * @param props The properties to look within
	 */
	public void addResourceAttribute(String propName, String attrName, Map<String, String> props) {
		if( props.containsKey(propName) ) {
			if( attrName == null ) attrName = propName;
			addAttribute(new ResourceAttribute(attrName, (String)props.get(propName)));
		}
	}
	
	/**
	 * Adds a expr attribute if it exists in the set of properties.
	 * An expression attribute is evaluated rather than just output.
	 * 
	 * @param propName The name of the property to look for
	 * @param attrName The attributes name (if different)
	 * @param props The properties to look within
	 */
	public void addExpressionAttribute(String propName, String attrName, Map<String, String> props, boolean escape) {
		if( props.containsKey(propName) ) {
			if( attrName == null ) attrName = propName;
			addAttribute(new ExpressionAttribute(attrName, (String)props.get(propName), escape));
		}
	}
	
	/**
	 * First determines if we are ignored, if we are rendering ends.
	 * Then writes out the start of the element and all its 
	 * attributes. It is not terminated until postRender.
	 */
	@Override
	protected boolean preRender(RenderModel model) throws IOException {
		// See if we are ignored
		if( ignoreExpr != null ) {
			if( model.evaluateExpression(ignoreExpr, Boolean.class).booleanValue() ) {
				return false;
			}
		}
		
		GenericElement elem = model.getGenericElement();
		elem.reset(getElementName(model));
		
		// Add the ID on
		if( id != null ) elem.addIdAttribute(ATTR_ID, id);
		
		// Add the Style on observing the isHidden setting
		String style = this.style;
		if( isHidden(model) ) {
			if( this.style == null ) style = "display: hidden";
			else style = this.style + "; display: hidden";
		}
		if( style != null ) elem.addAttribute(ATTR_STYLE, style, false);
		
		// Add in the Style Class
		String styleClass = getStyleClasses(model);
		if( styleClass != null ) elem.addAttribute(ATTR_CLASS, styleClass, false);
		
		// Add in templateRole/Type
		if( templateRole != null ) {
			elem.addAttribute("templateRole", templateRole, false);
			if( templateType != null ) elem.addAttribute("templateType", templateType, false);
		}
		
		addAttributes(model, elem);
		
		elem.write(model.getWriter(), false);
		if( newLineAfterStart ) model.getWriter().write("\n");
		
		return true;
	}
	
	/**
	 * Overriddable method to get this style classes at render time.
	 * The default simply returns any configured style classes.
	 * Derived classes may override to replace or augment
	 * 
	 * @param model The render model 
	 * @return Any extra dynamic classes
	 */
	protected String getStyleClasses(RenderModel model) {
		String ret = this.styleClass;
		if( errorField != null && model.isError(null, errorField) ) {
			ret = ret + " field-error";
		}
		return ret;
	}
	
	/**
	 * This method is called to add attributes to the single HTML element
	 * this class represents. The standard behaviour is to add on any
	 * event handler attributes that exist. The derived class cal override
	 * this method and either add on its own attributes and call this base
	 * method, just add on its own methods and handle any event handlers
	 * itself, or do nothing.
	 * 
	 * @param model The render model
	 * @param elem The element presetup with name, and std html attrs (id, style, class, title, tabIndex and accessKey)
	 */
	protected void addAttributes(RenderModel model, GenericElement elem) {
		if( attributes != null ) {
			int ln = attributes.size();
			for( int i = 0 ; i < ln ; i++ ) {
				attributes.get(i).addAttribute(model, elem);
			}
		}
	}
	
	/**
	 * Simply terminates the element
	 */
	@Override
	protected void postRender(RenderModel model) throws IOException {
		GenericElement.writeTerminate(model.getWriter(), getElementName(model));
		if( newLineAfterTerminate ) model.getWriter().write("\n");
	}
	
	/**
	 * Called to get the name of the element. Default returns element
	 * name.
	 * 
	 * @param model The model
	 * @return The element name
	 */
	public String getElementName(RenderModel model) {
		return elementName;
	}
	
	/**
	 * Determines if we are hidden or not based on any show or hide
	 * expression. Showing and hiding typically simply involves setting
	 * the style attribute on the element.
	 * 
	 * @param model The render model (expressions are evaluated here)
	 * @return True if we are hidden, false otherwise
	 */
	public boolean isHidden(RenderModel model) {
		boolean ret = false;
		
		// Hide if show expression evaluates to false
		if( showExpr != null ) {
			ret = ! model.evaluateExpression(showExpr, Boolean.class).booleanValue();
		}
		
		// Hide if hide expression evaluates to true
		if( hideExpr != null ) {
			ret = model.evaluateExpression(hideExpr, Boolean.class);
		}
		
		return ret;
	}

	/////////////////////////////////
	// Getters / Setters
	
	/**
	 * Psuedo setter to add a suffix to the id
	 */
	public void setIdSuffix(String idSuffix) {
		this.id = this.id + idSuffix;
	}
	
	/**
	 * Psuedo setter to add a prefix to the id
	 */
	public void setIdPrefix(String prefix) {
		this.id = prefix + this.id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the showExpr
	 */
	public String getShowExpr() {
		return showExpr;
	}

	/**
	 * @param showExpr the showExpr to set
	 */
	public void setShowExpr(String showExpr) {
		this.showExpr = showExpr;
	}

	/**
	 * @return the hideExpr
	 */
	public String getHideExpr() {
		return hideExpr;
	}

	/**
	 * @param hideExpr the hideExpr to set
	 */
	public void setHideExpr(String hideExpr) {
		this.hideExpr = hideExpr;
	}

	/**
	 * @return the ignoreExpr
	 */
	public String getIgnoreExpr() {
		return ignoreExpr;
	}

	/**
	 * @param ignoreExpr the ignoreExpr to set
	 */
	public void setIgnoreExpr(String ignoreExpr) {
		this.ignoreExpr = ignoreExpr;
	}

	/**
	 * @return the attributes
	 */
	public List<HtmlAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<HtmlAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the styleClass
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	/**
	 * Additional helper method to add a class, but
	 * not replace any others. The class is only added
	 * if not already present.
	 * 
	 * @param styleClass The style class to add 
	 */
	public void addStyleClass(String styleClass) {
		if( this.styleClass != null ) {
			String[] styles = this.styleClass.split(" ");
			boolean addNew = true;
			for( int i = 0 ; i < styles.length ; i++ ) {
				if( styles[i].equals(styleClass) ) {
					addNew = false;
					break;
				}
			}
			
			if( addNew ) {
				this.styleClass = this.styleClass + " " + styleClass;
			}
		}
		else {
			setStyleClass(styleClass);
		}
	}
	
	/**
	 * Additional helper to add in multiple style classes
	 * in one string. Each is checked to ensure it does
	 * not already exist. Use this if you are unsure if
	 * you have 1 or more (or 0) style classes to add.
	 * 
	 * @param styleClasses
	 */
	public void addStyleClasses(String styleClasses) {
		if( styleClasses == null ) return;
		
		String[] styles = styleClasses.split(" ");
		for( int i = 0 ; i < styles.length ; i++ ) {
			addStyleClass(styles[i]);
		}
	}

	/**
	 * @param elementName the elementName to set
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	/**
	 * @return the newLineAfterStart
	 */
	public boolean isNewLineAfterStart() {
		return newLineAfterStart;
	}

	/**
	 * @param newLineAfterStart the newLineAfterStart to set
	 */
	public void setNewLineAfterStart(boolean newLineAfterStart) {
		this.newLineAfterStart = newLineAfterStart;
	}

	/**
	 * @return the newLineAfterTerminate
	 */
	public boolean isNewLineAfterTerminate() {
		return newLineAfterTerminate;
	}

	/**
	 * @param newLineAfterTerminate the newLineAfterTerminate to set
	 */
	public void setNewLineAfterTerminate(boolean newLineAfterTerminate) {
		this.newLineAfterTerminate = newLineAfterTerminate;
	}

	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	/**
	 * @return the templateRole
	 */
	public String getTemplateRole() {
		return templateRole;
	}

	/**
	 * @param templateRole the templateRole to set
	 */
	public void setTemplateRole(String templateRole) {
		this.templateRole = templateRole;
	}

	/**
	 * @return the errorField
	 */
	public String getErrorField() {
		return errorField;
	}

	/**
	 * @param errorField the errorField to set
	 */
	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}
}
