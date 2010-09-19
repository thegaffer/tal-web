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

package org.talframework.talui.template.compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.talframework.tal.aspects.annotations.Trace;
import org.talframework.talui.template.RenderElement;
import org.talframework.talui.template.Template;
import org.talframework.talui.template.TemplateElement;

/**
 * This configurable class turns any Template Element into a set
 * of render elements by utilising a set of classes that each
 * form part (a fragment of) the whole. This is certainly true
 * of HTML where typically you might have an outer div or span,
 * which in turns contains a label, the field itself, and 
 * possibly other elements. Although different template elements
 * are different when rendered this is typically only minor
 * changes and by re-configuring with different fragments or
 * orders the consistent parts can be re-used meaning less code
 * to change in the future.
 * 
 * @author Tom Spencer
 */
public class SimpleTemplateElementMold implements TemplateElementRenderMold {
	
	/** If set to true then this raises an exception if the wrapper is not interested */
	private boolean requireElement = true;
	/** An optional list of styles that are applied around this element (& children inc templates) */
	private String[] styles = null;
	/** An optional list of template styles that are applied around this element (& children) */
	private String[] templateStyles = null;
	/** Member holds the wrapping fragment, if this is interested in the element no render elements are created */
	private FragmentMold wrapper = null;
	/** Member holds the fragments in order */
	private List<FragmentMold> fragments = null;

	/**
	 * Simply outputs the wrapper followed by any other configured
	 * fragment molds if they are interested.
	 */
	@Trace
	public RenderElement compile(GenericCompiler compiler, TemplateRenderMold templateMold, Template template, TemplateElement element) {
		if( this.wrapper.isInterested(compiler, template, element) ) {
			String[] styles = applyStyles(compiler);
			String[] templateStyles = applyTemplateStyles(compiler);
			
			RenderElement wrapper = this.wrapper.compile(compiler, templateMold, template, element);
			
			Iterator<FragmentMold> it = fragments != null ? fragments.iterator() : null;
			while( it != null && it.hasNext() ) {
				FragmentMold f = it.next();
				if( f.isInterested(compiler, template, element) ) {
					RenderElement re = f.compile(compiler, templateMold, template, element);
					if( re != null ) wrapper.addElement(re);
				}
			}
			
			removeStyles(compiler, styles);
			removeTemplateStyles(compiler, templateStyles);
			
			return wrapper;
		}
		else if( requireElement ) {
			throw new IllegalArgumentException("The wrapper [" + wrapper + "] was not interested in the element: " + element);
		}
		
		return null;
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
			if( !compiler.isStyle(this.templateStyles[i]) ) {
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
	
	/////////////////////////////////////////
	// Getters & Setters

	/**
	 * @return the wrapper
	 */
	public FragmentMold getWrapper() {
		return wrapper;
	}

	/**
	 * @param wrapper the wrapper to set
	 */
	public void setWrapper(FragmentMold wrapper) {
		this.wrapper = wrapper;
	}

	/**
	 * @return the fragments
	 */
	public List<FragmentMold> getFragments() {
		return fragments;
	}

	/**
	 * @param fragments the fragments to set
	 */
	public void setFragments(List<FragmentMold> fragments) {
		this.fragments = fragments;
	}
	
	/**
	 * Call to add a fragment into the set of fragments this
	 * mold holds.
	 * 
	 * @param fragment The fragment to add
	 */
	public void addFragment(FragmentMold fragment) {
		if( this.fragments == null ) this.fragments = new ArrayList<FragmentMold>();
		this.fragments.add(fragment);
	}

	/**
	 * @return the styles
	 */
	public String[] getStyles() {
		return styles;
	}

	/**
	 * @param styles the styles to set
	 */
	public void setStyles(String[] styles) {
		this.styles = styles;
	}

	/**
	 * @return the templateStyles
	 */
	public String[] getTemplateStyles() {
		return templateStyles;
	}

	/**
	 * @param templateStyles the templateStyles to set
	 */
	public void setTemplateStyles(String[] templateStyles) {
		this.templateStyles = templateStyles;
	}

	/**
	 * @return the requireElement
	 */
	public boolean isRequireElement() {
		return requireElement;
	}

	/**
	 * @param requireElement the requireElement to set
	 */
	public void setRequireElement(boolean requireElement) {
		this.requireElement = requireElement;
	}
}
