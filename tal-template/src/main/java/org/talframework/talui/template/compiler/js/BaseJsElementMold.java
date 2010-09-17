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

package org.talframework.talui.template.compiler.js;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;
import org.talframework.talui.template.behaviour.InnerTemplateElement;
import org.talframework.talui.template.behaviour.MemberProperty;
import org.talframework.talui.template.behaviour.supporting.ContainerElement;
import org.talframework.talui.template.compiler.GenericCompiler;
import org.talframework.talui.template.compiler.TemplateElementRenderMold;
import org.talframework.talui.template.compiler.TemplateRenderMold;
import org.talframework.talui.template.render.elements.js.DynamicHandlerAttachment;
import org.talframework.talui.template.render.elements.special.EmptyElement;

/**
 * This class is the base class for any Javascript element mold.
 * It includes some helpers for creatng HandlerAttachment render
 * elements to a given role. It also contains the logic to call
 * any children to ensure they get compiled in the JS compiler.
 * 
 * @author Tom Spencer
 *
 */
public class BaseJsElementMold implements TemplateElementRenderMold {
	
	/** Member holds the current styles */
	private String[] styles = new String[0];
	/** Member holds the current template styles */
	private String[] templateStyles = new String[0];
	
	/**
	 * Default constructor
	 */
	public BaseJsElementMold() {
	}
	
	/**
	 * Construct the base JSElement mold with given styles.
	 * These are used 
	 * @param styles
	 * @param templateStyles
	 */
	public BaseJsElementMold(String[] styles, String[] templateStyles) {
		this.styles = styles;
		this.templateStyles = templateStyles;
	}

	/**
	 * Simple tells the template mold to compile any children
	 */
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		RenderElement ret = null;
		
		String[] styles = applyStyles(compiler);
		String[] templateStyles = applyTemplateStyles(compiler);
		
		if( element.getBehaviour(ContainerElement.class) != null ) {
			ContainerElement container = element.getBehaviour(ContainerElement.class);
			List<TemplateElement> elems = container.getElements();
			if( elems != null && elems.size() > 0 ) {
				Iterator<TemplateElement> it = elems.iterator();
				while( it.hasNext() ) {
					RenderElement re = templateMold.compileChild(compiler, template, it.next());
					if( re != null ) {
						if( ret == null ) ret = new EmptyElement();
						ret.addElement(re);
					}
				}
			}
		}
		
		if( element.getBehaviour(MemberProperty.class) != null ) {
			MemberProperty member = element.getBehaviour(MemberProperty.class);
			member.getTemplate();
			compiler.compileTemplate(member.getTemplate(), null, null);
		}
		if( element.getBehaviour(InnerTemplateElement.class) != null ) {
			InnerTemplateElement member = element.getBehaviour(InnerTemplateElement.class);
			member.getTemplate();
			compiler.compileTemplate(member.getTemplate(), null, null);
		}
		
		removeTemplateStyles(compiler, templateStyles);
		removeStyles(compiler, styles);
		
		return ret;
	}
	
	/**
	 * Helper to apply styles to the compiler. Returns an array
	 * of styles that need removing (those that were not already
	 * set)
	 * 
	 * @param compiler The compiler
	 * @return The styles to remove afterwards
	 */
	private String[] applyStyles(GenericCompiler compiler) {
		if( this.styles == null || this.styles.length == 0 ) return null;
		
		List<String> removeStyles = new ArrayList<String>(); 
		for( int i = 0 ; i < this.styles.length ; i++ ) {
			if( !compiler.isStyle(this.styles[i]) ) {
				compiler.addStyle(this.styles[i]);
				removeStyles.add(this.styles[i]);
			}
		}
		
		return removeStyles.size() > 0 ? removeStyles.toArray(new String[removeStyles.size()]) : null;
	}
	
	/**
	 * Helper to remove styles
	 * 
	 * @param compiler The compiler
	 * @param styles The styles to remove
	 */
	private void removeStyles(GenericCompiler compiler, String[] styles) {
		if( styles == null || styles.length == 0 ) return;
		
		for( int i = 0 ; i < styles.length ; i++ ) {
			compiler.removeStyle(styles[i]);
		}
	}
	
	/**
	 * Helper to apply template styles to the compiler. Returns an array
	 * of styles that need removing (those that were not already
	 * set)
	 * 
	 * @param compiler The compiler
	 * @return The styles to remove afterwards
	 */
	private String[] applyTemplateStyles(GenericCompiler compiler) {
		if( this.templateStyles == null || this.templateStyles.length == 0 ) return null;
		
		List<String> removeStyles = new ArrayList<String>(); 
		for( int i = 0 ; i < this.templateStyles.length ; i++ ) {
			if( !compiler.isStyle(this.styles[i]) ) {
				compiler.addTemplateStyle(this.templateStyles[i]);
				removeStyles.add(this.templateStyles[i]);
			}
		}
		
		return removeStyles.size() > 0 ? removeStyles.toArray(new String[removeStyles.size()]) : null;
	}

	/**
	 * Call to remove template styles from the compiler
	 * 
	 * @param compiler The compiler
	 * @param templateStyles The template styles to remove
	 */
	private void removeTemplateStyles(GenericCompiler compiler, String[] templateStyles) {
		if( templateStyles == null || templateStyles.length == 0 ) return;
		
		for( int i = 0 ; i < templateStyles.length ; i++ ) {
			compiler.removeTemplateStyle(templateStyles[i]);
		}
	}
	
	/**
	 * Helper to find any handlers given the role (wrapper, field etc) configured
	 * against the template. If found, generate a variable render element to find
	 * the element(s) and another to connect them to each handler.
	 * 
	 * @param compiler The compiler
	 * @param template The template the element exists within
	 * @param element The element we are checking
	 * @param role The role to check handlers for (wrapper, field etc)
	 * @return A wrapping render element if any are found
	 */
	protected RenderElement checkHandlers(GenericCompiler compiler, Template template, TemplateElement element, String role, RenderElement base) {
		Map<String, String> props = element.getPropertySet(role);
		if( props == null ) return base;
		
		String propertyName = template.getName() + "-" + element.getName();
		
		base = addHandler(props, propertyName, role, "onBlur", base);
		base = addHandler(props, propertyName, role, "onFocus", base);
		
		base = addHandler(props, propertyName, role, "onClick", base);
		base = addHandler(props, propertyName, role, "onDblClick", base);
		base = addHandler(props, propertyName, role, "onMouseDown", base);
		base = addHandler(props, propertyName, role, "onMouseUp", base);
		base = addHandler(props, propertyName, role, "onMouseOver", base);
		base = addHandler(props, propertyName, role, "onMouseOut", base);
		base = addHandler(props, propertyName, role, "onMouseMove", base);
		base = addHandler(props, propertyName, role, "onKeyDown", base);
		base = addHandler(props, propertyName, role, "onKeyUp", base);
		base = addHandler(props, propertyName, role, "onKeyPress", base);
		
		//base = addHandler(props, propertyName, role, "onChange", base);
		//base = addHandler(props, propertyName, role, "onSelect", base);
		//base = addHandler(props, propertyName, role, "onReset", base);
		//base = addHandler(props, propertyName, role, "onSubmit", base);
		
		return base;
	}
	
	/**
	 * Internal helper to create the handler attached JS call
	 * 
	 * @param props
	 * @param propertyName
	 * @param role
	 * @param handler
	 * @param base
	 * @return
	 */
	private RenderElement addHandler(Map<String, String> props, String propertyName, String role, String handler, RenderElement base) {
		if( !props.containsKey(handler) ) return base;
		
		if( "wrapper".equals(role) ) role = null;
		
		if( base == null ) base = new EmptyElement();
		
		base.addElement(new DynamicHandlerAttachment(
				propertyName, role, handler.toLowerCase(), props.get(handler)));
		return base;
	}
}
